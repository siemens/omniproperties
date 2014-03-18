/*
 * Copyright Siemens AG, 2014
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.siemens.oss.omniproperties.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.oss.omniproperties.Initializable;
import com.siemens.oss.omniproperties.ObjectBuilder;
import com.siemens.oss.omniproperties.exceptions.InjectionException;
import com.siemens.oss.omniproperties.exceptions.ParseException;
import com.siemens.oss.omniproperties.exceptions.PropertyInstantiationException;
import com.siemens.oss.omniproperties.exceptions.WrongClassException;

/**
 * Bundles all reflection related helper methods. This class is not intended to
 * by called by the user directly.
 * 
 * @author Markus Michael Geipel
 * 
 */
final public class ReflectionUtil {

	private final static Map<Class<?>, Class<?>> BASETYPE_TO_WRAPPER_MAP = new ConcurrentHashMap<>();

	static {
		BASETYPE_TO_WRAPPER_MAP.put(int.class, Integer.class);
		BASETYPE_TO_WRAPPER_MAP.put(long.class, Long.class);
		BASETYPE_TO_WRAPPER_MAP.put(float.class, Float.class);
		BASETYPE_TO_WRAPPER_MAP.put(boolean.class, Boolean.class);
		BASETYPE_TO_WRAPPER_MAP.put(double.class, Double.class);
		BASETYPE_TO_WRAPPER_MAP.put(float.class, Float.class);
		BASETYPE_TO_WRAPPER_MAP.put(char.class, Character.class);
		BASETYPE_TO_WRAPPER_MAP.put(byte.class, Byte.class);
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(ReflectionUtil.class);
	private static final String PROPERTIES_FILENAME = "omniproperties-class-shortcuts.properties";

	private static final Properties CLASS_SHORTCUTS = new Properties();
	static {
		try {
			final Enumeration<URL> enumeration = Thread.currentThread()
					.getContextClassLoader().getResources(PROPERTIES_FILENAME);
			while (enumeration.hasMoreElements()) {
				final URL url = enumeration.nextElement();
				LOG.debug("loading " + url);
				CLASS_SHORTCUTS.load(url.openStream());
			}
		} catch (IOException e) {
			throw new ParseException("Error initializing OmniProperties", e);
		}
		LOG.debug("Loaded class shortcuts " + CLASS_SHORTCUTS);
	}

	private ReflectionUtil() {
		// no instances
	}

	public static void init(final Object obj) {
		if (obj instanceof Initializable) {
			final Initializable initializable = (Initializable) obj;
			initializable.init();
		}
	}

	public static Object mergeArrays(Object array1, Object array2) {

		final ArrayList<Object> list = new ArrayList<Object>();

		int length = Array.getLength(array1);
		for (int i = 0; i < length; i++) {
			list.add(Array.get(array1, i));
		}

		length = Array.getLength(array2);
		for (int i = 0; i < length; i++) {
			list.add(Array.get(array2, i));
		}

		return createArray(list, normalizeType(array1.getClass()
				.getComponentType()));
	}

	public static void inject(final Map<String, Object> properties,
			final Object object) throws InjectionException {
		for (Entry<String, Object> entry : properties.entrySet()) {
			inject(entry.getValue(), entry.getKey(), object);
		}
	}

	private static boolean injectByPut(Object value, String key, Object object) {
		final Class<?> clazz = object.getClass();

		try {
			final Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				final Class<?>[] parameterTypes = method.getParameterTypes();
				if (method.getName().equals("put")
						&& parameterTypes.length == 2
						&& parameterTypes[0].isAssignableFrom(String.class)
						&& normalizeType(parameterTypes[1]).isAssignableFrom(
								value.getClass())) {
					method.setAccessible(true);
					method.invoke(object, key, value);
					return true;
				}
			}
			return false;
		} catch (SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new PropertyInstantiationException(e);
		}
	}

	private static boolean injectBySetter(Object value, String key,
			Object object) {
		final Class<?> type = object.getClass();
		final String methodName = "set" + key.substring(0, 1).toUpperCase()
				+ key.substring(1);
		try {
			final Method[] methods = type.getMethods();
			for (Method method : methods) {
				final Class<?>[] parameterTypes = method.getParameterTypes();
				if (method.getName().equals(methodName)
						&& parameterTypes.length == 1
						&& normalizeType(parameterTypes[0]).isAssignableFrom(
								value.getClass())) {
					method.setAccessible(true);
					method.invoke(object, value);
					return true;
				}
			}
			return false;
		} catch (SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new PropertyInstantiationException(e);
		}
	}

	private static boolean injectByField(Object value, String key, Object object) {
		final Class<?> type = object.getClass();
		try {
			final Field field = type.getDeclaredField(key);
			field.setAccessible(true);
			field.set(object, value);

			return true;
		} catch (NoSuchFieldException e) {
			return false;
		} catch (SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			throw new PropertyInstantiationException(e);
		}
	}

	private static void inject(Object value, String key, Object object) {
		if (!(injectBySetter(value, key, object)
				|| injectByField(value, key, object) || injectByPut(value, key,
					object))) {
			throw new PropertyInstantiationException("Argument '" + key
					+ "' could not be injected into class " + object.getClass()
					+ ". No approriate setter, field or put method.");
		}
	}

	public static Object newInstanceOf(final String className,
			final Object... args) {
		return newInstanceOf(classForName(className), args);

	}

	public static Object buildIfBuilder(final Object instance) throws Exception {
		if (instance instanceof ObjectBuilder<?>) {
			final ObjectBuilder<?> builder = (ObjectBuilder<?>) instance;
			return builder.build();
		}
		return instance;
	}

	public static Object createArray(final List<?> args) {
		return createArray(args, null);
	}

	public static Object createArray(final List<?> args, Class<?> type) {

		if (type == null) {
			if (args.isEmpty()) {
				return new Object[] {};
			} else {
				type = args.get(0).getClass();
			}
		}

		try {

			if (type == Boolean.class) {
				final boolean[] array = new boolean[args.size()];
				for (int i = 0; i < args.size(); i++) {
					array[i] = ((Boolean) args.get(i)).booleanValue();
				}
				return array;
			}

			if (type == Byte.class) {
				final byte[] array = new byte[args.size()];
				for (int i = 0; i < args.size(); i++) {
					array[i] = ((Byte) args.get(i)).byteValue();
				}
				return array;
			}

			if (type == Character.class) {
				final char[] array = new char[args.size()];
				for (int i = 0; i < args.size(); i++) {
					array[i] = ((Character) args.get(i)).charValue();
				}
				return array;
			}

			if (type == Integer.class) {
				final int[] array = new int[args.size()];
				for (int i = 0; i < args.size(); i++) {
					array[i] = ((Integer) args.get(i)).intValue();
				}
				return array;

			}
			if (type == Long.class) {
				final long[] array = new long[args.size()];
				for (int i = 0; i < args.size(); i++) {
					array[i] = ((Long) args.get(i)).longValue();
				}
				return array;

			}
			if (type == Double.class) {
				final double[] array = new double[args.size()];
				for (int i = 0; i < args.size(); i++) {
					array[i] = ((Double) args.get(i)).doubleValue();
				}
				return array;

			}

			if (type == Float.class) {
				final float[] array = new float[args.size()];
				for (int i = 0; i < args.size(); i++) {
					array[i] = ((Float) args.get(i)).floatValue();
				}
				return array;

			}

			final Object[] array = (Object[]) Array.newInstance(type,
					args.size());
			for (int i = 0; i < args.size(); i++) {
				if (!type.isAssignableFrom(args.get(i).getClass())) {
					throw new ClassCastException(args.get(i) + " of class "
							+ args.get(i).getClass()
							+ " cannot be assigned to array of type " + type);
				}
				array[i] = args.get(i);
			}
			return array;
		} catch (ClassCastException e) {
			throw new WrongClassException("Array has mixed types", e);
		}

	}

	public static <T> T newInstanceOf(final Class<T> clazz,
			final Object... args) {
		T instance;
		try {
			instance = findConstructor(clazz, args).newInstance(args);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new PropertyInstantiationException(e);
		}

		return instance;
	}

	public static Class<?> normalizeType(final Class<?> type) {
		if (BASETYPE_TO_WRAPPER_MAP.containsKey(type)) {
			return BASETYPE_TO_WRAPPER_MAP.get(type);
		}
		return type;
	}

	private static Class<?>[] normalizeTypes(final Class<?>[] args) {

		for (int i = 0; i < args.length; i++) {
			args[i] = normalizeType(args[i]);
		}
		return args;
	}

	private static Class<?>[] toClassArray(final Object[] args) {
		final Class<?>[] classes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			classes[i] = args[i].getClass();
		}
		return classes;
	}

	@SuppressWarnings("unchecked")
	private static <T> Constructor<T> findConstructor(final Class<T> clazz,
			final Object... contructorArgs) throws NoSuchMethodException {
		Constructor<?> foundConstructor = null;
		final Class<?>[] inputArgTypes = normalizeTypes(toClassArray(contructorArgs));
		for (Constructor<?> constructor : clazz.getConstructors()) {
			final Class<?>[] constructorArgTypes = normalizeTypes(constructor
					.getParameterTypes());

			boolean correct = true;
			if (constructorArgTypes.length == contructorArgs.length) {
				for (int i = 0; i < constructorArgTypes.length; ++i) {
					final Class<?> contructorArgType = constructorArgTypes[i];
					final Class<?> inputArgType = inputArgTypes[i];
					if (!contructorArgType.isAssignableFrom(inputArgType)) {
						correct = false;
						break;
					}
				}
				if (correct) {
					if (foundConstructor == null) {
						foundConstructor = constructor;

					} else {
						throw new PropertyInstantiationException(
								"More than one fitting contructor found in class "
										+ clazz.getName() + " for arguments "
										+ Arrays.toString(inputArgTypes));
					}
				}
			}
		}
		if (foundConstructor == null) {
			throw new PropertyInstantiationException(
					"no appropriate constructor found for class " + clazz);
		}
		return (Constructor<T>) foundConstructor;
	}

	public static ClassLoader getClassLoader() {
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		if (loader == null) {
			throw new PropertyInstantiationException(
					"Class loader could not be found.");
		}
		return loader;
	}

	public static Class<?> classForName(final String className) {
		try {
			return getClassLoader().loadClass(
					CLASS_SHORTCUTS.getProperty(className, className));
		} catch (ClassNotFoundException e) {
			throw new PropertyInstantiationException(e);
		}
	}

	public static Object getMember(Object obj, String name) {
		try {
			final String getterName = "get"
					+ name.substring(0, 1).toUpperCase() + name.substring(1);
			final Method getter = obj.getClass().getMethod(getterName,
					new Class[] {});
			return getter.invoke(obj);

		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new PropertyInstantiationException(e);
		}
	}

	public static void setMember(Object obj, String name, Object argument) {
		final String setterName = "set" + name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		try {
			final Method getter = obj.getClass().getMethod(setterName,
					argument.getClass());
			getter.invoke(obj, argument);

		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new PropertyInstantiationException(e);
		}
	}

}

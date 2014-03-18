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

package com.siemens.oss.omniproperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.siemens.oss.omniproperties.exceptions.PropertyNotFoundException;
import com.siemens.oss.omniproperties.exceptions.WrongClassException;
import com.siemens.oss.omniproperties.util.OmniPropertiesReader;
import com.siemens.oss.omniproperties.validation.OValValidator;

/**
 * 
 * {@link OmniProperties} are organized as a {@link Map} with additional typed access methods.
 * A new {@link OmniProperties} object is created by the static <code>create()</code> method.
 * For a documentation of the OmniProperties file format and further details on object creation in OmniProperties see the README.md.
 * 
 * 
 * @author Markus Michael Geipel
 * 
 */
public abstract class OmniProperties implements Map<String, Object> {

	public static final String SELF = "self";
	
	private Validator validator = new OValValidator();
	
	/**
	 * @return a new {@link OmniProperties} object.
	 */
	public static OmniProperties create() {
		final OmniProperties properties = new MapOmniProperties();
		properties.put(SELF, properties);
		return properties;
	}
	
	public final Validator getValidator() {
		return validator;
	}
	
	/**
	 * Sets the validator used to validate objects ofter creation and setter invocation.
	 * @param validator
	 */
	public final void setValidator(Validator validator) {
		this.validator = validator;
	}

	/**
	 * Get an integer property
	 * 
	 * @param key
	 *            name of property
	 * @return integer value
	 * @throws PropertyNotFoundException
	 *             if missing
	 */
	public abstract int getInt(String key);

	/**
	 * Get an integer property
	 * 
	 * @param key
	 *            name of property
	 * @param def
	 *            default value
	 * @return integer value if existing, else default
	 */
	public abstract int getInt(String key, int def);

	/**
	 * Get an long property
	 * 
	 * @param key
	 *            name of property
	 * @return integer value
	 * @throws PropertyNotFoundException
	 *             if missing
	 */
	public abstract long getLong(String key);

	/**
	 * Get an long property
	 * 
	 * @param key
	 *            name of property
	 * @param def
	 *            default value
	 * @return integer value if existing, else default
	 */
	public abstract long getLong(String key, long def);

	/**
	 * Get a double property
	 * 
	 * @param key
	 *            name of property
	 * @return double value
	 * @throws PropertyNotFoundException
	 *             if missing
	 */
	public abstract double getDouble(String key);

	/**
	 * Get an double property
	 * 
	 * @param key
	 *            name of property
	 * @param def
	 *            default value
	 * @return double value if existing, else default
	 */
	public abstract double getDouble(String key, double def);

	/**
	 * Get a float property
	 * 
	 * @param key
	 *            name of property
	 * @return double value
	 * @throws PropertyNotFoundException
	 *             if missing
	 */
	public abstract float getFloat(String key);

	/**
	 * Get an float property
	 * 
	 * @param key
	 *            name of property
	 * @param def
	 *            default value
	 * @return double value if existing, else default
	 */
	public abstract float getFloat(String key, float def);

	/**
	 * Get a string property
	 * 
	 * @param key
	 *            name of property
	 * @return string value
	 * @throws PropertyNotFoundException
	 *             if missing
	 */
	public abstract String getString(String key);

	/**
	 * Get an string property
	 * 
	 * @param key
	 *            name of property
	 * @param def
	 *            default value
	 * @return string value if existing, else default
	 */
	public abstract String getString(String key, String def);

	/**
	 * Get an boolean property
	 * 
	 * @param key
	 *            name of property
	 * @return boolean value if existing, else default
	 */
	public abstract boolean getBoolean(String key);

	/**
	 * Get an boolean property
	 * 
	 * @param key
	 *            name of property
	 * @param def
	 *            default value
	 * @return boolean value if existing, else default
	 */
	public abstract boolean getBoolean(String key, boolean def);

	/**
	 * Get an object
	 * 
	 * @param key
	 *            name of property
	 * @param type
	 *            type of Object
	 * 
	 * @return Object of type 'type'
	 * @throws PropertyNotFoundException
	 *             if missing
	 * @throws WrongClassException
	 *             if existing but of incompatible type
	 */

	public abstract <T> T getObject(String key, Class<T> type);

	/**
	 * Get an object
	 * 
	 * @param key
	 *            name of property
	 * @param def
	 *            default object
	 * @param type
	 *            type of object
	 * @return object from properties if existing, else default
	 * @throws WrongClassException
	 *             if existing but of incompatible type
	 */
	public abstract <T> T getObject(String key, T def, Class<T> type);

	public abstract boolean containsString(String key);

	public abstract boolean containsInt(String key);

	public abstract boolean containsLong(String key);

	public abstract boolean containsBoolean(String key);

	public abstract boolean containsDouble(String key);

	public abstract boolean containsFloat(String key);

	public abstract <T> boolean containsObject(String key, Class<T> type);

	/**
	 * Read properties from an {@link InputStream}. Read properties are
	 * <em>added</em> to existing properties.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public OmniProperties readFromStream(final InputStream input) throws IOException {
		OmniPropertiesReader.readFromStream(input, this);
		return this;
	}

	/**
	 * Read properties from a {@link File}. Read properties are <em>added</em>
	 * to existing properties.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public OmniProperties readFromFile(final File file) throws IOException {
		OmniPropertiesReader.readFromFile(file, this);
		return this;
	}

	/**
	 * Read properties from a string. Read properties are <em>added</em> to
	 * existing properties.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public OmniProperties readFromString(final String string) throws IOException {
		OmniPropertiesReader.readFromString(string, this);
		return this;
	}

	/**
	 * Read properties from a resource. Read properties are <em>added</em> to
	 * existing properties.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public OmniProperties readFromResource(final String resource) throws IOException {
		OmniPropertiesReader.readFromResource(resource, this);
		return this;
	}
	
	/**
	 * Read properties from a {@link File}. Read properties are <em>added</em>
	 * to existing properties.
	 * 
	 * @param url
	 * @throws IOException
	 */
	public OmniProperties readFromUrl(final URL url) throws IOException {
		OmniPropertiesReader.readFromUrl(url, this);
		return this;
	}

	/**
	 * Read properties from a file. Read properties are <em>added</em> to
	 * existing properties.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public OmniProperties readFromFile(final String fileName) throws IOException {
		OmniPropertiesReader.readFromFile(fileName, this);
		return this;
	}


	/**
	 * @return Java properties containing original properties converted to {@link String}s 
	 */
	public final Properties toJavaProperties() {
		final Properties properties = new Properties();
		for (Entry<String, Object> entry : entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue().toString());
		}
		return properties;
	}

}
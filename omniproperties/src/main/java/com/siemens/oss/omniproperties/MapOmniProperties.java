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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.siemens.oss.omniproperties.exceptions.PropertyNotFoundException;
import com.siemens.oss.omniproperties.exceptions.WrongClassException;

/**
 * {@link HashMap} based implementation of {@link OmniProperties}
 * 
 * @author Markus Michael Geipel
 */
public final class MapOmniProperties extends  OmniProperties {

	private final Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * use {@link OmniProperties}.create();
	 */
	protected MapOmniProperties() {
		// package private
	}
	
	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getInt(java.lang.String)
	 */
	@Override
	public int getInt(final String key) {

		return getObject(key, Integer.class).intValue();
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(final String key, final int def) {
		return getObject(key, Integer.valueOf(def), Integer.class).intValue();
	}
	
	
	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#getLong(java.lang.String)
	 */
	@Override
	public long getLong(final String key) {
		if(containsInt(key)){
			return getObject(key, Integer.class).longValue();
		}
		return getObject(key, Long.class).longValue();
	}


	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#getLong(java.lang.String, long)
	 */
	@Override
	public long getLong(final String key, final long def) {
		if(containsInt(key)){
			return getObject(key, Integer.class).longValue();
		}
		return getObject(key, def, Long.class).longValue();
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(final String key) {
		if(containsFloat(key)){
			return getObject(key, Float.class).doubleValue();
		}
		return getObject(key, Double.class).doubleValue();
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getDouble(java.lang.String, int)
	 */
	@Override
	public double getDouble(final String key, final double def) {
		if(containsFloat(key)){
			return getObject(key, Float.class).doubleValue();
		}
		return getObject(key, def, Double.class).doubleValue();
	}
	
	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(final String key) {
		return getObject(key, Float.class).floatValue();
	}


	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#getFloat(java.lang.String, float)
	 */
	@Override
	public float getFloat(final String key, final float def) {
		return getObject(key, Float.valueOf(def), Float.class).floatValue();
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getString(java.lang.String)
	 */
	@Override
	public String getString(final String key) {
		return getObject(key, String.class);
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(final String key, final String def) {
		return getObject(key, def, String.class);
	}
	
	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String key) {
		return getObject(key, Boolean.class).booleanValue();
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#getBoolean(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean getBoolean(String key, boolean def) {
		return getObject(key, Boolean.valueOf(def), Boolean.class).booleanValue();
	}



	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getObject(java.lang.String, java.lang.Class)
	 */

	@Override
	public <T> T getObject(final String key, final Class<T> type) {
		final T result = getObject(key, null, type);
		if (result == null) {
			throw new PropertyNotFoundException("No property '" + key
					+ "' of type " + type.getSimpleName() + " found");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#getObject(java.lang.String, T, java.lang.Class)
	 */
	@Override
	public <T> T getObject(final String key, final T def, final Class<T> type) {
		final Object value = map.get(key);
		if (value == null) {
			return def;
		} else if (type.isAssignableFrom(value.getClass())) {
			return type.cast(value);
		} else {
			throw new WrongClassException("Property '" + key
					+ "' expected to be of type '" + type.getName()
					+ "' but has incompatible type '"
					+ value.getClass().getName() + "'");
		}
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#containsString(java.lang.String)
	 */
	@Override
	public boolean containsString(final String key) {
		return containsObject(key, String.class);
	}
	
	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#containsBoolean(java.lang.String)
	 */
	@Override
	public boolean containsBoolean(String key) {
		return containsObject(key, Boolean.class);
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#containsInt(java.lang.String)
	 */
	@Override
	public boolean containsInt(final String key) {
		return containsObject(key, Integer.class) ;
	}
	
	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#containsLong(java.lang.String)
	 */
	@Override
	public boolean containsLong(final String key) {
		return containsObject(key, Long.class) || containsObject(key, Integer.class);
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#containsDouble(java.lang.String)
	 */
	@Override
	public boolean containsDouble(final String key) {
		return containsObject(key, Double.class) || containsObject(key, Float.class);
	}
	
	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.OmniProperties#containsFloat(java.lang.String)
	 */
	@Override
	public boolean containsFloat(final String key) {
		return  containsObject(key, Float.class);
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#containsObject(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> boolean containsObject(final String key, final Class<T> type) {
		final Object object = map.get(key);
		return object != null && type.isAssignableFrom(object.getClass());
	}

	public void clear() {
		map.clear();
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return map.containsValue(arg0);
	}

	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object arg0) {
		return map.equals(arg0);
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#get(java.lang.Object)
	 */
	@Override
	public Object get(Object arg0) {
		return map.get(arg0);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	/* (non-Javadoc)
	 * @see com.siemens.oss.omniproperties.IOmniProperties#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object put(String arg0, Object arg1) {
		return map.put(arg0, arg1);
	}

	public void putAll(Map<? extends String, ? extends Object> arg0) {
		map.putAll(arg0);
	}

	public Object remove(Object arg0) {
		return map.remove(arg0);
	}

	public int size() {
		return map.size();
	}

	public Collection<Object> values() {
		return map.values();
	}
	
	@Override
	public String toString() {
		final List<String> keys = new ArrayList<>(keySet());
		Collections.sort(keys);
		final StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (String key : keys) {
			if(!key.equals(SELF)){
				builder.append(key + "=" + get(key) + ", ");
			}
		}
		builder.append("}");
		
		return builder.toString();
	}



}

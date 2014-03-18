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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implements scoping for {@link OmniProperties}. Variables in the inner
 * {@link OmniProperties} shadow the ones in the outer {@link OmniProperties}.
 * 
 * @author Markus Michel Geipel
 * 
 */
public final class ScopedOmniProperties extends OmniProperties {
	final private OmniProperties innerProperties;
	final private OmniProperties outerProperties;

	public ScopedOmniProperties(final OmniProperties outerProperties) {
		this.outerProperties = outerProperties;
		this.innerProperties = OmniProperties.create();
	}

	public ScopedOmniProperties(final OmniProperties outerProperties,
			final OmniProperties innerProperties) {
		this.outerProperties = outerProperties;
		this.innerProperties = innerProperties;
	}

	public OmniProperties getOuterScope() {
		return outerProperties;
	}

	public int getInt(String key) {
		if (innerProperties.containsInt(key)) {
			return innerProperties.getInt(key);
		}
		return outerProperties.getInt(key);
	}

	public int getInt(String key, int def) {
		if (innerProperties.containsInt(key)) {
			return innerProperties.getInt(key, def);
		} else {
			return outerProperties.getInt(key, def);
		}
	}

	public long getLong(String key) {
		if (innerProperties.containsLong(key)) {
			return innerProperties.getLong(key);
		}
		return outerProperties.getLong(key);
	}

	public long getLong(String key, long def) {
		if (innerProperties.containsLong(key)) {
			return innerProperties.getLong(key, def);
		} else {
			return outerProperties.getLong(key, def);
		}
	}

	public double getDouble(String key) {
		if (innerProperties.containsDouble(key)) {
			return innerProperties.getDouble(key);
		}
		return outerProperties.getDouble(key);
	}

	public double getDouble(String key, double def) {
		if (innerProperties.containsDouble(key)) {
			return innerProperties.getDouble(key, def);
		} else {
			return outerProperties.getDouble(key, def);
		}
	}

	public float getFloat(String key) {
		if (innerProperties.containsFloat(key)) {
			return innerProperties.getFloat(key);
		}
		return outerProperties.getFloat(key);
	}

	public float getFloat(String key, float def) {
		if (innerProperties.containsFloat(key)) {
			return innerProperties.getFloat(key, def);
		} else {
			return outerProperties.getFloat(key, def);
		}
	}

	public String getString(String key) {
		if (innerProperties.containsString(key)) {
			return innerProperties.getString(key);
		}
		return outerProperties.getString(key);
	}

	public String getString(String key, String def) {
		if (innerProperties.containsString(key)) {
			return innerProperties.getString(key, def);
		} else {
			return outerProperties.getString(key, def);
		}
	}

	public <T> T getObject(String key, Class<T> type) {
		if (innerProperties.containsObject(key, type)) {
			return innerProperties.getObject(key, type);
		}
		return outerProperties.getObject(key, type);
	}

	public <T> T getObject(String key, T def, Class<T> type) {
		if (innerProperties.containsObject(key, type)) {
			return innerProperties.getObject(key, def, type);
		} else {
			return outerProperties.getObject(key, def, type);
		}
	}

	public boolean containsString(String key) {
		return innerProperties.containsString(key)
				|| outerProperties.containsString(key);
	}

	public boolean containsInt(String key) {
		return innerProperties.containsInt(key)
				|| outerProperties.containsInt(key);
	}

	public boolean containsLong(String key) {
		return innerProperties.containsLong(key)
				|| outerProperties.containsLong(key);
	}

	public boolean containsDouble(String key) {
		return innerProperties.containsDouble(key)
				|| outerProperties.containsDouble(key);
	}

	public boolean containsFloat(String key) {
		return innerProperties.containsFloat(key)
				|| outerProperties.containsFloat(key);
	}

	public <T> boolean containsObject(String key, Class<T> type) {
		return innerProperties.containsObject(key, type)
				|| outerProperties.containsObject(key, type);
	}

	public void clear() {
		innerProperties.clear();
	}

	public boolean containsKey(Object arg0) {
		return innerProperties.containsKey(arg0)
				|| outerProperties.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return innerProperties.containsValue(arg0)
				|| outerProperties.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		final Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(outerProperties);
		map.putAll(innerProperties);
		return map.entrySet();
	}

	public boolean equals(Object arg0) {
		if (arg0 instanceof ScopedOmniProperties) {
			ScopedOmniProperties other = (ScopedOmniProperties) arg0;
			return other.innerProperties.equals(innerProperties)
					&& other.outerProperties.equals(outerProperties);
		}

		return false;
	}

	public Object get(Object arg0) {
		if (innerProperties.containsKey(arg0)) {
			return innerProperties.get(arg0);
		}
		return outerProperties.get(arg0);
	}

	public int hashCode() {
		return 997 * (innerProperties.hashCode()) ^ 991
				* (outerProperties.hashCode());
	}

	public boolean isEmpty() {
		return innerProperties.isEmpty() && outerProperties.isEmpty();
	}

	public Set<String> keySet() {
		final Set<String> keys = new HashSet<String>();
		keys.addAll(innerProperties.keySet());
		keys.addAll(outerProperties.keySet());

		return keys;
	}

	public Object put(String arg0, Object arg1) {
		return innerProperties.put(arg0, arg1);
	}

	public void putAll(Map<? extends String, ? extends Object> arg0) {
		innerProperties.putAll(arg0);
	}

	public Object remove(Object arg0) {
		if (innerProperties.containsKey(arg0)) {
			return innerProperties.remove(arg0);
		}
		return outerProperties.remove(arg0);
	}

	public int size() {
		return innerProperties.size() + outerProperties.size();
	}

	public Collection<Object> values() {
		final Collection<Object> values = new ArrayList<Object>(size());
		values.addAll(innerProperties.values());
		values.addAll(outerProperties.values());
		return values;
	}

	public String toString() {
		return outerProperties.toString() + " > " + innerProperties.toString();
	}

	@Override
	public boolean getBoolean(String key) {
		if (innerProperties.containsBoolean(key)) {
			return innerProperties.getBoolean(key);
		}
		return outerProperties.getBoolean(key);

	}

	@Override
	public boolean getBoolean(String key, boolean def) {
		if (innerProperties.containsBoolean(key)) {
			return innerProperties.getBoolean(key, def);
		}
		return outerProperties.getBoolean(key, def);
	}

	@Override
	public boolean containsBoolean(String key) {
		return innerProperties.containsBoolean(key)
				|| outerProperties.containsBoolean(key);
	}

}

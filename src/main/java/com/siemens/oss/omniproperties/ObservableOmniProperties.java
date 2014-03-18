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
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Wraps {@link OmniProperties} and notifies an {@link OmniPropertyObserver} of any changes.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class ObservableOmniProperties extends OmniProperties {

	private final OmniProperties properties;
	private final OmniPropertyObserver observer;
	
	private ObservableOmniProperties(final OmniProperties properties, OmniPropertyObserver observer) {
		this.properties = properties;
		this.observer = observer;
	}
	
	public static OmniProperties wrap(final OmniProperties properties, final OmniPropertyObserver observer){
		observer.setOmniproperties(properties);
		return new ObservableOmniProperties(properties, observer);
	}
	
	
	public void clear() {
		observer.clear();
		properties.clear();
	}

	public int getInt(String key) {
		observer.getInt(key);
		return properties.getInt(key);
	}

	public double getDouble(String key) {
		observer.getDouble(key);
		return properties.getDouble(key);
	}

	public String getString(String key) {
		observer.getString(key);
		return properties.getString(key);
	}

	public boolean getBoolean(String key) {
		observer.getBoolean(key);
		return properties.getBoolean(key);
	}

	public boolean containsString(String key) {
		observer.containsString(key);
		return properties.containsString(key);
	}

	public boolean containsInt(String key) {
		observer.containsInt(key);
		return properties.containsInt(key);
	}

	public boolean containsBoolean(String key) {
		observer.containsBoolean(key);
		return properties.containsBoolean(key);
	}

	public boolean containsDouble(String key) {
		observer.containsDouble(key);
		return properties.containsDouble(key);
	}

	public boolean containsKey(Object key) {
		observer.containsKey(key);
		return properties.containsKey(key);
	}

	public <T> boolean containsObject(String key, Class<T> type) {
		observer.containsObject(key, type);
		return properties.containsObject(key, type);
	}

	public boolean containsValue(Object value) {
		observer.containsValue(value);
		return properties.containsValue(value);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return properties.entrySet();
	}

	public boolean equals(Object obj) {
		return properties.equals(obj);
	}

	public Object get(Object key) {
		observer.get(key);
		return properties.get(key);
	}

	public int getInt(String key, int def) {
		observer.getInt(key,def);
		return properties.getInt(key, def);
	}

	public double getDouble(String key, double def) {
		observer.getDouble(key,def);
		return properties.getDouble(key, def);
	}

	public String getString(String key, String def) {
		observer.getString(key,def);
		return properties.getString(key, def);
	}

	public boolean getBoolean(String key, boolean def) {
		observer.getBoolean(key,def);
		return properties.getBoolean(key, def);
	}

	public <T> T getObject(String key, Class<T> type) {
		observer.getObject(key,type);
		return properties.getObject(key, type);
	}

	public <T> T getObject(String key, T def, Class<T> type) {
		observer.getObject(key,def,type);
		return properties.getObject(key, def, type);
	}

	public int hashCode() {
		return properties.hashCode();
	}

	public boolean isEmpty() {
		return properties.isEmpty();
	}

	public Set<String> keySet() {
		observer.keySet();
		return properties.keySet();
	}

	public Object put(String key, Object value) {
		observer.put(key,value);

		return properties.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		observer.putAll(m);
		properties.putAll(m);
	}

	public OmniProperties readFromStream(InputStream input) throws IOException {
		observer.readFromStream(input);
		properties.readFromStream(input);
		return this;
	}

	public OmniProperties readFromFile(File file) throws IOException {
		observer.readFromFile(file);
		properties.readFromFile(file);
		return this;
	}

	public OmniProperties readFromResource(String string) throws IOException {
		observer.readFromResource(string);
		properties.readFromResource(string);
		return this;
	}

	public OmniProperties readFromFile(String string) throws IOException {
		observer.readFromFile(string);
		properties.readFromFile(string);
		return this;
	}
	
	public OmniProperties readFromUrl(URL url) throws IOException {
		observer.readFromUrl(url);
		properties.readFromUrl(url);
		return this;
	}
	
	public OmniProperties readFromString(String string) throws IOException {
		observer.readFromString(string);
		properties.readFromString(string);
		return this;
	}

	public Object remove(Object key) {
		observer.remove(key);
		return properties.remove(key);
	}

	public int size() {
		return properties.size();
	}

	public String toString() {
		return properties.toString();
	}

	public Collection<Object> values() {
		return properties.values();
	}

	public long getLong(String key) {
		observer.getLong(key);
		return properties.getLong(key);
	}

	public long getLong(String key, long def) {
		observer.getLong(key, def);
		return properties.getLong(key, def);
	}


	public float getFloat(String key) {
		observer.getFloat(key);
		return properties.getFloat(key);
	}

	public float getFloat(String key, float def) {
		observer.getFloat(key, def);
		return properties.getFloat(key, def);
	}

	public boolean containsLong(String key) {
		observer.containsLong(key);
		return properties.containsLong(key);
	}

	public boolean containsFloat(String key) {
		observer.containsFloat(key);
		return properties.containsFloat(key);
	}
}

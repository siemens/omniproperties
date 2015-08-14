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
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Wraps {@link OmniProperties} and 
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class LockableOmniProperties extends OmniProperties implements ReadWriteLock {


	private static final long serialVersionUID = 1L;
	private final OmniProperties properties;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private LockableOmniProperties(final OmniProperties properties) {
		this.properties = properties;
	}

	public static LockableOmniProperties wrap(final OmniProperties properties) {
		return new LockableOmniProperties(properties);
	}
	


	public void clear() {
		try {
			lock.writeLock().lock();
			properties.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public int getInt(String key) {
		try {
			lock.readLock().lock();
			return properties.getInt(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getDouble(String key) {
		try {
			lock.readLock().lock();
			return properties.getDouble(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public String getString(String key) {
		try {
			lock.readLock().lock();
			return properties.getString(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean getBoolean(String key) {
		try {
			lock.readLock().lock();
			return properties.getBoolean(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsString(String key) {
		try {
			lock.readLock().lock();
			return properties.containsString(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsInt(String key) {
		try {
			lock.readLock().lock();
			return properties.containsInt(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsBoolean(String key) {
		try {
			lock.readLock().lock();
			return properties.containsBoolean(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsDouble(String key) {
		try {
			lock.readLock().lock();
			return properties.containsDouble(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsKey(Object key) {
		try {
			lock.readLock().lock();
			return properties.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public <T> boolean containsObject(String key, Class<T> type) {
		try {
			lock.readLock().lock();
			return properties.containsObject(key, type);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsValue(Object value) {
		try {
			lock.readLock().lock();
			return properties.containsValue(value);
		} finally {
			lock.readLock().unlock();
		}
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return Collections.unmodifiableSet(properties.entrySet());
	}

	public boolean equals(Object obj) {
		try {
			lock.readLock().lock();
			return properties.equals(obj);
		} finally {
			lock.readLock().unlock();
		}
	}

	public Object get(Object key) {
		try {
			lock.readLock().lock();
			return properties.get(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public int getInt(String key, int def) {
		try {
			lock.readLock().lock();
			return properties.getInt(key, def);
		} finally {
			lock.readLock().unlock();
		}
	}

	public double getDouble(String key, double def) {
		try {
			lock.readLock().lock();
			return properties.getDouble(key, def);
		} finally {
			lock.readLock().unlock();
		}
	}

	public String getString(String key, String def) {
		try {
			lock.readLock().lock();
			return properties.getString(key, def);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean getBoolean(String key, boolean def) {
		try {
			lock.readLock().lock();
			return properties.getBoolean(key, def);
		} finally {
			lock.readLock().unlock();
		}
	}

	public <T> T getObject(String key, Class<T> type) {
		try {
			lock.readLock().lock();
			return properties.getObject(key, type);
		} finally {
			lock.readLock().unlock();
		}
	}

	public <T> T getObject(String key, T def, Class<T> type) {
		try {
			lock.readLock().lock();
			return properties.getObject(key, def, type);
		} finally {
			lock.readLock().unlock();
		}
	}

	public int hashCode() {
		try {
			lock.readLock().lock();
			return properties.hashCode();
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean isEmpty() {
		try {
			lock.readLock().lock();
			return properties.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	public Set<String> keySet() {
		try {
			lock.readLock().lock();
			return Collections.unmodifiableSet(properties.keySet());
		} finally {
			lock.readLock().unlock();
		}

	}

	public Object put(String key, Object value) {
		try {
			lock.writeLock().lock();
			return properties.put(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void putAll(Map<? extends String, ? extends Object> m) {

		try {
			lock.writeLock().lock();
			properties.putAll(m);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public OmniProperties readFromStream(InputStream input) throws IOException {
		try {
			lock.writeLock().lock();
			properties.readFromStream(input);
			return this;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public OmniProperties readFromFile(File file) throws IOException {
		try {
			lock.writeLock().lock();
			properties.readFromFile(file);
			return this;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public OmniProperties readFromResource(String string) throws IOException {
		try {
			lock.writeLock().lock();
			properties.readFromResource(string);
			return this;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public OmniProperties readFromFile(String string) throws IOException {
		try {
			lock.writeLock().lock();
			properties.readFromFile(string);
			return this;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public OmniProperties readFromUrl(URL url) throws IOException {
		try {
			lock.writeLock().lock();
			properties.readFromUrl(url);
			return this;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public OmniProperties readFromString(String string) throws IOException {
		try {
			lock.writeLock().lock();
			properties.readFromString(string);
			return this;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Object remove(Object key) {
		try {
			lock.writeLock().lock();
			return properties.remove(key);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public int size() {
		try {
			lock.readLock().lock();
			return properties.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	public String toString() {

		try {
			lock.readLock().lock();
			return properties.toString();
		} finally {
			lock.readLock().unlock();
		}
	}

	public Collection<Object> values() {
		return Collections.unmodifiableCollection(properties.values());
	}

	public long getLong(String key) {
		try {
			lock.readLock().lock();
			return properties.getLong(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public long getLong(String key, long def) {
		try {
			lock.readLock().lock();
			return properties.getLong(key, def);
		} finally {
			lock.readLock().unlock();
		}
	}

	public float getFloat(String key) {
		try {
			lock.readLock().lock();
			return properties.getFloat(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public float getFloat(String key, float def) {
		try {
			lock.readLock().lock();
			return properties.getFloat(key, def);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsLong(String key) {
		try {
			lock.readLock().lock();
			return properties.containsLong(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean containsFloat(String key) {
		try {
			lock.readLock().lock();
			return properties.containsFloat(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Lock readLock() {
		return lock.readLock();
	}

	@Override
	public Lock writeLock() {
		return lock.writeLock();
	}
}

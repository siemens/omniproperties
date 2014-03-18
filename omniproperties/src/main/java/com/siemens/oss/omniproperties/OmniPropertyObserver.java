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
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Receiver of change notifications. Used in conjunction with {@link ObservableOmniProperties}.
 * 
 * @author Markus Michael Geipel
 *
 */
public interface OmniPropertyObserver {

	void setOmniproperties(OmniProperties properties);
	
	void clear();

	void getInt(String key);

	void getDouble(String key);

	void getString(String key);

	void getBoolean(String key);

	void containsString(String key);

	void containsInt(String key);

	void containsValue(Object value);

	void getInt(String key, int def);

	void get(Object key);

	void getDouble(String key, double def);

	void getBoolean(String key, boolean def);

	void getString(String key, String def);

	void keySet();

	void putAll(Map<? extends String, ? extends Object> m);

	void readFromStream(InputStream input);

	void readFromFile(File file);

	void readFromResource(String string);

	void readFromFile(String string);

	void remove(Object key);

	void readFromString(String string);

	void getLong(String key);

	void getLong(String key, long def);

	void getFloat(String key);

	void containsLong(String key);

	void getFloat(String key, float def);

	void readFromUrl(URL url);

	void put(String key, Object value);
	
	void getObject(String key, Class<?> type);

	void getObject(String key, Object def, Class<?> type);

	void containsDouble(String key);

	void containsBoolean(String key);

	void containsKey(Object key);
	
	void containsObject(String key, Class<?> type);

	void containsFloat(String key);
}

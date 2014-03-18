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

package com.siemens.oss.omniproperties.builders;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Dieter Bogdoll
 * 
 */
public abstract class BaseMapBuilder implements
		ObjectBuilder<Map<String, ? extends Object>> {

	private final Pattern regex;

	protected BaseMapBuilder() {
		regex = null;
	}

	protected BaseMapBuilder(String aRegex) {
		regex = Pattern.compile(aRegex);
	}

	protected abstract Map<String, String> base();

	@Override
	public Map<String, String> build() {
		Map<String, String> result = new ConcurrentHashMap<String, String>();
		Map<String, String> env = base();
		if (regex == null) {
			result.putAll(env);
		} else {
			for (String key : env.keySet()) {
				if (regex.matcher(key).matches()) {
					result.put(key, env.get(key));
				}
			}
		}
		return result;
	}
}

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
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Dieter Bogdoll
 *
 */
public class SystemPropertiesBuilder extends BaseMapBuilder {
	public SystemPropertiesBuilder() {
		super();
	}

	public SystemPropertiesBuilder(String aRegex) {
		super(aRegex);
	}

	@Override
	protected Map<String, String> base() {
		Map<String,String> result = new ConcurrentHashMap<String, String>();
		Properties props = System.getProperties();
		for(String key : props.stringPropertyNames()) {
			result.put(key,props.getProperty(key));
		}
		return result;
	}
}
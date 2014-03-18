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

import java.io.IOException;

import org.apache.commons.lang.SystemUtils;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Markus Michael Geipel
 *
 */
public class Os implements ObjectBuilder<String> {


	@Override
	public String build() throws IOException {
		if(SystemUtils.IS_OS_LINUX){
			return "linux";
		}else if(SystemUtils.IS_OS_MAC){
			return "mac";
		}else if(SystemUtils.IS_OS_WINDOWS){
			return "windows";
		}
		return "unknown";
	}
}

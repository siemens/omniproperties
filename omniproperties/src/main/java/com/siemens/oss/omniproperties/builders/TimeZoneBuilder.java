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

import java.util.Arrays;
import java.util.TimeZone;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Dieter Bogdoll, Markus Michael Geipel
 *
 */
public final class TimeZoneBuilder implements ObjectBuilder<TimeZone> {

	private final String id;

	public TimeZoneBuilder(final String id) {
		this.id = id;
	}
	
	@Override
	public TimeZone build() {
		if(!Arrays.asList(TimeZone.getAvailableIDs()).contains(id)){
			throw new IllegalArgumentException("'" + id + "' is not a valid timezone id");
		}
		
		
		return TimeZone.getTimeZone(id);
	}

}

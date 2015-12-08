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

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * Omniproperties builder which takes a String and a delimiter (regex) and
 * provides a String Array of the string parts between occurrences of the
 * delimiter.
 * 
 * @author Holger Schoener <holger.schoener@siemens.com>
 *
 */
public class StringSplitter implements ObjectBuilder<String[]> {
	
	private final String str;
	private final String delim_regex;
	private final boolean trim;

	public StringSplitter(final String str)  {
		this.str         = str;
		this.delim_regex = ",";
		this.trim        = true;
	}

	public StringSplitter(final String str, boolean trim)  {
		this.str         = str;
		this.delim_regex = ",";
		this.trim        = trim;
	}

	public StringSplitter(final String str, final String delim_regex)  {
		this.str         = str;
		this.delim_regex = delim_regex;
		this.trim        = true;
	}

	public StringSplitter(final String str, final String delim_regex, boolean trim)  {
		this.str         = str;
		this.delim_regex = delim_regex;
		this.trim        = trim;
	}

	@Override
	public String[] build(){
		String[] elements = this.str.split(this.delim_regex);
		if (trim)
			for (int i=0; i < elements.length; ++i) {
				elements[i] = elements[i].trim();
			}
		return elements;
	}
}

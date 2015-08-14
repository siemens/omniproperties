/*
* Copyright Siemens AG, 2015
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


import java.util.List;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Kai Heesche
 *
 */
public class JoinStrings implements ObjectBuilder<String> {

	@NotNull
	@NotEmpty
	private final String[] strings;
	
	@NotNull
	@NotEmpty
	private final String   delimiter;

	@NotNull
	private final String   quote;

	@NotNull
	private final String   escape;

	public JoinStrings(final String[] strings, String delimiter, String quote, String escape) {
		this.strings=strings;
		this.delimiter=delimiter;
		this.quote=quote;
		this.escape=escape;
	}

	public JoinStrings(final String[] strings, String delimiter) {
		this(strings,delimiter,"","");
	}
	public JoinStrings(final List<String> strings, String delimiter) {
		this(strings.toArray(new String[strings.size()]),delimiter);
	}
	
	public JoinStrings(final List<String> strings, String delimiter, String quote, String escape) {
		this(strings.toArray(new String[strings.size()]),delimiter,quote,escape);
	}

	
	@Override
	public String build() {
		
		String result="";

		boolean first=true;
		for (String s : strings) 
		{
			s=s.replace(quote, escape+quote);
			if(first)
			{
				result=quote+s+quote;
				first=false;
			}
			else
			{
				result+=delimiter+quote+s+quote;
			}
		}
		return result;
	}
}

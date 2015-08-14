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

import com.siemens.oss.omniproperties.ObjectBuilder;
/**
 * @author Kai Heesche
 * 
 */
public final class ModifyStringArray implements ObjectBuilder<Object> {

	final private String[] newArray;
	
	public ModifyStringArray(String[] array, String prefix, String postfix) {
		newArray=new String[array.length];
		for(int i=0;i<array.length;i++)
		{
			newArray[i]=prefix+array[i]+postfix;
		}
	}
	
	@Override
	public Object build() throws Exception {
		return  newArray;
	}

}

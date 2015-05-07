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

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * {@link ObjectBuilder} to convert an array (of non-primitive typed elements) to a List
 */
public class ArrayToList implements ObjectBuilder<List<?>> {

	private final Object[] array;
	
	public ArrayToList(Object[] aArray) {
		if(!aArray.getClass().isArray()) {
			throw new InvalidParameterException("expecting array, got " + aArray.getClass());
		}
		array = aArray;
	}
	
	@Override
	public List<?> build() throws Exception {
		return Arrays.asList(array);
	}

}

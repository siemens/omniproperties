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
 * @author Markus Michael Geipel
 * 
 */
public final class SelectArrayItemByIndex implements ObjectBuilder<Object> {

	final private Object value;
	
	public SelectArrayItemByIndex(Object[] array, int index) {
		this.value = array[index];
	}
	
	public SelectArrayItemByIndex(float[] array, int index) {
		this.value = Float.valueOf(array[index]);
	}
	
	public SelectArrayItemByIndex(Double[] array, int index) {
		this.value = Double.valueOf(array[index]);
	}
	
	public SelectArrayItemByIndex(byte[] array, int index) {
		this.value = Byte.valueOf(array[index]);
	}
	
	public SelectArrayItemByIndex(int[] array, int index) {
		this.value = Integer.valueOf(array[index]);
	}
	
	public SelectArrayItemByIndex(long[] array, int index) {
		this.value = Long.valueOf(array[index]);
	}
	
	public SelectArrayItemByIndex(boolean[] array, int index) {
		this.value = Boolean.valueOf(array[index]);
	}
	
	@Override
	public Object build() throws Exception {
		return  value;
	}


}

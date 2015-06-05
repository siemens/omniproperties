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

package com.siemens.oss.omniproperties.builders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.siemens.oss.omniproperties.ObjectBuilder;
import com.siemens.oss.omniproperties.util.ReflectionUtil;

public final class Union implements ObjectBuilder<Object> {

	private final Object unionArray;
	
	public Union(final Object[] arrays) {
		if(arrays.length==0){
			throw new IllegalArgumentException("No arrays given.");
		}
		final Set<Object> set = new HashSet<>();
		final List<Object> union = new ArrayList<>();
		for (int i = 0; i < arrays.length; i++) {
			final Object array = arrays[i];
			if (!arrays[i].getClass().isArray()) {
				throw new IllegalArgumentException("Object " + i + " is not an array.");
			}
			
			final int length = Array.getLength(array);
			for (int j = 0; j < length; j++) {
				Object element = Array.get(array, j);
				if(!set.contains(element)){
					union.add(element);
					set.add(element);
				}
			}
		}
		unionArray = ReflectionUtil.createArray(union, arrays[0].getClass().getComponentType());

	}

	@Override
	public Object build() throws Exception {
		return unionArray;
	}

}

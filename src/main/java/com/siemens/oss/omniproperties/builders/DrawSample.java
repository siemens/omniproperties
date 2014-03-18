package com.siemens.oss.omniproperties.builders;

import java.lang.reflect.Array;
import java.util.Random;

import com.siemens.oss.omniproperties.ObjectBuilder;

public final class DrawSample<T> implements ObjectBuilder<T[]> {
	final T[] array;
	final int sampleSize;
	public DrawSample(T[] array, int sampleSize) {
		this.array = array;
		this.sampleSize = sampleSize;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T[] build() throws Exception {
		final T[] sample = (T[]) Array.newInstance(array.getClass(), sampleSize);
		final Random random = new Random();
		
		
		for (int i = 0; i < sample.length; i++) {
			sample[i] = array[i];
		}
		
		int index = sample.length;
		while(index<array.length){
			double rnd = random.nextDouble();
			if(rnd > (double)sampleSize/(double)(index+1)){
				sample[random.nextInt(sampleSize)] = array[index];
			}
			index++;
		}
		return sample;
	}
}

package com.siemens.oss.omniproperties.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.siemens.oss.omniproperties.ObjectBuilder;
import com.siemens.oss.omniproperties.util.ReflectionUtil;

/**
 * Omniproperties builder which takes a number of Arrays (at least one) and
 * provides another Array of the same type as the first provided Array containing
 * all the elements present in each of the provided Arrays.
 * 
 * @author Holger Schoener <holger.schoener@siemens.com>
 * inspired by http://stackoverflow.com/questions/12919231/finding-the-intersection-of-two-arrays
 */
public final class Intersection implements ObjectBuilder<Object> {

	private Object intersectionArray;
	
	public Intersection() {
	        intersectionArray = null;
	}
	
        public Intersection(final Object[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Object> targetList = new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        List<Object> bList = Arrays.asList(arrays[i]);
                        targetList.retainAll(bList);
                }
                intersectionArray = ReflectionUtil.createArray(targetList, arrays[0].getClass().getComponentType());
        }

        public Intersection(final byte[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Byte> targetList = new ArrayList<Byte>() {{ for (byte j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Byte> bList = new ArrayList<Byte>() {{ for (byte j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                byte[] result = new byte[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        public Intersection(final short[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Short> targetList = new ArrayList<Short>() {{ for (short j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Short> bList = new ArrayList<Short>() {{ for (short j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                short[] result = new short[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        public Intersection(final int[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Integer> targetList = new ArrayList<Integer>() {{ for (int j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Integer> bList = new ArrayList<Integer>() {{ for (int j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                int[] result = new int[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        public Intersection(final long[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Long> targetList = new ArrayList<Long>() {{ for (long j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Long> bList = new ArrayList<Long>() {{ for (long j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                long[] result = new long[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        public Intersection(final double[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Double> targetList = new ArrayList<Double>() {{ for (double j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Double> bList = new ArrayList<Double>() {{ for (double j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                double[] result = new double[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        public Intersection(final float[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Float> targetList = new ArrayList<Float>() {{ for (float j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Float> bList = new ArrayList<Float>() {{ for (float j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                float[] result = new float[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        public Intersection(final boolean[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Boolean> targetList = new ArrayList<Boolean>() {{ for (boolean j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Boolean> bList = new ArrayList<Boolean>() {{ for (boolean j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                boolean[] result = new boolean[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        public Intersection(final char[][] arrays) {
                if(arrays.length==0){
                        throw new IllegalArgumentException("No arrays given.");
                }
                List<Character> targetList = new ArrayList<Character>() {{ for (char j : arrays[0]) add(j); }}; //new ArrayList<>(Arrays.asList(arrays[0]));
                for (int i = 1; i < arrays.length; ++i) {
                        final int ii = i;
                        List<Character> bList = new ArrayList<Character>() {{ for (char j : arrays[ii]) add(j); }};;
                        targetList.retainAll(bList);
                }
                char[] result = new char[targetList.size()];
                for (int i = 0; i < targetList.size(); ++i) result[i] = targetList.get(i);
                intersectionArray = result;
        }
        
        @Override
	public Object build() throws Exception {
	        if (intersectionArray == null)
	            throw new IllegalArgumentException("no arrays to intersect provided before call of build() (default constructor was called without setting the arrays to intersect afterward).");
		return intersectionArray;
	}
}

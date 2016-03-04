package com.dregronprogram.utils;

public class MathUtils {

	public static boolean isEqual(int a, int b, int tolerances) {
		return Math.abs(a - b) <= tolerances;
	}
}

package com.dregronprogram.utils;

public class MathUtils {

	public static boolean isEqual(int a, int b, int tolerances) {
		return Math.abs(a - b) <= tolerances;
	}

	public static float lerp(float targetPos, float currentPos, float progress) {
		if (targetPos - currentPos > 0) return currentPos + progress;
		else if (targetPos - currentPos < 0) return currentPos - progress;
		return currentPos;
	}
}

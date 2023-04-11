package com.gamefocal.rivenworld.game.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class FloatMath {

	public static final float PI;
	public static final float TWO_PI;
	public static final float E;
	private static final float[] SIN;

	static {
		SIN = new float[64];
		for (int i = 0; i < 64; ++i) {
			FloatMath.SIN[i] = (float) Math.sin(i * (Math.PI / 32D));
		}
		PI = (float) Math.PI;
		E = (float) Math.E;
		TWO_PI = 2 * PI;
	}

	private FloatMath() {
	}

	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}

	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}

	public static int manhattanDistance(int x, int y, int x2, int y2) {
		int dx = x > x2 ? x - x2 : x2 - x;
		int dy = y > y2 ? y - y2 : y2 - y;
		return dx + dy;
	}

	public static float manhattanDistance(float x, float y, float x2, float y2) {
		float dx = x > x2 ? x - x2 : x2 - x;
		float dy = y > y2 ? y - y2 : y2 - y;
		return dx + dy;
	}

	public static int diagonalDistance(int x, int y, int x2, int y2) {
		return Math.max(x > x2 ? x - x2 : x2 - x, y > y2 ? y - y2 : y2 - y);
	}

	public static float diagonalDistance(float x, float y, float x2, float y2) {
		return Math.max(x > x2 ? x - x2 : x2 - x, y > y2 ? y - y2 : y2 - y);
	}

	public static float distance(float x, float y, float x2, float y2) {
		return sqrt(distanceSquared(x, y, x2, y2));
	}

	public static float distance(int x, int y, int x2, int y2) {
		return sqrt(distanceSquared(x, y, x2, y2));
	}

	public static float distanceSquared(float x, float y, float x2, float y2) {
		return (x -= x2) * x + (y -= y2) * y;
	}

	public static int distanceSquared(int x, int y, int x2, int y2) {
		return (x -= x2) * x + (y -= y2) * y;
	}

	public static int floor(float f) {
		int i = (int) f;
		return i > f ? i - 1 : i;
	}

	public static int ceil(float f) {
		int i = (int) f;
		return i >= f ? i : i + 1;
	}

	public static int round(float f) {
		return (int) (f >= 0 ? f + 0.5D : f - 0.5D);
	}

	public static float invSqrt(float x) {
		float xhalf = 0.5f * x;
		x = Float.intBitsToFloat(0x5f3759df - (Float.floatToIntBits(x) >> 1));
		x *= (1.5f - xhalf * x * x);
		return x;
	}

	public static int hashPoint(float x, float y) {
		int hash = 7;
		hash = 79 * hash + (int) (Double.doubleToLongBits(x) ^ Double.doubleToLongBits(x) >>> 32);
		hash = 79 * hash + (int) (Double.doubleToLongBits(y) ^ Double.doubleToLongBits(y) >>> 32);
		return hash;
	}

	public static int min(int... values) {
		int min = values[0];
		for (int i = 1; i < values.length; ++i)
			if (values[i] < min)
				min = values[i];
		return min;
	}

	public static float min(float... values) {
		float min = values[0];
		for (int i = 1; i < values.length; ++i)
			if (values[i] < min)
				min = values[i];
		return min;
	}

	public static int max(int... values) {
		int max = values[0];
		for (int i = 1; i < values.length; ++i)
			if (values[i] > max)
				max = values[i];
		return max;
	}

	public static float max(float... values) {
		float max = values[0];
		for (int i = 1; i < values.length; ++i)
			if (values[i] > max)
				max = values[i];
		return max;
	}

	public static int average(int... values) {
		if (values.length == 0) {
			return 0;
		}
		int total = 0;
		for (int i : values) {
			total += i;
		}
		return total / values.length;
	}

	public static float average(float... values) {
		if (values.length == 0) {
			return 0f;
		}
		float total = 0f;
		for (float f : values) {
			total += f;
		}
		return total / values.length;
	}

	public static int roundToNearest(float f, int stack) {
		return FloatMath.round(f / stack) * stack;
	}

	public static float pow(float base, float exponent) {
		return (float) Math.pow(base, exponent);
	}

	public static <T> T[] shuffleArray(T[] array, Random source) {
		if (source == null) {
			source = ThreadLocalRandom.current();
		}
		for (int i = array.length - 1; i > 0; i--) {
			int index = source.nextInt(i + 1);
			T temp = array[index];
			array[index] = array[i];
			array[i] = temp;
		}
		return array;
	}

	public static int[] shuffleArray(int[] array, Random source) {
		if (source == null) {
			source = ThreadLocalRandom.current();
		}
		for (int i = array.length - 1; i > 0; i--) {
			int index = source.nextInt(i + 1);
			int temp = array[index];
			array[index] = array[i];
			array[i] = temp;
		}
		return array;
	}

	public static float sind(float n) {
		return sin(toRadians(n));
	}

	public static float cosd(float n) {
		return cos(toRadians(n));
	}

	public static float sin(float n) {
		int index = (int) (n * 10.18591635788128F);
		float dt = n - index * 0.09817477042468F;
		float dt2 = dt * dt * 0.5F;
		index &= 63;
		return FloatMath.SIN[index] * (1 - dt2) + FloatMath.SIN[(index + 16) & 63] * (dt - (dt2 * dt * (1F / 3F)));
	}

	public static float cos(float n) {
		int index = (int) (n * 10.18591635788128F);
		float dt = n - index * 0.09817477042468F;
		float dt2 = dt * dt * 0.5F;
		index &= 63;
		return FloatMath.SIN[(index + 16) & 63] * (1 - dt2) - FloatMath.SIN[index] * (dt - (dt2 * dt * (1F / 3F)));
	}

	public static float sqr(float f) {
		return f * f;
	}

	public static int sqr(int x) {
		return x * x;
	}

	public static float sqrt(float f) {
		float xhalf = f * 0.5F;
		float y = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1));
		y = y * (1.5F - xhalf * y * y);
		return f * y;
	}

	public static float toDegrees(float radians) {
		return radians * 180f / PI;
	}

	public static float toRadians(float degrees) {
		return degrees * PI / 180f;
	}

	public static float log(float s) {
		return (float) Math.log(s);
	}

	public static int randInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	public static int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	public static int randInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}

	public static float randFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}

	public static float randFloat(float min, float max) {
		if (min >= max)
			throw new IllegalArgumentException("bound must be greater than origin");
		float f = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
		if (f >= max) {
			f = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
		}
		return f;
	}

	public static long randLong(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max);
	}

	public static float lerp(float min, float max, float progress) {
		return min + (max - min) * progress;
	}

	public static float smoothLerp(float min, float max, float progress) {
		return min + (max - min) * (progress * progress * (3 - 2 * progress));
	}

	public static float smootherLerp(float min, float max, float progress) {
		float a = progress;
		return min + (max - min) * (a * a * a * (a * (a * 6 - 15) + 10));
	}

}
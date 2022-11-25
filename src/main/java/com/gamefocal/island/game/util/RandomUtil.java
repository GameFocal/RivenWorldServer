package com.gamefocal.island.game.util;

import java.util.*;

/**
 * Helper methods for generating random numbers.
 */
public class RandomUtil {
    /**
     * The random number stream used by this helper.
     */
    public final static Random RANDOM = new Random();

    /**
     * Returns a random int between min and max, inclusive.
     *
     * @param min The minimum number that the random number can be.
     * @param max The maximum number that the random number can be.
     * @return A random int between min and max, inclusive.
     */
    public static int getRandomNumberBetween(int min, int max) {
        return useRandomForNumberBetween(RANDOM, min, max);
    }

    /**
     * Returns a random float between min and max, inclusive.
     *
     * @param min The minimum number that the random number can be.
     * @param max The maximum number that the random number can be.
     * @return A random float between min and max, inclusive.
     */
    public static float getRandomNumberBetween(float min, float max) {
        return useRandomForNumberBetween(RANDOM, min, max);
    }

    /**
     * Returns a random double between min and max, inclusive.
     *
     * @param min The minimum number that the random number can be.
     * @param max The maximum number that the random number can be.
     * @return A random float between min and max, inclusive.
     */
    public static double getRandomNumberBetween(double min, double max) {
        return useRandomForNumberBetween(RANDOM, min, max);
    }

    /**
     * Chooses a set of random numbers between min and max, inclusive. Does not choose duplicate numbers.
     *
     * @param min         The minimum number that a random number can be.
     * @param max         The maximum number that a random number can be.
     * @param numElements The number of elements to pick.
     * @return The specified number of distinct random numbers.
     */
    public static int[] getRandomDistinctNumbersBetween(int min, int max, int numElements) {
        int totalNumbers = max - min + 1;
        if (numElements < 1) {
            return new int[0];
        } else {
            if (numElements > totalNumbers) {
                numElements = totalNumbers;
            }
        }
        int[] randomNumbers = new int[numElements];
        List<Integer> allNumbers = new ArrayList<>(totalNumbers);
        for (int i = 0; i < totalNumbers; i++) {
            allNumbers.add(min + i);
        }
        for (int i = 0; i < numElements; i++) {
            randomNumbers[i] = getRandomElementFromList(allNumbers);
            allNumbers.remove(new Integer(randomNumbers[i]));
        }
        return randomNumbers;
    }

    /**
     * Using a specified random stream, returns a random int between min and max, inclusive.
     *
     * @param random The random stream of numbers to get a random number from.
     * @param min    The minimum number that the random number can be.
     * @param max    The maximum number that the random number can be.
     * @return A random int between min and max, inclusive.
     */
    public static int useRandomForNumberBetween(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Using a specified random stream, returns a random float between min and max, inclusive.
     *
     * @param random The random stream of numbers to get a random number from.
     * @param min    The minimum number that the random number can be.
     * @param max    The maximum number that the random number can be.
     * @return A random float between min and max, inclusive.
     */
    public static float useRandomForNumberBetween(Random random, float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    /**
     * Using a specified random stream, returns a random double between min and max, inclusive.
     *
     * @param random The random stream of numbers to get a random number from.
     * @param min    The minimum number that the random number can be.
     * @param max    The maximum number that the random number can be.
     * @return A random float between min and max, inclusive.
     */
    public static double useRandomForNumberBetween(Random random, double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    /**
     * Gets a random element from a list.
     *
     * @param list The list to get a random element from.
     * @return A randomly selected element in the list, or null if the list is empty.
     */
    public static <T> T getRandomElementFromList(List<T> list) {
        if (!list.isEmpty()) {
            return list.get(getRandomNumberBetween(0, list.size() - 1));
        } else {
            return null;
        }
    }

    public static <T> T getRandomElementFromMap(Map<T, Integer> map) {
        if (map.isEmpty()) {
            return null;
        }

        // Get the keys and values.
        ArrayList<T> keys = new ArrayList<T>((Collection<? extends T>) map.keySet());
        List<Integer> weights = new ArrayList<>(map.values());

        int index = getRandomIndexFromWeights(weights);
        if(index == -1) {
            return null;
        }

        T k = keys.get(index);

        return k;
    }

    /**
     * Gets a random element from an array.
     *
     * @param array The array to get a random element from.
     * @return A randomly selected element in the array, or null is the list is null or empty.
     */
    public static <T> T getRandomElementFromArray(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        } else {
            return array[getRandomNumberBetween(0, array.length - 1)];
        }
    }

    /**
     * Removes a random element from a list and returns it.
     *
     * @param list The list to remove a random element from.
     * @return A randomly removed element in the list, or null if the list is empty.
     */
    public static <T> T removeRandomElementFromList(List<T> list) {
        if (!list.isEmpty()) {
            return list.remove(getRandomNumberBetween(0, list.size() - 1));
        } else {
            return null;
        }
    }

    /**
     * Randomly returns true or false, using a specific chance (0 to 1) of being true.
     *
     * @param chance The chance (0 to 1) of returning true.
     * @return A randomly selected boolean value according to the chance of being true.
     */
    public static boolean getRandomChance(double chance) {
        return RANDOM.nextDouble() < chance;
    }

    /**
     * Randomly returns true or false, using a specific chance (0 to 1) of being true.
     *
     * @param chance The chance (0 to 1) of returning true.
     * @return A randomly selected boolean value according to the chance of being true.
     */
    public static boolean getRandomChance(float chance) {
        return RANDOM.nextFloat() < chance;
    }

    /**
     * Randomly returns true or false, using a specific chance (0 to 100) of being true.
     *
     * @param chance The chance (0 to 100) of returning true.
     * @return A randomly selected boolean value according to the chance of being true.
     */
    public static boolean getRandomChance(int chance) {
        return getRandomChance(chance / 100.0f);
    }

    /**
     * Randomly returns true or false with even distribution.
     *
     * @return True or false randomly with even distribution.
     */
    public static boolean getRandomChance() {
        return getRandomChance(0.5f);
    }

    public static int getRandomIndexFromWeights(Set<Integer> weights) {
        return getRandomIndexFromWeights(new ArrayList<>(weights));
    }

    /**
     * Generates a random index from an array of integer weights
     *
     * @param weights The list of weights to choose the index from
     * @return The index chosen from the array of weights, or -1 if the weights are invalid.
     */
    public static int getRandomIndexFromWeights(List<Integer> weights) {
        int totalWeight = 0;

        for (Integer weight : weights) {
            totalWeight += weight;
        }

        if (totalWeight > 0) {
            int num = RandomUtil.getRandomNumberBetween(0, totalWeight - 1);
            int sum = 0;
            for (int i = 0; i < weights.size(); i++) {
                sum += weights.get(i);

                if (num < sum)
                    return i;
            }
        }
        return -1;
    }
}


package pl.edu.agh.iet.bo.fa;

import java.util.Arrays;

public class FireflySolution {
	public static Firefly[] fireflies; // tablica świetlików
	public static int bestTime; // najlepszy dotychczasowy czas
	public static int[] bestPermutation; // najlepsza dotychczasowa permutacja
	public static int numOfBestIteration;// w której iteracji znaleziono
											// najlepsze rozwiązanie
	public static int iterations; // liczba iteracji
	public static int tasks; // liczba zadań
	public static int machines; // liczba maszyn
	public static int firefliesNum; // liczba świetlików
	public static double alfa; // współczynnik randomizacji
	public static double alfaMinimizer; // współczynnik zmiany współczynnika
										// randomizacji
	public static double betaZero; // początkowa atrakcyjność
	public static double minimalBeta; // minimalna atrakcyjność
	public static double gamma; // współczynnik absorpcji
	public static double m; // wykładnik potęgi we wzorze na nową atrakcyjność
	public static int[][] times; // times[i][j] - ile czasu zajmie zadanie j na
									// maszynie i
	public static int[][] endTimes;
	public static java.util.Random rand = new java.util.Random();

	public static void setInitialValues(int iterations, int firefliesNum,
			int tasks, int machines, int[][] times, double alfa,
			double betaZero, double minimalBeta, double gamma, double m) {
		FireflySolution.iterations = iterations;
		FireflySolution.firefliesNum = firefliesNum;
		FireflySolution.tasks = tasks;
		FireflySolution.machines = machines;
		FireflySolution.times = times;
		FireflySolution.alfa = alfa;
		FireflySolution.betaZero = betaZero;
		FireflySolution.minimalBeta = minimalBeta;
		FireflySolution.gamma = gamma;
		FireflySolution.m = m;

		FireflySolution.fireflies = new Firefly[firefliesNum];
		endTimes = new int[machines][tasks];
		bestTime = Integer.MAX_VALUE;
	}

	public static void updateBest(int iteration) {
		for (Firefly firefly : fireflies) {
			if (firefly.getTotalTime() < bestTime) {
				bestTime = firefly.getTotalTime();
				bestPermutation = Arrays
						.copyOf(firefly.getPermutation(), tasks);
				numOfBestIteration = iteration;
			}
		}
	}

	public static void changeAlfa(int iterationNum) {
		alfa *= (1.0 - alfaMinimizer
				* (1.0 - Math.pow(1.0 / 9000.0, 1.0 / iterationNum)));
	}

	// czas zakończenia ostatniego zadania na ostatniej maszynie
	// - czas zakonczenia produkcji
	public static int endTime(int[] permutation) {
		int i, j;
		endTimes[0][0] = times[0][permutation[0]];
		for (i = 1; i < machines; ++i) {
			endTimes[i][0] = endTimes[i - 1][0] + times[i][permutation[0]];
		}

		for (j = 1; j < tasks; ++j) {
			endTimes[0][j] = endTimes[0][j - 1] + times[0][permutation[j]];

			for (i = 1; i < machines; ++i) {
				endTimes[i][j] = Math.max(endTimes[i][j - 1],
						endTimes[i - 1][j]) + times[i][permutation[j]];
			}
		}

		return endTimes[machines - 1][tasks - 1];
	}

	public static void doWork() {
		int i, j;
		for (j = 0; j < firefliesNum; ++j) {
			fireflies[j] = new Firefly();
		}

		for (i = 0; i < iterations; ++i) {
			Arrays.sort(fireflies);

			for (Firefly fireflyFirst : fireflies) {
				for (Firefly fireflySecond : fireflies) {
					if (fireflyFirst.compareTo(fireflySecond) < 0) { 
						// mniej atrakcyjny -> większy czas
						fireflyFirst.move(fireflySecond);
					}
				}
			}

			fireflies[firefliesNum - 1].moveRandomly();

			for (Firefly firefly : fireflies) {
				firefly.evalNewPermutation();
				firefly.updateTotalTime();
			}

			updateBest(i);
		}
		endTime(bestPermutation);

		System.out.println(Arrays.toString(bestPermutation));
		System.out.println(bestTime);
		System.out.println(numOfBestIteration);
	}
}

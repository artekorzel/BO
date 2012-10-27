package pl.edu.agh.iet.bo.fa;

import java.util.Arrays;

public class FireflySolution {
	public static Firefly[] fireflies;	//tablica świetlików
	public static int bestTime;			//najlepszy dotychczasowy czas
	public static int[] bestPermutation;	//najlepsza dotychczasowa permutacja
	public static int numOfBestIteration;//w której iteracji znaleziono najlepsze rozwiązanie	
	public static int iterations;			//liczba iteracji
	public static int tasks;				//liczba zadań
	public static int machines;			//liczba maszyn
	public static int firefliesNum;		//liczba świetlików
	public static double alfa;			//współczynnik randomizacji
	public static double alfaMinimizer;	//współczynnik zmiany współczynnika randomizacji
	public static double betaZero;		//początkowa atrakcyjność
	public static double minimalBeta;		//minimalna atrakcyjność
	public static double gamma;			//współczynnik absorpcji
	public static double m;				//wykładnik potęgi we wzorze na nową atrakcyjność
	public static int[][] times;			//times[i][j] - ile czasu zajmie zadanie j na maszynie i 
	public static int[][] endTimes;
	public static java.util.Random rand = new java.util.Random();
		
	public static void setInitialValues(
			int iterations,
			int firefliesNum,
			int tasks,
			int machines,
			int[][] times,
			double alfa,
			double betaZero,
			double minimalBeta,
			double gamma,
			double m) {
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
		bestTime = Integer.MAX_VALUE;
	}
	
	public static void updateBest(int iteration) {
		for(Firefly firefly : fireflies) {
			if(firefly.getTotalTime() < bestTime) {
				bestTime = firefly.getTotalTime();
				bestPermutation = Arrays.copyOf(firefly.getPermutation(), tasks);
				numOfBestIteration = iteration;
			}
		}
	}
	
	public static void changeAlfa(int iterationNum) {
		alfa *= (1.0 - alfaMinimizer * (1.0 - Math.pow(1.0 / 9000.0, 1.0 / iterationNum)));
	}

	//czas zakończenia wskazanego zadania na wskazanej maszynie
	public static int endTime(int[] permutation, int machine, int task) {
		endTimes = new int[machines][tasks];
		int theTaskPreviousMachineEndTime = 0;
		int previousTaskTheMachineEndTime = 0;
		
		if(task != 0) {
			previousTaskTheMachineEndTime = endTime(permutation, machine, task - 1);
		}

		if(machine != 0) {
			theTaskPreviousMachineEndTime = endTime(permutation, machine - 1, task);
		}
		
		return endTimes[machine][task] = Math.max(previousTaskTheMachineEndTime, 
				theTaskPreviousMachineEndTime) + times[machine][permutation[task]];
	}
	
	private static void test1() {
		setInitialValues(
				100, 
				100, 
				4, 
				3, 
				new int[][] {
						{5, 7, 3, 1},
						{2, 4, 5, 8},
						{6, 2, 4, 3}
				}, 
				2, 
				2, 
				1, 
				1, 
				0.5
		);
	}
	
	public static void main(String[] args) {
		//TODO wczytywanie pobieranie danych inicjujących skądś?
		test1();	//mały test żeby sprawdzić czy działa, minimum to 24 dla dwóch sekwencji: 3021. 2031
		
		int i, j;
		for(i = 0; i < iterations; ++i) {
			for(j = 0; j < firefliesNum; ++j) {
				fireflies[j] = new Firefly();
			}
			
			Arrays.sort(fireflies);
			
			for(Firefly fireflyFirst : fireflies) {
				for(Firefly fireflySecond : fireflies) {
					if(fireflyFirst.compareTo(fireflySecond) < 0) {	//mniej atrakcyjny -> większy czas
						fireflyFirst.move(fireflySecond);
					}
				}
			}
			
			fireflies[firefliesNum - 1].moveRandomly();
			
			for(Firefly firefly : fireflies) {
				firefly.evalNewPermutation();
				firefly.updateTotalTime();
			}
			
			updateBest(i);
		}
		
		System.out.println(Arrays.toString(bestPermutation));
		System.out.println(bestTime);
		System.out.println(numOfBestIteration);
	}
}

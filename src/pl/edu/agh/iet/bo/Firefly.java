package pl.edu.agh.iet.bo;

import static pl.edu.agh.iet.bo.FireflySolution.*;

public class Firefly implements Comparable<Firefly> {	// i - numer świetlika
	private double[][] coordinates; 	// coordinates[j][k] = 1 gdy j-te zadanie
										// jest k-tym el. permutacji
	private int[] permutation; 		// permutation[j] - aktualna pozycja zadania j w
										// permutacji
	private double totalTime; 			// makespan

	public Firefly() {
		coordinates = new double[tasks][tasks];
		permutation = new int[tasks];

		initPermutation();
	}

	public void initPermutation() {
		int k;
		boolean[] tmp = new boolean[tasks];
		for (int j = 0; j < tasks; ++j) {
			do {
				k = rand.nextInt(tasks);
			} while (tmp[k]);
			tmp[k] = true;
			permutation[j] = k;
			coordinates[j][k] = 1.0;
		}
		
		updateTotalTime();
	}

	public void move(Firefly brighter) {
		double distance = 0.0;
		int j, k;
		for (j = 0; j < tasks; ++j) {
			for (k = 0; k < tasks; ++k) {
				distance = coordinates[j][k] - brighter.coordinates[j][k];
				distance *= distance;
			}
		}

		distance = Math.sqrt(distance);
		distance = distance * (betaZero - minimalBeta)
				* Math.exp(-gamma * Math.pow(distance, m)) + minimalBeta;

		for (j = 0; j < tasks; ++j) {
			for (k = 0; k < tasks; ++k) {
				coordinates[j][k] += distance + alfa * (rand.nextDouble() - 0.5);
				
				if(brighter.permutation[k] == j) {
					coordinates[j][k] += distance;
				}
			}
		}
	}

	public void moveRandomly() {
		for (int j = 0; j < tasks; ++j) {
			for (int k = 0; k < tasks; ++k) {
				coordinates[j][k] += alfa * (rand.nextDouble() - 0.5);
			}
		}
	}

	public void evalNewPermutation() {
		int j, k;
		for (j = 0; j < tasks; ++j) {
			for (k = 0; k < tasks; ++k) {
				coordinates[j][k] = 1.0 / (1.0 + Math.exp(-coordinates[j][k]));
			}
		}

		boolean[] notAssigned = new boolean[tasks];
		for(j = 0; j < tasks; ++j) {
			notAssigned[j] = true;
		}
		
		int maxPossibilityIndex = 0;
		double maxPossibility;
		for (j = 0; j < tasks; ++j) {
			maxPossibility = -1.0;
			for (k = 0; k < tasks; ++k) {
				if (coordinates[j][k] > maxPossibility
						&& notAssigned[k]) {
					maxPossibilityIndex = k;
					maxPossibility = coordinates[j][k];
				}
			}
			
			permutation[maxPossibilityIndex] = j;
			notAssigned[maxPossibilityIndex] = false;
		}
	}
	
	public void updateTotalTime() {
		totalTime = endTime(permutation, machines - 1, tasks - 1);
	}

	public int[] getPermutation() {
		return permutation;
	}

	public void setPermutation(int[] permutation) {
		this.permutation = permutation;
	}

	public double[][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[][] coordinates) {
		this.coordinates = coordinates;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	@Override
	public int compareTo(Firefly o) {	//porównuje totalTime'y
		if(this.totalTime > o.totalTime) {
			return -1;
		}
		else if(this.totalTime < o.totalTime) {
			return 1;
		}
		return 0;
	}
}

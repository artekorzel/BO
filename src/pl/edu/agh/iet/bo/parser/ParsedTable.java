package pl.edu.agh.iet.bo.parser;

import java.util.Arrays;

public class ParsedTable {

	private String title;
	private int[][] table;
	private int tasksCount;
	private int machinesCount;
	private int upperBound;
	private int lowerBound;

	public ParsedTable(String title, int[][] table, int machinesCount,
			int tasksCount) {
		this.title = title;
		this.table = table;
		this.machinesCount = machinesCount;
		this.tasksCount = tasksCount;
	}

	public ParsedTable(String title, int[][] table, int machinesCount,
			int tasksCount, int upperBound, int lowerBound) {
		this(title, table, machinesCount, tasksCount);
		
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}
	
	public void printTable() {
		for (int i = 0; i < table.length; i++) {
			System.out.println(Arrays.toString(table[i]));
		}
	}

	public int getMachinesCount() {
		return machinesCount;
	}

	public int getTasksCount() {
		return tasksCount;
	}

	public int[][] getTable() {
		return table;
	}

	public String getTitle() {
		return title;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public int getLowerBound() {
		return lowerBound;
	}
}

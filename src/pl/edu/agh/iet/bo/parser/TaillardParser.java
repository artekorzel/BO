package pl.edu.agh.iet.bo.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaillardParser {
	private List<List<ParsedTable>> parsed;
	private static final String dirPath = "taillard";

	public TaillardParser() {
		parsed = new ArrayList<List<ParsedTable>>();
	}

	public ParsedTable getTable(int setNo, int tableNo) {
		if (parsed.isEmpty()) {
			parseAll();
		}

		if (parsed.size() <= setNo) {
			return null;
		} else {
			List<ParsedTable> tables = parsed.get(setNo);
			if (tables == null || tables.size() <= tableNo) {
				return null;
			}
			return tables.get(tableNo);
		}
	}

	public void parseAll() {
		File dir = new File(dirPath);

		if (dir.isDirectory()) {
			String[] files = dir.list();
			for (String fileName : files) {
				if (!fileName.startsWith("best_lb_up")) {
					parsed.add(parseOne(dirPath + "/" + fileName));
				}
			}
		}
	}

	private List<ParsedTable> parseOne(String filePath) {
		int jobs, machines, upper, lower, i, j;
		int[][] table;
		String line;
		String[] splitted;
		BufferedReader bufReader = null;
		List<ParsedTable> parsedTable = new ArrayList<ParsedTable>();

		try {
			bufReader = new BufferedReader(new FileReader(filePath));

			while ((line = bufReader.readLine()) != null) {
				if (line.startsWith("number of jobs")) {
					line = bufReader.readLine().trim();
					splitted = line.split("\\s+");
					jobs = Integer.parseInt(splitted[0]);
					machines = Integer.parseInt(splitted[1]);
					upper = Integer.parseInt(splitted[3]);
					lower = Integer.parseInt(splitted[4]);

					line = bufReader.readLine();

					table = new int[machines][jobs];
					for (i = 0; i < machines; ++i) {
						line = bufReader.readLine();
						splitted = line.split("\\s+");

						for (j = 0; j < jobs; ++j) {
							table[i][j] = Integer.parseInt(splitted[j + 1]);
						}
					}

					parsedTable.add(new ParsedTable(jobs + "/" + machines,
							table, machines, jobs, upper, lower));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return parsedTable;
	}
}

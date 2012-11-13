package pl.edu.agh.iet.bo.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TestParser {

    int jobs, machines;
    private ArrayList<ParsedTable> parsed;
    private String title, line;
    private String[] splitted;
    private BufferedReader bufReader;
    int[][] tmpTab;

    public TestParser() {
        jobs = 0;
        machines = 0;
        parsed = new ArrayList<ParsedTable>();
    }

    public ArrayList<ParsedTable> getParsed() {
        return parsed;
    }

    public void parse(String filepath) {
        try {
            bufReader = new BufferedReader(new FileReader(filepath));

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            while ((line = bufReader.readLine()) != null) {
                if (line.contains("+++") || line.length() == 1 || line.startsWith(" instance")) {
                    continue;
                }
                if (line.contains("Carlier") || line.contains("Heller") || line.contains("Reeves")) {
                    title = line;
                    continue;
                }
                splitted = line.split("\\s+");
                if (splitted.length == 3) {
                    jobs = Integer.parseInt(splitted[1]);
                    machines = Integer.parseInt(splitted[2]);
                    tmpTab = new int[machines][jobs];
                    for (int i = 0; i < jobs; i++) {
                        line = bufReader.readLine();
                        splitted = line.split("\\s+");
                        for (int j = 0; j < machines; j++) {
                            tmpTab[j][i] = Integer.parseInt(splitted[2 * j + 2]);
                        }
                    }
                    parsed.add(new ParsedTable(title, tmpTab, machines, jobs));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

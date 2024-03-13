package com.example.fxproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FILENAME = "data.txt";

    public static void saveData(List<String> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            for (String line : data) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadData() {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

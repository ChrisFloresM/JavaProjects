package com.cfloresh.mealplanner.files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    private final File file;
    File parentFile;

    public FileManager(String fileName) {
        String outputFile = String.format(".\\src\\com\\cfloresh\\mealplanner\\files\\%s", fileName);
        file = new File(outputFile);
    }

    public void createFileRoute() {
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch(IOException e) {
            System.out.println("Cannot create file: " + file.getPath());
        }
    }

    public void writeToFile(String toWrite) {
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(toWrite);
        } catch (IOException e) {
            System.out.printf("An exception occured: %s", e.getMessage());
        }
    }

    public List<String> getFromFile() {
        List<String> output = new ArrayList<>();

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String currentOutput = scan.nextLine();
                if (currentOutput.isBlank()) {
                    continue;
                }
                output.add(currentOutput);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("An exception occured: %s", e.getMessage());
        }

        return output;
    }
}

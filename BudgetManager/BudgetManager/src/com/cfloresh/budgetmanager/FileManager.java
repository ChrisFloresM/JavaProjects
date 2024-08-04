package com.cfloresh.budgetmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    private final File file;
    File parentFile;

    public FileManager() {
        String outputFile = ".\\src\\com\\cfloresh\\budgetmanager\\output\\purchases.txt";
        file = new File(outputFile);

        String outputFolder = ".\\src\\com\\cfloresh\\budgetmanager\\output";
        parentFile = new File(outputFolder);
    }

    public void createFileRoute() {
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }



        if(file.exists()) {
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

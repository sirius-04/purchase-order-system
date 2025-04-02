/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Chan Yong Liang
 */
public class FileManager {
    private static final Map<String, String> file_map = Map.of(
        "users", "src/data/users.txt",
        "items", "src/data/items.txt",
        "approvals", "src/data/approvals.txt",
        "payments", "src/data/payments.txt",
        "purchase_orders", "src/data/purchase_orders.txt",
        "purchase_requisitions", "src/data/purchase_requisition.txt",
        "suppliers", "src/data/suppliers.txt"
    );
    
    // retrieve file path by passing file name
    public static String getFilePath(String fileName) {
        if (!file_map.containsKey(fileName)) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }
        return file_map.get(fileName);
    }

    
    // append file
    public void writeFile(String fileName, String row) {
        String filePath = FileManager.getFilePath(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(row);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            e.printStackTrace();
        }
    }


    // read file and store inside arraylist
    // need split the row yourself after reading
    public String[] readFile(String fileName) {
       String filePath = FileManager.getFilePath(fileName);

       if (!Files.exists(Paths.get(filePath))) {
           System.err.println("File not found: " + filePath);
           return new String[0];
       }

       try {
           List<String> lines = Files.readAllLines(Paths.get(filePath));
           return lines.toArray(new String[0]);
       } catch (IOException e) {
           System.err.println("Error reading file: " + filePath);
           e.printStackTrace();
       }
       return new String[0];
   }


    // update a row in the file by passing the line num
    public void editFile(String fileName, int lineNumber, String updatedContent) {
        String filePath = FileManager.getFilePath(fileName);

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (lines.isEmpty()) {
                System.out.println("File is empty, cannot edit.");
                return;
            }

            if (lineNumber >= 0 && lineNumber < lines.size()) {
                lines.set(lineNumber, updatedContent);
                Files.write(Paths.get(filePath), lines);
            } else {
                System.out.println("Line number out of range.");
            }
        } catch (IOException e) {
            System.err.println("Error editing file: " + filePath);
            e.printStackTrace();
        }
    }
    
    // remove a row
    public void removeRow(String fileName, int lineNumber) {
        String filePath = getFilePath(fileName);
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            if (lineNumber >= 0 && lineNumber < lines.size()) {
                lines.remove(lineNumber);
                Files.write(Paths.get(filePath), lines);
            } else {
                System.out.println("Line number out of range.");
            }
        } catch (IOException e) {
            System.err.println("Error removing row from file: " + filePath);
            e.printStackTrace();
        }
    }

    // clear the file
    public void clearFile(String fileName) {
        String filePath = FileManager.getFilePath(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
        } catch (IOException e) {
            System.err.println("Error clearing file: " + filePath);
            e.printStackTrace();
        }
    }
}

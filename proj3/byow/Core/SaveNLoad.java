package byow.Core;

import java.io.*;
import java.util.Scanner;
/** Heavily Inspired by ChatGPT and project 3 party slides*/
public class SaveNLoad {

    public static void saveGame(String input) {
        try {
            FileWriter writer = new FileWriter("saved.txt", true);
            writer.write(input);
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to save game.");
        }
    }

    public static String loadGame() {
        StringBuilder fileContent = new StringBuilder();

        try {
            File file = new File("saved.txt");
            if (!file.exists()) {
                return null;
            }
            FileReader reader = new FileReader(file);
            int character;
            while ((character = reader.read()) != -1) {
                fileContent.append((char) character);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to load game.");
        }
        String[] lines = fileContent.toString().split("\n");
        if (lines.length == 0) {
            return null;
        }
        String lastSavedGame = lines[lines.length - 1];
        return lastSavedGame;
    }

    public static void clearFile() {
        try {
            FileWriter writer = new FileWriter("saved.txt", false);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to clear the file.");
        }
    }

    public static boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    public static Scanner openFile() throws FileNotFoundException {
        File file = new File("saved.txt");
        return new Scanner(file);
    }
    public static boolean isLoadable(String savedGameString) {
        return savedGameString != null && !savedGameString.isEmpty() && (savedGameString.length() - 1)!='q';
    }


}

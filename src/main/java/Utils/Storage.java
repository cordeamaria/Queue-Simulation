package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {
    public static void writeLog(String message,String FILE_NAME) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.append(message);
            writer.newLine();  // trecere la linia noua
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


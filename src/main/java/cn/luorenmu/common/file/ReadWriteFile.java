package cn.luorenmu.common.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author LoMu
 * Date 2023.11.22 23:03
 */
public class ReadWriteFile {

    static {
        init();
    }


    private static void init() {
        for (String s : FileManager.FILE_NAME) {
            String json = readRootFileJson(s);
        }
    }

    public static String readRootFileJson(String filename) {
        String fileRootDirectory = FileManager.FILE_ROOT_DIRECTORY;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileRootDirectory + filename))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(readRootFileJson("config.json"));

    }
}

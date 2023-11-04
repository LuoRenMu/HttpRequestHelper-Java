package cn.luorenmu.common.utils;

import java.io.File;
import java.util.Arrays;


/**
 * @author LoMu
 * Date 2023.10.29 20:55
 */
public class FileUtil {


    public static void main(String[] args) {
        File file = new File("./");
        System.out.println(Arrays.toString(file.listFiles()));
    }
}

package cn.luorenmu.common.file;

import cn.luorenmu.common.utils.ScanUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */

//根目录生成配置文件
public class FileGenerate {
    private static final String[] FILE = {"account.json", "config.json"};

    static {
        String path = ScanUtil.PATH.substring(0, ScanUtil.PATH.lastIndexOf("target"));

        for (String f : FILE) {
            path = path + f;
            File file = new File(path);
            if (!file.exists()) {
                try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path))) {

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}

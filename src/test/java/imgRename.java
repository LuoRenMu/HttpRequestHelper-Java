import cn.hutool.crypto.digest.MD5;
import cn.luorenmu.file.ReadWriteFile;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LoMu
 * Date 2024.01.04 15:49
 */
@Slf4j
public class imgRename {
    static final String PATH = "E:\\images\\";
    static List<String> FILES = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        File file = new File(PATH);
        String[] list = file.list();
        //TODO 跳过md5_files.json文件保存的文件
        for (String s : list) {
            File file1 = new File(file, s);
            reDirFileThendeleteNoneDir(file1);
        }
        ReadWriteFile.entiyWriteFile(PATH + "md5_files.json", FILES);
    }

    public static void reDirFileThendeleteNoneDir(File dir) {
        if (dir.isDirectory()) {
            String[] list1 = dir.list();
            if (list1 != null) {
                for (String string : list1) {
                    reDirFileThendeleteNoneDir(new File(dir, string));
                }
            } else {
                boolean ignored = dir.delete();
            }
        } else {
            File file = reName(dir);
            if (file != null) {
                FILES.add(file.getName());
            }
        }


    }

    public static File reName(File file, String child) {
        return reName(new File(file, child));
    }


    public static File reName(File file1) {
        String suffix = file1.getName();
        if (suffix.contains(".")) {
            int i = suffix.lastIndexOf(".");
            suffix = suffix.substring(i);
        }
        String md5FileName = MD5.create().digestHex16(file1) + suffix;
        String rePath = PATH + "\\" + md5FileName;

        try (FileInputStream io = new FileInputStream(file1)) {
            BufferedImage bufferedImage = ImageIO.read(io);
            if (bufferedImage.getWidth() >= 1920 && bufferedImage.getHeight() >= 1080) {
                File dir = new File(PATH + "\\gte1920-1080\\");
                if (!dir.exists()) {
                    boolean ignored = dir.mkdirs();
                }
                rePath = PATH + "\\gte1920-1080\\" + md5FileName;
            }
        } catch (IOException e) {
            log.error(file1.toPath().toString());
        }

        File rename = new File(rePath);
        if (rename.exists()) {
            if (!(file1.getPath().equals(rename.getPath()))) {
                if (file1.delete()) {
                    System.out.println("文件已存在 : 原已删除 : " + file1.getAbsolutePath() + "\t 原文件" + rename.getAbsolutePath());
                }
            }
            System.out.println("文件已存在 : 跳过 : " + file1.getAbsolutePath() + "\t 原文件" + rename.getAbsolutePath());
            return rename;
        }
        if (file1.renameTo(rename)) {
            System.out.println("文件重命名: oldName" + file1.getAbsolutePath() + " \tnewName" + rename.getAbsolutePath());
            return rename;
        } else {
            System.out.println("文件失败:  " + file1.getAbsolutePath() + "\tnewName" + rename.getAbsolutePath());
            return null;
        }
    }
}


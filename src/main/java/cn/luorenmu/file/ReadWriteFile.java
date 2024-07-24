package cn.luorenmu.file;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


/**
 * @author LoMu
 * Date 2023.11.22 23:03
 */
@Slf4j
public class ReadWriteFile {
    public static String CURRENT_PATH;


    protected static String scanFilePath(Class<?> clazz) {
        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
        String filePath = location.getPath().substring(0, location.getPath().lastIndexOf("/") + 1);
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        return filePath;
    }


    public static String readFileJson(String path) {
        log.debug("read file path : {}", path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error("\nfile -> {} <- read error\t{}", path, e.getMessage());
            throw new RuntimeException(e);
        }
        return sb.toString();
    }


    public static String readCurrentFileJson(String fileName) {
        return readFileJson(currentPathFileName(fileName));
    }


    /**
     * 如果init中存在fileName文件 则将其拷贝至outputPath(输出包括文件名)
     * 否则创建一个为空的文件
     *
     * @param fileName   init文件
     * @param outputPath 输出路径.含文件名
     */
    private static void getInitFileStreamThenCreate(String fileName, String outputPath) {
        String initPath = "/init/" + fileName;
        InputStream resourceAsStream = InitializeFile.class.getResourceAsStream(initPath);
        if (resourceAsStream == null) {
            return;
        }
        writeStreamFile(outputPath, resourceAsStream);
    }

    public static File writeStreamFile(String fileName, InputStream inputStream) {
        OutputStream currentFile = createFile(fileName);
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(currentFile)) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            log.error("\nfile -> {} <-  path -> + outputPath output error.\t{}", fileName, e.getMessage());
            throw new RuntimeException(e);
        }
        return new File(fileName);
    }

    public static File writeCurrentStreamFile(String fileName, InputStream inputStream) {
        return writeStreamFile(currentPathFileName(fileName), inputStream);
    }


    public static void checkFileThenCreate(String fileName, boolean remake) {
        String outputFilePath = currentPathFileName(fileName);
        File file = new File(outputFilePath);
        if (!file.exists() || remake) {
            getInitFileStreamThenCreate(fileName, outputFilePath);
        }
        log.info("文件 {} 初始化完成", fileName);
    }

    public static void createCurrentDirs(String fileName) {
        fileName = currentPathFileName(fileName);
        if (fileName.contains("/") || fileName.contains("\\")) {
            fileName = fileName.replaceAll("\\\\", "/");
        }
        int indexOf = fileName.lastIndexOf("/");
        String substring = fileName.substring(0, indexOf);
        File file = new File(substring);
        if (!file.exists()) {
            boolean ignored = file.mkdirs();
        }
    }

    public static OutputStream createFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                String dirStr = path.substring(0, path.lastIndexOf("/"));
                File dir = new File(dirStr);
                boolean ignored1 = dir.mkdirs();
                boolean ignored2 = file.createNewFile();
            } catch (IOException e) {
                log.error("\nfile -> {} <- create error\t{} \t{}", file.getName(), e.getMessage(), path);
                throw new RuntimeException(e);
            }
        }
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            log.error("\nio stream -> {} <- file not found\t{}", path, e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * @param fileName name
     * @return 输出流
     */
    public static OutputStream createCurrentFile(String fileName) {
        createCurrentDirs(fileName);
        String path = currentPathFileName(fileName);
        return createFile(path);
    }

    public static String currentPathFileName(String fileName) {
        return CURRENT_PATH + fileName;
    }

    public static <T> void entiyWriteFileToCurrentDir(String name, T t) {
        entiyWriteFile(currentPathFileName(name), t);
    }

    public static <T> void entiyWriteFile(String path, T t) {
        try (OutputStream outputStream = createFile(path)) {
            String jsonString = JSON.toJSONString(t);
            outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("\nio stream -> " + path + " <- write failed");
            throw new RuntimeException(e);
        }
    }

    public static boolean fileExists(String fileName) {
        return new File(currentPathFileName(fileName)).exists();
    }

    public static InputStream readCurrentFile(String fileName) throws FileNotFoundException {
        String path = CURRENT_PATH + fileName;
        File file = new File(path);
        return new FileInputStream(file);
    }

    public static File[] currentDirs(String fileName) {
        File file = new File(currentPathFileName(fileName));
        return file.listFiles();
    }

    public static String streamToString(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
        } catch (IOException io) {
            log.error("\nio stream -> {} <- read error\t{}", inputStream, io.getMessage());
        }
        return result.toString();
    }

    public static JSONObject readFiles(File[] files) {
        JSONObject jsonObject = new JSONObject();
        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file)) {
                String s = streamToString(fis);
                String name = file.getName();
                if (name.contains("."))
                    name = name.substring(0, name.lastIndexOf("."));
                jsonObject.put(name, JSON.parseObject(s));
            } catch (IOException e) {
                log.error("\nfile -> {} <- read error\t{}", file.getName(), e.getMessage());
            }
        }
        return jsonObject;
    }
}

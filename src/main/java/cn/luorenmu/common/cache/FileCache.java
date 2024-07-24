package cn.luorenmu.common.cache;

import cn.luorenmu.common.convert.JsonObjectConvert;
import cn.luorenmu.file.ReadWriteFile;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author LoMu
 * Date 2024.02.06 14:57
 */
@Slf4j
public class FileCache {


    public static void writeCache(String name, JSONObject t) {
        String cacheFileName = "cache/" + name + ".json";
        if (ReadWriteFile.fileExists(cacheFileName)) {
            JSONObject originCache = readCache(name);
            JSONObject newCache = JsonObjectConvert.skipNullValuesMapping(t, originCache);
            ReadWriteFile.entiyWriteFileToCurrentDir(cacheFileName, newCache);
        } else {
            ReadWriteFile.entiyWriteFileToCurrentDir(cacheFileName, t);
        }
    }

    public static JSONObject readCache(String name) {
        try (InputStream inputStream = ReadWriteFile.readCurrentFile("cache/" + name + ".json")) {
            byte[] bytes = inputStream.readAllBytes();
            return JSON.parseObject(bytes);
        } catch (FileNotFoundException e) {
            return new JSONObject();
        } catch (IOException e) {
            log.error("\nio stream -> " + name + " <- write failed");
            throw new RuntimeException(e);
        }
    }


}


package cn.luorenmu.common.cache;

import cn.luorenmu.common.convert.CacheConvert;
import cn.luorenmu.common.file.ReadWriteFile;
import cn.luorenmu.entiy.TaskCache;
import com.alibaba.fastjson2.JSON;
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


    public static void writeCache(String name, TaskCache t) {
        String cacheFileName = "cache/" + name + ".json";
        if (ReadWriteFile.fileExists(cacheFileName)) {
            TaskCache originCache = readCache(name);
            TaskCache taskCache = CacheConvert.INSTANCE.skipNullValuesMapping(originCache, t);
            ReadWriteFile.entiyWriteFile(cacheFileName, taskCache);
        } else {
            ReadWriteFile.entiyWriteFile(cacheFileName, t);
        }
    }

    public static TaskCache readCache(String name) {
        try (InputStream inputStream = ReadWriteFile.readRootFile("cache/" + name + ".json")) {
            byte[] bytes = inputStream.readAllBytes();
            return JSON.parseObject(bytes, TaskCache.class);
        } catch (FileNotFoundException e) {
            return new TaskCache();
        } catch (IOException e) {
            log.error("\nio stream -> " + name + " <- write failed");
            throw new RuntimeException(e);
        }
    }


}


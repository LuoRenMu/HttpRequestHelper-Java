import cn.luorenmu.common.cache.FileCache;
import cn.luorenmu.common.convert.JsonObjectConvert;
import cn.luorenmu.file.InitializeFile;
import com.alibaba.fastjson2.JSONObject;

/**
 * @author LoMu
 * Date 2024.07.04 7:39
 */
public class JsonObjectConvertTest {
    public static void main(String[] args) {
        InitializeFile.run(JsonObjectConvertTest.class, false);
        JSONObject jsonObject = FileCache.readCache("2");
        JSONObject jsonObject1 = FileCache.readCache("1");
        JSONObject jsonObject11 = JsonObjectConvert.skipNullValuesMapping(jsonObject, jsonObject1);
        FileCache.writeCache("3", jsonObject11);
    }
}

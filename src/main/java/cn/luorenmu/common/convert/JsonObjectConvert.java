package cn.luorenmu.common.convert;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author LoMu
 * Date 2024.02.18 23:37
 */

public class JsonObjectConvert {


    /**
     * @param j1
     * @param j2 copy t1 to t2 skip null values
     * @return merge obj
     */
    public static JSONObject skipNullValuesMapping(JSONObject j1, JSONObject j2) {
        for (String k : j1.keySet()) {
            if (j2.containsKey(k)) {
                Object o = j2.get(k);
                if (o instanceof JSONObject) {
                    skipNullValuesMapping((JSONObject) j1.get(k), (JSONObject) o);
                }
            } else {
                j2.put(k, j1.get(k));
            }
        }
        return j2;
    }


}

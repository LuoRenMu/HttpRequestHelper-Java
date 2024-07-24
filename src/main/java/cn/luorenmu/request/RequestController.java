package cn.luorenmu.request;

import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.request.HttpRequestCentre;
import cn.luorenmu.entiy.Request;
import cn.luorenmu.entiy.RunStorage;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;


/**
 * @author LoMu
 * Date 2024.05.19 15:11
 */
public class RequestController {
    private final JSONObject requests;
    private Request.RequestDetailed requestDetailed;

    public RequestController(String requestName) {

        requests = RunStorage.getConfig("request");
        requestDetailed = getJSONObject(requestName).to(Request.RequestDetailed.class);
    }

    public RequestController setRequestDetailed(String requestName) {
        JSONObject jsonObject = getJSONObject(requestName);
        requestDetailed = jsonObject.to(Request.RequestDetailed.class);
        return this;
    }

    private JSONObject getJSONObject(String requestName) {
        if (!requestName.contains(".")) {
            return requests.getJSONObject(requestName);
        }
        String[] strings = requestName.split("\\.");

        JSONObject jsonObject = null;
        for (int i = 0; i < strings.length; i++) {
            JSONObject temp = requests.getJSONObject(strings[i]);
            if (temp != null) {
                jsonObject = temp;
                if (i != strings.length - 1) {
                    jsonObject = jsonObject.getJSONObject(strings[i + 1]);
                }
            }
        }
        return jsonObject;

    }

    public void setParams(String name, String value) {
        List<Request.RequestParam> requestParams = requestDetailed.getParams();
        requestParams.add(new Request.RequestParam(name, value));
    }

    public void setBody(String name, String value) {
        List<Request.RequestParam> body = requestDetailed.getBody();
        body.add(new Request.RequestParam(name, value));
    }


    public HttpResponse request() {
        return HttpRequestCentre.execute(requestDetailed);
    }
}

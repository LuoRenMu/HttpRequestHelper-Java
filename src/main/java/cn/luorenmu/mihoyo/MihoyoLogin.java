package cn.luorenmu.mihoyo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.utils.RSAUtil;
import cn.luorenmu.common.utils.StringUtil;
import cn.luorenmu.mihoyo.entiy.account.MihoyoDeviceFpRequest;
import cn.luorenmu.mihoyo.entiy.account.MihoyoDeviceFpResponse;
import com.alibaba.fastjson2.JSON;

import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.19 1:01
 */
public class MihoyoLogin {

    public static void main(String[] args) throws Exception {
        MihoyoLogin mihoyoLogin = new MihoyoLogin();
        mihoyoLogin.mihoyoPasswordLogin(RSAUtil.encrypt("13260778819"), RSAUtil.encrypt("XG142536789"), mihoyoLogin.getDevice().getData().getDeviceFp());
        /* mihoyoLogin.mobileVerification("13260778819","389411");*/
    }

    public void mihoyoPasswordLogin(String account, String password, String device) {
        HttpRequest post = HttpRequest.post("https://passport-api.mihoyo.com/account/ma-cn-passport/app/loginByPassword");
        post.body(JSON.toJSONString(Map.of("account", account, "password", password)));
        post.header("x-rpc-device_fp", device);
        post.header("x-rpc-device_id", "b1d55213-05d8-477b-bef2-e2dc04ba25a7");
        post.header("x-rpc-game_biz", "bbs_cn");
        post.header("x-rpc-client_type", "2");
        post.header("x-rpc-app_id", "bll8iq97cem8");
        HttpResponse execute = post.execute();
        System.out.println(execute.body());
        System.out.println(execute.headers());
    }

    public void sendMobileVerificationDevic() {
        //HcFOVDUQ0TmvYwJlf9YCmnAGX362a9vV
        HttpRequest post = HttpRequest.post("https://webapi.account.mihoyo.com/Api/create_mobile_captcha?action_type=login&mmt_key=" + "HcFOVDUQ0TmvYwJlf9YCmnAGX362a9vV" + "&mobile=13260778819&t=" + System.currentTimeMillis() / 1000);
        HttpResponse execute = post.execute();
        System.out.println(execute);
    }

    public void mobileVerificationDevice(String mobile, String code) {
        HttpRequest post = HttpRequest.post("https://webapi.account.mihoyo.com/Api/login_by_mobilecaptcha?mobile=" + mobile + "&mobile_captcha=" + code + "&source=user.mihoyo.com&t=" + System.currentTimeMillis() / 1000);
        System.out.println(post.execute());
    }

    public String accessVerfication() {
        HttpRequest get = HttpRequest.get("https://webapi.account.mihoyo.com/Api/create_mmt?scene_type=1&now=" + System.currentTimeMillis() / 1000 + "&reason=user.mihoyo.com%2523%252Flogin%252Fcaptcha&action_type=login_by_mobile_captcha");
        System.out.println(get.execute().body());
        return null;
    }

    public MihoyoDeviceFpResponse getDevice() {
        HttpRequest post = HttpRequest.post("https://public-data-api.mihoyo.com/device-fp/api/getFp");

        MihoyoDeviceFpRequest mihoyoDeviceFpRequest = new MihoyoDeviceFpRequest();
        mihoyoDeviceFpRequest.setExtFields("{\"userAgent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0\",\"browserScreenSize\":\"1981440\",\"maxTouchPoints\":\"0\",\"isTouchSupported\":\"0\",\"browserLanguage\":\"zh-CN\",\"browserPlat\":\"Win32\",\"browserTimeZone\":\"Asia/Shanghai\",\"webGlRender\":\"ANGLE (AMD, AMD Radeon(TM) Graphics (0x00001681) Direct3D11 vs_5_0 ps_5_0, D3D11)\",\"webGlVendor\":\"Google Inc. (AMD)\",\"numOfPlugins\":\"5\",\"listOfPlugins\":[\"PDF Viewer\",\"Chrome PDF Viewer\",\"Chromium PDF Viewer\",\"Microsoft Edge PDF Viewer\",\"WebKit built-in PDF\"],\"screenRatio\":\"1\",\"deviceMemory\":\"8\",\"hardwareConcurrency\":\"16\",\"cpuClass\":\"unknown\",\"ifNotTrack\":\"unknown\",\"ifAdBlock\":\"0\",\"hasLiedLanguage\":\"0\",\"hasLiedResolution\":\"1\",\"hasLiedOs\":\"0\",\"hasLiedBrowser\":\"0\",\"canvas\":\"e7b877053f44d3f971c48f9bc6e8147830009bfce762c85c19a1754edec4c725\",\"webDriver\":\"0\",\"colorDepth\":\"24\",\"pixelRatio\":\"1\",\"packageName\":\"unknown\",\"packageVersion\":\"2.20.0\",\"webgl\":\"c715d4fde4ff2f69599541e1651294d674f4b2385f73951da1645ff0c869dc26\"}");
        mihoyoDeviceFpRequest.setDeviceFp("38d7f1a1e042b");
        mihoyoDeviceFpRequest.setPlatform("4");
        mihoyoDeviceFpRequest.setAppName("bbs_cn");
        mihoyoDeviceFpRequest.setDeviceId("b1d55213-05d8-477b-bef2-e2dc04ba25a7");
        mihoyoDeviceFpRequest.setSeedTime(System.currentTimeMillis() + "");
        mihoyoDeviceFpRequest.setSeedId(StringUtil.getRandomStr(16).toLowerCase());
        post.body(JSON.toJSONString(mihoyoDeviceFpRequest));
        HttpResponse execute = post.execute();
        System.out.println(execute);
        return JSON.parseObject(execute.body(), MihoyoDeviceFpResponse.class);
    }
}

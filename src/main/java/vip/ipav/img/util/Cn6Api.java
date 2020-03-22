package vip.ipav.img.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import vip.ipav.okhttp.OkHttpClientTools;
import java.util.Calendar;

/**
 * 6间房API
 */
public class Cn6Api {

    public final static String CN6_URL = "https://pic.v.6.cn/api/uploadForGeneral.php";

    public static JSONObject uploadImg(String imgBase64) {
        if (Base64.decode(imgBase64)==null){
            return null;
        }
        try {
            String ret = OkHttpClientTools.getInstance()
                    .upload()
                    .url(CN6_URL)
                    .addFile("file",Calendar.getInstance().getTime().toString(),VoneUtil.generateImage(imgBase64))
                    .addParam("size","b1,s23")
                    .addParam("pid","1002")
                    .execute()
                    .body()
                    .string();
            return JSON.parseObject(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
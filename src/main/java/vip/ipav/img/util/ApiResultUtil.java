package vip.ipav.img.util;


import vip.ipav.img.dto.ApiRes;
import com.alibaba.fastjson.JSONObject;

public class ApiResultUtil {

    public static ApiRes success(String img){
        ApiRes apiRes = new ApiRes();
        apiRes.setCode(1);
        apiRes.setMsg("操作成功");
        apiRes.setImg(img);
        return apiRes;
    }

    public static ApiRes error(String msg){
        ApiRes apiRes = new ApiRes();
        apiRes.setCode(0);
        apiRes.setMsg(msg);
        apiRes.setImg(null);
        return apiRes;
    }

    /**
     * CN6结果处理
     * @param map
     * @return
     */
    public static ApiRes success(JSONObject map){
        ApiRes apiRes = new ApiRes();
        apiRes.setCode(1);
        apiRes.setMsg("操作成功");
        if(map.containsKey("flag") && "001".equals(map.getString("flag"))){
            String url = map.getJSONObject("content").getJSONObject("url").getString("link");
            apiRes.setImg(url);
        }
        apiRes.setOrigin(map);
        return apiRes;
    }
}

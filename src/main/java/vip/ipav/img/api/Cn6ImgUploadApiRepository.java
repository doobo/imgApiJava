package vip.ipav.img.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import vip.ipav.img.dto.ApiRes;
import vip.ipav.img.dto.FileDTO;
import vip.ipav.img.util.ApiResultUtil;
import vip.ipav.img.util.Cn6Api;
import vip.ipav.okhttp.OkHttpClientTools;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

@Service("cn6ImgUploadApiRepository")
public class Cn6ImgUploadApiRepository implements ImgUploadApiRepository{
    /**
     * 6间房专用CDN类型
     * @return
     */
    @Override
    public Integer getApiType() {
        return 3;
    }

    @Override
    public String getApiName() {
        return "6间房CDN(无需配置,会掉图)";
    }

    @Override
    public ApiRes uploadImg(String imgBase64) {
        JSONObject obj = Cn6Api.uploadImg(imgBase64);
        if (Objects.nonNull(obj)){
            return ApiResultUtil.success(obj);
        }
        return ApiResultUtil.error("上传失败");

    }

    @Override
    public ApiRes uploadImg(FileDTO dto) {
        try {
            String res = OkHttpClientTools.getInstance()
                    .upload()
                    .url(Cn6Api.CN6_URL)
                    .addFile("file", URLEncoder.encode(dto.getFile().getOriginalFilename(),"UTF-8")
                            ,dto.getFile().getBytes())
                    .addParam("size","b1,s23")
                    .addParam("pid","1002")
                    .execute()
                    .body().string();
            JSONObject obj = JSON.parseObject(res);
            if (Objects.nonNull(obj)){
                return ApiResultUtil.success(obj);
            }
            return ApiResultUtil.error("上传失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResultUtil.error("上传失败");
    }
}

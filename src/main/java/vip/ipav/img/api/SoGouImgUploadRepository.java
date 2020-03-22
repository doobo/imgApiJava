package vip.ipav.img.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vip.ipav.img.dto.ApiRes;
import vip.ipav.img.dto.FileDTO;
import vip.ipav.img.util.ApiResultUtil;
import vip.ipav.img.util.SougouApi;
import vip.ipav.okhttp.OkHttpClientTools;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Service里面的名字不要重复
 */
@Service("soGouImgUploadRepository")
public class SoGouImgUploadRepository implements ImgUploadApiRepository {

    @Value("#{'${sg.proxies}'.split(',')}")
    private List<String> proxies;

    /**
     * SoGou的CDN专用类型:1
     * @return
     */
    @Override
    public Integer getApiType() {
        return 1;
    }

    @Override
    public String getApiName() {
        return "搜狗CDN,需配置域名代理,目测会掉图";
    }

    /**
     * base64位方式上传图片
     * @param imgBase64
     * @return
     */
    @Override
    public ApiRes uploadImg(String imgBase64) {
        String res = SougouApi.uploadImg(imgBase64);
        if (res.indexOf("http")>=0){
            return ApiResultUtil.success(res).setProxy(proxies);
        }else{
            return ApiResultUtil.error("上传失败").setOriginData(res);
        }
    }

    @Override
    public ApiRes uploadImg(FileDTO dto) {
        try {
            String res = OkHttpClientTools.getInstance()
                    .upload()
                    .url(SougouApi.url)
                    .addFile("files", URLEncoder.encode(dto.getFile().getOriginalFilename(),"UTF-8")
                            ,dto.getFile().getBytes())
                    .execute()
                    .body().string();
            if (res.indexOf("http")>=0){
                return ApiResultUtil.success(res).setProxy(proxies);
            }else{
                return ApiResultUtil.error("上传失败").setOriginData(res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResultUtil.error("上传失败");
    }


}

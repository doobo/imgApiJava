package vip.ipav.img.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.coobird.thumbnailator.Thumbnails;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vip.ipav.img.dto.ApiRes;
import vip.ipav.img.dto.FileDTO;
import vip.ipav.img.entity.VoneConfig;
import vip.ipav.img.service.VoneService;
import vip.ipav.img.util.ApiResultUtil;
import vip.ipav.img.util.VoneUtil;
import vip.ipav.okhttp.OkHttpClientTools;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

/**
 * 邮箱注册即可获取5G空间,需配置token
 * @link https://sm.ms/
 */
@Service("smMsImgUploadApiRepository")
public class SmMsImgUploadApiRepository implements ImgUploadApiRepository{

    @Value("${smToken}")
    private String smToken;

    private static final String SM_URL="https://sm.ms/api/v2/upload";

    @Resource
    private VoneService voneService;

    /**
     * 6间房专用CDN类型
     * @return
     */
    @Override
    public Integer getApiType() {
        return 2;
    }

    @Override
    public String getApiName() {
        return "SM.MS(需要邮箱注册,免费5G空间)";
    }

    @Override
    public ApiRes uploadImg(String imgBase64) {
        JSONObject obj = upload(imgBase64);
        if (Objects.nonNull(obj)){
            if(obj.getBoolean("success")){
                return ApiResultUtil.success(obj.getJSONObject("data").getString("url"))
                        .setOriginData(obj.getJSONObject("data"));
            }else if(StringUtils.isNotBlank(obj.getString("images"))){
                return ApiResultUtil.success(obj.getString("images")).setOriginData(obj);
            }
            return ApiResultUtil.error("上传失败").setOriginData(obj);
        }else{
            return ApiResultUtil.error("上传失败");
        }
    }

    @Override
    public ApiRes uploadImg(FileDTO dto) {
        try {
            String token = voneService.getVoneConfig().getMsToken();
            if(StringUtils.isNotBlank(token)){
                smToken = token;
            }
            MultipartFile multFile = dto.getFile();
            byte[] bs = multFile.getBytes();
            if(multFile.getSize()/1024/1024>5 && !multFile.getOriginalFilename().endsWith(".png")){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Thumbnails.of(multFile.getInputStream()).scale(1f).outputQuality(0.5f).toOutputStream(out);
                bs = out.toByteArray();
                out.close();
            }
            String res = OkHttpClientTools.getInstance()
                    .upload()
                    .url(SM_URL)
                    .addHeader("Authorization",smToken)
                    .addHeader("User-Agent", "Mozilla/5.0 (SymbianOS/9.1; U; en-us) AppleWebKit/413 (KHTML, like Gecko) Safari/413")
                    .addFile("smfile", VoneUtil.getValueEncoded(multFile.getOriginalFilename()), bs)
                    .execute()
                    .body().string();
            JSONObject obj = JSON.parseObject(res);
            if (Objects.nonNull(obj)){
                if(obj.getBoolean("success")){
                    return ApiResultUtil.success(obj.getJSONObject("data").getString("url"))
                            .setOriginData(obj.getJSONObject("data"));
                }else if(StringUtils.isNotBlank(obj.getString("images"))){
                    return ApiResultUtil.success(obj.getString("images")).setOriginData(obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResultUtil.error("上传失败");
    }

    private JSONObject upload(String imgBase64) {
        if (Base64.decode(imgBase64)==null){
            return null;
        }
        String token = voneService.getVoneConfig().getMsToken();
        if(StringUtils.isNotBlank(token)){
            smToken = token;
        }
        try {
            Response ret = OkHttpClientTools.getInstance()
                    .upload()
                    .url(SM_URL)
                    .addHeader("Authorization",smToken)
                    .addHeader("Accept-Language", "zh-cn")
                    .addHeader("Connection", "Keep-Alive")
                    .addHeader("Accept-Charset", "UTF-8,utf-8;q=0.7,*;q=0.7")
                    .addHeader("User-Agent", "Mozilla/5.0 (SymbianOS/9.1; U; en-us) AppleWebKit/413 (KHTML, like Gecko) Safari/413")
                    .addFile("smfile",Calendar.getInstance().getTime().toString(),VoneUtil.generateImage(imgBase64))
                    .execute();
            if(ret.code() == 200){
                return JSON.parseObject(ret.body().string());
            }else {
                System.out.println(ret.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 合并byte数组
     */
    public static byte[] unitByteArray(byte[] byte1, byte[] byte2) {
        byte[] unitByte = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
        System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
        return unitByte;
    }

    public String getSmToken() {
        return smToken;
    }

    public void setSmToken(String smToken) {
        this.smToken = smToken;
    }
}

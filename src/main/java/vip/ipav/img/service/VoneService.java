package vip.ipav.img.service;

import org.apache.commons.lang.StringUtils;
import vip.ipav.img.api.ImgUploadApiModeFactory;
import vip.ipav.img.api.ImgUploadApiRepository;
import vip.ipav.img.dto.ApiRes;
import vip.ipav.img.dto.CommonRes;
import vip.ipav.img.dto.FileDTO;
import vip.ipav.img.entity.VoneConfig;
import org.springframework.stereotype.Service;
import vip.ipav.img.util.*;


@Service
public class VoneService {

    private VoneConfig voneConfig = null;

    //上传
    public ApiRes doUpload(String key, String imgBase64){
        if (voneConfig == null){
            getVoneConfig();
        }
        if (!key.equals(voneConfig.getKey())){
            return ApiResultUtil.error("通讯密钥错误");
        }
        ImgUploadApiRepository api = ImgUploadApiModeFactory.getWorkflowRepository(voneConfig.getType());
        if(api == null){
            return ApiResultUtil.error("类型错误");
        }
        return api.uploadImg(imgBase64);
    }

    /**
     * 文件上传
     * @param dto
     * @return
     */
    public ApiRes doUploadFile(FileDTO dto){
        if (voneConfig == null){
            getVoneConfig();
        }
        if(StringUtils.isBlank(dto.getKey())){
            return ApiResultUtil.error("通讯密钥错误");
        }
        if (!dto.getKey().equals(voneConfig.getKey())){
            return ApiResultUtil.error("通讯密钥错误");
        }
        ImgUploadApiRepository api = ImgUploadApiModeFactory.getWorkflowRepository(dto.getType());
        if(api == null){
            return ApiResultUtil.error("类型错误");
        }
        return api.uploadImg(dto);
    }

    //拉取配置
    public CommonRes getConfig(){
        if (voneConfig == null){
            getVoneConfig();
        }
        return ResultUtil.success(voneConfig);
    }

    //设置配置
    public CommonRes setConfig(VoneConfig newConfig){
        if (voneConfig == null){
            voneConfig = getVoneConfig();
        }
        voneConfig.setType(newConfig.getType());
        voneConfig.setSinaUser(newConfig.getSinaUser());
        voneConfig.setSinaPass(newConfig.getSinaPass());
        voneConfig.setKey(newConfig.getKey());
        voneConfig.setAdmin(newConfig.getAdmin());
        voneConfig.setSinaCookie("");
        voneConfig.setSinaUpdateTime("");
        voneConfig.setMsToken(newConfig.getMsToken());
        return ResultUtil.success();
    }


    public VoneConfig getVoneConfig(){
        if (voneConfig == null){
            voneConfig = new VoneConfig();
            voneConfig.setAdmin("123456");
            voneConfig.setSinaUser("");
            voneConfig.setSinaPass("");
            voneConfig.setSinaCookie("");
            voneConfig.setKey("");
            voneConfig.setType(1);
            voneConfig.setSinaUpdateTime("");
        }
        return voneConfig;
    }

    public void save(VoneConfig voneConfig){
    }

    public void setVoneConfig(VoneConfig voneConfig) {
        this.voneConfig = voneConfig;
    }
}

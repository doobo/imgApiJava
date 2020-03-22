package vip.ipav.img.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.ipav.img.api.ImgUploadApiModeFactory;
import vip.ipav.img.api.SmMsImgUploadApiRepository;
import vip.ipav.img.dto.ApiRes;
import vip.ipav.img.dto.CommonRes;
import vip.ipav.img.dto.FileDTO;
import vip.ipav.img.entity.VoneConfig;
import vip.ipav.img.service.VoneService;
import vip.ipav.img.util.ApiResultUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import vip.ipav.img.util.ResultUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VoneController {

    @Autowired
    private VoneService voneService;

    @Value("${token}")
    private String token;

    @Resource
    SmMsImgUploadApiRepository smMsImgUploadApiRepository;

    /**
     *
     * @param rsp
     * @throws IOException
     */
    @RequestMapping("/api")
    public void api(FileDTO dto, HttpServletResponse rsp) throws IOException {
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        boolean str = false;
        String onlyUrl = dto.getOnlyUrl();
        String key = dto.getKey();
        String imgBase64 = dto.getImgBase64();
        if (onlyUrl!=null && onlyUrl.equals("1")){
            str = true;
        }
        if (key==null || key.equals("")){
            if (str){
                rsp.getWriter().print("null");
            }else{
                rsp.getWriter().print(new Gson().toJson(ApiResultUtil.error("请传入通讯密钥")));
            }
        }
        if (imgBase64 == null || imgBase64.equals("")){
            if (str){
                rsp.getWriter().print("null");
            }else{
                rsp.getWriter().print(new Gson().toJson(ApiResultUtil.error("请传入图片的Base64编码")));
            }
        }
        ApiRes apiRes = voneService.doUpload(key, imgBase64);
        if (str){
            rsp.getWriter().print(apiRes.getImg());
        }else{
            rsp.getWriter().print(new Gson().toJson(apiRes));
        }
    }

    /**
     * 配置初始
     * @return
     */
    @RequestMapping("/conf/getConfig")
    public CommonRes getConfig(){
        VoneConfig conf = voneService.getVoneConfig();
        String msToken = conf.getMsToken();
        if(StringUtils.isBlank(msToken) && StringUtils.isNotBlank(smMsImgUploadApiRepository.getSmToken())){
            conf.setMsToken(smMsImgUploadApiRepository.getSmToken());
        }
        String key = conf.getKey();
        if(StringUtils.isBlank(key) && StringUtils.isNotBlank(token)){
            conf.setKey(token);
        }
        return voneService.getConfig();
    }

    @RequestMapping("/conf/update")
    public CommonRes setConfig(VoneConfig pvoneConfig){
        return voneService.setConfig(pvoneConfig);
    }

    @RequestMapping("/conf/apis")
    public Map<Integer, String> getApis(){
        return ImgUploadApiModeFactory.getImgUploadNames();
    }

    /**
     * 单文件上传
     * @param dto
     * log.info("[文件类型] - [{}]", file.getContentType());
     * log.info("[文件名称] - [{}]", file.getOriginalFilename());
     * log.info("[文件大小] - [{}]", file.getSize());
     * file.transferTo(new File("D:/Users/" + file.getOriginalFilename()));
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Object upload(FileDTO dto){
        Assert.notNull(dto,"参数不能为空");
        Assert.notNull(dto.getFile(),"文件不能为空");
        ApiRes res = voneService.doUploadFile(dto);
        if(res == null){
            return ResultUtil.error(0,"上传失败");
        }
        if(res.getCode() == 0){
            return res;
        }
        if(StringUtils.isNotBlank(dto.getOnlyUrl())){
            return StringUtils.isBlank(res.getProxy())?res.getImg():res.getProxy();
        }
        return res;
    }

    /**
     * 多文件上传
     * @return
     * @throws IOException
     */
    @PostMapping("/uploads")
    public List<Map<String, String>> uploads(FileDTO dto)
            throws IOException {
        MultipartFile[] files = dto.getFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        List<Map<String, String>> results = new ArrayList<>();
        for (MultipartFile file : files) {
            // TODO Spring Mvc 提供的写入方式
            file.transferTo(File.createTempFile(file.getOriginalFilename(),"png"));
            Map<String, String> map = new HashMap<>(16);
            map.put("contentType", file.getContentType());
            map.put("fileName", file.getOriginalFilename());
            map.put("fileSize", file.getSize() + "");
            results.add(map);
        }
        return results;
    }

    @PostMapping("/upload_base64")
    public void uploadBase64(FileDTO dto) throws IOException {
        // TODO BASE64 方式的 格式和名字需要自己控制
        // （如 png 图片编码后前缀就会是 data:image/png;base64,）
        String base64 = dto.getImgBase64();
        final File tempFile = File.createTempFile("img","png");
        // TODO 防止有的传了 data:image/png;base64, 有的没传的情况
        String[] d = base64.split("base64,");
        final byte[] bytes = Base64Utils.decodeFromString(d.length > 1 ? d[1] : d[0]);
        FileCopyUtils.copy(bytes, tempFile);
    }
}

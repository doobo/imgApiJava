package vip.ipav.img.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileDTO {

    /**
     * 上传文件
     */
    private MultipartFile file;

    /**
     * 通讯密钥
     */
    private String key;

    /**
     * 是否只返回URL
     */
    private String onlyUrl;

    /**
     * 默认传输类型,SoGou
     */
    private Integer type = 1;

    private String imgBase64;

    private MultipartFile[] files;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOnlyUrl() {
        return onlyUrl;
    }

    public void setOnlyUrl(String onlyUrl) {
        this.onlyUrl = onlyUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}

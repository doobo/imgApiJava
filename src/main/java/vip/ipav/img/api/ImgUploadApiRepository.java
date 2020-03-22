package vip.ipav.img.api;

import vip.ipav.img.dto.ApiRes;
import vip.ipav.img.dto.FileDTO;

public interface ImgUploadApiRepository {

    /**
     * 对应的API类型,不要重复
     * @return
     */
    Integer getApiType();

    /**
     * 对应的API说明
     * @return
     */
    String getApiName();

    /**
     * base64位方式上传图片
     * @param imgBase64
     * @return
     */
    ApiRes uploadImg(String imgBase64);

    /**
     * 通过文件形式上传
     * @param dto
     * @return
     */
    ApiRes uploadImg(FileDTO dto);
}

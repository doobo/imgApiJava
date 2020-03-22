package vip.ipav.img.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传API工厂类
 */
@Component
public class ImgUploadApiModeFactory implements ApplicationContextAware {
    private static Map<Integer, ImgUploadApiRepository> imgUploadApis;
    private static Map<Integer, String> imgUploadNames;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ImgUploadApiRepository> map = applicationContext.getBeansOfType(ImgUploadApiRepository.class);
        imgUploadApis = new HashMap<>();
        imgUploadNames = new HashMap<>();
        map.forEach((key, value) -> imgUploadApis.put(value.getApiType(), value));
        map.forEach((key, value) -> imgUploadNames.put(value.getApiType(), value.getApiName()));
    }

    @SuppressWarnings("unchecked")
    public static <T extends ImgUploadApiRepository> T getWorkflowRepository(Integer type) {
        return (T)imgUploadApis.get(type);
    }

    public static Map<Integer, String> getImgUploadNames() {
        return imgUploadNames;
    }
}

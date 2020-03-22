package vip.ipav.img.conf;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class UploadConfig {
    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize(DataSize.parse("10MB"));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse("50MB"));
        // 规避找不到临时目录问题
        factory.setLocation("/tmp");
        return factory.createMultipartConfig();
    }

}
package vip.ipav.img.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileCommonUtils {

    /**
     * MultipartFile 转 File
     * 文件使用完成后请调用 {@link FileCommonUtils#deleteFile} 方法删除临时文件
     * @param file MultipartFile对象
     * @param suffix 文件后缀 .jpg等
     * @return 返回 File 对象
     * @throws IOException
     */
    public static File multipartFileToFile(MultipartFile file, String suffix) throws IOException {
        InputStream ins = file.getInputStream();
        File f = new File(UUID.randomUUID().toString() + suffix);
        FileUtils.copyInputStreamToFile(ins,f);
        return f;
    }

    /**
     * 删除文件
     *
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            file.deleteOnExit();
        }
    }
}
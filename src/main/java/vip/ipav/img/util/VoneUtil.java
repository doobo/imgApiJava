package vip.ipav.img.util;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class VoneUtil {

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            //e.printStackTrace();
            return null;
        }
        return result.toString();
    }

    /**
     * 取两个文本之间的文本值
     *
     * @param text
     * @param left
     * @param right
     * @return
     */
    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    public static void contentToTxt(String filePath, String content) {
        try{
            File file = new File(URLDecoder.decode(filePath,"utf-8"));
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,false));
            writer.write(content);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 生成指定范围的随机整数
     * @param min
     * @param max
     * @return
     */
    public static int randomRange(final int min,final int max){
        Random random = ThreadLocalRandom.current();
        return random.nextInt(max)%(max-min+1) + min;
    }

    /**
     * @param imgData base64编码字符串
     * @Description: 将base64编码字符串转换为图片
     */
    public static byte[] generateImage(String imgData) {
        if (imgData == null){
            return null;
        }
        imgData = imgData.substring(imgData.indexOf(',')+1);
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgData);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValueEncoded(String value) throws UnsupportedEncodingException {
        if (value == null) return "null";
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                return URLEncoder.encode(newValue, "UTF-8");
            }
        }
        return newValue;
    }


    public static void inputStream2file(InputStream ins,File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

}

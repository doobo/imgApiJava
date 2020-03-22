package vip.ipav.img.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//========搜狗图床========
public class SougouApi {

    public final static String url = "http://pic.sogou.com/pic/upload_pic.jsp";

    public static String uploadImg(String imgBase64) {
        if (Base64.decode(imgBase64)==null){
            return "";
        }
        byte[] PostData;

        byte[] tmp = unitByteArray(Base64.decode("LS0tLS0tV2ViS2l0Rm9ybUJvdW5kYXJ5R0xmR0IwSGdVTnRwVFQxaw0KQ29udGVudC1EaXNwb3NpdGlvbjogZm9ybS1kYXRhOyBuYW1lPSJwaWNfcGF0aCI7IGZpbGVuYW1lPSIxMS5wbmciDQpDb250ZW50LVR5cGU6IGltYWdlL3BuZw0KDQo=")
                , Base64.decode(imgBase64));

        PostData = unitByteArray(tmp, Base64.decode("DQotLS0tLS1XZWJLaXRGb3JtQm91bmRhcnlHTGZHQjBIZ1VOdHBUVDFrLS0NCg=="));

        String ret = "";
        URL u = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        //尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryGLfGB0HgUNtpTT1k");
            con.setRequestProperty("Content-Length", String.valueOf(PostData.length));

            OutputStream outStream = con.getOutputStream();
            outStream.write(PostData);
            outStream.flush();
            outStream.close();
            //读取返回内容
            inputStream = con.getInputStream();

            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufr = new BufferedReader(isr);
            String str;

            while ((str = bufr.readLine()) != null) {
                ret+=str;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
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
}
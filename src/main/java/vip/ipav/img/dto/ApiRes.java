package vip.ipav.img.dto;

import vip.ipav.img.util.VoneUtil;

import java.util.List;

public class ApiRes {
    private Integer code;

    private String msg;

    private String img;

    /**
     * 接口返回的源数据
     */
    private Object origin;

    /**
     * 代理图片地址
     */
    private String proxy;

    /**
     * 设置代理地址
     * @param proxies
     * @return
     */
    public ApiRes setProxy(List<String> proxies) {
        if(img != null && img.indexOf("sogoucdn.com") > -1){
            if(proxies == null || proxies.isEmpty()){
                return this;
            }
            String[] str = img.split("sogoucdn.com");
            if(str != null && str.length > 1){
                if(proxies.size() == 1){
                    proxy = proxies.get(0) +"/sg" + str[1];
                }else {
                    proxy = proxies.get(VoneUtil.randomRange(0,proxies.size()-1)) +"/sg" + str[1];
                }
            }
        }
        return this;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public ApiRes setOriginData(Object origin) {
        this.origin = origin;
        return this;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }


    @Override
    public String toString() {
        return "ApiRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", img='" + img + '\'' +
                ", origin=" + origin +
                '}';
    }
}

package vip.ipav.img.entity;

public class VoneConfig {
    private String admin;
    private String SinaUser;
    private String SinaPass;
    private String key;
    /**
     * 1搜狗
     * 2新浪
     * 4其它
     */
    private Integer type;
    private String SinaUpdateTime;
    private String SinaCookie;
    /**
     * https://ms.sm密钥
     */
    private String msToken;

    @Override
    public String toString() {
        return "VoneConfig{" +
                "admin='" + admin + '\'' +
                ", SinaUser='" + SinaUser + '\'' +
                ", SinaPass='" + SinaPass + '\'' +
                ", key='" + key + '\'' +
                ", type=" + type +
                ", SinaUpdateTime='" + SinaUpdateTime + '\'' +
                ", SinaCookie='" + SinaCookie + '\'' +
                '}';
    }

    public String getSinaCookie() {
        return SinaCookie;
    }

    public void setSinaCookie(String sinaCookie) {
        SinaCookie = sinaCookie;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getSinaUser() {
        return SinaUser;
    }

    public void setSinaUser(String sinaUser) {
        SinaUser = sinaUser;
    }

    public String getSinaPass() {
        return SinaPass;
    }

    public void setSinaPass(String sinaPass) {
        SinaPass = sinaPass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSinaUpdateTime() {
        return SinaUpdateTime;
    }

    public void setSinaUpdateTime(String sinaUpdateTime) {
        SinaUpdateTime = sinaUpdateTime;
    }

    public String getMsToken() {
        return msToken;
    }

    public void setMsToken(String msToken) {
        this.msToken = msToken;
    }
}

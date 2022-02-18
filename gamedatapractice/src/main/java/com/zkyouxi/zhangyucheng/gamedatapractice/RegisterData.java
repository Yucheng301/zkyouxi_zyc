package com.zkyouxi.zhangyucheng.gamedatapractice;

public class RegisterData {

    int time = 541311;
    String jsonString = "{'app_id':12875493, 'ad_id': 5312154, 'login_username':'zyc4513547', 'login_password':'zyc1997112','has_tips': '0', 'time': "+time +
            ", 'os':'android', 'extra':{'imei':'SDAKNSDKGALPWRT'}, 'sign':'asda23234ad4g1lkk2os'}";

    int app_id, ad_id;
    String login_username, login_password, has_tips, os, extra, sign;


    public int getB() {
        return time;
    }

    public void setB(int time) {
        this.time = time;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getAd_id() {
        return ad_id;
    }

    public void setAd_id(int ad_id) {
        this.ad_id = ad_id;
    }

    public String getLogin_username() {
        return login_username;
    }

    public void setLogin_username(String login_username) {
        this.login_username = login_username;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

    public String getHas_tips() {
        return has_tips;
    }

    public void setHas_tips(String has_tips) {
        this.has_tips = has_tips;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSign() {
        return sign;
    }

    private void setSign() {
        this.sign = getSignature();
    }

    public String getSignature(){
        String signature = null;
        String secret_key = "92a6c1ab-161a-48de-b05c-492f8db2fc96";
        String requestData = "app_id="+ad_id+"&ad_id="+ad_id+"&login_username="
                +login_username+"&login_password="+login_password+"&has_tips="+has_tips
                +"&os="+os+"&extra="+extra+"&time="+time;
        signature = MD5Util.string2MD5(requestData + secret_key);
        return signature;
    }
}

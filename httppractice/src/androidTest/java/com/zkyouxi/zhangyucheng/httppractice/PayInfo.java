package com.zkyouxi.zhangyucheng.httppractice;

import java.io.Serializable;

/**
 * 支付信息
 * <p>
 * Created by gui on 2016/9/20.
 */
public class PayInfo implements Serializable {


    private String out_trade_no;//cp订单号 out_trade_no
    private int total_charge;//支付金额，rmb，单位为分
    private String access_token;//登录成功后服务端返回的access_token
    private String async_callback_url;//支付成功回调地址
    private String product_id;//商品id
    private String product_name;//商品名
    private int product_amount;//商品数量
    private String product_desc;//商品展示描述
    private int rate;//兑换比率
    private String role_id;//角色id
    private String role_name;//角色名
    private String server_id;//角色所在的区服id
    private String extend;//透传字段，原样返回，
    private String union_extend;//透传字段，SDK专用
    private String currency_code;//使用国际通用货币代码
    private int pay_type;//支付类型，如：支付宝，微信
    private String order_no;


    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getOutTradeNo() {
        return out_trade_no;
    }


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_charge() {
        return total_charge;
    }

    public void setTotal_charge(int total_charge) {
        this.total_charge = total_charge;
    }

    public String getAsync_callback_url() {
        return async_callback_url;
    }

    public void setAsync_callback_url(String async_callback_url) {
        this.async_callback_url = async_callback_url;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(int product_amount) {
        this.product_amount = product_amount;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getUnion_extend() {
        return union_extend;
    }

    public void setUnion_extend(String union_extend) {
        this.union_extend = union_extend;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    @Override
    public String toString() {
        return "PayInfo{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", total_charge=" + total_charge +
                ", access_token='" + access_token + '\'' +
                ", async_callback_url='" + async_callback_url + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_amount=" + product_amount +
                ", product_desc='" + product_desc + '\'' +
                ", rate=" + rate +
                ", role_id='" + role_id + '\'' +
                ", role_name='" + role_name + '\'' +
                ", server_id='" + server_id + '\'' +
                ", extend='" + extend + '\'' +
                ", union_extend='" + union_extend + '\'' +
                ", currency_code='" + currency_code + '\'' +
                ", pay_type=" + pay_type +
                '}';
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

}

package com.zkyouxi.zhangyucheng.proxytesting;

public class HuangNiu implements SellerInterface {


    public SellerInterface target;

    public HuangNiu (SellerInterface sellerInterface){
        target = sellerInterface;
    }



    @Override
    public Ticket getTicket() {
        return target.getTicket();
    }

    @Override
    public String sellTicket() {
        return "这是" + (target.getTicket().getTicketPrice() + 5) +" 块钱的黄牛票";
    }
}

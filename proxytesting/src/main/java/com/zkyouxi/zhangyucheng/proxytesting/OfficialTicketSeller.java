package com.zkyouxi.zhangyucheng.proxytesting;


public class OfficialTicketSeller implements SellerInterface{




    @Override
    public Ticket getTicket() {
       Ticket ticket = new Ticket(001,10);
        return ticket;
    }

    @Override
    public String sellTicket() {
        return "这是" + this.getTicket().getTicketPrice() + " 块钱的的官方票";
    }
}

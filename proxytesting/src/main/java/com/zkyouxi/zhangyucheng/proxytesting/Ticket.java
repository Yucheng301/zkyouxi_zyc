package com.zkyouxi.zhangyucheng.proxytesting;

public class Ticket {

    private int ticketNo;
    private int ticketPrice;

    public Ticket(int ticketNo, int ticketPrice) {
        this.ticketNo = ticketNo;
        this.ticketPrice = ticketPrice;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketNo(int ticketNo) {
        this.ticketNo = ticketNo;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

}

package com.zkyouxi.zhangyucheng.proxytesting;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import javax.security.auth.Subject;

public class MainActivity extends Activity {

    Button normalTicket;
    Button huangNiuTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OfficialTicketSeller officialSeller = new OfficialTicketSeller();
        HuangNiu huangNiu = new HuangNiu(officialSeller);


        normalTicket = findViewById(R.id.bt_normal_ticket);
        huangNiuTicket = findViewById(R.id.bt_huangniu_ticket);

        normalTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, officialSeller.sellTicket(),Toast.LENGTH_SHORT).show();
            }
        });


        huangNiuTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, huangNiu.sellTicket(),Toast.LENGTH_SHORT).show();

            }
        });



    }
}


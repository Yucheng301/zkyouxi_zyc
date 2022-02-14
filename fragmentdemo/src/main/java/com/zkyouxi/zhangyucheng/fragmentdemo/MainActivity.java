package com.zkyouxi.zhangyucheng.fragmentdemo;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    boolean flag = false;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textView = findViewById(R.id.tv_activity);
        Button button = findViewById(R.id.left_fragment_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    replaceFragment(new AnotherRightFragment());
                }else{
                    replaceFragment(new RightFragment());
                }
                flag = !flag;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction beginFragmentTransaction = fragmentManager.beginTransaction();
        beginFragmentTransaction.replace(R.id.frame_layout, fragment);
        beginFragmentTransaction.commit();
    }

    public void getMessage(String msg){
        textView.setText(msg);
    }
}
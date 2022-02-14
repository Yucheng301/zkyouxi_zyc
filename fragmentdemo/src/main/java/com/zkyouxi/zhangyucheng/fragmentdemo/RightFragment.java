package com.zkyouxi.zhangyucheng.fragmentdemo;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class RightFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.right_fragment, container, false);
    }

    public static RightFragment newInstance(String msg) {
        
        Bundle args = new Bundle();
        args.putString("fragment: ",msg);
        RightFragment fragment = new RightFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

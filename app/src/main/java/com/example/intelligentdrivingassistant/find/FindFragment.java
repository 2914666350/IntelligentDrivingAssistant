package com.example.intelligentdrivingassistant.find;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;


import com.example.intelligentdrivingassistant.R;

public class FindFragment extends Fragment {


    private ImageButton imageButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_find, container, false);
        //final TextView textView = root.findViewById(R.id.text_find);

        ImageButton imageButton=root.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//跳转其他APP的固定页面，需要APP的包名，activity的全路径
//在要跳转的APP的activity的配置添加 android:exported="true"，将activity暴露出来
                ComponentName name = new ComponentName("com.imooc.muju_md","com.imooc.muju_md.MainActivity");
                intent.setComponent(name);
                startActivity(intent);
            }
        });
        return root;
    }
}
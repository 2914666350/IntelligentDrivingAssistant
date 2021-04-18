package com.example.intelligentdrivingassistant.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.util.MyLogger;
import com.example.intelligentdrivingassistant.R;
import com.example.intelligentdrivingassistant.home.wakeup.ActivityWakeUp;
import com.example.intelligentdrivingassistant.home.wakeup.ActivityWakeUpRecog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DisplayFragment extends Fragment {

private ImageView imageViewHome;
    public DisplayFragment(){
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         View displayRoot = inflater.inflate(R.layout.fragment_show,container,false);
         imageViewHome= displayRoot.findViewById(R.id.imageViewHome);

        imageViewHome.setOnClickListener(view ->{
             Intent intent = new Intent();
             intent.setClass(getActivity(),ActivityWakeUpRecog.class);
             startActivity(intent);
         });


        return displayRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

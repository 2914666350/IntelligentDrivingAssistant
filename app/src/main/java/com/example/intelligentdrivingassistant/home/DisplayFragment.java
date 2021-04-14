package com.example.intelligentdrivingassistant.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.example.intelligentdrivingassistant.R;
import com.example.intelligentdrivingassistant.home.wakeup.ActivityWakeUp;
import com.example.intelligentdrivingassistant.home.wakeup.ActivityWakeUpRecog;

public class DisplayFragment extends Fragment {

    TextView ttCarPressureLR;
    TextView ttCarPressureLF;
    TextView ttCarPressureRF;
    TextView ttCarPressureRR;
//    TextView ttCarStartSate;
//    TextView ttCarWindow;
//    TextView ttCarLock;
//    TextView ttCarRemainPower;
    TextView ttCarPower;
//    TextView ttCarTravelNum;
    TextView ttCarKilometer;
    ImageView imgDoorLF;
    ImageView imgDoorLR;
    ImageView imgDoorRF;
    ImageView imgDoorRR;
    ImageView imgDoorReal;
    ImageView imgCarStartState;
    ImageView imgCarWindow;
    ImageView imgCarLock;

    public DisplayFragment(){

    }

    @SuppressLint("FragmentLiveDataObserve")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View displayRoot = inflater.inflate(R.layout.fragment_show,container,false);
        Intent i = new Intent();
        i.setClass(getActivity(), ActivityWakeUpRecog.class);
        startActivity(i);
        return displayRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

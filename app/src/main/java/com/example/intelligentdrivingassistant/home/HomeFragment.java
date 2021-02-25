package com.example.intelligentdrivingassistant.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.intelligentdrivingassistant.R;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
    private String TAG = "tag";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        ViewPager2 viewPager2StateShow = root.findViewById(R.id.viewPager2_Display);
        viewPager2StateShow.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Log.d(TAG, "createFragment: is called");
                switch (position){
                    case 0: return new DisplayFragment();
                    case 1: return new ControlFragment();
                    default: return null;
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        TabLayout tabLayout = root.findViewById(R.id.tabLayout_home);
        new TabLayoutMediator(tabLayout, viewPager2StateShow, new TabLayoutMediator.TabConfigurationStrategy(){
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Log.d(TAG, "onConfigureTab: is called");
                switch (position){
                    case 0: tab.setText("显示");break;
                    case 1: tab.setText("控制");break;
                }
            }
        }).attach();
        return root;
    }
}
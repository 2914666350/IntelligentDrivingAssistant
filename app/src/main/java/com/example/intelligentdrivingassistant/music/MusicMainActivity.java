package com.example.intelligentdrivingassistant.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.intelligentdrivingassistant.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.StarrySkyConfig;

public class MusicMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navView.getMenu()).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        Intent intent=getIntent();
        int id=intent.getIntExtra("id",-1);
        switch (id) {
            case 1: navController.navigate(R.id.navigation_music);
                break;
            case 2: navController.navigate(R.id.navigation_identity);
                break;
            default:break;
        }
        //NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        StarrySkyConfig config = new StarrySkyConfig
                .Builder()
                .addInterceptor(new RequestSongInfoInterceptor())
                .build();
        StarrySky.init(getApplication(), config, null);
    }
}

package com.example.intelligentdrivingassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.intelligentdrivingassistant.music.RequestSongInfoInterceptor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.StarrySkyConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        方法1
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard,
//                R.id.navigation_notifications, R.id.navigation_me)
//                .build();
//        方法2
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                navView.getMenu()).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /**音乐**/
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
package com.example.intelligentdrivingassistant.music;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.provider.SongInfo;

import java.util.ArrayList;
import java.util.List;

public class MusicCollect extends AppCompatActivity {
    ImageButton button;
    RecyclerView recyclerView;
    private List<SongInfo> playList;
    SQLiteDatabase db1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_collect);
        button = findViewById(R.id.imageButton12);
        recyclerView = findViewById(R.id.recyclerview3);
        playList = new ArrayList<>();
        com.neusoft.alpine.teamsix.MusicDBHelper db=new com.neusoft.alpine.teamsix.MusicDBHelper(this);
        db.open();
        db1= SQLiteDatabase.openOrCreateDatabase(com.neusoft.alpine.teamsix.MusicDBHelper.path,null);
        Cursor cursor=db1.query("user_info",null,null,null,null,null,null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String artist =cursor.getString(cursor.getColumnIndex("artist"));
            long duration = cursor.getLong(cursor.getColumnIndex("duration"));
            SongInfo info = new SongInfo();
            info.setSongId(id);
            info.setSongName(name);
            info.setArtist(artist);
            info.setDuration(duration);
            playList.add(info);
        }
        StarrySky.with().updatePlayList(playList);
        com.neusoft.alpine.teamsix.SongAdapter adapter = new com.neusoft.alpine.teamsix.SongAdapter(this, playList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new com.neusoft.alpine.teamsix.SongAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String id, String name, String artist, long time) {
                StarrySky.with().playMusicById(id);
                MusicFragment.ppuase=1;
                MusicFragment.pattern=0;
                Intent intent = new Intent();
                intent.setClass(com.neusoft.alpine.teamsix.MusicCollect.this, MainActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(com.neusoft.alpine.teamsix.MusicCollect.this, MainActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });
    }
}
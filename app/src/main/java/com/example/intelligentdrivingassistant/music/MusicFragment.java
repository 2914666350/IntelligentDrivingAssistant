package com.example.intelligentdrivingassistant.music;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.control.RepeatMode;
import com.lzx.starrysky.provider.SongInfo;
import com.example.intelligentdrivingassistant.R;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment {


    ImageButton button,button1,button2,button3,button4,button5,button6;
    TextView textView1,textView2;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private List<SongInfo> playList;
    SQLiteDatabase db1;
    public static int ppuase=0;
    public static int pattern=1;
    public static String ID=null;
    public static String NAME=null;
    public static String ARTIST=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        View root = inflater.inflate(R.layout.fragment_music,container,false);
        //final TextView textView = root.findViewById(R.id.text_music);
        /*musicViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });*/
        button=root.findViewById(R.id.imageButton);
        button1=root.findViewById(R.id.imageButton2);
        button2=root.findViewById(R.id.imageButton3);
        button3=root.findViewById(R.id.imageButton4);
        button4=root.findViewById(R.id.imageButton5);
        button5=root.findViewById(R.id.imageButton6);
        button6=root.findViewById(R.id.imageButton7);
        recyclerView = root.findViewById(R.id.recyclerview);
        textView1=root.findViewById(R.id.textView2);
        textView2=root.findViewById(R.id.textView3);
        progressBar=root.findViewById(R.id.progressBar2);
        playList = new ArrayList<>();

        DBHelper db=new DBHelper(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity());
        db.open();
        db1= SQLiteDatabase.openOrCreateDatabase(DBHelper.path,null);
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
        SongAdapter adapter = new SongAdapter(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity(), playList);
        adapter.initHandler(mHandler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String id, String name, String artist, long time) {
                StarrySky.with().playMusicById(id);
                ppuase=1;
                button1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_ONE,true);
                button4.setImageDrawable(getResources().getDrawable(R.drawable.a));
                pattern=1;

            }
        });

        if (ppuase == 0) {
            button1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
        }else if(ppuase==1){
            button1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
        }

        if(StarrySky.with().isPlaying()|StarrySky.with().isPaused()) {
            if (pattern == 1) {
                StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_ONE, true);
                button4.setImageDrawable(getResources().getDrawable(R.drawable.a));
            } else if (pattern == 2) {
                StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_NONE, true);
                button4.setImageDrawable(getResources().getDrawable(R.drawable.b));
            } else if (pattern == 3) {
                StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_NONE, false);
                button4.setImageDrawable(getResources().getDrawable(R.drawable.c));
            } else if (pattern == 0) {
                StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_ONE, false);
                button4.setImageDrawable(getResources().getDrawable(R.drawable.a));
                pattern=1;
            }
            Message msg = new Message();
            msg.what = 1;
            ID=StarrySky.with().getNowPlayingSongInfo().getSongId();
            NAME=StarrySky.with().getNowPlayingSongInfo().getSongName();
            ARTIST=StarrySky.with().getNowPlayingSongInfo().getArtist();
            mHandler.sendMessage(msg);

        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity(), MusicFind.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ppuase==1){
                    StarrySky.with().pauseMusic();
                    button1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                    ppuase=0;
                }else if(ppuase==0){
                    StarrySky.with().restoreMusic();
                    button1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                    ppuase=1;
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StarrySky.with().isPlaying()|StarrySky.with().isPaused()){
                    StarrySky.with().skipToPrevious();
                    button1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                    ppuase=1;

                    Message msg = new Message();
                    msg.what = 1;
                    ID=StarrySky.with().getNowPlayingSongInfo().getSongId();
                    NAME=StarrySky.with().getNowPlayingSongInfo().getSongName();
                    ARTIST=StarrySky.with().getNowPlayingSongInfo().getArtist();
                    mHandler.sendMessage(msg);
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StarrySky.with().isPlaying()|StarrySky.with().isPaused()){
                    StarrySky.with().skipToNext();
                    button1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                    ppuase=1;

                    Message msg = new Message();
                    msg.what = 1;
                    ID=StarrySky.with().getNowPlayingSongInfo().getSongId();
                    NAME=StarrySky.with().getNowPlayingSongInfo().getSongName();
                    ARTIST=StarrySky.with().getNowPlayingSongInfo().getArtist();
                    mHandler.sendMessage(msg);
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pattern==1){
                    StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_NONE,true);
                    pattern=2;
                    button4.setImageDrawable(getResources().getDrawable(R.drawable.b));
                }else if(pattern==2){
                    StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_NONE,false);
                    pattern=3;
                    button4.setImageDrawable(getResources().getDrawable(R.drawable.c));
                }else if(pattern==3){
                    StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_ONE,true);
                    pattern=1;
                    button4.setImageDrawable(getResources().getDrawable(R.drawable.a));
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=StarrySky.with().getNowPlayingSongInfo().getSongId();
                String name=StarrySky.with().getNowPlayingSongInfo().getSongName();
                String artist=StarrySky.with().getNowPlayingSongInfo().getArtist();
                long duration=StarrySky.with().getNowPlayingSongInfo().getDuration();
                boolean ret;
                MusicDBHelper db=new MusicDBHelper(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity());
                db.open();
                ret=db.intert(id,name,artist,duration);
                if(ret){
                    Toast.makeText(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity(),"收藏成功！", Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity(),"已经收藏过了！", Toast.LENGTH_SHORT).show();
                }
                button5.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity(), MusicCollect.class);
                startActivity(intent);
            }
        });
        return root;
    }
    public Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {

            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    MusicDBHelper db=new MusicDBHelper(com.neusoft.alpine.teamsix.ui.music.MusicFragment.this.getActivity());
                    db.open();
                    textView1.setText(NAME);
                    textView1.setSelected(true);
                    textView2.setText(ARTIST);
                    if(db.findid(ID)){
                        button5.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    }else {
                        button5.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    }
                    break;
                case 2:
                    break;
                default:
                    break;
            }

        }
    };

}

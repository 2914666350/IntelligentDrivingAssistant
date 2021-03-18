package com.example.intelligentdrivingassistant.music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.intelligentdrivingassistant.MainActivity;
import com.example.intelligentdrivingassistant.R;
import com.lzx.starrysky.provider.SongInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class MusicFind extends AppCompatActivity {
    ImageButton button;
    private List<SongInfo> playList;
    RecyclerView recyclerView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_find);
        editText=findViewById(R.id.editText);
        button = findViewById(R.id.imageButton10);
        playList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MusicFind.this, MainActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });
        initOKGO();
       /* StarrySkyConfig config = new StarrySkyConfig
                .Builder()
                .addInterceptor(new RequestSongInfoInterceptor())
                .build();
        StarrySky.init(getApplication(), config, null);*/

    }

    private void initOKGO() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效

        //https相关设置
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/okhttp-OkGo
        OkGo.getInstance().init(com.neusoft.alpine.teamsix.MusicFind.this.getApplication())                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(1);                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }
    public void onClick(View v){
        String key = editText.getText().toString();
        playList.clear();
        OkGo.<String>get("http://levistar.cn:64000/search?keywords=" + key ).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String jsonData = response.body();
                JSONObject json = JSON.parseObject(jsonData);
                JSONArray list = json.getJSONObject("result").getJSONArray("songs");
                int i;
                for (i = 0; i < list.size(); i++){
                    JSONObject j = list.getJSONObject(i);
                    String id = j.getString("id");
                    String name = j.getString("name");
                    String artist = j.getJSONArray("artists").getJSONObject(0).getString("name");
                    long duration = j.getLong("duration");
                    SongInfo info = new SongInfo();
                    info.setSongId(id);
                    info.setSongName(name);
                    info.setArtist(artist);
                    info.setDuration(duration);
                    playList.add(info);
                }
                onGetPlayList();
            }
        });
    }
    private void onGetPlayList(){
        //Log.d(TAG, "onGetPlayList: " + playList.size());
        //StarrySky.with().updatePlayList(playList);
        SongAdapter adapter = new SongAdapter(com.neusoft.alpine.teamsix.MusicFind.this, playList);
        //adapter.initHandler(mHandler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(com.neusoft.alpine.teamsix.MusicFind.this);
        // layoutManager.setOrientation(RecyclerView.VERTICAL);//线性布局
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String id, String name, String artist, long time) {
                boolean ret;
                DBHelper db=new DBHelper(com.neusoft.alpine.teamsix.MusicFind.this);
                db.open();
                ret=db.intert(id,name,artist,time);
                if(ret){
                Toast.makeText(com.neusoft.alpine.teamsix.MusicFind.this,"添加到播放列表成功！", Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(com.neusoft.alpine.teamsix.MusicFind.this,"播放列表中已经存在！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
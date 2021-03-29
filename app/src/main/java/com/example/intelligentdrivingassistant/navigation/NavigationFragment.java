package com.example.intelligentdrivingassistant.navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.baidu.location.LocationClient;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.example.intelligentdrivingassistant.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.*;
import com.example.intelligentdrivingassistant.navigation.carnavi.CarNaviActivity;
import com.example.intelligentdrivingassistant.navigation.carnavi.DemoExtGpsActivity;
import com.example.intelligentdrivingassistant.navigation.carnavi.DemoGuideActivity;
import com.example.intelligentdrivingassistant.navigation.carnavi.DemoMainActivity;
import com.example.intelligentdrivingassistant.navigation.carnavi.util.NormalUtils;
import com.example.intelligentdrivingassistant.navigation.liteapp.ForegroundService;

import static android.content.Context.SENSOR_SERVICE;

public class NavigationFragment extends Fragment implements SensorEventListener{
    Context context;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    public String city="";
    // UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private ImageView routeDialogBtn;
    private final static String TAG = NavigationFragment.class.getSimpleName();
    public String route="";
    private MapView  mMapView;
    private BaiduMap mBaiduMap;

    /*导航起终点Marker，可拖动改变起终点的坐标*/
    private Marker mStartMarker;
    private Marker mEndMarker;

    private LatLng startPt;
    private LatLng endPt;


    private static boolean isPermissionRequested = false;

    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";

    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int authBaseRequestCode = 1;

    private Button mNaviBtn = null;

    private String mSDCardPath = null;
    private BNRoutePlanNode mStartNode = null;
    private BNRoutePlanNode mEndNode = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_navigation, container, false);
        initRoutePlanNode();
        if (initDirs()) {
            initNavi();
        }
        requestLocButton = root.findViewById(R.id.buttonLocMode);
        mSensorManager = (SensorManager)getActivity().getApplicationContext().getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        requestLocButton.setText("点击切换定位模式：普通");
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("点击切换定位模式：跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.overlook(0);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                        break;
                    case COMPASS:
                        requestLocButton.setText("点击切换定位模式：普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder1 = new MapStatus.Builder();
                        builder1.overlook(0);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("点击切换定位模式：罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);
        requestPermission();
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R ||
//                Environment.isExternalStorageManager()) {
//            Toast.makeText(NavigationFragment.this.getActivity(), "已获得访问所有文件权限", Toast.LENGTH_SHORT).show();
//        } else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(NavigationFragment.this.getActivity())
//                    .setMessage("本程序需要您同意允许访问所有文件权限")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                                    startActivity(intent);
//                                }
//                            });
//
//            builder.show();
//        }

        mMapView =(MapView) root.findViewById(R.id.mapviewRoute);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        /*
        地图初始化
         */
        // 定位初始化
        mLocClient = new LocationClient(NavigationFragment.this.getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        Log.d(TAG, "onCreateView: "+city);
       /**路线起终点弹出框**/
       routeDialogBtn=root.findViewById(R.id.route_dialog_btn);
       routeDialogBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               route="true";
//               View dialogView = View.inflate(NavigationFragment.this.getActivity(), R.layout
//                       .dialog_node, null);
//               final EditText editStart = dialogView.findViewById(R.id.edit_start);
//               final EditText editEnd = dialogView.findViewById(R.id.edit_end);
//               new AlertDialog.Builder(NavigationFragment.this.getActivity())
//                       .setView(dialogView)
//                       .setPositiveButton("导航", new DialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(DialogInterface dialog, int which) {
//                               String startAddress = editStart.getText().toString().trim();
//                               String endAddress = editEnd.getText().toString().trim();
//                               /**起点与终点**/
//                               PlanNode stNode = PlanNode.withCityNameAndPlaceName(city,startAddress);
//
//                               PlanNode enNode = PlanNode.withCityNameAndPlaceName(city,endAddress);
//                               Double  startPoLa=stNode.getLocation().latitude;
//                               Double  startPoLo=stNode.getLocation().longitude;
//                               Double  endPoLa=enNode.getLocation().latitude;
//                               Double  endPoLo=enNode.getLocation().longitude;
//                               String startPoint=startPoLa.toString()+","+startPoLo.toString();
//                               String endPoint=endPoLa.toString()+","+endPoLo.toString();
//                               if (!checkValid(startPoint, endPoint)) {
//                                   Toast.makeText(NavigationFragment.this.getActivity(), "填写格式有误", Toast
//                                           .LENGTH_SHORT).show();
//                                   return;
//                               }
//                               String[] starts = startPoint.split(",");
//                               String[] ends = endPoint.split(",");
//                               BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
//                                       .latitude(Double.parseDouble(starts[1]))
//                                       .longitude(Double.parseDouble(starts[0]))
//                                       .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
//                                       .build();
//                               BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
//                                       .latitude(Double.parseDouble(ends[1]))
//                                       .longitude(Double.parseDouble(ends[0]))
//                                       .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
//                                       .build();
//
//                               routePlanToNavi(sNode, eNode);
//                           }
//                       })
//                       .show();
           }
       });


        startPt = new LatLng(34.638385,113.590845);
//        startPt= new LatLng(new BDLocation().getLatitude(),new BDLocation().getAltitude());
        endPt = new LatLng(34.63892, 113.592606);



        return root;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {
//        public String cityNa;
//        public MyLocationListenner(){
//
//        }
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            /**获取所在城市名**/
             city=location.getCity();
//             cityNa=city;
            Log.d(TAG, "onReceiveLocation: "+city);

//            String addr=location.getAddrStr();
//            city=addr.substring(addr.indexOf("省")+1, addr.indexOf("市")+1);

            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            if(route.equals("true")){
                Intent intent=new Intent();
                intent.setClass(NavigationFragment.this.getActivity(), CarNaviActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
                mLocClient.stop();
            }
        }

//        public void setCityNa(String cityNa) {
//            this.cityNa=cityNa;
//        }
//
//
//        public String getCityNa(){
//            return cityNa;
//        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(NavigationFragment.this.getActivity(),
                            "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        }
    }
    private void initRoutePlanNode() {
        mStartNode = new BNRoutePlanNode.Builder()
                .latitude(40.050969)
                .longitude(116.300821)
                .name("百度大厦")
                .description("百度大厦")
                .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                .build();
        mEndNode = new BNRoutePlanNode.Builder()
                .latitude(39.908749)
                .longitude(116.397491)
                .name("北京天安门")
                .description("北京天安门")
                .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                .build();
    }
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    private boolean hasBasePhoneAuth() {
        PackageManager pm = NavigationFragment.this.getActivity().getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, NavigationFragment.this.getActivity().getPackageName()) != PackageManager
                    .PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(NavigationFragment.this.getActivity().getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());
    }
    public void  initNavi(){

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
            return;
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(NavigationFragment.this.getActivity(),
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(NavigationFragment.this.getActivity(), result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(NavigationFragment.this.getActivity(),
                                "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(NavigationFragment.this.getActivity(),
                                "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed(int errCode) {
                        Toast.makeText(NavigationFragment.this.getActivity(),
                                "百度导航引擎初始化失败 " + errCode, Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    private void routePlanToNavi(BNRoutePlanNode sNode, BNRoutePlanNode eNode, final Bundle bundle) {
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                bundle,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(NavigationFragment.this.getActivity().getApplicationContext(),
                                        "算路开始", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(NavigationFragment.this.getActivity().getApplicationContext(),
                                        "算路成功", Toast.LENGTH_SHORT).show();
                                // 躲避限行消息
                                Bundle infoBundle = (Bundle) msg.obj;
                                if (infoBundle != null) {
                                    String info = infoBundle.getString(
                                            BNaviCommonParams.BNRouteInfoKey.TRAFFIC_LIMIT_INFO
                                    );
                                    Log.d("OnSdkDemo", "info = " + info);
                                }
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(NavigationFragment.this.getActivity().getApplicationContext(),
                                        "算路失败", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(NavigationFragment.this.getActivity().getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();

                                Intent intent = null;
                                if (bundle == null) {
                                    intent = new Intent(NavigationFragment.this.getActivity(),
                                            DemoExtGpsActivity.class);
                                } else {
                                    intent = new Intent(NavigationFragment.this.getActivity(),
                                            DemoGuideActivity.class);
                                }
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }


    /**
     * Android6.0之后需要动态申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;

            ArrayList<String> permissionsList = new ArrayList<>();

            String[] permissions = {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_MULTICAST_STATE


            };

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != NavigationFragment.this.getActivity().checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                    // 进入到这里代表没有权限.
                }
            }

            if (permissionsList.isEmpty()) {
                return;
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 0);
            }
        }
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NavigationFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static NavigationFragment newInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 开启前台服务防止应用进入后台gps挂掉
        NavigationFragment.this.getActivity()
                .startService(new Intent(NavigationFragment.this.getActivity(),
                        ForegroundService.class));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView = null;
        super.onDestroy();
        mMapView.onDestroy();
        NavigationFragment.this.getActivity().stopService(new Intent(NavigationFragment.this.getActivity(), ForegroundService.class));
    }

}
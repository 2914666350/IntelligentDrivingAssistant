package com.example.intelligentdrivingassistant.navigation.carnavi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.example.intelligentdrivingassistant.R;

import java.util.ArrayList;
import java.util.List;

public class CarNaviActivity extends AppCompatActivity {
    private Button routeButton;
    private EditText end_et;
    private EditText start_et;
    private RoutePlanSearch rpSearch;
    private BaiduMap baiduMap;
    private MapView mapView;
    private LocationClient mLocClient;
    private Marker myLocationMarker;
    private boolean isFirstLoc=true;
    private LocationManager mLocationManager;

    private double mCurrentLat;
    private double mCurrentLng;
//    private MyRouteLocationListener myListener=new MyRouteLocationListener();
    private static final String TAG = "CarNaviActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_route_recycler_view);

        initLocation();
        start_et=findViewById(R.id.start_et);
        end_et=findViewById(R.id.end_et);

//        mapView =(MapView)findViewById(R.id.mapviewRoute);
//        baiduMap = mapView.getMap();
//        // 开启定位图层
//        baiduMap.setMyLocationEnabled(true);
//        /*
//        地图初始化
//         */
//        // 定位初始化
//        mLocClient = new LocationClient(CarNaviActivity.this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();

        routeButton = findViewById(R.id.route_btn);
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String city = intent.getStringExtra("city");
                Log.d(TAG, "city: "+city);

                String startAddress = start_et.getText().toString().trim();
                String endAddress = end_et.getText().toString().trim();

                /*
                地址转换为坐标
                 */
                OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        //正向地理编码回调，注意geoCodeResult中错误码的处理
                        Double startPoLa=geoCodeResult.getLocation().latitude;//坐标点
                        Log.d(TAG, "DoubleStart: "+startPoLa);
                        Double startPoLo=geoCodeResult.getLocation().longitude;

                        String startPoint = startPoLo.toString()+"," +startPoLa.toString();
//                        String startPoint="116.30142,40.05087";
                        Log.d(TAG, "StringStart: "+ startPoint);

                        OnGetGeoCoderResultListener endListener = new OnGetGeoCoderResultListener() {
                            @Override
                            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                                Double endPoLa=geoCodeResult.getLocation().latitude;
                                Log.d(TAG, "DoubleEnd: "+endPoLa);
                                Double endPoLo=geoCodeResult.getLocation().longitude;

                                String endPoint = endPoLo.toString()+","+endPoLa.toString() ;
//                                String endPoint="116.39750,39.90882";
                                Log.d(TAG, "StringEnd: "+ endPoint);

                                Log.d(TAG, "StringEnd2: "+ endPoint);
                                Log.d(TAG, "StringStart2: "+ startPoint);

                                /**
                                 *导航
                                 */
                                if (!checkValid(startPoint, endPoint)) {
                                    Toast.makeText(CarNaviActivity.this, "填写格式有误", Toast
                                            .LENGTH_SHORT).show();
                                    return;
                                }
                                String[] starts = startPoint.split(",");
                                String[] ends = endPoint.split(",");
                                BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                                        .latitude(Double.parseDouble(starts[1]))
                                        .longitude(Double.parseDouble(starts[0]))
                                        .coordinateType(BNRoutePlanNode.CoordinateType.BD09LL)
                                        .build();
                                BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                                        .latitude(Double.parseDouble(ends[1]))
                                        .longitude(Double.parseDouble(ends[0]))
                                        .coordinateType(BNRoutePlanNode.CoordinateType.BD09LL)
                                        .build();

                                routePlanToNavi(sNode, eNode);

                            }

                            @Override
                            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                            }
                        };
                        GeoCoder mSearchEnd = GeoCoder.newInstance();//初始化地理编码
                        mSearchEnd.setOnGetGeoCodeResultListener(endListener);//设置回调监听

                        GeoCodeOption geoCodeOptionEnd = new GeoCodeOption();
                        geoCodeOptionEnd.address(endAddress);//设置地址
                        geoCodeOptionEnd.city(city);//设置城市

                        mSearchEnd.geocode(geoCodeOptionEnd);
                        geoCodeResult.getAddress();//地址信息
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        //反向地理编码回调
//                        reverseGeoCodeResult.getAddress();//简要地址信息
//                        reverseGeoCodeResult.getAddressDetail();//层次化地址信息
//                        reverseGeoCodeResult.getBusinessCircle();//所属商圈名称
//                        reverseGeoCodeResult.getPoiList();//周边POI信息集合
//                        reverseGeoCodeResult.getSematicDescription();//描述信息
//                        reverseGeoCodeResult.getLocation();//坐标点
                    }
                };


                GeoCoder mSearch = GeoCoder.newInstance();//初始化地理编码
                mSearch.setOnGetGeoCodeResultListener(listener);//设置回调监听
//                ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
//                reverseGeoCodeOption.location(new LatLng(36.897,127.9897));//设置坐标点
//                mSearch.reverseGeoCode(reverseGeoCodeOption);//将坐标点转换为地址信息
                GeoCodeOption geoCodeOption = new GeoCodeOption();
                geoCodeOption.address(startAddress);//设置地址
                geoCodeOption.city(city);//设置城市

                mSearch.geocode(geoCodeOption);//将地址信息转换为坐标点

                /**起点与终点**/
//                PlanNode stNode = PlanNode.withCityNameAndPlaceName(city, startAddress);
//                Log.d(TAG, "stNode.getCity(): "+stNode.getCity());
//                Log.d(TAG, "stNode.getName(): "+stNode.getName());
//                Log.d(TAG, "stNode.getLocation(): "+stNode.getLocation());
//                Log.d(TAG, "stNode.toString(): "+stNode.toString());
//                PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, endAddress);
//                Log.d(TAG, "enNode: "+enNode);
//                /**步行路线规划**/
//                boolean res= rpSearch.walkingSearch(
//                        new WalkingRoutePlanOption().from(stNode).to(enNode));



//                Double startPoLa = (Double)stNode.getLocation().latitude;
//                Log.d(TAG, "startPoLa: "+startPoLa);
//                Double startPoLo = (Double)stNode.getLocation().longitude;
//                Double endPoLa = (Double)enNode.getLocation().latitude;
//                Double endPoLo = (Double)enNode.getLocation().longitude;

            }
        });
    }
//    public class MyRouteLocationListener extends BDAbstractLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            if(location==null){
//                return;
//            }
//            LatLng cenpt = new LatLng(location.getLatitude(),location.getLongitude());
//            try {
//                //以定位的城市为默认
////                try {
////
////                    String addr=location.getAddrStr();
////                    city=addr.substring(addr.indexOf("省")+1,
////                            addr.indexOf("市")+1);
////                }catch (Exception e) {
////                }
//                /**定义marker坐标点**/
//                LatLng point = new LatLng(cenpt.latitude,cenpt.longitude);
//                //构建marker圖標
//                BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
//                //構建MarkerOption，用於在地圖上添加Marker
//                OverlayOptions option =new MarkerOptions().position(point).icon(bitmap);
//                //在地圖上添加marker，並显示
//                myLocationMarker=(Marker) baiduMap.addOverlay(option);
//                isFirstLoc=false;
//                Log.d("Log", "==>经度："+cenpt.longitude+"纬度："+cenpt.latitude);
////                updateLocationPosition(baiduMap,cenpt,18);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLat = location.getLatitude();
            mCurrentLng = location.getLongitude();
            Toast.makeText(CarNaviActivity.this, mCurrentLat
                    + "--" + mCurrentLng, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };
    private boolean checkValid(String startPoint, String endPoint) {
        if (TextUtils.isEmpty(startPoint) || TextUtils.isEmpty(endPoint)) {
            return false;
        }

        if (!startPoint.contains(",") || !endPoint.contains(",")) {
            return false;
        }
        return true;
    }

    private void routePlanToNavi(BNRoutePlanNode sNode, BNRoutePlanNode eNode) {
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(CarNaviActivity.this.getApplicationContext(),
                                        "算路开始", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(CarNaviActivity.this.getApplicationContext(),
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
                                Toast.makeText(CarNaviActivity.this.getApplicationContext(),
                                        "算路失败", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CarNaviActivity.this,
                                        DemoGuideActivity.class);

                                startActivity(intent);
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(CarNaviActivity.this.getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(CarNaviActivity.this,
                                        DemoGuideActivity.class);

                                startActivity(i);
                                break;
                            default:// nothing
                                break;
                        }
                    }
                });
    }
    private void initLocation() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 1000, mLocationListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    static{
        System.loadLibrary("BaiduMapSDK_base_v6_4_0");
        System.loadLibrary("app_BaiduMapBaselib");
        System.loadLibrary("locSDK8a");
        System.loadLibrary("aime");
        System.loadLibrary("app_BaiduMapApplib");
        System.loadLibrary("app_BaiduMapBaselib");
        System.loadLibrary("app_BaiduNaviApplib");
        System.loadLibrary("app_BaiduPanoramaAppLib");
        System.loadLibrary("BaiduMapSDK_bikenavi_v6_4_0");
        System.loadLibrary("BaiduMapSDK_map_for_bikenavi_v6_4_0");
        System.loadLibrary("bd_etts");
        System.loadLibrary("BDSpeechDecoder_V1");
        System.loadLibrary("crypto");
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("indoor");
        System.loadLibrary("mpcr");
        System.loadLibrary("ssl");
        System.loadLibrary("app_BaiduVIlib");
    }
}
package com.ys.tvnews.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ys.tvnews.R;
import com.ys.tvnews.bean.PushNews;

import java.util.List;

/**
 * Created by sks on 2015/12/8.
 */
public class TVFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button btn_push,btn_location;
    private PushNews pushNews;
    private LocationClient locationClient;
    private BDLocationListener myLoactionListener;
    private MapView mapView;
    private PoiSearch poiSearch;
    private BaiduMap baiduMap;
    private String busLineId;
   // private NotifyLister mNotifyer;
    BusLineSearch busLineSearch;


    private BitmapDescriptor bitmapDescriptor; //定位的图标
    private MyOrientationListener myOrentationListener;   //传感器监听
    private float mCurrentX;
    private float lat;
    private float lng;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_tv,container,false);
        findView(view);
        initBaiduMap();
        initLocation();

        setListener();
        pushNews = new PushNews();
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<PoiInfo> allPoi = poiResult.getAllPoi();
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });

        poiSearch.searchInCity(new PoiCitySearchOption().city("北京").keyword("新闻").pageNum(5));
        return view;
    }

    private void initLocation(){
        locationClient = new LocationClient(getActivity());
        myLoactionListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.e("info","接受到了定位的监听"+bdLocation.getLocType());
                lat = (float) bdLocation.getLatitude();
                lng = (float) bdLocation.getLongitude();

                MyLocationData data = new MyLocationData.Builder()//
                        .direction(mCurrentX)//
                        .accuracy(bdLocation.getRadius())//
                        .latitude(bdLocation.getLatitude())//
                        .longitude(bdLocation.getLongitude())//
                        .build();
                baiduMap.setMyLocationData(data);
                // 设置自定义图标
                MyLocationConfiguration config = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor);
                baiduMap.setMyLocationConfigeration(config);

                if(bdLocation==null){

                    return;
                }
                Log.e("location=====>>>",bdLocation.getAddrStr());
            }
        };
        //注册监听
        locationClient.registerLocationListener(myLoactionListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
        // 初始化图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.mipmap.navi_map_gps_locked);

        myOrentationListener = new MyOrientationListener(getActivity());

        myOrentationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener()
                {
                    @Override
                    public void onOrientationChanged(float x)
                    {
                        mCurrentX = x;
                    }
                });

    }

    private void initBaiduMap(){
        baiduMap = mapView.getMap();
        //普通地图类型
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        baiduMap.setTrafficEnabled(true);

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        //改变地图状态
        baiduMap.setMapStatus(msu);
    }

    /**
     * 初始化地图标注物
     * @param
     */
    private void settingOverLay(long lat,long lng){
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lng);
       //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.maker);
       //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    private void findView(View view){
        btn_location = (Button) view.findViewById(R.id.btn_poi);
        btn_push = (Button) view.findViewById(R.id.btn_push);
        mapView = (MapView) view.findViewById(R.id.mapView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        poiSearch.destroy();
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btn_poi:
               Log.e("info","点击了按钮");
             //  loadOption();
               baiduMap.setMyLocationEnabled(true);
               locationClient.start();
               myOrentationListener.start();
               break;
           default: break;
       }
    }



    private void loadOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000; //5秒发送一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setNeedDeviceDirect(true); //返回的定位结果包含手机机头方向

        locationClient.setLocOption(option);
        locationClient.start();  //启动位置请求
        locationClient.requestLocation(); //发送请求
    }

    private void setListener(){
        btn_location.setOnClickListener(this);
        btn_push.setOnClickListener(this);
    }
}

class MyOrientationListener implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mSensor;
    private float lastX;

    public MyOrientationListener(Context context)
    {
        this.mContext = context;
    }

    @SuppressWarnings("deprecation")
    public void start()
    {
        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null)
        {
            // 获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        if (mSensor != null)
        {
            mSensorManager.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop()
    {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }

    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            float x = event.values[SensorManager.DATA_X];

            if (Math.abs(x - lastX) > 1.0)
            {
                if (mOnOrientationListener != null)
                {
                    mOnOrientationListener.onOrientationChanged(x);
                }
            }

            lastX = x;

        }
    }

    private OnOrientationListener mOnOrientationListener;

    public void setOnOrientationListener(
            OnOrientationListener mOnOrientationListener)
    {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    public interface OnOrientationListener
    {
        void onOrientationChanged(float x);
    }

}

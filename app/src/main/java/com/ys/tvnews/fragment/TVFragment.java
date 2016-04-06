package com.ys.tvnews.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ys.tvnews.R;
import com.ys.tvnews.bean.PushNews;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TVFragment extends Fragment implements View.OnClickListener{

    private MapView mapView;
    private BaiduMap baiduMap;
    private MyLocationListener locationListener;
    private BitmapDescriptor mCurrentMarker;
    private LocationClient locationClient;
    private MyLocationConfiguration.LocationMode mLocationMode;
    private boolean isFirst = true;
    private Button btn_location;
    private EditText et_seach_news;
    private ImageView search_image;
    private float latitude,longitude;
    private PoiSearch mPoiSearch;
    private MyPoiSearchListener searchListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_location,container,false);
        initView(view);
        initBaiduMapView(view);
        initPoiSearch();
        initOptions();
        setListener();
        return view;
    }

    private void initPoiSearch(){
        mPoiSearch = PoiSearch.newInstance();
        searchListener = new MyPoiSearchListener();
        mPoiSearch.setOnGetPoiSearchResultListener(searchListener);
    }

    private void setListener(){
        btn_location.setOnClickListener(this);
        search_image.setOnClickListener(this);
    }

    private void initView(View view){
        btn_location = (Button) view.findViewById(R.id.start_location);
        et_seach_news = (EditText) view.findViewById(R.id.et_search_news);
        search_image = (ImageView) view.findViewById(R.id.image_search);
    }

    private void initOptions(){
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        locationClient = new LocationClient(getActivity());
        locationListener = new MyLocationListener();
        locationClient.registerLocationListener(locationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
        // 初始化图标
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.navi_map_gps_locked);

    }

    private void initBaiduMapView(View view){
        mapView = (MapView) view.findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        //实时交通
        baiduMap.setTrafficEnabled(true);
        //构造一个更新地图的msu对象，然后设置该对象为缩放等级15.0，最后设置地图状态。
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        baiduMap.setMapStatus(msu);
    }

    @Override
    public void onStart() {
        super.onStart();
        baiduMap.setMyLocationEnabled(true);
        if(!locationClient.isStarted()){
            //定位开启
            locationClient.start();
            locationClient.requestLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mPoiSearch.destroy();
    }


    /**
     * 初始化地图标注物
     * @param
     */
    private void settingOverLay(LatLng latLng){
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.maker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.e("===============",bdLocation.getAddrStr());
            //推送自己所在位置的信息
            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
            RemoteViews remoteView = new RemoteViews(getActivity().getPackageName(),R.layout.notification);
            remoteView.setTextViewText(R.id.notify_content,bdLocation.getAddrStr());
            Intent intent = new Intent(getActivity(), PushNews.class);
            PushNews pushNews = new PushNews();
            pushNews.setMessage(bdLocation.getAddrStr());
            intent.putExtra("pushNews",pushNews);
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0,intent,0);
            Notification notification = builder.build();
            manager.notify(100,notification);


            MyLocationData data = new MyLocationData.Builder()//
                    .direction(100)// // 此处设置开发者获取到的方向信息，顺时针0-360
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();
            baiduMap.setMyLocationData(data);
            // 设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mLocationMode, true, mCurrentMarker);
            baiduMap.setMyLocationConfigeration(config);

            //更新经纬度
            latitude = (float) bdLocation.getLatitude();
            longitude = (float) bdLocation.getLongitude();

            if(isFirst){
                LatLng latLng = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
                isFirst = false;
                Toast.makeText(getActivity(), bdLocation.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.start_location:
                currentToMyLocation();
                break;
            case R.id.image_search:
                String news_name = et_seach_news.getText().toString();
                if(news_name!=null) {
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city("北京")
                            .keyword(news_name)
                            .pageNum(10));
                }else{
                    Toast.makeText(getActivity(), "请输入您要查询的地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default: break;
        }
    }

    private void currentToMyLocation(){
        //  if(locationClient!=null && locationClient.isStarted()){
        //locationClient.requestLocation();
        LatLng latLng = new LatLng(latitude,longitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
        //   }
    }

    class MyPoiSearchListener implements OnGetPoiSearchResultListener {

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            List<PoiInfo> poiInfos = poiResult.getAllPoi();
            for(PoiInfo poiInfo: poiInfos){
                Log.e("--->",poiInfo.address);
                settingOverLay(poiInfo.location);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    }
}

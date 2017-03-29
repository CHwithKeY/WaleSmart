package com.waletech.walesmart.map;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.main.MainActivity;
import com.waletech.walesmart.publicClass.Methods;

/**
 * Created by KeY on 2016/7/11.
 */
public abstract class MapHelper {

    private BaiduMap map;

    private Context context;

    public MapHelper(Context context, MapView mapView) {
        this.context = context;

        setupMapView(mapView);
    }

    private void setupMapView(MapView mapView) {
        map = mapView.getMap();
        map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);

        setupLocation();
    }

    private void setupLocation() {
        LocationClientOption option = initLocOption();
        LocationClient client = new LocationClient(context.getApplicationContext());
        client.setLocOption(option);
        client.registerLocationListener(locationListener);
        client.requestLocation();
        client.start();
    }

    private LocationClientOption initLocOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        return option;
    }

    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {

            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();

            map.setMyLocationData(data);

            LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());

            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(lng, 19);
            map.animateMapStatus(update);

            if (!location.getProvince().equals("")) {

                String province = location.getProvince();
                String city = location.getCity();
                String district = location.getDistrict();

                if (district.equals("白云区")) {
                    HttpSet.setBaseIP(HttpSet.DEDICATED_IP, context);
                    ((MainActivity) context).setupNetText();
                }

                switch (Methods.getLanguage(context)) {
                    case "zh":
                        onLoadLocationDone(province, city, district);
                        break;

                    case "en":
                        String[] loc = LocTranslate(province, city, district);
                        onLoadLocationDone(loc[0], loc[1], loc[2]);
                        break;

                    default:
                        onLoadLocationDone(province, city, district);
                        break;
                }

//                if (new SharedAction(context).getAppLanguage().equals("Chinese")) {
//                    Log.i("Result", "map chinese");
//                    onLoadLocationDone(province, city, district);
//                } else {
//                    Log.i("Result", "map english");
//                    String[] loc = LocTranslate(province, city, district);
//
//                    for (int i = 0; i < loc.length; i++) {
//                        Log.i("Result", "loc is : " + loc[i]);
//                    }
//                    onLoadLocationDone(loc[0], loc[1], loc[2]);
//                }
            }
        }
    };

    private String[] LocTranslate(String province, String city, String district) {
        String[] loc = new String[3];

        switch (province) {
            case "广东省":
                loc[0] = "GuangDong";
                break;

            case "福建省":
                loc[0] = "FuJian";
                break;

            case "香港特别行政区":
                loc[0] = "HongKong";
                break;

            case "云南省":
                loc[0] = "YunNan";
                break;

            default:
                loc[0] = "";
                break;
        }

        switch (city) {
            case "广州市":
                loc[1] = "GuangZhou";
                break;

            case "深圳市":
                loc[1] = "ShenZhen";
                break;

            case "厦门市":
                loc[1] = "XiaMen";
                break;

            case "香港特别行政区":
                loc[1] = "HongKong";
                break;

            case "昆明市":
                loc[1] = "KunMing";
                break;

            default:
                loc[1] = "";
                break;
        }

        switch (district) {
            case "白云区":
                loc[2] = "BaiYun";
                break;
            case "越秀区":
                loc[2] = "YueXiu";
                break;
            case "南山区":
                loc[2] = "NanShan";
                break;
            case "福田区":
                loc[2] = "FuTian";
                break;
            case "思明区":
                loc[2] = "SiMing";
                break;
            case "荃湾区":
                loc[2] = "TSUEN WAN";
                break;
            case "呈贡区":
                loc[2] = "ChengGong";
                break;
            default:
                loc[2] = "";
                break;
        }

        return loc;
    }

    public abstract void onLoadLocationDone(String province, String city, String county);

}

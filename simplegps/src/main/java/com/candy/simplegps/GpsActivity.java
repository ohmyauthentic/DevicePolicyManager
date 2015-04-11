package com.candy.simplegps;
/**
 * 尝试百度定位SDK
 * @author harvic
 * @date 2013-12-28
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class GpsActivity extends Activity {
    private TextView mTv = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public Button ReLBSButton=null;
    public static String TAG = "msg";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv = (TextView)findViewById(R.id.textView);
        ReLBSButton=(Button)findViewById(R.id.button);

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener( myListener );

        setLocationOption();//设定定位参数

        mLocationClient.start();//开始定位

        // 重新定位
        ReLBSButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mLocationClient != null && mLocationClient.isStarted())
                    mLocationClient.requestLocation();
                else
                    Log.d("msg", "locClient is null or not started");
            }
        });

    }

    //设置相关参数
    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();//停止定位
        mTv = null;
        super.onDestroy();
    }


    /**
     * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        //接收位置信息
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nreturn code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                /**
                 * 格式化显示地址信息
                 */
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            sb.append("\nsdk version : ");
            sb.append(mLocationClient.getVersion());
            sb.append("\nisCellChangeFlag : ");
            sb.append(location.isCellChangeFlag());
            mTv.setText(sb.toString());
            Log.i(TAG, sb.toString());
        }
        //接收POI信息函数，我不需要POI，所以我没有做处理

    }


}
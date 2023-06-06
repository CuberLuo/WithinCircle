import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import cn.edu.zjut.withincircle.R
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapWidget() {
    val currContext= LocalContext.current
    val applicationContext= LocalContext.current.applicationContext
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val baiduMapApiKey = Properties().apply {
        load(applicationContext.assets.open("mapkey.properties"))
    }.getProperty("baidu.map.key")
    SDKInitializer.setApiKey(baiduMapApiKey)
    SDKInitializer.setAgreePrivacy(applicationContext,true)
    SDKInitializer.initialize(applicationContext)
    var myLat = rememberSaveable { mutableStateOf(0.0) }
    val myLon = rememberSaveable { mutableStateOf(0.0) }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                SDKInitializer.setCoordType(CoordType.BD09LL)
            }
        },
        update = { mapView ->
            mapView.showZoomControls(false)//禁止显示缩放按钮控件
            val mBaiduMap = mapView.map
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18f))//设置地图缩放级别
            mBaiduMap.isMyLocationEnabled = true//开启地图的定位图层
            LocationClient.setAgreePrivacy(true)
            val locationListener = object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation?) {
                    //mapView 销毁后不在处理新接收的位置
                    if (location == null || mapView == null) {
                        return
                    }
                    val locData = MyLocationData.Builder()
                        .accuracy(location.radius)
                        .latitude(location.latitude)
                        .longitude(location.longitude)
                        .build()
                    mBaiduMap.setMyLocationData(locData)
                    /*myLat.value=location.latitude
                    myLon.value=location.longitude*/
                    // 设置地图中心点为当前位置
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
                }
            }
            val mLocationClient = LocationClient(applicationContext)

            //通过LocationClientOption设置LocationClient相关参数
            val option = LocationClientOption()
            option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            option.openGps=true//GPS定位
            option.isOpenGnss=true//卫星定位
            //option.setIsNeedAddress(true)
            option.setCoorType("bd09ll") // 设置坐标类型
            option.setScanSpan(100)// 定位间隔设置为0表示只定位一次

            //设置locationClientOption
            mLocationClient.locOption = option
            //注册LocationListener监听器
            mLocationClient.registerLocationListener(locationListener)
            //开启地图定位图层
            mLocationClient.start()

            //配置图片大小
            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.pic)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false)
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
            val myLocationConfig = MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL,
                true,
                bitmapDescriptor
            )
            mBaiduMap.setMyLocationConfiguration(myLocationConfig)
        }
    )
}
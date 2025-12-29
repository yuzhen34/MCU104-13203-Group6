package com.example.lab14   // ← 如果你原本不是這個，改成你檔案原本第一行的 package

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var currentPolyline: Polyline? = null

    // 台北101 → 台北車站
    private val taipei101 = LatLng(25.033611, 121.565000)
    private val taipeiMainStation = LatLng(25.047924, 121.517081)

    private val reqLoc = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.btnDriving).setOnClickListener {
            drawRoute(TravelMode.DRIVING)
        }
        findViewById<Button>(R.id.btnWalking).setOnClickListener {
            drawRoute(TravelMode.WALKING)
        }
        findViewById<Button>(R.id.btnBicycling).setOnClickListener {
            drawRoute(TravelMode.BICYCLING)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val fineGranted = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                reqLoc
            )
        }

        // 加 Marker（這裡之前就是你紅掉的地方）
        map.addMarker(
            MarkerOptions()
                .position(taipei101)
                .title("台北101")
        )

        map.addMarker(
            MarkerOptions()
                .position(taipeiMainStation)
                .title("台北車站")
        )

        // 移動鏡頭
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(25.04, 121.54),
                13f
            )
        )

        // 預設畫走路路線（作業7）
        drawRoute(TravelMode.WALKING)
    }

    private fun drawRoute(mode: TravelMode) {
        val apiKey = packageManager
            .getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            .metaData
            .getString("com.google.android.geo.API_KEY")

        if (apiKey.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "找不到 Google Maps API Key",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        currentPolyline?.remove()
        currentPolyline = null

        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = DirectionsApi.newRequest(geoApiContext)
                    .origin(
                        com.google.maps.model.LatLng(
                            taipei101.latitude,
                            taipei101.longitude
                        )
                    )
                    .destination(
                        com.google.maps.model.LatLng(
                            taipeiMainStation.latitude,
                            taipeiMainStation.longitude
                        )
                    )
                    .mode(mode)
                    .await()

                withContext(Dispatchers.Main) {
                    if (result.routes.isEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "找不到路線",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@withContext
                    }

                    val route = result.routes[0]
                    val decodedPath =
                        PolyUtil.decode(route.overviewPolyline.encodedPath)

                    val polylineOptions = PolylineOptions()
                        .addAll(decodedPath)
                        .color(Color.RED)
                        .width(15f)

                    currentPolyline = map.addPolyline(polylineOptions)

                    val boundsBuilder = LatLngBounds.Builder()
                    decodedPath.forEach { p: LatLng ->
                        boundsBuilder.include(p)
                    }

                    map.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            boundsBuilder.build(),
                            100
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "路線取得失敗（請確認 Directions API / Billing / API Key）",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } finally {
                geoApiContext.shutdown()
            }
        }
    }
}

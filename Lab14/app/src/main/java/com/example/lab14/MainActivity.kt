package com.example.lab14

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            loadMap()
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadMap()
    }

    override fun onMapReady(map: GoogleMap) {
        if (hasLocationPermissions()) {
            setupMap(map)
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermissions(): Boolean {
        val fineLocationGranted = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineLocationGranted && coarseLocationGranted
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0
        )
    }

    private fun setupMap(map: GoogleMap) {
        map.apply {
            isMyLocationEnabled = true

            val locations = listOf(
                LatLng(25.033611, 121.565000) to "台北101",
                LatLng(25.047924, 121.517081) to "台北車站"
            )

            locations.forEach { (position, title) ->
                addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(title)
                        .draggable(true)
                )
            }

            val polylineOptions = PolylineOptions().apply {
                add(LatLng(25.033611, 121.565000))
                add(LatLng(25.032435, 121.534905))
                add(LatLng(25.047924, 121.517081))
                color(Color.BLUE)
                width(10f)
            }
            addPolyline(polylineOptions)

            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.035, 121.54), 13f))
        }
    }

    private fun loadMap() {
        (supportFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment)
            ?.getMapAsync(this)
    }
}

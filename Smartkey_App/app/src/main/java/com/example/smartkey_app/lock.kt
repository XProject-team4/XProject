package com.example.smartkey_app

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_lock.*
import org.altbeacon.beacon.*

class lock : AppCompatActivity(), BeaconConsumer{
    private var beaconManager: BeaconManager? = null
    private val region: Region = Region("TrackMedRegion", null, null, null)

    private val PERMISSION_REQUEST_COARSE_LOCATION = 1

    private val MIN_SECONDS_TO_RECORD = 2
    private val MAX_DISTANCE_FOR_DETECTION = 10
    private val MAX_DISTANCE_FOR_RECORD = 2

    private var detectionFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        if(!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("This app needs location access")
                builder.setMessage("Please grant location access so this app can detect beacons in the background")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION)
                }
                builder.show()
            }

            plus_btn.setOnClickListener {
                var intent = Intent(applicationContext, add_category::class.java)
                startActivity(intent)
            }

            lock_btn.setOnClickListener {
                // 1. 비콘 스캔
                // 2. 비콘 스캔한 id랑 서버에서 받은 비콘 id 비교
                // 2-1. 같으면 서버에 "True, 비콘 id" 전송
                // 2-2. 다르면 서버에 보내는거 없이 dialog msg만 띄우기
//          * 소상공인 (사업자)
                var intent = Intent(applicationContext, unlock::class.java)
                startActivity(intent)
//            * 게스트
//            var intent = Intent(applicationContext, qr_check::class.java)
//            startActivity(intent)

            }
        }

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager!!.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
        beaconManager!!.bind(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionally limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacon when in the background")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener {  }
                    builder.show()
                }
                return
            }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        super.onPause()
        detectionFlag = true
    }

    override fun onResume() {
        super.onResume()
        detectionFlag = false
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager!!.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        beaconManager!!.addRangeNotifier(object : RangeNotifier {
            override fun didRangeBeaconsInRegion(beacons: Collection<Beacon>, region: Region) {
                if (beacons.isNotEmpty()) {
                    for (i in beacons) {
                        Log.d("BEACON", "id : " + i.id2)
                    }
                }
            }
        })
        try {
            beaconManager!!.startRangingBeaconsInRegion(region)
        } catch (e : RemoteException) { }
    }


}
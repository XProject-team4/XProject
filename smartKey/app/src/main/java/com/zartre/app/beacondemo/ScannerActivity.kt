package com.zartre.app.beacondemo

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_lock.*
import kotlinx.android.synthetic.main.activity_scanner.*
import org.altbeacon.beacon.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import java.lang.reflect.Constructor
import kotlin.coroutines.coroutineContext

class ScannerActivity : AppCompatActivity(), BeaconConsumer {

    private val PERMISSION_REQUEST_COARSE_LOCATION = 1
    private val SCAN_PERIOD: Long = 150
    private val ENTER_AREA = 0.5
    private val LOG_TAG = "distance"
    private val EXTRA_BEACON = "beacon_uuid"
    private val ALLOWED = "allowed_area"

    private lateinit var beaconManager: BeaconManager
    private var beaconUuid: String = "74278BDA-B644-4520-8F0C-720EAF059935" // default uuid to scan
    private var id: String = "id"
    private val ID = "id"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_scanner)
        val uuid = intent.getStringExtra(EXTRA_BEACON)
        if (!uuid.isNullOrEmpty()) {
            beaconUuid = uuid // replace uuid with user input
        }

        val s_id = intent.getStringExtra(ID)
        id = s_id



//        text_uuid.text = beaconUuid


        // Android M Permission check
        requestPerm()

        initBeaconManager()



//        text_distance.text = "scanning"

//        test_btn.setOnClickListener {
//            var intent = Intent(applicationContext, unlock::class.java)
//            startActivity(intent)
//        }
//        var intent = Intent(applicationContext, unlock::class.java)
//        startActivity(intent)


    }

    private fun initBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers
            .add(
                BeaconParser()
                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24") // iBeacon
            )
        beaconManager.backgroundScanPeriod = SCAN_PERIOD
        beaconManager.bind(this)
    }

    private fun requestPerm() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("This app needs location access")
            builder.setMessage("Please grant location access so this app can detect beacons in the background.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_COARSE_LOCATION
                )
            }
            builder.show()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("CHECKING", "ONPAUSE??")
        beaconManager.unbind(this)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("CHECKING", "ONRESTART??")
        beaconManager.bind(this)
    }

    override fun onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(object : MonitorNotifier {
            override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
            }

            override fun didEnterRegion(region: Region?) {
                Log.i(LOG_TAG, "Enter area")
                beaconManager.startRangingBeaconsInRegion(region!!)
            }

            override fun didExitRegion(region: Region?) {
                Log.i(LOG_TAG, "Exit area")
            }
        })

        val rangeNotifier = RangeNotifier { beacons, region ->
            if (beacons.size > 0) {
                Log.d(LOG_TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size)

                val firstBeacon = beacons.iterator().next()
                Log.d("near beacon ID:", "="+firstBeacon.id1)

                if (firstBeacon.id1.toString() == beaconUuid) {
                    if (firstBeacon.distance < ENTER_AREA) {
                        //server에 request
                        var retrofit = Retrofit.Builder()
                            .baseUrl("http://220.67.124.145:8080")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                        var scanservice: forScannerService = retrofit.create(forScannerService::class.java)

                        val id = id
                        var uuid = beaconUuid
                        Log.d("request to server :", "id:"+id+" beaconUuid:"+beaconUuid)

                        scanservice.requestUuid(id, uuid).enqueue(object : Callback<forScanner> {
                            override fun onFailure(call: Call<forScanner>, t: Throwable) {
                                var dialog = AlertDialog.Builder(this@ScannerActivity)
                                dialog.setTitle("ERROR")
                                dialog.setMessage("서버와의 통신이 실패하였습니다.")
                                dialog.show()
                            }

                            override fun onResponse(call: Call<forScanner>, response: Response<forScanner>) {
                                if (response?.isSuccessful) {
                                    //response 받으면 화면전환(unlock)
                                    var forScan = response.body()
                                    Log.d("open", "msg : " + forScan?.msg)
                                    Log.d("open", "code : " + forScan?.code)
                                    if (forScan?.msg == "true") {
                                        Toast.makeText(this@ScannerActivity, "잠금이 해제됩니다.", Toast.LENGTH_SHORT).show()

                                        var intent = Intent(applicationContext, unlock::class.java)
                                        startActivity(intent)
                                    }
                                    else if (forScan?.msg == "false") {
                                        Toast.makeText(this@ScannerActivity, "서비스 시간이 만료되었습니다. \n" + "QR코드를 재인증 하세요.", Toast.LENGTH_SHORT).show()
                                    }
                                }
//                                else {
//                                    Toast.makeText(this@ScannerActivity, "출입 가능 구역을 확인하세요.", Toast.LENGTH_SHORT).show()
//                                }
                            }
                        })
                    }

                    Log.d(LOG_TAG, firstBeacon.distance.toString())
//                    text_distance.text = firstBeacon.distance.toString()
                }
                else {
                    Log.d("firstBeacon id ; " , firstBeacon.id1.toString())
                    Log.d("myUuid:" , beaconUuid)

                    //해당비콘 없다고 toast띄우기
                    Toast.makeText(this@ScannerActivity, "비콘을 확인하세요.", Toast.LENGTH_SHORT).show()
                    var intent = Intent(applicationContext, close::class.java)
                    startActivity(intent)
                }

            }
            else {
                Log.d("MSG", "!!!!!!!!NO BEACON!!!!!!")
//                onPause()
                Toast.makeText(this@ScannerActivity, "접근이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
                var intent = Intent(applicationContext, close::class.java)
                startActivity(intent)
            }
        }

        try {
            val monRegion = Region("myMonitoringUniqueId", Identifier.parse(beaconUuid), null, null)
            val rangRegion = Region("myRangingUniqueId", Identifier.parse(beaconUuid), null, null)
            beaconManager.startMonitoringBeaconsInRegion(monRegion)
            beaconManager.startRangingBeaconsInRegion(rangRegion)
            beaconManager.addRangeNotifier(rangeNotifier)
        }
        catch (e: RemoteException) { }
        catch (il: IllegalArgumentException) {
            Toast.makeText(applicationContext, "Invalid UUID", Toast.LENGTH_LONG).show()
            finish()
        }

    }
}

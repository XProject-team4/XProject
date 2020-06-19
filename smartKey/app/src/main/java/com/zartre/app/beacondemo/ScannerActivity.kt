package com.zartre.app.beacondemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scanner.*
import org.altbeacon.beacon.*
import java.lang.IllegalArgumentException

class ScannerActivity : AppCompatActivity(), BeaconConsumer {

    private val PERMISSION_REQUEST_COARSE_LOCATION = 1
    private val SCAN_PERIOD: Long = 150
    private val GREEN_THRESHOLD = 0.2
    private val LOG_TAG = "nathan"
    private val EXTRA_BEACON = "beacon_uuid"

    private lateinit var beaconManager: BeaconManager
    private var beaconUuid: String = "74278BDA-B644-4520-8F0C-720EAF059935" // default uuid to scan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        val temp = intent.getStringExtra(EXTRA_BEACON)
        if (!temp.isNullOrEmpty()) {
            beaconUuid = temp // replace uuid with user input
        }

        text_uuid.text = beaconUuid


        // Android M Permission check
        requestPerm()

        initBeaconManager()

        text_distance.text = "scanning"

        test_btn.setOnClickListener {
            var intent = Intent(applicationContext, lock::class.java)
            startActivity(intent)
        }
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
        beaconManager.unbind(this)
    }

    override fun onRestart() {
        super.onRestart()
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
                text_distance.text = "nothing here"
                scanner_container.setBackgroundColor(Color.WHITE)
            }
        })

        val rangeNotifier = RangeNotifier { beacons, region ->
            if (beacons.size > 0) {
                Log.d(LOG_TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size)

                val firstBeacon = beacons.iterator().next()
                Log.d("ID:", "="+firstBeacon.id1)

                if (firstBeacon.id1.toString() == beaconUuid) {
                    scanner_container.setBackgroundColor(Color.WHITE)

                    if (firstBeacon.distance < GREEN_THRESHOLD) {
                        scanner_container.setBackgroundColor(Color.parseColor("#76FF03"))
                    }

                    Log.d(LOG_TAG, firstBeacon.distance.toString())
                    text_distance.text = firstBeacon.distance.toString()
                }
                else {
                    Log.d("firstBeacon id ; " , firstBeacon.id1.toString())
                    Log.d("myUuid:" , beaconUuid)

                    scanner_container.setBackgroundColor(Color.RED)
                }

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

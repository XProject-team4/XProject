package com.example.smartkey_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qr_check.*
import java.lang.Exception


class qr_check : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_check)
        if(ContextCompat.checkSelfPermission(
                this@qr_check,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        }
        else {
            setupControls()
        }

        qr_check_btn.setOnClickListener {
            var intent = Intent(applicationContext, unlock::class.java)
            startActivity(intent)
        }

    }

    private fun setupControls() {
        detector = BarcodeDetector.Builder(this@qr_check).build()
        cameraSource = CameraSource.Builder(this@qr_check, detector)
            .setAutoFocusEnabled(true)
            .build()
        cameraSurfaceView.holder.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@qr_check,
            arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            }
            else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback {
        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder?) {
            cameraSource.stop()
        }

        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            try {
                cameraSource.start(surfaceHolder)
            } catch (exception: Exception) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {

        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if(detections != null && detections.detectedItems.isNotEmpty()) {
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                textScanResult.text = code.displayValue
            }
            else {
                textScanResult.text = ""
            }
        }
    }


}

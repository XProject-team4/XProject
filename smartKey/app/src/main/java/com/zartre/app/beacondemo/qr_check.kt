package com.zartre.app.beacondemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qr_check.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class qr_check : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector

    private val USERINFO = "UserID"
    private val ID = "id"
    private val ALLOWEDAREA = "Allowed area after QR"

//    var u_id = intent.getStringExtra(USERINFO).toString()

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
            var intent = Intent(applicationContext, lockActivity::class.java)
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

                textScanResult.text = code.displayValue //= 치킨마루/화장실:74278bda-b644-4520-8f0c-720eaf059935,주차장:1

                var retrofit = Retrofit.Builder()
                    .baseUrl("http://220.67.124.145:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                var qrservice: forQRService = retrofit.create(forQRService::class.java)

                var data = textScanResult.text.toString()
                var output = data.split("/")
                var store = output[0]
                var area = output[1]

                qrservice.requestQRScan(store, area).enqueue(object: Callback<forQR> {
                    override fun onFailure(call: Call<forQR>, t: Throwable) {
                        var dialog = AlertDialog.Builder(this@qr_check)
                        dialog.setTitle("ERROR")
                        dialog.setMessage("서버와의 통신이 실패하였습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<forQR>, response: Response<forQR>) {
                        if (response.isSuccessful) {
                            var forQR = response.body()
                            Log.d("QRCHECK", "msg(random guest id) : " + forQR?.msg)
                            Log.d("QRCHECK", "code : " + forQR?.code)
                            Toast.makeText(this@qr_check, "QR인증이 성공하였습니다.", Toast.LENGTH_SHORT).show()

                            var intent = Intent(applicationContext, lock2Activity::class.java).apply {
                                putExtra(ID, forQR?.msg)
                                putExtra(ALLOWEDAREA, code.displayValue)
                            }
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this@qr_check, "QR인증이 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            else {
                textScanResult.text = ""
            }
        }
    }


}

package com.kotlin.linterna

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.tbruyelle.rxpermissions2.RxPermissions


class MainActivity : AppCompatActivity() {

    var torch: Torch? = null
    var wakeLock: PowerManager.WakeLock? = null
    val rxPermissions = RxPermissions(this)
    var granted = false

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Permissions
        rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe { granted ->
                if (granted) {
                    torch = Torch()
                    Toast.makeText(this, R.string.instructions, Toast.LENGTH_LONG).show()
                    this.granted = true
                } else {
                    // Oups permission denied
                    this.granted = false
                    finish()
                }
            }


        // Acquire the Wake Lock
        wakeLock()

        // Button on/off
        buttonOnOff()

    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Turn off the flash
        torch?.let {
            it.release()
        }

        // Drop the  Wake Lock
        wakeLock?.let {
            it.release()
        }

    }

    @SuppressLint("InvalidWakeLockTag", "WakelockTimeout")
    private fun wakeLock(){
        var powerManager: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Linterna")
        wakeLock?.let {
            it.setReferenceCounted(false)

            if(!it.isHeld){
                it.acquire()
            }
        }
    }

    private fun buttonOnOff(){
        lLayout.setOnClickListener {
            torch?.let {
                if(it.isOnTorch()){
                    it.off()
                    imgView.setColorFilter(resources.getColor(R.color.white))
                    lLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                } else {
                    it.on()
                    imgView.setColorFilter(resources.getColor(R.color.yellow))
                    lLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                }
            }


        }
    }

}

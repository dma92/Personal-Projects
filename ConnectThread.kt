package com.example.robotcarapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.robotcarapp.MainActivity.Companion.bluetoothAdapaterpaired
import com.example.robotcarapp.MainActivity.Companion.myBluetoothSocket
import com.example.robotcarapp.MainActivity.Companion.myDevice
import com.example.robotcarapp.MainActivity.Companion.myUUID
import java.io.IOException

class ConnectThread(myContext : Context) : Thread() {

    private var context : Context
    private var finish : Boolean

    init {
        this.context = myContext
        finish = false
    }

    override fun run(){

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }

        while(finish == false){
            if(bluetoothAdapaterpaired == true){
                if(myBluetoothSocket == null) {
                    try {
                        myBluetoothSocket = myDevice!!.createInsecureRfcommSocketToServiceRecord(myUUID)
                        myBluetoothSocket!!.connect()
                        finish = true

                    } catch (e: IOException) {
                        Toast.makeText(context, "unable to connect please make sure bluetooth module is on", Toast.LENGTH_SHORT)
                            .show()
                        finish = true
                    }
                }
            }
        }
    }
}
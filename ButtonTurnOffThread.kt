package com.example.robotcarapp

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.robotcarapp.MainActivity.Companion.availableDevices
import com.example.robotcarapp.MainActivity.Companion.bluetoothAdapaterpaired
import com.example.robotcarapp.MainActivity.Companion.myBluetoothAdapter
import com.example.robotcarapp.MainActivity.Companion.myDevice
import java.io.IOException

class ButtonTurnOffThread(myContext : Context, myButton : Button, myButton2 : Button) : Thread() {

    @Volatile private var isRunning : Boolean = true
    private var context : Context
    private var button : Button
    private var deviceAddress: String
    private var button2 : Button
    private var finish : Boolean

    init {
        this.context = myContext
        this.button = myButton
        this.button2 = myButton2
        deviceAddress = ""
        finish = false
    }

    override fun run(){

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }

        while(!finish) {
            availableDevices = myBluetoothAdapter!!.bondedDevices

            for (device: BluetoothDevice in availableDevices) {

                deviceAddress = device.address

                if (deviceAddress == "00:14:03:05:0B:05") {
                    bluetoothAdapaterpaired = true
                    myDevice = device
                }
            }

            if (!bluetoothAdapaterpaired) {
                button.visibility = View.VISIBLE
            } else {
                button2.visibility = View.VISIBLE
                button.visibility = View.INVISIBLE
                finish = true
            }
        }
    }

}
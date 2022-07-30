package com.example.robotcarapp

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

//@Volatile var bluetoothAdapaterpaired: Boolean = false

class MainActivity : AppCompatActivity() {

    //Creates bluetooth adapter to request bluetooth use
    companion object{
        @Volatile var bluetoothAdapaterpaired: Boolean = false
        var availableDevices: MutableSet<BluetoothDevice> = mutableSetOf()
        var myBluetoothAdapter: BluetoothAdapter? = null
        var myDevice: BluetoothDevice? = null
        var myBluetoothSocket: BluetoothSocket? = null
        var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    //private var checkDevice: BluetoothDevice? = null
    //private var temp : BluetoothServerSocket? = null
    //variable sent to request bluetooth use
    private val REQUEST_ENABLE_BLUETOOTH = 1
    private val REQUEST_LOCATION = 3
    private var deviceConnected: Boolean = false
    private var EXTRA_ADDRESS = "device adress"
    private var DEVICE_NAME: String? = "device name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b = findViewById<Button>(R.id.button)
        val scanB = findViewById<Button>(R.id.scan)
        val textBox = findViewById<EditText>(R.id.editTextNumber)
       // val textView = findViewById<TextView>(R.id.connection)
        var beforeText: String
        var buttonThread : ButtonTurnOffThread = ButtonTurnOffThread(this, scanB, b)
        var connectionThread : ConnectThread = ConnectThread(this)
        //var statusThread : CheckCionnectionStatus = CheckCionnectionStatus(this, textView)


        //gets phone default adapter will return null if phone does not support bluetooth
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        checkPermissions()

        enableBluetooth()

        buttonThread.start()
        connectionThread.start()
        //statusThread.start()
        //if phone does not support bluetooth the app will toast user that their phone
        //does not support bluetooth

        //checkForDevice()


        b.setOnClickListener {

            beforeText = textBox.text.toString()
            textBox.setText("")
            Toast.makeText(applicationContext,"$beforeText",Toast.LENGTH_SHORT).show()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            }

            if(myDevice != null) {
                Toast.makeText(this, "${myDevice!!.bondState}", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "${myDevice!!.name}", Toast.LENGTH_SHORT)
                    .show()
            }

            if(myBluetoothSocket != null){
                Toast.makeText(this, "${myBluetoothSocket!!.isConnected}", Toast.LENGTH_SHORT) .show()
            }

            Toast.makeText(this, "ButtonTurnOffThread : ${buttonThread.isAlive}", Toast.LENGTH_SHORT) .show()
            Toast.makeText(this, "ConnectThread : ${connectionThread.isAlive}", Toast.LENGTH_SHORT) .show()
            //Toast.makeText(this, "StatusThread : ${statusThread.isAlive}", Toast.LENGTH_SHORT) .show()

        }

        scanB.setOnClickListener {
            discoverDevices()
        }

    }


    private fun checkPermissions(){

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            val permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            val permissionCourseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION)

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (!isGpsEnabled) {
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101)
            }
        }
    }

    private fun enableBluetooth(){

        if(myBluetoothAdapter == null) {
            Toast.makeText(this, "this device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        //If bluetooth is supported on phone but it is not currently enabled
        //the app will request permision to turn on bluetooth
        if(!myBluetoothAdapter!!.isEnabled) {

            //creating intent object requesting to turn on bluetooth
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //starting activity to turn on bluetooth on user phone
                //with the previously created intent and REQUEST_ENABLE_BLUETOOTH witch
                //is equal to 1. function will return RESULT_OK if user accepts request and
                //RESULT_CANCELED if result is denied
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            }

        }


    }

    private fun discoverDevices(){

        if(myBluetoothAdapter!!.isDiscovering){

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                myBluetoothAdapter!!.cancelDiscovery()
            }
        }

        startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
    }

    private fun sendData(myString : String){
        if (myBluetoothSocket != null) {
            try{
                myBluetoothSocket!!.outputStream.write(myString.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }
}


package com.example.robotcarapp

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.robotcarapp.MainActivity.Companion.myBluetoothSocket
import java.io.IOException

class CheckCionnectionStatus(myContext: Context, myTextView: TextView) : Thread() {

    private var context : Context
    private var textView : TextView

    init {
        this.context = myContext
        this.textView = myTextView
    }

    override fun run() {

        while(true){

           // sleep(10000)
            //Thread.sleep(1000)
            if(myBluetoothSocket == null){
               // textView.visibility = View.VISIBLE
            }
                //if(myBluetoothSocket!!.isConnected){
                   // textView.visibility =View.VISIBLE
               // }
                //else{
                   // textView.text = "Disconnected"
              //  }

        }
    }
}
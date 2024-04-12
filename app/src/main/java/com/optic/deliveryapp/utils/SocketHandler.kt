package com.optic.deliveryapp.utils

import android.util.Log
import com.github.nkzawa.socketio.client.Socket
import com.github.nkzawa.socketio.client.IO
import java.net.URISyntaxException

object SocketHandler {

    lateinit var msocket: Socket

    @Synchronized
    fun setSocket(){
        try{
            msocket = IO.socket("http://192.168.1.36:3000/orders/delivery")
        } catch (e: URISyntaxException){
            Log.d("Error","No se pudo conectar al socket ${e.message}")
        }
    }

    @Synchronized
    fun getSocket(): Socket{
        return msocket
    }

    @Synchronized
    fun establishConnection(){
        msocket.connect()
    }

    @Synchronized
    fun closeConnection(){
        msocket.disconnect()
    }
}
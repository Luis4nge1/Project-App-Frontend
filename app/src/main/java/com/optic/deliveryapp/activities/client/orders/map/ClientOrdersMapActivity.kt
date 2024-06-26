package com.optic.deliveryapp.activities.client.orders.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.maps.route.extensions.drawRouteOnMap
import com.optic.deliveryapp.R
import com.optic.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.optic.deliveryapp.models.Order
import com.optic.deliveryapp.models.ResponseHttp
import com.optic.deliveryapp.models.SocketEmit
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.providers.OrdersProvider
import com.optic.deliveryapp.utils.SharedPref
import com.optic.deliveryapp.utils.SocketHandler
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientOrdersMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var TAG = "DeliveryOrdersMap"
    var googleMap: GoogleMap? = null

    var PERMISSION_ID = 42
    var fusedLocationClient: FusedLocationProviderClient? = null

    var city = ""
    var country = ""
    var address = ""
    var addressLatLng: LatLng? = null

    var markerDelivery: Marker? = null
    var markerAddress: Marker? = null
    var myLocationLatLng: LatLng? = null
    var deliveryLatLng: LatLng? = null

    var order: Order? = null
    var gson = Gson()

    var textViewClient: TextView? = null
    var textViewAddress: TextView? = null
    var textViewNeighborhood: TextView? = null
    var circleImageUser: CircleImageView? = null
    var imageViewPhone: ImageView? = null

    val REQUEST_PHONE_CALL = 30

    var ordersProvider: OrdersProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null

    var socket: Socket? = null

    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation = locationResult.lastLocation
            myLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)

            //googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
            //CameraPosition.builder().target(
            //LatLng(myLocationLatLng?.latitude!!, myLocationLatLng?.longitude!!)
            //).zoom(15f).build()
            //))
            //removeDeliveryMarker()
            //addDeliveryMarker()
            Log.d("LOCALIZACION", "Callback: $lastLocation")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_orders_map)

        sharedPref = SharedPref(this)
        getUserFromSession()

        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)

        if(order?.lat!! !=null && order?.lng!! != null){
            deliveryLatLng = LatLng(order?.lat!!, order?.lng!!)
        }

        ordersProvider = OrdersProvider(user?.sessionToken!!)

        addressLatLng = LatLng(order?.address?.lat!!, order?.address?.lng!!)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        textViewClient = findViewById(R.id.textview_client)
        textViewAddress = findViewById(R.id.textview_address)
        textViewNeighborhood = findViewById(R.id.textview_neighborhood)
        circleImageUser = findViewById(R.id.circleimage_user)
        imageViewPhone = findViewById(R.id.imageview_phone)


        getLastLocation()
        connectSocket()

        textViewClient?.text = "${order?.delivery?.name} ${order?.delivery?.lastname}"
        textViewAddress?.text = order?.address?.address
        textViewNeighborhood?.text = order?.address?.neighborhood

        if (!order?.client?.image.isNullOrBlank()){
            Glide.with(this).load(order?.client?.image).into(circleImageUser!!)
        }


        imageViewPhone?.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            }
            else{
                call()
            }
        }

    }

    private fun connectSocket(){
        SocketHandler.setSocket()
        socket = SocketHandler.getSocket()
        socket?.connect()

        socket?.on("position/${order?.id}") {args ->
            if( args[0] != null){
                runOnUiThread {
                    val data = gson.fromJson(args[0].toString(), SocketEmit::class.java)
                    removeDeliveryMarker()
                    addDeliveryMarker(data.lat, data.lng)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(locationCallback != null && fusedLocationClient != null){
            fusedLocationClient?.removeLocationUpdates(locationCallback)
        }

        socket?.disconnect()
    }

    private fun call(){
        val i = Intent(Intent.ACTION_CALL)
        i.data = Uri.parse("tel: ${order?.delivery?.phone}")

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permiso denegado para llamada", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(i)
    }

    private fun drawRoute(){

        if(deliveryLatLng != null){
            val addressLocation = LatLng(order?.address?.lat!!,order?.address?.lng!!)
            googleMap?.drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = deliveryLatLng!!,
                destination = addressLocation,
                context = this,
                color = Color.RED,
                polygonWidth = 10,
                boundMarkers = false,
                markers = false
            )
        }
    }

    private fun removeDeliveryMarker(){
        markerDelivery?.remove()
    }

    private fun addDeliveryMarker(lat: Double, lng: Double){
        val location = LatLng(lat, lng)


            markerDelivery = googleMap?.addMarker(
                MarkerOptions().
                position(location).
                title("Posición del repartidor").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery))
            )
    }

    private fun addAddressMarker(){
        val addressLocation = LatLng(order?.address?.lat!!,order?.address?.lng!!)
        markerAddress = googleMap?.addMarker(
            MarkerOptions().
            position(addressLocation).
            title("Entregar aquí").
            icon(BitmapDescriptorFactory.fromResource(R.drawable.home))
        )
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true

    }

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnable()){

                fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->

                    var location = task.result

                    if(location != null){
                        myLocationLatLng = LatLng(location.latitude, location.longitude)

                        removeDeliveryMarker()
                        addDeliveryMarker(deliveryLatLng?.latitude!!, deliveryLatLng?.longitude!!)
                        addAddressMarker()
                        drawRoute()

                        if(deliveryLatLng != null){
                            googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
                                CameraPosition.builder().target(
                                    LatLng(deliveryLatLng?.latitude!!, deliveryLatLng?.longitude!!)
                                ).zoom(15f).build()
                            ))
                        }
                    }
                }
            }
            else{
                Toast.makeText(this, "Habilita la localización", Toast.LENGTH_LONG).show()
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }
        }
        else{
            requestPermission()
        }
    }

    private fun requestNewLocationData(){
        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
    private fun isLocationEnable(): Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun checkPermission(): Boolean{
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_ID){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }

        if(requestCode == REQUEST_PHONE_CALL){
            call()
        }
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

}
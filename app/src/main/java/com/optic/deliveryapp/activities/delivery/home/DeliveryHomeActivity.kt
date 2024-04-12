package com.optic.deliveryapp.activities.delivery.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.optic.deliveryapp.R
import com.optic.deliveryapp.activities.MainActivity
import com.optic.deliveryapp.fragments.client.ClientCategoriesFragment
import com.optic.deliveryapp.fragments.client.ClientOrdersFragment
import com.optic.deliveryapp.fragments.client.ClientProfileFragment
import com.optic.deliveryapp.fragments.delivery.DeliveryOrdersFragment
import com.optic.deliveryapp.fragments.restaurant.RestaurantCategoryFragment
import com.optic.deliveryapp.fragments.restaurant.RestaurantOrdersFragment
import com.optic.deliveryapp.fragments.restaurant.RestaurantProductFragment
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.utils.SharedPref

class DeliveryHomeActivity : AppCompatActivity() {

    private val TAG = "DeliveryHomeActivity"
    //var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_home)

        sharedPref = SharedPref(this)
        //buttonLogout = findViewById(R.id.btn_logout)
        //buttonLogout?.setOnClickListener {logout()}

        openFragment(DeliveryOrdersFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_orders -> {
                    openFragment(DeliveryOrdersFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }

                else -> false
            }
        }

        getUserFromSession()
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout(){
        sharedPref?.remove("user")
        var i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG, "Usuario: $user")
        }
    }
}
package com.optic.deliveryapp.activities.client.home

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
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.providers.UsersProvider
import com.optic.deliveryapp.utils.SharedPref

class ClientHomeActivity : AppCompatActivity() {

    private val TAG = "ClientHomeActivity"
    //var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null
    var usersProvider: UsersProvider? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

        sharedPref = SharedPref(this)
        //buttonLogout = findViewById(R.id.btn_logout)
        //buttonLogout?.setOnClickListener {logout()}

        openFragment(ClientCategoriesFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home -> {
                    openFragment(ClientCategoriesFragment())
                    true
                }
                R.id.item_orders -> {
                    openFragment(ClientOrdersFragment())
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
        usersProvider = UsersProvider(token = user?.sessionToken!!)
        createToken()
    }

    private fun createToken(){
        usersProvider?.createToken(user!!, this)
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}
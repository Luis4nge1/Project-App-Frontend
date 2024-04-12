package com.optic.deliveryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.optic.deliveryapp.R
import com.optic.deliveryapp.adapters.RolesAdapter
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.utils.SharedPref

class SelectRolesActivity : AppCompatActivity() {

    var recyclerViewRoles: RecyclerView? = null
    var user: User? = null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles = findViewById(R.id.recyclerview_roles)
        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapter(this, user?.roles!!)
        recyclerViewRoles?.adapter = adapter

    }

    private fun getUserFromSession(){

        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)

        }
    }
}
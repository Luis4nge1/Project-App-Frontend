package com.optic.deliveryapp.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.optic.deliveryapp.R
import com.optic.deliveryapp.activities.client.home.ClientHomeActivity
import com.optic.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.optic.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.optic.deliveryapp.models.Rol
import com.optic.deliveryapp.utils.SharedPref

class RolesAdapter (val context: Activity, val roles: ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>(){

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)

        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]

        holder.textViewRole.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageViewRol)

        holder.itemView.setOnClickListener{goToRol(rol)}
    }

    private fun goToRol(rol: Rol){
        when (rol.name) {
            "RESTAURANTE" -> {
                sharedPref.save("rol", "RESTAURANTE")

                val i = Intent(context, RestaurantHomeActivity::class.java)
                context.startActivity(i)
            }
            "CLIENTE" -> {
                sharedPref.save("rol", "CLIENTE")

                val i = Intent(context, ClientHomeActivity::class.java)
                context.startActivity(i)
            }
            "REPARTIDOR" -> {
                sharedPref.save("rol", "REPARTIDOR")

                val i = Intent(context, DeliveryHomeActivity::class.java)
                context.startActivity(i)
            }
        }
    }

    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewRole: TextView
        val imageViewRol: ImageView

        init{
            textViewRole = view.findViewById(R.id.textview_rol)
            imageViewRol = view.findViewById(R.id.imageview_rol)
        }
    }
}
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
import com.optic.deliveryapp.activities.client.products.detail.ClientProductsDetailActivity
import com.optic.deliveryapp.activities.client.shopping_bag.ClientShoppingBagActivity
import com.optic.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.optic.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.optic.deliveryapp.models.Category
import com.optic.deliveryapp.models.Product
import com.optic.deliveryapp.models.Rol
import com.optic.deliveryapp.utils.SharedPref

class OrderProductsAdapter (val context: Activity, val products: ArrayList<Product>): RecyclerView.Adapter<OrderProductsAdapter.OrderProductsViewHolder>(){

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_order_products, parent, false)

        return OrderProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: OrderProductsViewHolder, position: Int) {
        val product = products[position]

        holder.textViewName.text = product.name
        if(product.quantity != null){
            holder.textViewQuantity.text = "${product.quantity!!}"
        }
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

    }


    class OrderProductsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val imageViewProduct: ImageView
        val textViewName: TextView
        val textViewQuantity: TextView

        init{
            textViewName = view.findViewById(R.id.textview_name)
            textViewQuantity= view.findViewById(R.id.textview_quantity)
            imageViewProduct = view.findViewById(R.id.imageview_product)

        }
    }
}
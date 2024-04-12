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
import com.google.gson.Gson
import com.optic.deliveryapp.R
import com.optic.deliveryapp.activities.client.address.list.ClientAddressListActivity
import com.optic.deliveryapp.activities.client.home.ClientHomeActivity
import com.optic.deliveryapp.activities.client.orders.detail.ClientOrdersDetailActivity
import com.optic.deliveryapp.activities.client.products.list.ClientProductsListActivity
import com.optic.deliveryapp.activities.delivery.home.DeliveryHomeActivity
import com.optic.deliveryapp.activities.restaurant.home.RestaurantHomeActivity
import com.optic.deliveryapp.models.Address
import com.optic.deliveryapp.models.Category
import com.optic.deliveryapp.models.Order
import com.optic.deliveryapp.models.Rol
import com.optic.deliveryapp.utils.SharedPref

class OrdersClientAdapter (val context: Activity, val orders: ArrayList<Order>): RecyclerView.Adapter<OrdersClientAdapter.OrdersViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders, parent, false)

        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = orders[position]


        holder.textViewOrderId.text = "Order #${order.id}"
        holder.textViewDate.text = "${order.timestamp}"
        holder.textViewAddress.text = "${order.address?.address}"

        holder.itemView.setOnClickListener{ goToOrderDetail(order) }
    }

    private fun goToOrderDetail(order: Order){
        val i = Intent(context, ClientOrdersDetailActivity::class.java)
        i.putExtra("order", order.toJson())
        context.startActivity(i)
    }


    class OrdersViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewOrderId: TextView
        val textViewDate: TextView
        val textViewAddress: TextView

        init{
            textViewOrderId = view.findViewById(R.id.textview_order_id)
            textViewDate = view.findViewById(R.id.textview_date)
            textViewAddress = view.findViewById(R.id.textview_address)
        }
    }
}
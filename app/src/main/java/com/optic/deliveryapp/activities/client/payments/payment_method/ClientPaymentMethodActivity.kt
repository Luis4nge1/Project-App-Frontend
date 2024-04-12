package com.optic.deliveryapp.activities.client.payments.payment_method

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.optic.deliveryapp.R
import com.optic.deliveryapp.activities.client.payments.mercado_pago.forms.ClientPaymentFormActivity
import com.optic.deliveryapp.activities.client.payments.paypal.form.ClientPaymentPaypalFormActivity

class ClientPaymentMethodActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var imageViewMercadoPago: ImageView? = null
    var imageViewPaypal: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_method)

        toolbar = findViewById(R.id.toolbar)
        imageViewPaypal = findViewById(R.id.imageview_paypal)
        imageViewMercadoPago = findViewById(R.id.imageview_mercadopago)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title = "Métodos de Pago"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageViewMercadoPago?.setOnClickListener { goToMercadoPago() }
        imageViewPaypal?.setOnClickListener { goToPaypal() }

    }

    private fun goToMercadoPago(){
        val i = Intent(this, ClientPaymentFormActivity::class.java)
        startActivity(i)
    }

    private fun goToPaypal(){
        val i = Intent(this, ClientPaymentPaypalFormActivity::class.java)
        startActivity(i)
    }
}
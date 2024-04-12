package com.optic.deliveryapp.activities.client.payments.mercado_pago.status

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.optic.deliveryapp.R
import com.optic.deliveryapp.activities.client.home.ClientHomeActivity
import de.hdodenhof.circleimageview.CircleImageView

class ClientPaymentsStatusActivity : AppCompatActivity() {

    var textViewStatus: TextView? = null
    var circleImageStatus: CircleImageView? = null
    var buttonFinish: Button? = null

    var paymentMethodId = ""
    var paymentStatus = ""
    var lastFourDigits = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payments_status)

        textViewStatus = findViewById(R.id.textview_status)
        circleImageStatus = findViewById(R.id.circleimage_status)
        buttonFinish = findViewById(R.id.btn_finish)

        paymentMethodId = intent.getStringExtra("paymentMethodId").toString()
        paymentStatus = intent.getStringExtra("paymentStatus").toString()
        lastFourDigits = intent.getStringExtra("lastFourDigits").toString()

        if(paymentStatus == "approved"){
            circleImageStatus?.setImageResource(R.drawable.ic_check)
            textViewStatus?.text = "Tu orden fue procesada existosamente usando ( $paymentMethodId **** $lastFourDigits ) \n\nMira el estado de tus pedidos en la sección Mis Pedidos"
        }
        else{
            circleImageStatus?.setImageResource(R.drawable.ic_cancel)
            textViewStatus?.text = "Hubo un error procesando tu pago."
        }

        buttonFinish?.setOnClickListener { goToHome() }
    }

    private fun goToHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}
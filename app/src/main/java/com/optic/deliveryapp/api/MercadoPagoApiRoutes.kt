package com.optic.deliveryapp.api

import com.optic.deliveryapp.routes.MercadoPagoRoutes

class MercadoPagoApiRoutes {
    val API_URL = "https://api.mercadopago.com/"
    val retrofit = RetrofitClient()

    fun getMercadoPagoRoutes(): MercadoPagoRoutes{
        return retrofit.getClient(API_URL).create(MercadoPagoRoutes::class.java)
    }

}
package com.optic.deliveryapp.routes


import com.optic.deliveryapp.models.MercadoPagoPayment
import com.optic.deliveryapp.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.*

interface PaymentsRoutes {

    @POST("payments/create")
    fun createPayment(
        @Body mercadoPagoPayment: MercadoPagoPayment,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>


}
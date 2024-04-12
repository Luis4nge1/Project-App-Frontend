package com.optic.deliveryapp.routes

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.optic.deliveryapp.models.MercadoPagoCardTokenBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MercadoPagoRoutes {

    @GET("v1/payment_methods/installments?access_token=TEST-8724115554838541-020712-f25bd39c2b5763d862fa281092ee40a8-1673487716")
    fun getInstallments(@Query("bin") bin: String, @Query("amount") amount: String): Call<JsonArray>

    @POST("v1/card_tokens?public_key=TEST-3096c9f5-02dd-45ac-9f2a-25825f595777")
    fun createCardToken(@Body body: MercadoPagoCardTokenBody): Call<JsonObject>
}
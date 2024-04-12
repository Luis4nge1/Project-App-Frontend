package com.optic.deliveryapp.providers

import com.optic.deliveryapp.api.ApiRoutes
import com.optic.deliveryapp.models.Category
import com.optic.deliveryapp.models.MercadoPagoPayment
import com.optic.deliveryapp.models.ResponseHttp
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.routes.CategoriesRoutes
import com.optic.deliveryapp.routes.PaymentsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class PaymentsProvider (val token: String) {

    private var paymentsRoutes: PaymentsRoutes? = null

    init{
        val api = ApiRoutes()
        paymentsRoutes = api.getPaymentsRoutes(token)
    }

    fun create(mercadoPagoPayment: MercadoPagoPayment): Call<ResponseHttp>?{
        return paymentsRoutes?.createPayment(mercadoPagoPayment, token)
    }
}
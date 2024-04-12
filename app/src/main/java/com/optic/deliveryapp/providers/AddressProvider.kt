package com.optic.deliveryapp.providers

import com.optic.deliveryapp.api.ApiRoutes
import com.optic.deliveryapp.models.Address
import com.optic.deliveryapp.models.Category
import com.optic.deliveryapp.models.ResponseHttp
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.routes.AddressRoutes
import com.optic.deliveryapp.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class AddressProvider (val token: String) {

    private var addressRoutes: AddressRoutes? = null

    init{
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
    }

    fun getAddress(idUser: String): Call<ArrayList<Address>>?{
        return addressRoutes?.getAddress(idUser, token)
    }

    fun create(address: Address): Call<ResponseHttp>?{
        return addressRoutes?.create(address, token)
    }
}
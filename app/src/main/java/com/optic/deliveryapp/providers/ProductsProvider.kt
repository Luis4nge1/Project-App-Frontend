package com.optic.deliveryapp.providers

import com.optic.deliveryapp.api.ApiRoutes
import com.optic.deliveryapp.models.Category
import com.optic.deliveryapp.models.Product
import com.optic.deliveryapp.models.ResponseHttp
import com.optic.deliveryapp.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider (val token: String) {

    private var productsRoutes: ProductsRoutes? = null

    init{
        val api = ApiRoutes()
        productsRoutes = api.getProductsRoutes(token)
    }

    fun findByCategory(idCategory: String): Call<ArrayList<Product>>?{
        return productsRoutes?.findByCategory(idCategory, token)
    }

    fun create(files: List<File>, product: Product): Call<ResponseHttp>?{

        val images =  arrayOfNulls<MultipartBody.Part>(files.size)
        for (i in 0 until files.size){
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            images[i] = MultipartBody.Part.createFormData("image",files[i].name, reqFile)
        }


        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJason())

        return productsRoutes?.create(images, requestBody, token!!)
    }
}
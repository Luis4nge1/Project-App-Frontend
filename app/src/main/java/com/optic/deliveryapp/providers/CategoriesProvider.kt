package com.optic.deliveryapp.providers

import com.optic.deliveryapp.api.ApiRoutes
import com.optic.deliveryapp.models.Category
import com.optic.deliveryapp.models.ResponseHttp
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriesProvider (val token: String) {

    private var categoriesRoutes: CategoriesRoutes? = null

    init{
        val api = ApiRoutes()
        categoriesRoutes = api.getCategoriesRoutes(token)
    }

    fun getAll(): Call<ArrayList<Category>>?{
        return categoriesRoutes?.getAll(token)
    }

    fun create(file: File, category: Category): Call<ResponseHttp>?{
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image",file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), category.toJason())

        return categoriesRoutes?.create(image, requestBody, token!!)
    }
}
package com.optic.deliveryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.optic.deliveryapp.R
import com.optic.deliveryapp.activities.client.home.ClientHomeActivity
import com.optic.deliveryapp.models.ResponseHttp
import com.optic.deliveryapp.models.User
import com.optic.deliveryapp.providers.UsersProvider
import com.optic.deliveryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    var imageViewGoToLogin: ImageView? = null
    var editTextName: EditText? = null
    var editTextLastName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextPhone: EditText? = null
    var editTextPassword: EditText? = null
    var editTextConfirmPassword: EditText? = null
    var buttonRegister: Button? = null

    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageViewGoToLogin = findViewById(R.id.imageview_go_to_login)
        editTextName = findViewById(R.id.edittext_name)
        editTextLastName = findViewById(R.id.edittext_lastname)
        editTextEmail = findViewById(R.id.edittext_email)
        editTextPhone = findViewById(R.id.edittext_phone)
        editTextPassword = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister = findViewById(R.id.btn_register)

        imageViewGoToLogin?.setOnClickListener{goToLogin()}
        buttonRegister?.setOnClickListener { register() }
    }

    private fun register(){
        val name = editTextName?.text.toString()
        val lastname = editTextLastName?.text.toString()
        val email = editTextEmail?.text.toString()
        val phone = editTextPhone?.text.toString()
        val password = editTextPassword?.text.toString()
        val confirmPassword = editTextConfirmPassword?.text.toString()

        if(isValidForm(phone = phone, lastname = lastname, email = email, password = password, confirmPassword = confirmPassword, name = name)){

            val user = User(
                name = name,
                lastname = lastname,
                email = email,
                phone = phone,
                password = password
            )
            usersProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    if(response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }
                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response: ${response}")
                    Log.d(TAG, "Response: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }

    }

    private fun goToClientHome(){
        val i = Intent(this, SaveImageActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }

    fun String.isEmailValid(): Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(
        name: String,
        lastname: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String):
            Boolean{
        if(name.isNullOrBlank()){
            Toast.makeText(this, "Debes ingresar un nombre", Toast.LENGTH_SHORT).show()
            return false
        }
        if(lastname.isNullOrBlank()){
            Toast.makeText(this, "Debes ingresar un apellido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(email.isNullOrBlank()){
            Toast.makeText(this, "Debes ingresar un correo", Toast.LENGTH_SHORT).show()
            return false
        }
        if(phone.isNullOrBlank()){
            Toast.makeText(this, "Debes ingresar un teléfono", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.isNullOrBlank()){
            Toast.makeText(this, "Debes ingresar una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if(confirmPassword.isNullOrBlank()){
            Toast.makeText(this, "Debes confirmar la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!email.isEmailValid()){
            Toast.makeText(this, "Debes ingresar un correo válido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}
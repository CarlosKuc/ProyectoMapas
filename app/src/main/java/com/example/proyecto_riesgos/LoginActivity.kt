package com.example.proyecto_riesgos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnlogin.setOnClickListener {
            if(lguser.text.trim().isNotEmpty() && lgpass.text.trim().isNotEmpty()){
                val rque = Volley.newRequestQueue(this)
                val url = "http://192.168.0.6/P_riesgos/login.php"//url del server
                val userinput:String = lguser.text.toString()
                val passinput:String = lgpass.text.toString()
                val jsonObject = JSONObject()
                jsonObject.put("usuario",userinput)
                jsonObject.put("password",passinput)
                val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                    Response.Listener { response ->
                        val json = JSONObject(response.toString())
                        val success:String = json.getString("success")
                        if (success.equals("true")){
                            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Ventana::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Usuario o contraseña incorrrectos", Toast.LENGTH_SHORT).show()
                            println(jsonObject.toString())
                        }
                    }, Response.ErrorListener{
                            error -> error.printStackTrace()
                    })
                rque.add(request)
            }else{
                Toast.makeText(this, "Introduzca usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
        logcreate.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }
}

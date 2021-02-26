package com.example.proyecto_riesgos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_signin.*
import org.json.JSONObject

class SigninActivity : AppCompatActivity() {

    private var b:Boolean=false //variable auxiliar para la funcion chekuser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        btnsignin.setOnClickListener {
            if(sguser.text.trim().isNotEmpty() && sgpass.text.trim().isNotEmpty()){
                val usuario:String = sguser.text.toString()
                val password:String = sgpass.text.toString()
                checkuser(usuario)
                if (getB()){ //se evalua la varible auxiliar
                    Toast.makeText(this, "Usuario ya existente", Toast.LENGTH_SHORT).show()
                    setB(false) //el auxiliar regresa a false para volver a evaluar
                }else{
                    resgistro(usuario,password)
                }
            }else{
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resgistro(usuario:String, password:String){
        val rque = Volley.newRequestQueue(this)
        val url = "http://192.168.0.6/P_riesgos/register.php"//url del server
        val jsonObject = JSONObject()
        jsonObject.put("usuario",usuario)
        jsonObject.put("password",password)
        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            Response.Listener { response ->
                val json = JSONObject(response.toString())
                val successs:String = json.getString("success")
                if (successs.equals("true")){
                    Toast.makeText(this, "Resgistro Exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Resgistro Fallido", Toast.LENGTH_SHORT).show()
                    println("Algo salio mal con el Insert Query")
                }
            }, Response.ErrorListener{
                    error -> error.printStackTrace()
                Toast.makeText(this, "Error fatal", Toast.LENGTH_SHORT).show()
            })
        rque.add(request)
    }


    //funcion que lanza un query a la DB con el input proporcionado en sguser (nombre del usuario)
    //verifica si el user name proporcionado ya esta registrado
    private fun checkuser(user:String){
        val rque = Volley.newRequestQueue(this)
        val url = "http://192.168.0.6/P_riesgos/checkUser.php" //url del server
        val userinput:String = user
        val jsonObject = JSONObject()
        jsonObject.put("usuario",userinput)
        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            Response.Listener { response ->
                val json = JSONObject(response.toString())
                val successs:String = json.getString("success")
                if (successs.equals("true")){
                    setB(true) //la query fue exitosa, usuario existente, variable aux = true
                }
            }, Response.ErrorListener{
                    error -> error.printStackTrace()

            })
        rque.add(request)

    }

    //funciones para manejar la variable auxiliar de la funcion checkuser

    private fun setB(x:Boolean){
        this.b = x
    }

    private fun getB():Boolean{
        return this.b
    }
}

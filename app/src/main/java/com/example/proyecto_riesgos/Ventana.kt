package com.example.proyecto_riesgos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ventana.*

class Ventana : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana)

        btnsvdmap.setOnClickListener {
            val intent = Intent(this, MainService::class.java)
            intent.putExtra("key",1)
            startActivity(intent)
        }

        add_btn.setOnClickListener {
            val intent = Intent(this, MainService::class.java)
            intent.putExtra("key",2)
            startActivity(intent)
        }
    }
}
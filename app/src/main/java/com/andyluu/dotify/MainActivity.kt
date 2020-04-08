package com.andyluu.dotify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnChangeUser.setOnClickListener {

        }
    }

    fun changeUsername(view: View) {
        val username = findViewById<TextView>(R.id.username)
        username.text = "fdsfds"
    }
}

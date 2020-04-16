package com.andyluu.dotify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var randomNumber = Random.nextInt(1000, 100000)
        var numPlays = findViewById<TextView>(R.id.numPlays)
        numPlays.text = "${randomNumber.toString()} plays"

        btnPlay.setOnClickListener {
            randomNumber += 1
            numPlays.text = "${randomNumber.toString()} plays"
        }

        btnChangeUser.setOnClickListener {
            val username = findViewById<TextView>(R.id.username)
            val editUsername = findViewById<EditText>(R.id.editUsername)
            if (btnChangeUser.text == "Change User") {
                btnChangeUser.text = "Apply"
                username.visibility = View.INVISIBLE
                editUsername.visibility = View.VISIBLE
            } else if (btnChangeUser.text == "Apply" && editUsername.text.isNotEmpty()) {
                btnChangeUser.text = "Change User"
                username.text = editUsername.text
                username.visibility = View.VISIBLE
                editUsername.visibility = View.INVISIBLE
            }
        }
    }

    fun nextClicked(view: View) {
        Toast.makeText(this, "Skipping to next track", Toast.LENGTH_SHORT).show()
    }

    fun prevClicked(view: View) {
        Toast.makeText(this, "Skipping to previous track", Toast.LENGTH_SHORT).show()
    }
}

package com.andyluu.dotify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andyluu.dotify.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        Picasso.get().load("https://picsum.photos/seed/voldemort/256").into(userCover)
    }
}

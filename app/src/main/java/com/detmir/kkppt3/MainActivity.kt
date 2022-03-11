package com.detmir.kkppt3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val case0000Button = findViewById<Button>(R.id.activity_main_0000)
        val case0100Button = findViewById<Button>(R.id.activity_main_0100)
        val case0101Button = findViewById<Button>(R.id.activity_main_0101)
        val case0200Button = findViewById<Button>(R.id.activity_main_0200)
        val case0300Button = findViewById<Button>(R.id.activity_main_0300)
        val case0301Button = findViewById<Button>(R.id.activity_main_0301)
        val case0400Button = findViewById<Button>(R.id.activity_main_0400)
        val case0500Button = findViewById<Button>(R.id.activity_main_0500)
        val case0600Button = findViewById<Button>(R.id.activity_main_0600)
        val case0700Button = findViewById<Button>(R.id.activity_main_0700)

        case0000Button.setOnClickListener {
            startActivity(Intent(this, Case0000DemoActivity::class.java))
        }

        case0100Button.setOnClickListener {
            startActivity(Intent(this, Case0100SimpleActivity::class.java))
        }

        case0101Button.setOnClickListener {
            startActivity(Intent(this, Case0101SimpleVHActivity::class.java))
        }

        case0200Button.setOnClickListener {
            startActivity(Intent(this, Case0200ClickAndStateActivity::class.java))
        }

        case0300Button.setOnClickListener {
            startActivity(Intent(this, Case0300SealedActivity::class.java))
        }

        case0301Button.setOnClickListener {
            startActivity(Intent(this, Case0301SealedSeveralBindsActivity::class.java))
        }

        case0400Button.setOnClickListener {
            startActivity(Intent(this, Case0400IntoViewActivity::class.java))
        }

        case0500Button.setOnClickListener {
            startActivity(Intent(this, Case0500HorizontalActivity::class.java))
        }

        case0600Button.setOnClickListener {
            startActivity(Intent(this, Case0600InfinityActivity::class.java))
        }

        case0700Button.setOnClickListener {
            startActivity(Intent(this, Case0700Paging::class.java))
        }

    }
}

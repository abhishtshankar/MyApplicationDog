package com.example.myapplicationdog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val btnRecentlyGeneratedDogs = findViewById<Button>(R.id.btnRecentlyGeneratedDogs)

        btnGenerate.setOnClickListener {
            val intent = Intent(this, DogDetailsActivity::class.java)
            startActivity(intent)
        }


        btnRecentlyGeneratedDogs.setOnClickListener {
            val intent = Intent(this, RecentDogsActivity::class.java)
            startActivity(intent)
        }

    }
}

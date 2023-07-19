package com.example.myapplicationdog

import android.os.Bundle
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecentDogsActivity : AppCompatActivity() {

    private lateinit var rvRecentDogs: RecyclerView
    private lateinit var btnClearDogs: Button
    private lateinit var cache: LruCache<String, String>
    private lateinit var dogImageAdapter: DogImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_dogs)

        val toolbar_second: Toolbar = findViewById(R.id.toolbar_second)
        toolbar_second.title = getString(R.string.my_recently_generated_dogs)
        setSupportActionBar(toolbar_second)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvRecentDogs = findViewById(R.id.rvRecentDogs)
        btnClearDogs = findViewById(R.id.btnClearDogs)

        val cacheData = CacheManager.getRecentDogImageUrls()

        dogImageAdapter = DogImageAdapter(cacheData.toMutableList())
        rvRecentDogs.apply {
            layoutManager = LinearLayoutManager(this@RecentDogsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = dogImageAdapter
        }

        btnClearDogs.setOnClickListener {
            CacheManager.clearDogImageUrls()
            dogImageAdapter.clearData()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private inner class DogImageAdapter(private val data: MutableList<String>) :
        RecyclerView.Adapter<DogImageAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dog, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val imageUrl = data[position]
            Glide.with(holder.itemView)
                .load(imageUrl)
                .into(holder.ivDogImage)
        }

        override fun getItemCount(): Int = data.size

        fun clearData() {
            data.clear()
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivDogImage: ImageView = itemView.findViewById(R.id.ivDogImage)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        CacheManager.clearDogImageUrls()
        finish()
    }

}

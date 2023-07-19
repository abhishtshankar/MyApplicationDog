package com.example.myapplicationdog

import android.content.Context
import android.os.Bundle
import android.util.LruCache
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class DogDetailsActivity : AppCompatActivity() {

    private lateinit var btnGenerateDogs: Button
    private lateinit var ivDogImage: ImageView
    private lateinit var cache: LruCache<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.generate_dogs)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sharedPrefs = getSharedPreferences("MyCache", Context.MODE_PRIVATE)
        val cacheData = sharedPrefs.getStringSet("CacheData", HashSet<String>())?.toList()
        cache = LruCache(20)

        cacheData?.forEach { imageUrl -> cache.put(imageUrl, imageUrl) }

        btnGenerateDogs = findViewById(R.id.btnGenerateDogs)
        ivDogImage = findViewById(R.id.ivDogImage)

        btnGenerateDogs.setOnClickListener {
            generateDogImage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPrefs = getSharedPreferences("MyCache", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putStringSet("CacheData", HashSet(cache.snapshot().keys))
        editor.apply()
    }

    private fun generateDogImage() {
        GlobalScope.launch(Dispatchers.Main) {
            val imageUrl = getDogImageUrl().unescapeJson()
            cacheImage(imageUrl)
            loadImage(imageUrl)

            CacheManager.addImageUrl(imageUrl)
        }
    }


    private fun String.unescapeJson(): String {
        return replace("\\/", "/")
    }

    private suspend fun getDogImageUrl(): String {
        return withContext(Dispatchers.IO) {
            val url = URL("https://dog.ceo/api/breeds/image/random")
            val connection = url.openConnection()
            val json = connection.getInputStream().bufferedReader().use { it.readText() }
            val imageUrl = json.substringAfter("\"message\":\"").substringBefore("\"")
            imageUrl
        }
    }

    private fun cacheImage(imageUrl: String) {
        cache.put(imageUrl, imageUrl)
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(ivDogImage)
    }
}

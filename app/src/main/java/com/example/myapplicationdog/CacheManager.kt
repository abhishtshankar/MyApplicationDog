package com.example.myapplicationdog

object CacheManager {
    private val dogImageUrls: MutableList<String> = mutableListOf()

    fun addImageUrl(imageUrl: String) {
        dogImageUrls.add(imageUrl)
        if (dogImageUrls.size > 20) {
            dogImageUrls.removeAt(0)
        }
    }

    fun getRecentDogImageUrls(): List<String> {
        return dogImageUrls.toList()
    }

    fun clearDogImageUrls() {
        dogImageUrls.clear()
    }
}

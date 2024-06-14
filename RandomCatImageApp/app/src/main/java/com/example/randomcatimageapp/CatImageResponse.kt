package com.example.randomcatimageapp
// CatImageResponse.kt
// This file contains the CatImageResponse data class
// CatImageResponse.kt
data class CatImageResponse(
    val id: String,
    val url: String,
    val breeds: List<Breed>
)

data class Breed(
    val name: String,
    val temperament: String,
    val origin: String,
    val life_span: String
)
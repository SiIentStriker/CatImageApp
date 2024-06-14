package com.example.randomcatimageapp

// Cat.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cats")
data class Cat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrl: String,
    val name: String,
    val race: String,
    val temperament: String,
    val origin: String,
    val age: String,
    val lifespan: String,
    val creationTime: Long = System.currentTimeMillis()
)
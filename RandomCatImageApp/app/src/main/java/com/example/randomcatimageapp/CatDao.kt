package com.example.randomcatimageapp

// CatDao.kt
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CatDao {
    @Insert
    fun insertCat(cat: Cat)

    @Insert
    fun insertCats(cats: List<Cat>)

    @Query("SELECT * FROM cats ORDER BY creationTime DESC")
    fun getAllCatsSortedByNewest(): List<Cat>

    @Query("SELECT * FROM cats ORDER BY creationTime ASC")
    fun getAllCatsSortedByOldest(): List<Cat>

    @Query("SELECT * FROM cats ORDER BY name ASC")
    fun getAllCatsSortedByNameAsc(): List<Cat>

    @Query("SELECT * FROM cats ORDER BY name DESC")
    fun getAllCatsSortedByNameDesc(): List<Cat>

    @Query("SELECT * FROM cats WHERE id = :id")
    fun getCatById(id: Int): Cat
}
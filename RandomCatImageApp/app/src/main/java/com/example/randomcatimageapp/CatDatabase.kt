package com.example.randomcatimageapp

// CatDatabase.kt

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Cat::class], version = 2, exportSchema = false)
abstract class CatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao

    companion object {
        @Volatile
        private var INSTANCE: CatDatabase? = null

        fun getDatabase(context: Context): CatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CatDatabase::class.java,
                    "cat_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // You can perform some actions when the database is created.
                        }
                    })
                    .fallbackToDestructiveMigration() // This will clear the database when version changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
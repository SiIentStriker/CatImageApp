package com.example.randomcatimageapp

// CatDetailActivity.kt

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatDetailActivity : AppCompatActivity() {

    private lateinit var catDatabase: CatDatabase
    private lateinit var imageViewCat: ImageView
    private lateinit var textViewCatName: TextView
    private lateinit var textViewCatRace: TextView
    private lateinit var textViewCatTemperament: TextView
    private lateinit var textViewCatOrigin: TextView
    private lateinit var textViewCatAge: TextView
    private lateinit var textViewCatLifespan: TextView
    private lateinit var imageViewQRCode: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_detail)

        catDatabase = CatDatabase.getDatabase(this)

        imageViewCat = findViewById(R.id.imageViewCat)
        textViewCatName = findViewById(R.id.textViewCatName)
        textViewCatRace = findViewById(R.id.textViewCatRace)
        textViewCatTemperament = findViewById(R.id.textViewCatTemperament)
        textViewCatOrigin = findViewById(R.id.textViewCatOrigin)
        textViewCatAge = findViewById(R.id.textViewCatAge)
        textViewCatLifespan = findViewById(R.id.textViewCatLifespan)
        imageViewQRCode = findViewById(R.id.imageViewQRCode)

        val catId = intent.getIntExtra("catId", -1)
        if (catId != -1) {
            loadCatDetails(catId)
        }
    }

    private fun loadCatDetails(catId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val cat = catDatabase.catDao().getCatById(catId)
            Log.d("CatDetailActivity", "Loaded Cat: $cat")
            runOnUiThread {
                textViewCatName.text = cat.name
                textViewCatRace.text = cat.race
                textViewCatTemperament.text = cat.temperament
                textViewCatOrigin.text = cat.origin
                textViewCatAge.text = cat.age
                textViewCatLifespan.text = cat.lifespan

                Glide.with(this@CatDetailActivity)
                    .load(cat.imageUrl)
                    .into(imageViewCat)

                generateQRCode(cat)
            }
        }
    }

    private fun generateQRCode(cat: Cat) {
        val catInfo = Gson().toJson(cat)
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(catInfo, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            imageViewQRCode.setImageBitmap(bmp)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}
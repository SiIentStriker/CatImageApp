package com.example.randomcatimageapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrCodeScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)

        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                setResult(Activity.RESULT_CANCELED)
                finish()
            } else {
                val qrContent = result.contents
                handleQrCodeResult(qrContent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleQrCodeResult(qrContent: String) {
        // Assuming the QR code content is a URL with cat details
        val uri = Uri.parse(qrContent)
        val name = uri.getQueryParameter("name")
        val race = uri.getQueryParameter("race")
        val age = uri.getQueryParameter("age")
        val temperament = uri.getQueryParameter("temperament")
        val origin = uri.getQueryParameter("origin")
        val imageUrl = uri.getQueryParameter("imageUrl")
        val lifespan = uri.getQueryParameter("lifespan")

        if (name != null && race != null && age != null) {
            val cat = Cat(
                imageUrl = imageUrl ?: "",
                name = name,
                race = race,
                temperament = temperament ?: "",
                origin = origin ?: "",
                age = age,
                lifespan = lifespan ?: ""
            )
            saveCatToDatabase(cat)
        } else {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun saveCatToDatabase(cat: Cat) {
        CoroutineScope(Dispatchers.IO).launch {
            val catDatabase = CatDatabase.getDatabase(this@QrCodeScannerActivity)
            catDatabase.catDao().insertCat(cat)
            withContext(Dispatchers.Main) {
                val resultIntent = Intent()
                resultIntent.putExtra("catName", cat.name)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
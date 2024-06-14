package com.example.randomcatimageapp

// CameraActivity.kt
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraActivity : AppCompatActivity() {

    private lateinit var textViewScanPrompt: TextView
    private lateinit var catDatabase: CatDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        textViewScanPrompt = findViewById(R.id.textViewScanPrompt)
        catDatabase = CatDatabase.getDatabase(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
            startQRCodeScanner()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startQRCodeScanner()
        } else {
            finish()
        }
    }

    private fun startQRCodeScanner() {
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents != null) {
            val qrCodeContent = result.contents
            Log.d("CameraActivity", "QR Code Content: $qrCodeContent")
            processQRCodeContent(qrCodeContent)
        } else {
            Toast.makeText(this, "No QR code found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun processQRCodeContent(qrCodeContent: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Try to parse as integer (ID)
                val catId = qrCodeContent.toIntOrNull()
                if (catId != null) {
                    val cat = catDatabase.catDao().getCatById(catId)
                    Log.d("CameraActivity", "Retrieved Cat by ID: $cat")
                    if (cat != null) {
                        withContext(Dispatchers.Main) {
                            openCatDetails(catId)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@CameraActivity, "Cat not found", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                } else {
                    // Try to parse as JSON (Cat object)
                    val cat = parseCatFromJson(qrCodeContent)
                    if (cat != null) {
                        // Insert cat into database
                        catDatabase.catDao().getCatById(cat.id)
                        Log.d("CameraActivity", "Inserted Cat: $cat")
                        withContext(Dispatchers.Main) {
                            openCatDetails(cat.id)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@CameraActivity, "Invalid QR Code", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("CameraActivity", "Error processing QR Code", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CameraActivity, "Error processing QR Code", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun parseCatFromJson(json: String): Cat? {
        return try {
            Gson().fromJson(json, Cat::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e("CameraActivity", "Error parsing JSON", e)
            null
        }
    }

    private fun openCatDetails(catId: Int) {
        val intent = Intent(this@CameraActivity, CatDetailActivity::class.java)
        intent.putExtra("catId", catId)
        startActivity(intent)
        finish()
    }
}
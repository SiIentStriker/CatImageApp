package com.example.randomcatimageapp

// MainActivity.kt
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var catDatabase: CatDatabase
    private lateinit var recyclerViewCats: RecyclerView
    private lateinit var catAdapter: CatAdapter
    private lateinit var buttonGenerateCat: Button
    private lateinit var buttonOpenCamera: Button
    private lateinit var buttonScanQrCode: Button
    private var catList: List<Cat> = emptyList()
    private var mostRecentCat: Cat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catDatabase = CatDatabase.getDatabase(this)

        recyclerViewCats = findViewById(R.id.recyclerViewCats)
        buttonGenerateCat = findViewById(R.id.buttonGenerateCat)
        buttonOpenCamera = findViewById(R.id.buttonOpenCamera)

        recyclerViewCats.layoutManager = LinearLayoutManager(this)
        catAdapter = CatAdapter(emptyList()) { cat ->
            val intent = Intent(this, CatDetailActivity::class.java)
            intent.putExtra("catId", cat.id)
            startActivity(intent)
        }
        recyclerViewCats.adapter = catAdapter

        buttonGenerateCat.setOnClickListener {
            generateRandomCat()
        }

        buttonOpenCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        loadSavedCats("newest")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QR_CODE_SCAN_REQUEST_CODE && resultCode == RESULT_OK) {
            val catName = data?.getStringExtra("catName")
            Toast.makeText(this, "Cat $catName added to the database", Toast.LENGTH_SHORT).show()
            loadSavedCats("newest")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_database -> {
                deleteDatabase()
                true
            }
            R.id.menu_add_ten_cats -> {
                generateRandomCats()
                true
            }
            R.id.menu_sort_name_az -> {
                loadCats("name_az")
                true
            }
            R.id.menu_sort_name_za -> {
                loadCats("name_za")
                true
            }
            R.id.menu_sort_newest -> {
                loadCats("newest")
                true
            }
            R.id.menu_sort_oldest -> {
                loadCats("oldest")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadCats(sortOrder: String) {
        CoroutineScope(Dispatchers.IO).launch {
            catList = when (sortOrder) {
                "name_az" -> catDatabase.catDao().getAllCatsSortedByNameAsc()
                "name_za" -> catDatabase.catDao().getAllCatsSortedByNameDesc()
                "newest" -> catDatabase.catDao().getAllCatsSortedByNewest()
                "oldest" -> catDatabase.catDao().getAllCatsSortedByOldest()
                else -> catDatabase.catDao().getAllCatsSortedByNewest()
            }
            withContext(Dispatchers.Main) {
                updateCatListWithMostRecent()
            }
        }
    }

    private fun deleteDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            catDatabase.clearAllTables()
            loadSavedCats("newest")
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Database deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateRandomCat() {
        RetrofitClient.apiService.getRandomCatImage().enqueue(object : Callback<List<CatImageResponse>> {
            override fun onResponse(call: Call<List<CatImageResponse>>, response: Response<List<CatImageResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    val catImageResponse = response.body()!![0]
                    val catImageUrl = catImageResponse.url
                    val breedInfo = catImageResponse.breeds.firstOrNull()
                    if (breedInfo != null) {
                        val catName = RandomCatInfoGenerator.getRandomName()
                        val cat = Cat(
                            imageUrl = catImageUrl,
                            name = catName,
                            race = breedInfo.name,
                            temperament = breedInfo.temperament,
                            origin = breedInfo.origin,
                            age = "Unknown",
                            lifespan = breedInfo.life_span
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            catDatabase.catDao().insertCat(cat)
                            mostRecentCat = cat
                            loadSavedCats("newest")
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "No breed information found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch cat image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CatImageResponse>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun generateRandomCats() {
        RetrofitClient.apiService.getRandomCatImages(10).enqueue(object : Callback<List<CatImageResponse>> {
            override fun onResponse(call: Call<List<CatImageResponse>>, response: Response<List<CatImageResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    val catsToInsert = response.body()!!.mapNotNull { catImageResponse ->
                        val catImageUrl = catImageResponse.url
                        val breedInfo = catImageResponse.breeds.firstOrNull()
                        if (breedInfo != null) {
                            val catName = RandomCatInfoGenerator.getRandomName()
                            Cat(
                                imageUrl = catImageUrl,
                                name = catName,
                                race = breedInfo.name,
                                temperament = breedInfo.temperament,
                                origin = breedInfo.origin,
                                age = "Unknown",
                                lifespan = breedInfo.life_span
                            )
                        } else {
                            null
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        catDatabase.catDao().insertCats(catsToInsert)
                        loadSavedCats("newest")
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch cat images", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CatImageResponse>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadSavedCats(sortOrder: String) {
        CoroutineScope(Dispatchers.IO).launch {
            catList = when (sortOrder) {
                "name_az" -> catDatabase.catDao().getAllCatsSortedByNameAsc()
                "name_za" -> catDatabase.catDao().getAllCatsSortedByNameDesc()
                "newest" -> catDatabase.catDao().getAllCatsSortedByNewest()
                "oldest" -> catDatabase.catDao().getAllCatsSortedByOldest()
                else -> catDatabase.catDao().getAllCatsSortedByNewest()
            }
            withContext(Dispatchers.Main) {
                updateCatListWithMostRecent()
            }
        }
    }

    private fun updateCatListWithMostRecent() {
        val displayList = if (mostRecentCat != null) {
            listOf(mostRecentCat!!) + catList
        } else {
            catList
        }
        catAdapter.updateCatList(displayList)
    }

    companion object {
        private const val QR_CODE_SCAN_REQUEST_CODE = 1
    }
}
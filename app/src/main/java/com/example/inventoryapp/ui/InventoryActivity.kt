package com.example.inventoryapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.example.inventoryapp.ui.api.AssetDTO
import com.example.inventoryapp.ui.api.InventoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryActivity : AppCompatActivity() {
    private lateinit var periodText: AppCompatTextView
    private lateinit var dateText: AppCompatTextView
    private lateinit var buttomInventory: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AssetAdapter
    private var assetList: MutableList<AssetDTO> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        periodText = findViewById(R.id.period)
        dateText = findViewById(R.id.date)
        buttomInventory = findViewById(R.id.button_inventory)
        recyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AssetAdapter(assetList)
        recyclerView.adapter = adapter

        // Fetch data from API
        fetchInventoryData()

        buttomInventory.setOnClickListener {
            startActivity(Intent(this, QRScannerActivity::class.java))
        }
    }

    private fun fetchInventoryData() {
        val token = getToken() // Lấy token từ SharedPreferences

        if (token != null) {
            // Gửi request với token
            ApiClient.instance.getInventoryInfo("Bearer " + token).enqueue(object : Callback<InventoryResponse> {
                override fun onResponse(call: Call<InventoryResponse>, response: Response<InventoryResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@InventoryActivity, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show()
                        response.body()?.let {
                            val inventoryDTO = it.result.inventoryDTO
                            periodText.text = inventoryDTO.inventoryName
                            val startDateFormatted = inventoryDTO.startDate.split("T")[0]
                            val endDateFormatted = inventoryDTO.endDate.split("T")[0]

                            dateText.text = "$startDateFormatted đến $endDateFormatted"

                            // Update asset list
                            assetList.clear()
                            assetList.addAll(it.result.assetDTO)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e("API_ERROR", "Error Code: ${response.code()}, Message: ${response.errorBody()?.string()}")
                        Toast.makeText(this@InventoryActivity, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<InventoryResponse>, t: Throwable) {
                    Toast.makeText(this@InventoryActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Nếu không có token, hiển thị thông báo lỗi
            Toast.makeText(this@InventoryActivity, "Token không hợp lệ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("TOKEN", null) // Trả về null nếu không có token
    }
}




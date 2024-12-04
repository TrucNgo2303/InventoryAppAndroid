package com.example.inventoryapp.ui

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.inventoryapp.R
import java.util.*

class InventoryInfoActivity : AppCompatActivity() {
    private lateinit var  assetCode: AppCompatTextView
    private lateinit var tv_unit: AppCompatTextView
    private lateinit var tv_user: AppCompatTextView
    private lateinit var tv_status: AppCompatTextView
    private lateinit var saveButton: AppCompatButton
    private lateinit var assetNameTV: AppCompatTextView
    private lateinit var assetTypeTV: AppCompatTextView
    private lateinit var statusTV: AppCompatTextView

    var data_unit = listOf("Đơn vị A", "Đơn vị B", "Đơn vị C")
    var data_user = listOf("Nguyen Van A", "Tran Thi B")
    var data_status = listOf("Dang su dung", "Chua su dung")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_info)

        assetCode = findViewById(R.id.asset_code)
        tv_unit = findViewById(R.id.unit)
        tv_user = findViewById(R.id.user)
        tv_status = findViewById(R.id.status)
        saveButton = findViewById(R.id.save_button)
        assetNameTV = findViewById(R.id.asset_name)
        assetTypeTV = findViewById(R.id.asset_type)
        statusTV = findViewById(R.id.status)

        assetCode.text = intent.getStringExtra("asset_code")

        tv_unit.setOnClickListener {
            chooseWaringLevel(tv_unit,data_unit)
        }
        tv_user.setOnClickListener {
            chooseWaringLevel(tv_user,data_user)
        }
        tv_status.setOnClickListener {
            chooseWaringLevel(tv_status,data_status)
        }
        saveButton.setOnClickListener{
            val assetName = assetNameTV.text.toString()
            val assetType = assetTypeTV.text.toString()
            val status = statusTV.text.toString()
            val dateChecked = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            Toast.makeText(this, "Data: $assetName, $assetType, $dateChecked", Toast.LENGTH_SHORT).show()
//            // Lưu dữ liệu vào SharedPreferences
//            val sharedPreferences = getSharedPreferences("InventoryPrefs", MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            val data = "$assetName|$assetType|$dateChecked|$status"
//
//            // Lưu danh sách các mục vào SharedPreferences (tạo danh sách nếu chưa có)
//            val existingData = sharedPreferences.getString("inventoryData", "")
//            val newData = if (existingData.isNullOrEmpty()) data else "$existingData,$data"
//
//            editor.putString("inventoryData", newData)
//            editor.apply()

            // Chuyển đến InventoryActivity
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)

        }
    }

    private fun chooseWaringLevel(tv: AppCompatTextView, data: List<String>) {

        val popupMenu = PopupMenu(this, tv)

        // Add menu items from the list
        data.forEachIndexed { index, warningLevel ->
            popupMenu.menu.add(0, index, index, warningLevel)
        }


        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedLevel = data[menuItem.itemId]
            tv.text =
                selectedLevel // Set the button text to the selected item
            true
        }
        popupMenu.show()
    }
}
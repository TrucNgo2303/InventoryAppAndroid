package com.example.inventoryapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.inventoryapp.R

class HomeActivity : AppCompatActivity() {
    private lateinit var avatarImage: AppCompatImageView
    private lateinit var nameText: AppCompatTextView
    private lateinit var department: AppCompatTextView
    private lateinit var inventoryButton: AppCompatButton
    private lateinit var inventoryHistoryButton: AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        inventoryButton = findViewById(R.id.inventory_button)
        inventoryButton.setOnClickListener {
            startActivity(Intent(this, InventoryActivity::class.java))
        }
    }
}
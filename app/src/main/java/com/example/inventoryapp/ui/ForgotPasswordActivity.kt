package com.example.inventoryapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import com.example.inventoryapp.R

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var forgotpasswordUsernameEditText: AppCompatEditText
    private lateinit var forgotpasswordEmailEditText: AppCompatEditText
    private lateinit var forgotpasswordUsernameErrorText: AppCompatTextView
    private lateinit var forgotpasswordEmailErrorText: AppCompatTextView
    private lateinit var forgotpasswordButton: AppCompatButton
    private var forgotpasswordUsername = ""
    private var forgotpasswordEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotpasswordUsernameEditText = findViewById(R.id.forgotpasswordUsername)
        forgotpasswordEmailEditText = findViewById(R.id.forgotpasswordEmail)
        forgotpasswordUsernameErrorText = findViewById(R.id.forgotpasswordUsernameError)
        forgotpasswordEmailErrorText = findViewById(R.id.forgotpasswordEmailError)
        forgotpasswordButton = findViewById(R.id.forgotpasswordButton)

        forgotpasswordUsernameEditText.addTextChangedListener{
            forgotpasswordUsernameErrorText.visibility = View.GONE
        }
        forgotpasswordEmailEditText.addTextChangedListener{
            forgotpasswordEmailErrorText.visibility = View.GONE
        }

        forgotpasswordButton.setOnClickListener{
            forgotpasswordUsername = forgotpasswordUsernameEditText.text.toString()
            forgotpasswordEmail = forgotpasswordEmailEditText.text.toString()

            if(validateInputs()){
                Toast.makeText(this, "Lay ma xac nhan thanh cong", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ResetPasswordActivity::class.java))
            }
        }
    }
    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate username
        if (TextUtils.isEmpty(forgotpasswordUsername)) {
            forgotpasswordUsernameErrorText.text = "Tài khoản không được để trống"
            forgotpasswordUsernameErrorText.visibility = View.VISIBLE
            isValid = false
        }
        // Validate password
        if (TextUtils.isEmpty(forgotpasswordEmail)) {
            forgotpasswordEmailErrorText.text = "Email không được để trống"
            forgotpasswordEmailErrorText.visibility = View.VISIBLE
            isValid = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(forgotpasswordEmail).matches())
        {
            forgotpasswordEmailErrorText.text = "Email sai định dạng"
            forgotpasswordEmailErrorText.visibility = View.VISIBLE
            isValid = false
        }
        return isValid

    }
}
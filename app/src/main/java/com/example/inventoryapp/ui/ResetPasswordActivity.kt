package com.example.inventoryapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import com.example.inventoryapp.R

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var RSPUsernameEditText: AppCompatEditText
    private lateinit var newPasswordEditText: AppCompatEditText
    private lateinit var confirmPasswordEditText: AppCompatEditText
    private lateinit var codeEditText: AppCompatEditText
    private lateinit var RSPUsernameError: AppCompatTextView
    private lateinit var newPasswordError: AppCompatTextView
    private lateinit var confirmPasswordError: AppCompatTextView
    private lateinit var codeError: AppCompatTextView
    private lateinit var resetPasswordButton:AppCompatButton

    private var RSPUsername = ""
    private var newPassword = ""
    private var confirmPassword = ""
    private var code = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        RSPUsernameEditText = findViewById(R.id.resetpasswordUsername)
        newPasswordEditText = findViewById(R.id.newPassword)
        confirmPasswordEditText = findViewById(R.id.newPasswordConfirm)
        codeEditText = findViewById(R.id.code)
        RSPUsernameError = findViewById(R.id.RPUsernameError)
        newPasswordError = findViewById(R.id.newPasswordError)
        confirmPasswordError = findViewById(R.id.newPasswordConfirmError)
        codeError = findViewById(R.id.codeError)
        resetPasswordButton = findViewById(R.id.RSPButton)

        RSPUsernameEditText.addTextChangedListener{
            RSPUsernameError.visibility = View.GONE
        }
        newPasswordEditText.addTextChangedListener{
            newPasswordError.visibility = View.GONE
        }
        confirmPasswordEditText.addTextChangedListener{
            confirmPasswordError.visibility = View.GONE
        }
        codeEditText.addTextChangedListener{
            codeError.visibility = View.GONE
        }

        resetPasswordButton.setOnClickListener{
            RSPUsername = RSPUsernameEditText.text.toString()
            newPassword = newPasswordEditText.text.toString()
            confirmPassword = confirmPasswordEditText.text.toString()
            code = codeEditText.text.toString()

            if(validateInputs()){
                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate username
        if (TextUtils.isEmpty(RSPUsername)) {
            RSPUsernameError.text = "Tài khoản không được để trống"
            RSPUsernameError.visibility = View.VISIBLE
            isValid = false
        } else if (RSPUsername.length < 8 || RSPUsername.length > 100) {
            RSPUsernameError.text = "Tài khoản phải từ 8 đến 100 ký tự"
            RSPUsernameError.visibility = View.VISIBLE
            isValid = false
        }

        // Validate password
        if (TextUtils.isEmpty(newPassword)) {
            newPasswordError.text = "Mật khẩu không được để trống"
            newPasswordError.visibility = View.VISIBLE
            isValid = false
        } else if (newPassword.length < 8 || newPassword.length > 30) {
            newPasswordError.text = "Mật khẩu phải từ 8 đến 30 ký tự"
            newPasswordError.visibility = View.VISIBLE
            isValid = false
        }
        if (confirmPassword != newPassword) {
            confirmPasswordError.text = "Mật khẩu không trùng khớp"
            confirmPasswordError.visibility = View.VISIBLE
            isValid = false
        }
        if (code != "123456") {
            codeError.text = "Mã xác nhận sai"
            codeError.visibility = View.VISIBLE
            isValid = false
        }

        return isValid
    }
}
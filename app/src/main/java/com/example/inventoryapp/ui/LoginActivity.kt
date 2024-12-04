package com.example.inventoryapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inventoryapp.R
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.example.inventoryapp.ui.api.LoginRequest
import com.example.inventoryapp.ui.api.LoginResponse

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: AppCompatEditText
    private lateinit var passwordEditText: AppCompatEditText
    private lateinit var usernameError: AppCompatTextView
    private lateinit var passwordError: AppCompatTextView
    private lateinit var loginButton: AppCompatButton
    private lateinit var forgotPassword: AppCompatTextView
    private var username = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        usernameError = findViewById(R.id.usernameError)
        passwordError = findViewById(R.id.passwordError)
        loginButton = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotpassword)

        usernameEditText.addTextChangedListener{
            usernameError.visibility = View.GONE
        }
        passwordEditText.addTextChangedListener{
            passwordError.visibility = View.GONE
        }

        loginButton.setOnClickListener {
            username = usernameEditText.text.toString()
            password = passwordEditText.text.toString()

            if (validateInputs()) {
                loginApi()
            }
        }
        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }
    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate username
        if (TextUtils.isEmpty(username)) {
            usernameError.text = "Tài khoản không được để trống"
            usernameError.visibility = View.VISIBLE
            isValid = false
        } else if (username.length < 8 || username.length > 100) {
            usernameError.text = "Tài khoản phải từ 8 đến 100 ký tự"
            usernameError.visibility = View.VISIBLE
            isValid = false
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordError.text = "Mật khẩu không được để trống"
            passwordError.visibility = View.VISIBLE
            isValid = false
        } else if (password.length < 8 || password.length > 30) {
            passwordError.text = "Mật khẩu phải từ 8 đến 30 ký tự"
            passwordError.visibility = View.VISIBLE
            isValid = false
        }

        return isValid
    }
    private fun loginApi() {
        val request = LoginRequest(username, password)

        // Gửi request đến API
        ApiClient.instance.login(request).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: retrofit2.Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    val token = loginResponse.result.token // Lấy token từ result
                    Log.d("API", "Token là: $token")

                    if (token != null) {
                        // Lưu token vào SharedPreferences
                        saveToken(token)

                        // Thông báo đăng nhập thành công
                        Toast.makeText(this@LoginActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()

                        // Chuyển đến HomeActivity
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        // Xử lý trường hợp không có token (nếu có lỗi gì đó trong API)
                        Toast.makeText(this@LoginActivity, "Không có token", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    passwordError.text = "Mật khẩu không chính xác"
                    passwordError.visibility = View.VISIBLE
                    Toast.makeText(this@LoginActivity, "Đăng nhập thất bại: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN", token) // Lưu token vào SharedPreferences
        editor.apply() // Lưu thay đổi
    }


}
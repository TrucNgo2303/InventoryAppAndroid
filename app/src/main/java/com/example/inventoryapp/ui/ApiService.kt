package com.example.inventoryapp.ui.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// Request body class
data class LoginRequest(
    val username: String,
    val password: String
)

// Response body class
data class LoginResponse(
    val code: Int,
    val result: LoginResult
)

data class LoginResult(
    val token: String?,
    val loginResponse: LoginDetails
)
data class LoginDetails(
    val userId: Int,
    val username: String,
    val fullName: String,
    val emailAddress: String,
    val role: String,
    val deptId: Int,
    val deptName: String,
    val status: Int
)
data class InventoryDTO(
    val inventoryName: String,
    val startDate: String,
    val endDate: String
)

data class AssetDTO(
    val assetName: String,
    val assetType: Int,
    val inventoryDate: String,
    val status: Int
)

data class Result(
    val inventoryDTO: InventoryDTO,
    val assetDTO: List<AssetDTO>
)

data class InventoryResponse(
    val code: Int,
    val result: Result
)

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @GET("inventory/inventory-info")
    fun getInventoryInfo(@Header("Authorization") token: String): Call<InventoryResponse>
}



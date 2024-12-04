package com.example.inventoryapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.example.inventoryapp.R
import java.util.concurrent.ExecutionException
import androidx.camera.view.PreviewView
import com.google.common.util.concurrent.ListenableFuture

class QRScannerActivity : AppCompatActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var qrCodeText: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)
        Log.d("QRScannerActivity", "Bắt đầu chạy")
        // Initialize the PreviewView for camera preview
        previewView = findViewById(R.id.preview_view)
        qrCodeText = findViewById(R.id.qrCodeText)

        // Request camera permission at runtime (if not already granted)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA), 101
            )
        } else {
            // Initialize CameraX if permission is granted
            initializeCamera()
        }
    }

    // Initialize CameraX and bind use cases
    private fun initializeCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()
            } catch (e: ExecutionException) {
                Log.e("QRScannerActivity", "CameraX initialization failed", e)
            } catch (e: InterruptedException) {
                Log.e("QRScannerActivity", "CameraX initialization failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        // Create the Preview use case
        preview = Preview.Builder().build()

        // Create the ImageAnalysis use case for barcode scanning
        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetRotation(previewView.display.rotation) // Set rotation to match the device's display
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                    processImageProxy(imageProxy)
                }
            }

        // Bind the camera lifecycle to the use cases
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll() // Unbind use cases before rebinding
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e: Exception) {
            Log.e("QRScannerActivity", "Binding use cases failed", e)
        }
    }

    private fun processImageProxy(imageProxy: ImageProxy) {
        // Convert the image to InputImage
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            // Use imageProxy.imageInfo.rotationDegrees to get the rotation degrees
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees

            val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

            // Initialize ML Kit Barcode Scanner
            val scanner = BarcodeScanning.getClient()

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val firstBarcode = barcodes[0] // Lấy phần tử đầu tiên
                        handleDetectedBarcode(firstBarcode) // Gọi hàm xử lý QR Code với phần tử đầu tiên
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("QRScannerActivity", "Barcode scan failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close() // Close the image to avoid memory leaks
                }
        }
    }

    private fun handleDetectedBarcode(barcode: Barcode) {
        val qrCodeValue = barcode.displayValue
        Log.d("QRScannerActivity", "QR Code detected: $qrCodeValue")

        if (qrCodeValue.isNotEmpty() && qrCodeValue != null) {
            val intent = Intent(this, InventoryInfoActivity::class.java)
            intent.putExtra("asset_code", qrCodeValue)
            startActivity(intent)
            finish()
        } else {
            // Nếu QR Code không hợp lệ, hiển thị Toast và quay lại màn InventoryActivity
            Toast.makeText(this, "QR không hợp lệ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        cameraProvider.unbindAll()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Permission granted, initialize CameraX
            initializeCamera()
        } else {
            Log.e("QRScannerActivity", "Camera permission denied")
        }
    }
}

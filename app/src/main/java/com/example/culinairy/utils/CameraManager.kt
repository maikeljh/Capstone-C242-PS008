package com.example.culinairy.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

interface ImageCaptureCallback {
    fun onImageCaptureInitialized(imageCapture: ImageCapture)
}

class CameraManager (
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView?,
    private val imageCaptureCallback: ImageCaptureCallback?
) {
    // Request camera permission
    fun requestCameraPermission() {
        if (context is Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Start camera
     fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView?.surfaceProvider
                }
                val imageCapture = ImageCapture.Builder().build()
                imageCaptureCallback?.onImageCaptureInitialized(imageCapture)
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )
                Log.d("CameraManager", "Camera successfully bound")
            } catch (exc: Exception) {
                Log.e("CameraManager", "Use case binding failed: ${exc.message}", exc)
                Toast.makeText(context, "Use case binding failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    // Static methods
    companion object {
        // Constants
        const val CAMERA_PERMISSION_REQUEST_CODE = 123

        // Check if all permissions are granted
        fun allPermissionGranted(context: Context?) = arrayOf(Manifest.permission.CAMERA).all {
            ContextCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
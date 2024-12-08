package com.example.culinairy.ui.capture_ocr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentCaptureReceiptBinding
import com.example.culinairy.utils.CameraManager
import com.example.culinairy.utils.CameraManager.Companion.allPermissionGranted
import com.example.culinairy.utils.ImageCaptureCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureReceiptFragment : Fragment(), ImageCaptureCallback {

    private var _binding: FragmentCaptureReceiptBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Variables
    private lateinit var cameraManager: CameraManager
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null


    override fun onResume() {
        super.onResume()
        if (allPermissionGranted(context)) {
            cameraManager.startCamera()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val captureReceiptViewModel = ViewModelProvider(this)[CaptureReceiptViewModel::class.java]
        _binding = FragmentCaptureReceiptBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Remove navbar
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.GONE
        val fab: FloatingActionButton = requireActivity().findViewById(R.id.fab)
        fab.visibility = View.GONE

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Check camera permission
        cameraManager = CameraManager(requireContext(), viewLifecycleOwner, binding.previewView, this)
        if (!allPermissionGranted(requireContext())) {
            cameraManager.requestCameraPermission()
        } else {
            cameraManager.startCamera()
        }

        // Set up button listeners
        setupButtonListeners()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.VISIBLE
        val fab: FloatingActionButton = requireActivity().findViewById(com.example.culinairy.R.id.fab)
        fab.visibility = View.VISIBLE
    }


    // Set up button listeners
    private fun setupButtonListeners() {
        binding.cameraButton.setOnClickListener {
            takePhoto()
        }
        binding.galleryButton.setOnClickListener {
            pickFromGallery()
        }
        binding.retakeButton.setOnClickListener {
            binding.overlay.setImageBitmap(null)
            binding.retakeButton.visibility = View.GONE
            binding.confirmButton.visibility = View.GONE
            binding.cameraButton.visibility = View.VISIBLE
            binding.galleryButton.visibility = View.VISIBLE
        }
        binding.confirmButton.setOnClickListener {
            val bitmap = (binding.overlay.drawable as? BitmapDrawable)?.bitmap

            // Navigate to result fragment
            findNavController().navigate(R.id.action_captureReceiptFragment_to_captureReceiptResultFragment)
        }
    }

    // Get output directory
    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    // Take photo
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory, System.currentTimeMillis().toString() + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    val exif = ExifInterface(photoFile.absolutePath)
                    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    val rotatedBitmap = rotateBitmap(bitmap, orientation)
                    binding.overlay.setImageBitmap(rotatedBitmap)

                    binding.retakeButton.visibility = View.VISIBLE
                    binding.confirmButton.visibility = View.VISIBLE
                    binding.cameraButton.visibility = View.GONE
                    binding.galleryButton.visibility = View.GONE
                }
            }
        )
    }

    // Rotate bitmap
    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(270f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // Pick from gallery
    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    // Gallery result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                    val exif = ExifInterface(requireActivity().contentResolver.openInputStream(selectedImageUri)!!)
                    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    val rotatedBitmap = rotateBitmap(bitmap, orientation)

                    binding.overlay.setImageBitmap(rotatedBitmap)
                    binding.retakeButton.visibility = View.VISIBLE
                    binding.confirmButton.visibility = View.VISIBLE
                    binding.cameraButton.visibility = View.GONE
                    binding.galleryButton.visibility = View.GONE

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onImageCaptureInitialized(imageCapture: ImageCapture) {
        this.imageCapture = imageCapture
    }
}
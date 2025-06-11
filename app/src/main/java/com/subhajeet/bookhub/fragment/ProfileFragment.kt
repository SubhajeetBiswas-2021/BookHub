package com.subhajeet.bookhub.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.subhajeet.bookhub.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProfileFragment : Fragment() {


    lateinit var imageViewProfile: ImageView
    lateinit var btnAdd:Button
    lateinit var txtname: TextView
    lateinit var txtphone: TextView
    lateinit var txtemail: TextView

    // File name for the saved image
    private val IMAGE_FILE_NAME = "profile_image.png"



    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            imageViewProfile.setImageURI(uri)
            saveImageToInternalStorage(uri) // Save the image locally
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        imageViewProfile = view.findViewById(R.id.imageViewProfile)
        btnAdd = view.findViewById(R.id.btnAdd)

        txtname = view.findViewById(R.id.txtname)
        txtphone = view.findViewById(R.id.txtphone)
        txtemail = view.findViewById(R.id.txtemail)

        // Load the saved image if it exists
        loadImageFromInternalStorage()?.let {
            imageViewProfile.setImageBitmap(it)
        }


        btnAdd.setOnClickListener {
            //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            showAccessGalleryConfirmation()
        }


        return view
    }

    private fun showAccessGalleryConfirmation() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Gallery")
        builder.setMessage("Are you sure you want to give access to your Gallery?")
        builder.setPositiveButton("Yes") { _, _ ->

                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        builder.setNegativeButton("No") { dialog, _ ->
            // Dismiss the dialog
            dialog.dismiss()
        }
        builder.create().show()
    }


    // Save the image to internal storage
    private fun saveImageToInternalStorage(uri: Uri) {
        try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val file = File(requireContext().filesDir, IMAGE_FILE_NAME)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Load the image from internal storage
    private fun loadImageFromInternalStorage(): Bitmap? {
        return try {
            val file = File(requireContext().filesDir, IMAGE_FILE_NAME)
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
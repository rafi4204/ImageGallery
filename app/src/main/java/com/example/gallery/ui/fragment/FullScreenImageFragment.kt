package com.example.gallery.ui.fragment

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gallery.BuildConfig
import com.example.gallery.R
import com.example.gallery.databinding.FragmentFullScreenImageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*


@AndroidEntryPoint
class FullScreenImageFragment : Fragment() {
    private val IMAGES_FOLDER_NAME = "images"
    private var toolbar: Toolbar? = null
    private var URL: String = ""
    private var _binding: FragmentFullScreenImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var bitmapValue: Bitmap
    val TAG = "log!!!!!!!!!!!!!!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullScreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        URL = requireArguments().getString("url").toString()
        toolbar = requireActivity().findViewById(R.id.toolbar)
        (toolbar as Toolbar).visibility = View.VISIBLE
        toolbar?.title = ""

        loadImage()
    }

    private fun loadImage() {
        Glide.with(this)
            .asBitmap()
            .load(URL)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.touchImage.setImageBitmap(resource)
                    bitmapValue = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }


    private fun share() {
        lifecycleScope.launch(Dispatchers.Default) {
            storeAndShareImage(bitmapValue)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_ui, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                share()
                true
            }
            R.id.download -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        if (saveImage(bitmapValue, getImageName()))
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Photo saved successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                true
            }

            else ->
                super.onOptionsItemSelected(item)

        }

    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun storeAndShareImage(image: Bitmap) {
        val pictureFile: File? = getOutputMediaFile()
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ") // e.getMessage());
            return
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                pictureFile
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM, photoURI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/*"
            val resInfoList = requireActivity().packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireActivity().grantUriPermission(
                    packageName,
                    photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            startActivity(intent)
            Log.d(TAG, "img dir: $pictureFile")
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "File not found: " + e.message)
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: " + e.message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getOutputMediaFile(): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir =
            File(
                ContextCompat.getExternalFilesDirs(requireContext(), "image").firstOrNull(),
                "outputPath"
            )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        return File(mediaStorageDir.path + File.separator.toString() + getImageName())
    }

    private fun getImageName(): String {
        val generator = Random()
        var n = 1000
        n = generator.nextInt(n)
        return "Image-$n.jpg"
    }

    @Throws(IOException::class)
    private fun saveImage(bitmap: Bitmap, name: String): Boolean {
        val saved: Boolean
        val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver: ContentResolver = requireContext().contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/$IMAGES_FOLDER_NAME")
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            resolver.openOutputStream(imageUri!!)
        } else {
            val imagesDir =
                requireContext().getExternalFilesDir(null)?.absolutePath + IMAGES_FOLDER_NAME
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val image = File(imagesDir, "$name.png")
            FileOutputStream(image)
        }
        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos?.flush()
        fos?.close()
        return saved
    }
}
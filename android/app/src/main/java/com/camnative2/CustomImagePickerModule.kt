package com.camnative2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.NonNull
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.io.IOException
import java.util.*

class CustomImagePickerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {

    private var pickerPromise: Promise? = null

    init {
        reactContext.addActivityEventListener(this)
    }

    override fun getName(): String {
        return "CustomImagePickerModule"
    }

    @ReactMethod
    fun openPicker(promise: Promise) {
        val startTime = System.currentTimeMillis()
        val activity: Activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "Activity doesn't exist")
            return
        }

        pickerPromise = promise
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activity.startActivityForResult(intent, IMAGE_PICKER_REQUEST)
           // Log.d("CustomImagePickerModule", "Time to start picker: " + (System.currentTimeMillis() - startTime) + " ms")
        } catch (e: Exception) {
            pickerPromise?.reject("ERROR", e)
            pickerPromise = null
        }
    }

    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uri: Uri? = data.data
                try {
                    val inputStream = activity.contentResolver.openInputStream(uri!!)
                    val bytes = inputStream!!.readBytes()
                    val base64Image = Base64.getEncoder().encodeToString(bytes)
                    pickerPromise?.resolve(base64Image)
                } catch (e: IOException) {
                    pickerPromise?.reject("ERROR", e)
                }
            } else {
                pickerPromise?.reject("NO_DATA", "No data found")
            }
        } else if (requestCode == IMAGE_PICKER_REQUEST) {
            pickerPromise?.reject("CANCELLED", "User cancelled image picker")
        }
    }

    override fun onNewIntent(intent: Intent?) {}

    companion object {
        private const val IMAGE_PICKER_REQUEST = 467081
    }
}
package com.camnative2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

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
        val activity: Activity = currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "Activity doesn't exist")
            return
        }

        pickerPromise = promise
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activity.startActivityForResult(intent, IMAGE_PICKER_REQUEST)
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
                    if (uri != null) {
                        pickerPromise?.resolve(uri.toString())
                    } else {
                        pickerPromise?.reject("NO_URI", "No URI returned from picker")
                    }
                } catch (e: Exception) {
                    pickerPromise?.reject("ERROR", e)
                } finally {
                    pickerPromise = null
                }
            }
        } else if (requestCode == IMAGE_PICKER_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            pickerPromise?.reject("CANCELLED", "Image picker was cancelled")
            pickerPromise = null
        }
    }

    override fun onNewIntent(intent: Intent?) {
        // This method is required to implement the ActivityEventListener interface
    }

    companion object {
        private const val IMAGE_PICKER_REQUEST = 1
    }
}

//package com.camnative2
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.provider.MediaStore
//import androidx.annotation.NonNull
//import com.facebook.react.bridge.ActivityEventListener
//import com.facebook.react.bridge.Promise
//import com.facebook.react.bridge.ReactApplicationContext
//import com.facebook.react.bridge.ReactContextBaseJavaModule
//import com.facebook.react.bridge.ReactMethod
//import java.io.IOException
//import java.util.*
//
//class CustomImagePickerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {
//
//    private var pickerPromise: Promise? = null
//
//    init {
//        reactContext.addActivityEventListener(this)
//    }
//
//    override fun getName(): String {
//        return "CustomImagePickerModule"
//    }
//
//    @ReactMethod
//    fun openPicker(promise: Promise) {
//        val startTime = System.currentTimeMillis()
//        val activity: Activity = currentActivity ?: run {
//            promise.reject("NO_ACTIVITY", "Activity doesn't exist")
//            return
//        }
//
//        pickerPromise = promise
//        try {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            activity.startActivityForResult(intent, IMAGE_PICKER_REQUEST)
//           // Log.d("CustomImagePickerModule", "Time to start picker: " + (System.currentTimeMillis() - startTime) + " ms")
//        } catch (e: Exception) {
//            pickerPromise?.reject("ERROR", e)
//            pickerPromise = null
//        }
//    }
//
//    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                val uri: Uri? = data.data
//                try {
//                    val inputStream = activity.contentResolver.openInputStream(uri!!)
//                    val bytes = inputStream!!.readBytes()
//                    val base64Image = Base64.getEncoder().encodeToString(bytes)
//                    pickerPromise?.resolve(base64Image)
//                } catch (e: IOException) {
//                    pickerPromise?.reject("ERROR", e)
//                }
//            } else {
//                pickerPromise?.reject("NO_DATA", "No data found")
//            }
//        } else if (requestCode == IMAGE_PICKER_REQUEST) {
//            pickerPromise?.reject("CANCELLED", "User cancelled image picker")
//        }
//    }
//
//    override fun onNewIntent(intent: Intent?) {}
//
//    companion object {
//        private const val IMAGE_PICKER_REQUEST = 467081
//    }
//}
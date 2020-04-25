package com.example.corona.ui.upload

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.example.corona.ui.AsyncTaskCompleteListener
import com.example.corona.ui.MultiPartRequester
import java.util.HashMap
import androidx.fragment.app.FragmentActivity

class UploadImage(
    private val activity: FragmentActivity
):AsyncTaskCompleteListener {
    fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? =
            activity?.getContentResolver()?.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
    fun uploadImage(path: String,comment:String) {

        val map = HashMap<String, String>()
        map.put("url", "http://192.168.1.8:8000/api/casSignaler/show_list/")
        map.put("media", path)
        map.put("commentaire", comment)

        activity?.let { MultiPartRequester(it, map, this) }
    }

    override fun onTaskCompleted(response: String) {
        Toast.makeText(activity,"completed" , Toast.LENGTH_SHORT).show()
    }
}
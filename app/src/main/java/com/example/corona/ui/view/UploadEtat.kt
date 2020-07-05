package com.example.corona.ui.view

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.example.corona.ui.AsyncTaskCompleteListener
import com.example.corona.ui.MultiPartRequester
import java.util.HashMap
import androidx.fragment.app.FragmentActivity

class UploadEtat(
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
    fun uploadEtat(path: String,poids:String,temperature: String, rythmeCardiaque:String,date:String,url:String?) {

        val map = HashMap<String, String>()
        if(url!=null){
            map.put("url", url)
            map.put("media", path)
            map.put("date", date)
            map.put("poids", poids)
            map.put("temperature", temperature)
            map.put("rythmeCardiaque", rythmeCardiaque)

            activity?.let { MultiPartRequester(it, map, this) }}
    }

    override fun onTaskCompleted(response: String) {
        Toast.makeText(activity,"تم الارسال بنجاح سيتم دراسة معلوماتك الصحية " , Toast.LENGTH_SHORT).show()
    }
}
package com.example.corona.ui

import android.app.ActivityManager
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MIME
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.HttpConnectionParams
import org.apache.http.util.EntityUtils
import java.io.File

class MultiPartRequester(
    private val activity: FragmentActivity, private val map: MutableMap<String, String>,
    asyncTaskCompleteListener: AsyncTaskCompleteListener
) {
    private var mAsynclistener: AsyncTaskCompleteListener? = null
    private var httpclient: HttpClient? = null
    private var request: AsyncHttpRequest? = null

    init {

        // is Internet Connection Available...


        mAsynclistener = asyncTaskCompleteListener as AsyncTaskCompleteListener
        request = AsyncHttpRequest().execute(map["url"]) as AsyncHttpRequest


    }

    @Suppress("DEPRECATION")
    internal inner class AsyncHttpRequest : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg urls: String): String? {
            map.remove("url")
            try {

                val httppost = HttpPost(urls[0])
                httpclient = DefaultHttpClient()

                HttpConnectionParams.setConnectionTimeout(
                    httpclient!!.getParams(), 600000
                )

                val builder = MultipartEntityBuilder
                    .create()

                for (key in map.keys) {

                    if (key.equals("media", ignoreCase = true)) {
                        val f = File(map[key])

                        builder.addBinaryBody(
                            key, f,
                            ContentType.MULTIPART_FORM_DATA, f.getName()
                        )
                    } else {
                        builder.addTextBody(
                            key, map[key], ContentType
                                .create("text/plain", MIME.DEFAULT_CHARSET)
                        )
                    }
                    Log.d("TAG", key + "---->" + map[key])
                    // System.out.println(key + "---->" + map.get(key));
                }

                httppost.setEntity(builder.build())

                val manager = activity
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

                if (manager.memoryClass < 25) {
                    System.gc()
                }
                val response = httpclient!!.execute(httppost)

                return EntityUtils.toString(
                    response.getEntity(), "UTF-8"
                )

            } catch (e: Exception) {
                e.printStackTrace()
            } catch (oume: OutOfMemoryError) {
                System.gc()

                Toast.makeText(
                    activity.parent.parent,
                    "Run out of memory please colse the other background apps and try again!",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                if (httpclient != null)
                    httpclient!!.getConnectionManager().shutdown()

            }
            return null
        }

        override fun onPostExecute(response: String) {

            if (mAsynclistener != null) {
                mAsynclistener!!.onTaskCompleted(response)
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

}
package com.example.corona.ui.video


import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.Target
import com.example.corona.R


class VideoPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var media_container: FrameLayout
    var title: TextView
    var thumbnail: ImageView
    var volumeControl: ImageView
    var progressBar: ProgressBar
    var parent: View
    var requestManager: RequestManager? = null
    fun onBind(mediaObject: MediaObject, requestManager: RequestManager?) {
        this.requestManager = requestManager
        parent.setTag(this)
        title.text = mediaObject.title
        this.requestManager
            ?.load(mediaObject.thumbnail)
            ?.into(thumbnail)
    }

    init {
        parent = itemView
        media_container = itemView.findViewById(R.id.media_container)
        thumbnail = itemView.findViewById(R.id.thumbnail)
        title = itemView.findViewById(R.id.titlee)
        progressBar = itemView.findViewById(R.id.progressBar)
        volumeControl = itemView.findViewById(R.id.volume_control)
    }
}
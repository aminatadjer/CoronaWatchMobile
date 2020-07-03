package com.example.corona.ui.video


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.corona.R


class VideoPlayerRecyclerAdapter(
    private val mediaObjects: ArrayList<MediaObject>,
    private val requestManager: RequestManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return VideoPlayerViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.video_user_item,
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        (viewHolder as VideoPlayerViewHolder).onBind(mediaObjects[i], requestManager)
    }

    override fun getItemCount(): Int {
        return mediaObjects.size
    }

}
package com.example.corona.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R

import com.example.corona.ui.view.Video


class VideoAdapter(val context: Context) : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {
    private var videos: List<Video> = ArrayList()
    private var listener: OnItemClickListener? = null
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): VideoHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        return VideoHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: VideoHolder, position: Int) {
        val currentVideo: Video = videos[position]
        //set video title
        holder.videoTitleView.text = currentVideo.title
        holder.videoDescriptionView.text = currentVideo.description
        holder.videoDateView.text = currentVideo.date


        when(currentVideo.type){
            0->holder.videoIconView.setImageResource(R.drawable.ic_new)
            1->holder.videoIconView.setImageResource(R.drawable.ic_validate)
            2->holder.videoIconView.setImageResource(R.drawable.ic_danger)
        }



    }

    override fun getItemCount(): Int {
        return videos.size
    }

    fun setVideos(videos: List<Video>) {
        this.videos = videos
        notifyDataSetChanged()
    }


    inner class VideoHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val videoTitleView: TextView = itemView.findViewById(R.id.notif_title)
        internal val videoDescriptionView: TextView = itemView.findViewById(R.id.notif_description)
        internal val videoDateView: TextView = itemView.findViewById(R.id.notif_date)
        internal val videoIconView: ImageView = itemView.findViewById(R.id.notif_type)


    }

    interface OnItemClickListener {
        fun onItemClick(video: Video)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}
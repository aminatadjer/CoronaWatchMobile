package com.example.corona.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R

import com.example.corona.ui.notification.entity.Notif


class NotifAdapter(val context: Context) : RecyclerView.Adapter<NotifAdapter.VideoHolder>() {
    private var notifs: List<Notif> = ArrayList()
    private var listener: OnItemClickListener? = null
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): VideoHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        return VideoHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: VideoHolder, position: Int) {
        val currentNotif: Notif = notifs[position]
        //set video title
        holder.videoTitleView.text = currentNotif.titre
        holder.videoDescriptionView.text = currentNotif.description
        holder.videoDateView.text = currentNotif.date


        when(currentNotif.typeNotif){
            0->holder.videoIconView.setImageResource(R.drawable.ic_new)
            1->holder.videoIconView.setImageResource(R.drawable.ic_validate)
            2->holder.videoIconView.setImageResource(R.drawable.ic_danger)
        }



    }

    override fun getItemCount(): Int {
        return notifs.size
    }

    fun setVideos(notifs: List<Notif>) {
        this.notifs = notifs
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
        fun onItemClick(notif: Notif)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}
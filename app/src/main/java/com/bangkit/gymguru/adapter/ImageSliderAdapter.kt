package com.bangkit.gymguru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.data.ImageData

class ImageSliderAdapter(private val imageDatas: List<ImageData>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    inner class ImageSliderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val appName = view.findViewById<TextView>(R.id.app_name)
        private val textTitle = view.findViewById<TextView>(R.id.tv_title)
        private val imageIcon = view.findViewById<ImageView>(R.id.imageSlideIcon)

        fun bind(imageData: ImageData){
            appName.text = imageData.appName
            textTitle.text = imageData.title
            imageIcon.setImageResource(imageData.imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        return ImageSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_slide,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageDatas.size
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bind(imageDatas[position])
    }

}

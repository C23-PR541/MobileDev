package com.bangkit.gymguru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.data.ImageData
import com.bangkit.gymguru.databinding.ItemSlideBinding

class ImageSliderAdapter(private val imageDatas: List<ImageData>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    inner class ImageSliderViewHolder(private val binding: ItemSlideBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(imageData: ImageData){
            binding.apply {
                binding.appName.text = imageData.appName
                binding.tvTitle.text = imageData.title
                binding.imageSlideIcon.setImageResource(imageData.imageUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val binding = ItemSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSliderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageDatas.size
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bind(imageDatas[position])
    }

}

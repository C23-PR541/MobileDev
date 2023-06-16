package com.bangkit.gymguru.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.gymguru.R
import com.bangkit.gymguru.adapter.ImageSliderAdapter
import com.bangkit.gymguru.data.ImageData
import com.bangkit.gymguru.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageSliderAdapter = ImageSliderAdapter(
            listOf(
                ImageData(
                    getString(R.string.app_name),
                    getString(R.string.intro_one_desc),
                    R.drawable.intro1
                ),
                ImageData(
                    getString(R.string.app_name),
                    getString(R.string.intro_two_desc),
                    R.drawable.intro2
                ),
                ImageData(
                    getString(R.string.app_name),
                    getString(R.string.intro_three_desc),
                    R.drawable.intro3
                )
            )
        )

        val introSliderViewPager: ViewPager2 = binding.introSliderViewPager
        introSliderViewPager.adapter = imageSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(object:
        ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        val tvNext: TextView = binding.tvNext
        tvNext.setOnClickListener{
            if(introSliderViewPager.currentItem + 1 < imageSliderAdapter.itemCount){
                introSliderViewPager.currentItem += 1
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        val tvPrev: TextView = binding.tvPrev
        tvPrev.setOnClickListener {
            if (introSliderViewPager.currentItem > 0) {
                introSliderViewPager.currentItem -= 1
            } else {
                startActivity(Intent(this, UnlockActivity::class.java))
            }
        }
        val tvSkip: TextView = binding.tvSkip
        tvSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupIndicators(){
        val dotsIndicator: LinearLayout = binding.dotsIndicator
        val indicators = arrayOfNulls<ImageView>(imageSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            dotsIndicator.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int){
        val dotsIndicator: LinearLayout = binding.dotsIndicator
        val childCount = dotsIndicator.childCount
        for (i in 0 until childCount){
            val imageView = dotsIndicator[i] as ImageView
            if (i == index){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }
}
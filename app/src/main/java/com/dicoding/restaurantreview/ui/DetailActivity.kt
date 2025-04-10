package com.dicoding.restaurantreview.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.data.response.ListEventsItem
import com.dicoding.restaurantreview.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel:DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getStringExtra("EVENT_ID") ?: ""
        if (eventId.isNotEmpty()) {
            viewModel.fetchDetailEvent(eventId)
        } else {
            showError("Invalid Event ID")
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(this) { event ->
            if (event != null) {
                showEventDetail(event)
            } else {
                showError("Event tidak ditemukan")
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            showError(errorMessage)

        }
    }
    private fun showEventDetail(event: ListEventsItem) {
        Log.d("EventDetail", "Menampilkan event: ${event.name}")

        binding.tvEventName.text = event.name
        binding.tvOwnerName.text = event.ownerName
        binding.tvEventDate.text = event.beginTime
        binding.tvEventLocation.text = event.cityName
        binding.tvEventQuota.text =
            ("Sisa Kuota:  ${(event.quota ?: 0) - (event.registrants ?: 0)}")
        binding.tvEventDescription.text = HtmlCompat.fromHtml(event.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivEventImage)

        binding.btnRegist.setOnClickListener {
            val url = event.link
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
        binding.tvEventName.text = message
        binding.tvEventLocation.visibility = View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
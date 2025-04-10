package com.dicoding.restaurantreview.ui.ui.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.data.response.EventResponse
import com.dicoding.restaurantreview.data.response.ListEventsItem
import com.dicoding.restaurantreview.data.retrofit.ApiConfig
import com.dicoding.restaurantreview.databinding.FragmentDashboardBinding
import com.dicoding.restaurantreview.databinding.FragmentHomeBinding
import com.dicoding.restaurantreview.ui.DetailActivity
import com.dicoding.restaurantreview.ui.ReviewAdapter
import com.dicoding.restaurantreview.ui.ui.dashboard.DashboardViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        findRestaurant(1)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        findRestaurant(1)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findRestaurant(active: Int) {
        showLoading(true)
        val client = ApiConfig.getApiService().getEvent(active)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.listEvents
                    if (!responseBody.isNullOrEmpty()) {
                        setRestaurantData(responseBody)
                        binding.animationView.visibility = View.GONE
                        binding.rvReview.visibility = View.VISIBLE
                    } else {
                        binding.animationView.visibility = View.VISIBLE
                        binding.rvReview.visibility = View.GONE
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                binding.animationView.visibility = View.VISIBLE
                binding.rvReview.visibility = View.GONE
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    private fun setRestaurantData(event: List<ListEventsItem>) {
        val eventAdapter = ReviewAdapter(event){
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", it.id.toString())
            startActivity(intent)
        }
        binding.rvReview.adapter = eventAdapter
        binding.rvReview.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
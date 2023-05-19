package com.dicoding.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        if (username != null) {
            viewModel.setUserDetail(username)
            showLoading(true)
        }
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = StringBuilder().append(it.followers).append(" Followers")
                    tvFollowing.text = StringBuilder().append(it.following).append(" Following")
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .into(ivProfile)
                }
                showLoading(false)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if(count != null){
                    isChecked = if (count>0){
                        binding.fabFav.setImageResource(R.drawable.ic_favorite_24)
                        true
                    } else {
                        binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24)
                        false
                    }
                }
            }
        }

        binding.fabFav.setOnClickListener {
            isChecked = !isChecked
            if(isChecked){
                if (username  != null && avatarUrl != null) {
                    binding.fabFav.setImageResource(R.drawable.ic_favorite_24)
                    viewModel.addToFavorite(username, id, avatarUrl)
                }
            } else {
                binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24)
                viewModel.removeFromFavorite(id)
            }
        }


        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }
}
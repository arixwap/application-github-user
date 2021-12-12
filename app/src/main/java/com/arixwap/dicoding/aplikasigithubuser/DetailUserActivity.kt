package com.arixwap.dicoding.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Profile Detail"

        showUserDetail()
    }

    private fun showUserDetail() {
        showLoading(true)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            val request = GithubApiConfig.getApiService().getUserDetail(username)
            request.enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val username = responseBody?.login
                        binding.nameDetailUser.text = username
                        binding.usernameDetailUser.text = username
                        if (responseBody?.name != null) {
                            binding.nameDetailUser.text = responseBody.name
                        }
                        if (responseBody?.location != null) {
                            binding.locationDetailUser.text = responseBody.location
                            binding.locationDetailUser.visibility = View.VISIBLE
                        }
                        if (responseBody?.company != null) {
                            binding.companyDetailUser.text = responseBody.company
                            binding.companyDetailUser.visibility = View.VISIBLE
                        }
                        if (responseBody?.publicRepos != null) {
                            binding.repositoryDetailUser.text = getString(R.string.num_repository, responseBody.publicRepos)
                            binding.repositoryDetailUser.visibility = View.VISIBLE
                        }
                        Glide.with(binding.root)
                            .load(responseBody?.avatarUrl)
                            .circleCrop()
                            .into(binding.imgDetailUser)
                    } else {
                        Toast.makeText(this@DetailUserActivity, response.toString(), Toast.LENGTH_SHORT).show()
                    }

                    // Display tab navigation
                    val sectionsPagerAdapter = UserDetailTabAdapter(this@DetailUserActivity, username)
                    val viewPager: ViewPager2 = binding.viewPagerDetailUser
                    viewPager.adapter = sectionsPagerAdapter
                    val tabs: TabLayout = binding.tabsDetailUser
                    TabLayoutMediator(tabs, viewPager) { tab, position ->
                        tab.text = TAB_TITLES[position]
                    }.attach()
                    supportActionBar?.elevation = 0f

                    showLoading(false)
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@DetailUserActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.imgDetailUser.visibility = View.GONE
            binding.nameDetailUser.visibility = View.GONE
            binding.usernameDetailUser.visibility = View.GONE
            binding.locationDetailUser.visibility = View.GONE
            binding.companyDetailUser.visibility = View.GONE
            binding.repositoryDetailUser.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.imgDetailUser.visibility = View.VISIBLE
            binding.nameDetailUser.visibility = View.VISIBLE
            binding.usernameDetailUser.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private val TAB_TITLES = arrayListOf(
            "Followers",
            "Following"
        )
    }
}

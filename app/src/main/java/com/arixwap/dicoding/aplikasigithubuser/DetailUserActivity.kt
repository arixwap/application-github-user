package com.arixwap.dicoding.aplikasigithubuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import kotlin.math.ln
import kotlin.math.pow

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
        var followers = 0
        var following = 0

        if (username != null) {
            val request = GithubApiConfig.getApiService().getUserDetail(username)
            request.enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        // Display information textview
                        binding.usernameDetailUser.text = username
                        if (responseBody?.name != null) {
                            // Display user full name
                            binding.nameDetailUser.text = responseBody.name
                        } else {
                            // Use username as user full name
                            binding.nameDetailUser.text = username
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
                            binding.repositoryDetailUser.text = getString(R.string.num_repository, prettyNumber(responseBody.publicRepos))
                            binding.repositoryDetailUser.visibility = View.VISIBLE
                        }

                        // Display user avatar
                        Glide.with(binding.root)
                            .load(responseBody?.avatarUrl)
                            .circleCrop()
                            .into(binding.imgDetailUser)

                        // Get follower & following count
                        if (responseBody?.followers != null) {
                            followers = responseBody.followers
                        }
                        if (responseBody?.following != null) {
                            following = responseBody.following
                        }
                    } else {
                        Toast.makeText(this@DetailUserActivity, response.toString(), Toast.LENGTH_SHORT).show()
                    }

                    // Display tab navigation
                    val sectionsPagerAdapter = UserDetailTabAdapter(this@DetailUserActivity, username)
                    val viewPager: ViewPager2 = binding.viewPagerDetailUser
                    viewPager.adapter = sectionsPagerAdapter
                    val tabs: TabLayout = binding.tabsDetailUser
                    TabLayoutMediator(tabs, viewPager) { tab, position ->
                        tab.text = when(position) {
                            0 -> getString(R.string.num_follower, prettyNumber(followers))
                            1 -> getString(R.string.num_following, prettyNumber(following))
                            else -> null
                        }
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

    private fun prettyNumber(count: Int): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        val format = DecimalFormat("0.#")
        val value: String = format.format(count / 1000.0.pow(exp.toDouble()))
        return String.format("%s%c", value, "kMBTPE"[exp - 1])
    }


    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}

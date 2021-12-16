package com.arixwap.dicoding.aplikasigithubuser

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import kotlin.math.ln
import kotlin.math.pow

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val githubApiModel : GithubApiModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.profile_detail)
        binding.run {
            nameDetailUser.visibility = View.GONE
            usernameDetailUser.visibility = View.GONE
            locationDetailUser.visibility = View.GONE
            companyDetailUser.visibility = View.GONE
            repositoryDetailUser.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        showLoading(true)

        githubApiModel.userDetail.observe(this, { userDetail ->
            showUserDetail(userDetail)
        })

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            githubApiModel.getUserDetail(username)
        }
    }

    private fun showUserDetail(user: UserResponse) {
        // Display basic information
        binding.run {
            usernameDetailUser.text = user.login
            nameDetailUser.text = user.name ?: user.login
            locationDetailUser.run {
                text = user.location
                if (user.location != null) visibility = View.VISIBLE
            }
            companyDetailUser.run {
                text = user.company
                if (user.company != null) visibility = View.VISIBLE
            }
            repositoryDetailUser.run {
                text = getString(R.string.num_repository, prettyNumber(user.publicRepos))
            }

            nameDetailUser.visibility = View.VISIBLE
            usernameDetailUser.visibility = View.VISIBLE
            repositoryDetailUser.visibility = View.VISIBLE
        }

        // Display user avatar
        Glide.with(binding.root)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.imgDetailUser)

        // Display tab navigation
        val sectionsPagerAdapter = UserDetailTabAdapter(this@DetailUserActivity, user.login)
        val viewPager: ViewPager2 = binding.viewPagerDetailUser
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabsDetailUser
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.num_follower, prettyNumber(user.followers))
                1 -> getString(R.string.num_following, prettyNumber(user.following))
                else -> null
            }
        }.attach()
        supportActionBar?.elevation = 0f

        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

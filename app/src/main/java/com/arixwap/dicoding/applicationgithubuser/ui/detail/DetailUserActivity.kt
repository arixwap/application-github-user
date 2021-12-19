package com.arixwap.dicoding.applicationgithubuser.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.arixwap.dicoding.applicationgithubuser.R
import com.arixwap.dicoding.applicationgithubuser.api.GithubApiModel
import com.arixwap.dicoding.applicationgithubuser.database.User
import com.arixwap.dicoding.applicationgithubuser.databinding.ActivityDetailUserBinding
import com.arixwap.dicoding.applicationgithubuser.helper.ViewModelFactory
import com.arixwap.dicoding.applicationgithubuser.ui.favorite.FavoriteViewModel
import com.arixwap.dicoding.applicationgithubuser.ui.main.dataStore
import com.arixwap.dicoding.applicationgithubuser.ui.setting.SettingPreferences
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import kotlin.math.ln
import kotlin.math.pow

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val githubApiModel: GithubApiModel by viewModels()
    private var isFavorite: Boolean = false

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
            btnFavorite.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        showLoading(true)
        val username = intent.getStringExtra(EXTRA_USERNAME)

        // Get user data from api
        githubApiModel.run {
            if (username != null) getUserDetail(username)

            userDetail.observe(this@DetailUserActivity, { userDetail ->
                showUserDetail(userDetail)
            })
        }

        // Check user favorite
        if (username != null) {
            favoriteViewModel = obtainFavoriteViewModel(this@DetailUserActivity)
            favoriteViewModel.findByUsername(username).observe(this, { user ->
                if (user == null) {
                    isFavorite = false
                    binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                } else {
                    isFavorite = true
                    binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            })
        }
    }

    private fun showUserDetail(user: User) {
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
                text = getString(R.string.num_repository, prettyNumber(user.publicRepos!!))
            }

            nameDetailUser.visibility = View.VISIBLE
            usernameDetailUser.visibility = View.VISIBLE
            repositoryDetailUser.visibility = View.VISIBLE
            btnFavorite.visibility = View.VISIBLE

            // Display user avatar
            Glide.with(root)
                .load(user.avatarUrl)
                .circleCrop()
                .into(imgDetailUser)
        }

        // Action button favorite
        binding.btnFavorite.setOnClickListener {
            if (!isFavorite) {
                // Insert to database
                favoriteViewModel.insert(user)
                Toast.makeText(this, R.string.favorite_added, Toast.LENGTH_SHORT).show()
            } else {
                // Remove from database
                favoriteViewModel.delete(user)
                Toast.makeText(this, R.string.favorite_removed, Toast.LENGTH_SHORT).show()
            }
        }

        // Display tab navigation
        val sectionsPagerAdapter = UserDetailTabAdapter(this@DetailUserActivity, user.login!!)
        val viewPager: ViewPager2 = binding.viewPagerDetailUser
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabsDetailUser
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.num_follower, prettyNumber(user.followers!!))
                1 -> getString(R.string.num_following, prettyNumber(user.following!!))
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

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}

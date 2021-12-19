package com.arixwap.dicoding.applicationgithubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arixwap.dicoding.applicationgithubuser.R
import com.arixwap.dicoding.applicationgithubuser.database.User
import com.arixwap.dicoding.applicationgithubuser.databinding.ActivityFavoriteBinding
import com.arixwap.dicoding.applicationgithubuser.helper.ListUserAdapter
import com.arixwap.dicoding.applicationgithubuser.helper.ViewModelFactory
import com.arixwap.dicoding.applicationgithubuser.ui.detail.DetailUserActivity
import com.arixwap.dicoding.applicationgithubuser.ui.main.dataStore
import com.arixwap.dicoding.applicationgithubuser.ui.setting.SettingPreferences

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        showLoading(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        val favoriteViewModel = obtainFavoriteViewModel(this@FavoriteActivity)
        favoriteViewModel.getAll().observe(this, { users ->
            if (users.count() > 0) {
                displayListUser(users)
            } else {
                binding.rvUsers.adapter = null
                setMessage(getString(R.string.user_not_found))
            }
            showLoading(false)
        })
    }

    private fun displayListUser(listUser: List<User>) {
        val listUserAdapter = ListUserAdapter(listUser)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                intentUserDetail(username)
            }
        })
    }

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun intentUserDetail(username: String) {
        val detailUserIntent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
        startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUsers.visibility = if (isLoading) View.GONE else View.VISIBLE

        if (isLoading) setMessage(null)
    }

    private fun setMessage(message: String?) {
        binding.textMessage.run {
            visibility = if (message != "") View.VISIBLE else View.GONE
            text = message ?: ""
        }
    }
}

package com.arixwap.dicoding.aplikasigithubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arixwap.dicoding.aplikasigithubuser.R
import com.arixwap.dicoding.aplikasigithubuser.api.GithubApiModel
import com.arixwap.dicoding.aplikasigithubuser.database.User
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityMainBinding
import com.arixwap.dicoding.aplikasigithubuser.helper.ListUserAdapter
import com.arixwap.dicoding.aplikasigithubuser.helper.ViewModelFactory
import com.arixwap.dicoding.aplikasigithubuser.ui.detail.DetailUserActivity
import com.arixwap.dicoding.aplikasigithubuser.ui.favorite.FavoriteActivity
import com.arixwap.dicoding.aplikasigithubuser.ui.setting.SettingActivity
import com.arixwap.dicoding.aplikasigithubuser.ui.setting.SettingPreferences
import com.arixwap.dicoding.aplikasigithubuser.ui.setting.SettingViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val githubApiModel : GithubApiModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        // Set theme dark mode
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(this.application, pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this, { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        githubApiModel.isLoading.observe(this, {
            showLoading(it)
        })

        githubApiModel.listUser.observe(this, {
            if (it.count() > 0) {
                displayListUser(it)
            } else {
                binding.rvUsers.adapter = null
                showLoading(false)
                setMessage(getString(R.string.failed_get_data))
            }
        })

        githubApiModel.searchUser.observe(this, {
            if (it.totalCount > 0) {
                displayListUser(it.items)
            } else {
                binding.rvUsers.adapter = null
                showLoading(false)
                setMessage(getString(R.string.user_not_found))
            }
        })

        githubApiModel.getUserList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.button_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Method run on search submit
            override fun onQueryTextSubmit(search: String): Boolean {
                githubApiModel.searchUser(search)
                return true
            }
            // Method run on text changed
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_item_favorites -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return true
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

    private fun intentUserDetail(username: String) {
         val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
         detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
         startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUsers.visibility = if (isLoading) View.GONE else View.VISIBLE

        if (isLoading) setMessage(null)
    }

    private fun setMessage(message: String?) {
        binding.textMainMessage.run {
            visibility = if (message != "") View.VISIBLE else View.GONE
            text = message ?: ""
        }
    }
}

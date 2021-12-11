package com.arixwap.dicoding.aplikasigithubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        displayListUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Method run on search submit
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUser(query)
                return true
            }
            // Method run on text changed
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun displayListUser() {
        showLoading(true)
        val request = GithubApiConfig.getApiService().getUserList()
        request.enqueue(object : Callback<List<ListUserResponse>> {
            override fun onResponse(call: Call<List<ListUserResponse>>, response: Response<List<ListUserResponse>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null ) {
                        val listUserAdapter = ListUserAdapter(responseBody)
                        binding.rvUsers.adapter = listUserAdapter

                        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(username: String) {
                                intentUserDetail(username)
                            }
                        })
                    } else {
                        binding.rvUsers.adapter = null
                        showMessage(getString(R.string.user_not_found))
                    }
                } else {
                    showMessage(response.toString())
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<List<ListUserResponse>>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchUser(searchQuery: String) {
        showLoading(true)
        val request = GithubApiConfig.getApiService().searchUser(searchQuery)
        request.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(call: Call<SearchUserResponse>, response: Response<SearchUserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.totalCount!! > 0) {
                        val listUserAdapter = ListUserAdapter(responseBody.items)
                        binding.rvUsers.adapter = listUserAdapter

                        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(username: String) {
                                intentUserDetail(username)
                            }
                        })
                    } else {
                        binding.rvUsers.adapter = null
                        showMessage(getString(R.string.user_not_found))
                    }
                } else {
                    showMessage(response.toString())
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun intentUserDetail(username: String) {
         val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
         detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
         startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvUsers.visibility = View.GONE
            showMessage(null)
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvUsers.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String? = "") {
        if (message != null) {
            binding.textMainMessage.visibility = View.VISIBLE
            binding.textMainMessage.text = message
            showLoading(false)
        } else {
            binding.textMainMessage.visibility = View.GONE
            binding.textMainMessage.text = ""
        }
    }
}

package com.arixwap.dicoding.aplikasigithubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvUsers: RecyclerView
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUsers = findViewById(R.id.rv_users)
        rvUsers.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecycleList()
    }

    private val listUsers: ArrayList<User> get() {
        val name = resources.getStringArray(R.array.name)
        val username = resources.getStringArray(R.array.username)
        val location = resources.getStringArray(R.array.location)
        val company = resources.getStringArray(R.array.company)
        val repository = resources.getStringArray(R.array.repository)
        val follower = resources.getStringArray(R.array.followers)
        val following = resources.getStringArray(R.array.following)
        val avatar = resources.obtainTypedArray(R.array.avatar)

        val listUser = ArrayList<User>()

        for (i in name.indices) {
            val user = User(
                name = name[i],
                username = username[i],
                location = location[i],
                repository = repository[i].toInt(),
                company = company[i],
                follower = follower[i].toInt(),
                following = following[i].toInt(),
                avatar = avatar.getResourceId(i, -1)
            )
            listUser.add(user)
        }

        return listUser
    }

    private fun showRecycleList() {
        val listUserAdapter = ListUserAdapter(list)
        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showUserDetail(data)
            }
        })
    }

    private fun showUserDetail(user: User) {
        val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(detailUserIntent)
    }
}

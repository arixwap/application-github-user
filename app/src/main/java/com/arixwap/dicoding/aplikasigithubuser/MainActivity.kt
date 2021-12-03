package com.arixwap.dicoding.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityMainBinding

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
        val repository = resources.getIntArray(R.array.repository)
        val follower = resources.getIntArray(R.array.followers)
        val following = resources.getIntArray(R.array.following)
        val avatar = resources.obtainTypedArray(R.array.avatar)

        val listUser = ArrayList<User>()

        for (i in name.indices) {
            val user = User(
                name = name[i],
                username = username[i],
                location = location[i],
                repository = repository[i],
                company = company[i],
                follower = follower[i],
                following = following[i],
                avatar = avatar.getResourceId(i, -1)
            )
            listUser.add(user)
        }

        return listUser
    }

    private fun showRecycleList() {
        rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter
    }
}

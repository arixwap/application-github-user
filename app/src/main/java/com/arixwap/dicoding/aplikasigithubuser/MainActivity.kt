package com.arixwap.dicoding.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var rvUsers : RecyclerView
    private val list = ArrayList<User>()

    private val listUsers: ArrayList<User> get() {
        val listUser = ArrayList<User>()
        val name = resources.getStringArray(R.array.name)
        val username = resources.getStringArray(R.array.username)
        val location = resources.getStringArray(R.array.location)
        val company = resources.getStringArray(R.array.company)
        val repository = resources.getStringArray(R.array.repository)
        val follower = resources.getStringArray(R.array.followers)
        val following = resources.getStringArray(R.array.following)
        val avatar = resources.getStringArray(R.array.avatar)

        for (i in name.indices) {
            val user = User(username = name[i], name = name[i]) // WIP
            listUser.add(user)
        }

        return listUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        binding.rvUsers.setHasFixedSize(true)
        list.addAll(listUsers)
    }
}

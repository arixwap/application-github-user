package com.arixwap.dicoding.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        binding.nameDetailUser.text = user.name
        binding.usernameDetailUser.text = "@${user.username}"
        binding.locationDetailUser.text = user.location
        binding.companyDetailUser.text = user.company
        binding.followerDetailUser.text = "${user.follower.toString()} followers"
        binding.followingDetailUser.text = "${user.following.toString()} following"
        binding.repositoryDetailUser.text = "${user.repository.toString()} repositories"

        Glide.with(binding.root)
            .load(user.avatar)
            .circleCrop()
            .into(binding.imgDetailUser)
    }
}

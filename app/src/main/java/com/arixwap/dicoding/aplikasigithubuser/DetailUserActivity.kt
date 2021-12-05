package com.arixwap.dicoding.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arixwap.dicoding.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        binding.nameDetailUser.text = user.name
        binding.usernameDetailUser.text = getString(R.string.at_username, user.username)
        binding.locationDetailUser.text = user.location
        binding.companyDetailUser.text = user.company
        binding.followerDetailUser.text = getString(R.string.num_follower, user.follower)
        binding.followingDetailUser.text = getString(R.string.num_following, user.following)
        binding.repositoryDetailUser.text = getString(R.string.num_repository, user.repository)

        Glide.with(binding.root)
            .load(user.avatar)
            .circleCrop()
            .into(binding.imgDetailUser)
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}

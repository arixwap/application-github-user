package com.arixwap.dicoding.applicationgithubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arixwap.dicoding.applicationgithubuser.database.User
import com.arixwap.dicoding.applicationgithubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(user: User) {
        mFavoriteRepository.insert(user)
    }

    fun delete(user: User) {
        mFavoriteRepository.delete(user)
    }

    fun findByUsername(username: String): LiveData<User> =
        mFavoriteRepository.findByUsername(username)

    fun getAll(): LiveData<List<User>> = mFavoriteRepository.getAll()
}

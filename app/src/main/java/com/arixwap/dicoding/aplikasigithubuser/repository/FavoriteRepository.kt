package com.arixwap.dicoding.aplikasigithubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.arixwap.dicoding.aplikasigithubuser.database.FavoriteDao
import com.arixwap.dicoding.aplikasigithubuser.database.FavoriteRoomDatabase
import com.arixwap.dicoding.aplikasigithubuser.database.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun insert(user: User) {
        executorService.execute { mFavoritesDao.insert(user) }
    }

    fun delete(user: User) {
        executorService.execute { mFavoritesDao.delete(user) }
    }

    fun findByUsername(username: String): LiveData<User> = mFavoritesDao.findByUsername(username)

    fun getAll(): LiveData<List<User>> = mFavoritesDao.getAll()
}

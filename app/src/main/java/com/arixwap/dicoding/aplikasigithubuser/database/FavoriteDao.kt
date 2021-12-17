package com.arixwap.dicoding.aplikasigithubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user WHERE login = :username LIMIT 1")
    fun findByUsername(username: String): LiveData<User>

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAll(): LiveData<List<User>>
}

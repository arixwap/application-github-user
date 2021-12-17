package com.arixwap.dicoding.aplikasigithubuser.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arixwap.dicoding.aplikasigithubuser.database.SearchUserResponse
import com.arixwap.dicoding.aplikasigithubuser.database.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubApiModel : ViewModel() {
    private lateinit var mirrorListUser: List<User>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser

    private val _searchUser = MutableLiveData<SearchUserResponse>()
    val searchUser: LiveData<SearchUserResponse> = _searchUser

    private val _userDetail = MutableLiveData<User>()
    val userDetail: LiveData<User> = _userDetail

    private val _listFollower = MutableLiveData<List<User>>()
    val listFollower: LiveData<List<User>> = _listFollower

    private val _listFollowing = MutableLiveData<List<User>>()
    val listFollowing: LiveData<List<User>> = _listFollowing

    private fun fetchUserDetail(username: String, type: String) {
        val client = GithubApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val userDetail = response.body()
                    mirrorListUser.forEach {
                        if (it.id == userDetail?.id) {
                            it.name = userDetail.name
                            it.location = userDetail.location
                            it.company = userDetail.company
                        }
                    }

                    when (type) {
                        "list" -> _listUser.value = mirrorListUser
                        "search" -> _searchUser.value?.items = mirrorListUser
                        "follower" -> _listFollower.value = mirrorListUser
                        "following" -> _listFollowing.value = mirrorListUser
                    }
                } else {
                    Log.e(TAG, "fun fetchUserDetail() response.isSuccessful on FALSE: ${response.message()}")
                }

                _isLoading.value = false
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "fun fetchUserDetail() onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserList() {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().getUserList()
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful ) {
                    _listUser.value = response.body()
                    mirrorListUser = _listUser.value!!
                    mirrorListUser.forEach { fetchUserDetail(it.login!!, "list") }

                } else {
                    Log.e(TAG, "fun getUserList() response.isSuccessful on FALSE: ${response.message()}")
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e(TAG, "fun getUserList() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    fun searchUser(search: String) {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().searchUser(search)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(call: Call<SearchUserResponse>, response: Response<SearchUserResponse>) {
                if (response.isSuccessful) {
                    _searchUser.value = response.body()
                    mirrorListUser = _searchUser.value!!.items
                    mirrorListUser.forEach { fetchUserDetail(it.login!!, "search") }
                } else {
                    Log.e(TAG, "fun searchUser() response.isSuccessful on FALSE: ${response.message()}")
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                Log.e(TAG, "fun searchUser() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "fun getUserDetail() response.isSuccessful on FALSE: ${response.message()}")
                }
                _isLoading.value = false
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "fun getUserDetail() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    fun getUserFollower(username: String) {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().getUserFollower(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    _listFollower.value = response.body()
                    mirrorListUser = _listFollower.value!!
                    mirrorListUser.forEach { fetchUserDetail(it.login!!, "follower") }
                } else {
                    Log.e(TAG, "fun getUserFollower() response.isSuccessful on FALSE: ${response.message()}")
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e(TAG, "fun getUserFollower() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                    mirrorListUser = _listFollowing.value!!
                    mirrorListUser.forEach { fetchUserDetail(it.login!!, "following") }
                } else {
                    Log.e(TAG, "fun getUserFollowing() response.isSuccessful on FALSE: ${response.message()}")
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e(TAG, "fun getUserFollowing() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "GithubApiModel"
    }
}

package com.arixwap.dicoding.aplikasigithubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubApiModel : ViewModel() {
    private lateinit var mirrorListUser: List<UserResponse>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUser = MutableLiveData<List<UserResponse>>()
    val listUser: LiveData<List<UserResponse>> = _listUser

    private val _searchUser = MutableLiveData<SearchUserResponse>()
    val searchUser: LiveData<SearchUserResponse> = _searchUser

    private val _userDetail = MutableLiveData<UserResponse>()
    val userDetail: LiveData<UserResponse> = _userDetail

    private val _listFollower = MutableLiveData<List<UserResponse>>()
    val listFollower: LiveData<List<UserResponse>> = _listFollower

    private val _listFollowing = MutableLiveData<List<UserResponse>>()
    val listFollowing: LiveData<List<UserResponse>> = _listFollowing

    private fun fetchUserDetail(username: String, type: String) {
        val client = GithubApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
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
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "fun fetchUserDetail() onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserList() {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().getUserList()
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                if (response.isSuccessful ) {
                    _listUser.value = response.body()
                    mirrorListUser = _listUser.value!!
                    mirrorListUser.forEach { fetchUserDetail(it.login, "list") }

                } else {
                    Log.e(TAG, "fun getUserList() response.isSuccessful on FALSE: ${response.message()}")
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
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
                    mirrorListUser.forEach { fetchUserDetail(it.login, "search") }
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
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "fun getUserDetail() response.isSuccessful on FALSE: ${response.message()}")
                }
                _isLoading.value = false
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "fun getUserDetail() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    fun getUserFollower(username: String) {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().getUserFollower(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                if (response.isSuccessful) {
                    _listFollower.value = response.body()
                    mirrorListUser = _listFollower.value!!
                    mirrorListUser.forEach { fetchUserDetail(it.login, "follower") }
                } else {
                    Log.e(TAG, "fun getUserFollower() response.isSuccessful on FALSE: ${response.message()}")
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "fun getUserFollower() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = GithubApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                    mirrorListUser = _listFollowing.value!!
                    mirrorListUser.forEach { fetchUserDetail(it.login, "following") }
                } else {
                    Log.e(TAG, "fun getUserFollowing() response.isSuccessful on FALSE: ${response.message()}")
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "fun getUserFollowing() onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "GithubApiModel"
    }
}

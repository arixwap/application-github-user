package com.arixwap.dicoding.aplikasigithubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String,
    var username: String,
    var location: String,
    var repository: Int,
    var company: String,
    var follower: Int,
    var following: Int,
    var avatar: String
) : Parcelable

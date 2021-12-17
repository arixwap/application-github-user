package com.arixwap.dicoding.aplikasigithubuser.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchUserResponse(
	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
    var items: List<User>
)

@Entity
@Parcelize
data class User(
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "id")
	@field:SerializedName("id")
	var id: Int = 0,

	@ColumnInfo(name = "node_id")
	@field:SerializedName("node_id")
	var nodeId: String? = null,

	@ColumnInfo(name = "login")
	@field:SerializedName("login")
	var login: String? = null,

	@ColumnInfo(name = "name")
	@field:SerializedName("name")
	var name: String? = null,

	@ColumnInfo(name = "email")
	@field:SerializedName("email")
	var email: String? = null,

	@ColumnInfo(name = "bio")
	@field:SerializedName("bio")
	var bio: String? = null,

	@ColumnInfo(name = "url")
	@field:SerializedName("url")
	var url: String? = null,

	@ColumnInfo(name = "location")
	@field:SerializedName("location")
	var location: String? = null,

	@ColumnInfo(name = "company")
	@field:SerializedName("company")
	var company: String? = null,

	@field:SerializedName("public_repos")
	var publicRepos: Int? = 0,

	@ColumnInfo(name = "followers")
	@field:SerializedName("followers")
	var followers: Int? = 0,

	@ColumnInfo(name = "following")
	@field:SerializedName("following")
	var following: Int? = 0,

	@ColumnInfo(name = "avatar_url")
	@field:SerializedName("avatar_url")
	var avatarUrl: String? = null
) : Parcelable

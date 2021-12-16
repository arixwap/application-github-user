package com.arixwap.dicoding.aplikasigithubuser

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
    var items: List<UserResponse>
)

data class UserResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("node_id")
	val nodeId: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("name")
	var name: String?,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("bio")
	val bio: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("location")
	var location: String?,

	@field:SerializedName("company")
	var company: String?,

	@field:SerializedName("public_repos")
	val publicRepos: Int,

	@field:SerializedName("public_gists")
	val publicGists: Int,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("updated_at")
	val updatedAt: String
)

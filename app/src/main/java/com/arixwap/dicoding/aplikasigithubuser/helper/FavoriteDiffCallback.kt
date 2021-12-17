//package com.arixwap.dicoding.aplikasigithubuser.helper
//
//import androidx.recyclerview.widget.DiffUtil
//import com.arixwap.dicoding.aplikasigithubuser.api.User
//
//class FavoriteDiffCallback(private val mOldFavoriteList: List<User>, private val mNewFavoriteList: List<User>) : DiffUtil.Callback() {
//    override fun getOldListSize(): Int {
//        return mOldFavoriteList.size
//    }
//
//    override fun getNewListSize(): Int {
//        return mNewFavoriteList.size
//    }
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return mOldFavoriteList[oldItemPosition].id == mNewFavoriteList[newItemPosition].id
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        val oldFavorite = mOldFavoriteList[oldItemPosition]
//        val newFavorite = mNewFavoriteList[newItemPosition]
//        return oldFavorite.login == newFavorite.login && oldFavorite.avatarUrl == newFavorite.avatarUrl
//    }
//}

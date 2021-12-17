package com.arixwap.dicoding.aplikasigithubuser.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arixwap.dicoding.aplikasigithubuser.database.User
import com.arixwap.dicoding.aplikasigithubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide

class ListUserAdapter(private val users: List<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        // Set user list information
        holder.binding.run {
            usernameItemUser.text = users[position].login
            nameItemUser.text = users[position].name ?: users[position].login

            locationItemUser.text = users[position].location
            locationItemUser.visibility = if (users[position].location == null) View.GONE else View.VISIBLE

            companyItemUser.text = users[position].company
            companyItemUser.visibility = if (users[position].company == null) View.GONE else View.VISIBLE
        }

        // Set user list image
        Glide.with(holder.itemView.context)
            .load(users[position].avatarUrl)
            .circleCrop()
            .into(holder.binding.imgItemUser)

        // Set action callback on image click
        holder.binding.imgItemUser.setOnClickListener {
            onItemClickCallback.onItemClicked(users[position].login!!)
        }
    }

    override fun getItemCount(): Int = users.size

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}

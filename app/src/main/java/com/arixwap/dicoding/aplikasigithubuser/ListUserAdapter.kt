package com.arixwap.dicoding.aplikasigithubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arixwap.dicoding.aplikasigithubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide

class ListUserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, location, repository, company, follower, following, avatar) = listUser[position]
        holder.binding.usernameItemUser.text = username
        holder.binding.nameItemUser.text = name
        holder.binding.locationItemUser.text = location
        holder.binding.companyItemUser.text = company
        // holder.binding.imgItemUser.setImageResource(avatar)

        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgItemUser)
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)
}

package com.arixwap.dicoding.aplikasigithubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arixwap.dicoding.aplikasigithubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide

class ListUserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, username, location, _, company, _, _, avatar) = listUser[position]
        holder.binding.usernameItemUser.text = holder.itemView.context.resources.getString(R.string.at_username, username)
        holder.binding.nameItemUser.text = name
        holder.binding.locationItemUser.text = location
        holder.binding.companyItemUser.text = company
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgItemUser)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}

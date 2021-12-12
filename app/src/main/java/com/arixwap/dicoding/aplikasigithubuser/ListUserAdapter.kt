package com.arixwap.dicoding.aplikasigithubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arixwap.dicoding.aplikasigithubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUserAdapter(private val listUser: List<ListUserResponse>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val username = listUser[position].login
        val avatar = listUser[position].avatarUrl

        // Display basic data
        holder.binding.nameItemUser.text = username
        holder.binding.usernameItemUser.text = username
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgItemUser)
        holder.binding.imgItemUser.setOnClickListener {
            onItemClickCallback.onItemClicked(username)
        }

        holder.binding.linearProgressBar.visibility = View.VISIBLE
        holder.binding.nameItemUser.visibility = View.GONE
        holder.binding.usernameItemUser.visibility = View.GONE
        holder.binding.locationItemUser.visibility = View.GONE
        holder.binding.companyItemUser.visibility = View.GONE

        val request = GithubApiConfig.getApiService().getUserDetail(username)
        request.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    holder.binding.nameItemUser.visibility = View.VISIBLE
                    holder.binding.usernameItemUser.visibility = View.VISIBLE
                    if (responseBody?.name != null) {
                        holder.binding.nameItemUser.text = responseBody.name
                    }
                    if (responseBody?.location != null) {
                        holder.binding.locationItemUser.text = responseBody.location
                        holder.binding.locationItemUser.visibility = View.VISIBLE
                    }
                    if (responseBody?.company != null) {
                        holder.binding.companyItemUser.text = responseBody.company
                        holder.binding.companyItemUser.visibility = View.VISIBLE
                    }
                }
                holder.binding.linearProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {}
        })
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}

package com.arixwap.dicoding.aplikasigithubuser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arixwap.dicoding.aplikasigithubuser.databinding.FragmentFollowingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment(private var username: String) : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowingBinding.inflate(inflater)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowing.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE
        binding.textMessage.visibility = View.GONE

        val request = GithubApiConfig.getApiService().getUserFollowing(username)
        request.enqueue(object : Callback<List<ListUserResponse>> {
            override fun onResponse(call: Call<List<ListUserResponse>>, response: Response<List<ListUserResponse>>) {
                var isSuccess = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.isNotEmpty() == true) {
                        val listUserAdapter = ListUserAdapter(responseBody)
                        binding.rvFollowing.adapter = listUserAdapter

                        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(username: String) {
                                intentUserDetail(username)
                            }
                        })

                        isSuccess = true
                    }
                }

                if ( ! isSuccess ) {
                    binding.rvFollowing.visibility = View.GONE
                    binding.textMessage.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<ListUserResponse>>, t: Throwable) {}
        })
    }

    private fun intentUserDetail(username: String) {
        val detailUserIntent = Intent(activity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
        startActivity(detailUserIntent)
    }
}

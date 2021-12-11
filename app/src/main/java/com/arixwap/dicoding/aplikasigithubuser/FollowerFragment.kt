package com.arixwap.dicoding.aplikasigithubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arixwap.dicoding.aplikasigithubuser.databinding.FragmentFollowerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment(private var username: String) : Fragment() {
    private lateinit var binding: FragmentFollowerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowerBinding.inflate(inflater)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollower.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollower.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE
        binding.textMessage.visibility = View.GONE

        val request = GithubApiConfig.getApiService().getUserFollower(username)
        request.enqueue(object : Callback<List<ListUserResponse>> {
            override fun onResponse(call: Call<List<ListUserResponse>>, response: Response<List<ListUserResponse>>) {
                var isSuccess = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.isNotEmpty() == true) {
                        val listUserAdapter = ListUserAdapter(listUser = responseBody, showButton = false)
                        binding.rvFollower.adapter = listUserAdapter
                        isSuccess = true
                    }
                }

                if ( ! isSuccess ) {
                    binding.rvFollower.visibility = View.GONE
                    binding.textMessage.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<ListUserResponse>>, t: Throwable) {}
        })
    }
}

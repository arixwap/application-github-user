package com.arixwap.dicoding.aplikasigithubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arixwap.dicoding.aplikasigithubuser.api.GithubApiModel
import com.arixwap.dicoding.aplikasigithubuser.database.User
import com.arixwap.dicoding.aplikasigithubuser.databinding.FragmentFollowingBinding
import com.arixwap.dicoding.aplikasigithubuser.helper.ListUserAdapter

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val githubApiModel: GithubApiModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        showLoading(false)
        binding.textMessage.visibility = View.GONE

        val username = arguments?.getString(USERNAME)
        githubApiModel.getUserFollowing(username!!)

        githubApiModel.listFollowing.observe(this, { users ->
            displayListUsers(users)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayListUsers(users: List<User>) {
        if (users.isNotEmpty()) {
            val listUserAdapter = ListUserAdapter(users)
            binding.rvUsers.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object :
                ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(username: String) {
                    intentUserDetail(username)
                }
            })
        } else {
            binding.textMessage.visibility = View.VISIBLE
        }

        showLoading(false)
    }

    private fun intentUserDetail(username: String) {
        val detailUserIntent = Intent(activity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
        startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String) = FollowingFragment().apply {
            arguments = Bundle().apply {
                putString(USERNAME, username)
            }
        }
    }
}

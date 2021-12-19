package com.arixwap.dicoding.applicationgithubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arixwap.dicoding.applicationgithubuser.api.GithubApiModel
import com.arixwap.dicoding.applicationgithubuser.database.User
import com.arixwap.dicoding.applicationgithubuser.databinding.FragmentFollowerBinding
import com.arixwap.dicoding.applicationgithubuser.helper.ListUserAdapter

class FollowerFragment : Fragment() {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private val githubApiModel: GithubApiModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textMessage.visibility = View.GONE

        val username = arguments?.getString(USERNAME)
        githubApiModel.getUserFollower(username!!)

        githubApiModel.listFollower.observe(this, { users ->
            displayListUsers(users)
        })

        githubApiModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
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
        fun newInstance(username: String) = FollowerFragment().apply {
            arguments = Bundle().apply {
                putString(USERNAME, username)
            }
        }
    }
}

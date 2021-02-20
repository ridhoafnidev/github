package com.dailyapps.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailyapps.githubuser.adapter.GithubAdapter
import com.dailyapps.githubuser.databinding.FragmentFollowingBinding
import com.dailyapps.githubuser.model.Github
import com.dailyapps.githubuser.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: GithubAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle: Bundle? = Bundle();
        bundle = this?.arguments

        val username: String = bundle?.get(FollowerFragment.EXTRA_DATA).toString()

        adapter = GithubAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        getListFollowing(username)

        adapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Github) {
                Toast.makeText(context, data.username, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getListFollowing(username: String?){
        username?.let { followingViewModel.setFollowing(it) }
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer {
            if (it != null){
                for (data in it){
                    getUserDetail(data.username)
                }
            }
        })
    }

    private fun getUserDetail(username: String) {
        followingViewModel.setDetailUserGithub(username)
        followingViewModel.getDetailUserGithub().observe(viewLifecycleOwner, Observer {
            if (it != null){
                adapter.setData(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

}
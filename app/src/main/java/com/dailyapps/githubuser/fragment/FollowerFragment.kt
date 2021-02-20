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
import com.dailyapps.githubuser.databinding.FragmentFollowerBinding
import com.dailyapps.githubuser.model.Github
import com.dailyapps.githubuser.viewmodel.FollowerViewModel

class FollowerFragment : Fragment() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var adapter: GithubAdapter
    private lateinit var binding: FragmentFollowerBinding
    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle: Bundle? = Bundle();
        bundle = this?.arguments

        val username: String = bundle?.get(EXTRA_DATA).toString()

        adapter = GithubAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFollower.adapter = adapter
        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)
        getListFollower(username)
        adapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Github) {
                Toast.makeText(context, data.username, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getListFollower(username: String?){
        username?.let { followerViewModel.setFollower(it) }
        followerViewModel.getFollower().observe(viewLifecycleOwner, Observer {
            if (it != null){
                for (data in it){
                    getUserDetail(data.username)
                }
            }
        })
    }

    private fun getUserDetail(username: String) {
        followerViewModel.setDetailUserGithub(username)
        followerViewModel.getDetailUserGithub().observe(viewLifecycleOwner, Observer {
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
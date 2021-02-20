package com.dailyapps.githubuser.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailyapps.githubuser.viewmodel.GithubViewModel
import com.dailyapps.githubuser.R
import com.dailyapps.githubuser.adapter.GithubAdapter
import com.dailyapps.githubuser.databinding.FragmentHomeBinding
import com.dailyapps.githubuser.model.Github

class HomeFragment : Fragment() {
    private lateinit var adapter: GithubAdapter
    private lateinit var binding: FragmentHomeBinding
    private var githubs = ArrayList<Github>()
    private lateinit var githubViewModel: GithubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setTitle("Home")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.rvGithub.setHasFixedSize(true)

        showLoading(true)

        adapter = GithubAdapter()
        adapter.notifyDataSetChanged()

        binding.rvGithub.layoutManager = LinearLayoutManager(activity)
        binding.rvGithub.adapter = adapter

        // ViewModel
        githubViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            GithubViewModel::class.java)

        getListGithub()

        adapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Github) {
                val toDetailGithubFragment =
                    HomeFragmentDirections.actionHomeToDetail(
                        data
                    )
                findNavController().navigate(toDetailGithubFragment)
            }
        })

    }

    private fun getListGithub() {
        githubViewModel.setLoginUserGithub()
        githubViewModel.getLoginUserGithub().observe(viewLifecycleOwner, Observer {
            if (it != null){
                for (data in it){
                    getUserByLogin(data.username)
                }
            }
        })
    }

    private fun getUserByLogin(login: String) {
        githubViewModel.setDetailUserGithub(login)
        githubViewModel.getDetailUserGithub().observe(viewLifecycleOwner, Observer {
            if (it != null){
                githubs = it
                adapter.setData(githubs)
                showLoading(false)
            }
        })
    }

    private fun setMode(itemId: Int) {
        when(itemId){
            R.id.action_search -> {
            }
            R.id.action_setting -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()){
                    return true
                }else{
                    githubs.clear()
                    searchUser(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun searchUser(query: String) {
        Toast.makeText(activity, query, Toast.LENGTH_SHORT).show()
        githubViewModel.setSearchUserGithub(query)
        githubViewModel.getSearchUserGethub().observe(viewLifecycleOwner, Observer {
            if (it != null){
                for (data in it){
                    getUserByLogin(data.username)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

}
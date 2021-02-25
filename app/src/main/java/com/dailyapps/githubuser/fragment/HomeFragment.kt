package com.dailyapps.githubuser.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailyapps.githubuser.R
import com.dailyapps.githubuser.adapter.GithubAdapter
import com.dailyapps.githubuser.databinding.FragmentHomeBinding
import com.dailyapps.githubuser.model.Github
import com.dailyapps.githubuser.viewmodel.GithubViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GithubAdapter
    private var githubs = ArrayList<Github>()
    private lateinit var githubViewModel: GithubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStatusBar()

        initAdapter()

        initRecyclerView()

        initViewModel()

        getListGithub()

        initActions()

    }

    private fun initActions() {
        adapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Github) {
                val toDetailGithubFragment = HomeFragmentDirections.actionHomeToDetail(data)
                findNavController().navigate(toDetailGithubFragment)
            }
        })
    }

    private fun initViewModel() {
        githubViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                GithubViewModel::class.java)
    }

    private fun initRecyclerView() {
        binding.rvGithub.setHasFixedSize(true)
        binding.rvGithub.layoutManager = LinearLayoutManager(activity)
        binding.rvGithub.adapter = adapter
    }

    private fun initAdapter() {
        adapter = GithubAdapter()
        adapter.notifyDataSetChanged()
    }

    private fun initStatusBar() {
        (activity as AppCompatActivity).supportActionBar?.setTitle("Home")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

    private fun getUserByLogin(login: String?) {
        binding.progressBar.visibility = View.VISIBLE
        githubViewModel.setDetailUserGithub(login)
        githubViewModel.getDetailUserGithub().observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.progressBar.visibility = View.GONE
                githubs = it
                adapter.setData(githubs)
            }
        })
    }

    private fun setMode(itemId: Int) {
        when(itemId){
            R.id.action_search -> {
            }
            R.id.action_setting -> {
                val toSettings = HomeFragmentDirections.actionHomeFragmentToNotificationFragment()
                findNavController().navigate(toSettings)
            }
            R.id.action_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_favorite -> {
                val toFragmentFavorite = HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                findNavController().navigate(toFragmentFavorite)
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
        binding.progressBar.visibility = View.VISIBLE
        binding.rvGithub.visibility = View.GONE
        githubViewModel.setSearchUserGithub(query)
        githubViewModel.getSearchUserGethub().observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.progressBar.visibility = View.GONE
                binding.rvGithub.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
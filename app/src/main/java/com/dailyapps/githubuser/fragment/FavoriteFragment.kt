package com.dailyapps.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailyapps.githubuser.R
import com.dailyapps.githubuser.adapter.GithubAdapter
import com.dailyapps.githubuser.databinding.FragmentFavoriteBinding
import com.dailyapps.githubuser.db.FavoriteHelper
import com.dailyapps.githubuser.model.Github
import com.dailyapps.githubuser.util.reObserve
import com.dailyapps.githubuser.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private var TAG: String = FavoriteFragment::class.java.simpleName

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GithubAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteHelper: FavoriteHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val observer = Observer<ArrayList<Github>> { github ->
        binding.progressBar.visibility = View.VISIBLE
        if (github != null){
            binding.progressBar.visibility = View.GONE
            adapter.setData(github)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.favorite_user))
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteHelper = FavoriteHelper.getInstance((activity as AppCompatActivity).applicationContext)
        favoriteHelper.open()

        binding.rvFavorite.setHasFixedSize(true)

        initRecyclerView()

        favoriteViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                FavoriteViewModel::class.java
        )

        favoriteViewModel.loadFavorite(context)

        adapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Github) {
                val toDetailFragment = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(data)
                findNavController().navigate(toDetailFragment)
            }
        })

        if (savedInstanceState == null) {
            favoriteViewModel.getFavorite().reObserve(viewLifecycleOwner, observer)
        } else {
            val list = savedInstanceState.getParcelableArrayList<Github>(EXTRA_STATE)
            if (list != null) {
                adapter.setData(list)
            }
        }

    }

    private fun initRecyclerView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(activity)
        binding.rvFavorite.setHasFixedSize(true)
        adapter = GithubAdapter()
        binding.rvFavorite.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.setting_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            val homeFragment = FavoriteFragmentDirections.actionFavoriteFragmentToHomeFragment()
            findNavController().navigate(homeFragment)
        }else if(item.itemId == R.id.action_setting){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.setData(ArrayList())
        favoriteViewModel.getFavorite().removeObserver(observer)
    }
}
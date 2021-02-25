package com.dailyapps.githubuser.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dailyapps.githubuser.R
import com.dailyapps.githubuser.adapter.SectionsPagerAdapter
import com.dailyapps.githubuser.databinding.FragmentDetailBinding
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.AVATAR
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.COMPANY
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.FAVORITE
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.FOLLOWERS
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.FOLLOWING
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.LOCATION
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.NAME
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.REPOSITORY
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.dailyapps.githubuser.model.Github
import com.dailyapps.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout

class DetailFragment : Fragment() {

    private lateinit var github :Github
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var isFavorite = false
    private lateinit var detailViewModel: DetailViewModel
    private var TAG: String = DetailFragment::class.java.simpleName
    private lateinit var uriWithId: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataArgument()
        initViewModel()
        getDetailUser()
    }

    private fun getDetailUser() {
        val btnFavorite: Int = R.drawable.ic_baseline_favorite
        val btnNotFavorite: Int = R.drawable.ic_baseline_favorite_border
        detailViewModel.setDetailUser(context, github.username)
        detailViewModel.getDetailUser().observe(viewLifecycleOwner, Observer {
            if (it.username != null){
                isFavorite = true
                binding.fabFavorite.setImageResource(btnFavorite)
            }else{
                isFavorite = false
                binding.fabFavorite.setImageResource(btnNotFavorite)
            }
        })

        binding.fabFavorite.setOnClickListener {
            if (isFavorite){
                Log.d(TAG, uriWithId.toString())
                context?.contentResolver?.delete(uriWithId, github.username, null)
                binding.fabFavorite.setImageResource(btnNotFavorite)
                Toast.makeText(context,  getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
                isFavorite = false
            }else{
                val username = github.username
                val name = github.name
                val avatar = github.avatar
                val company = github.company
                val location = github.location
                val repository = github.repository
                val followers = github.followers
                val following = github.following
                val favorite = true

                val contentValueFavorite = contentValuesOf(
                    USERNAME to username,
                    NAME to name,
                    AVATAR to avatar,
                    COMPANY to company,
                    LOCATION to location,
                    REPOSITORY to repository,
                    FOLLOWERS to followers,
                    FOLLOWING to following,
                    FAVORITE to favorite,
                )

                context?.contentResolver?.insert(CONTENT_URI, contentValueFavorite)

                isFavorite = true
                binding.fabFavorite.setImageResource(btnFavorite)

                Toast.makeText(context,  getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun initViewModel() {
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                DetailViewModel::class.java
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDataArgument() {
        arguments?.let {
            github = DetailFragmentArgs.fromBundle(it).github
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + github.username)
            (activity as AppCompatActivity).supportActionBar?.setTitle(github.name)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.txtCompany.text = github.company
            binding.txtUsername.text = github.username
            binding.txtFollowers.text = github.followers
            binding.txtFollowing.text = github.following
            binding.txtName.text = github.name
            binding.txtLocation.text = github.location
            "${github.repository} Repository".also {
                binding.txtRepository.text = it
            }
            activity?.applicationContext?.let { data ->
                Glide.with(data)
                    .load(github.avatar)
                    .apply(RequestOptions().override(55,55))
                    .into(binding.imgPhoto)
            }
        }

        val sectionPagerAdapter = activity?.let {
            SectionsPagerAdapter(
                    it,
                    childFragmentManager,
                    github.username
            )
        }

        val viewPager = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            val homeFragment =
                DetailFragmentDirections.actionDetailFragmentToHomeFragment()
            findNavController().navigate(homeFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}
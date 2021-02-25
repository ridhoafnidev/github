package com.dailyapps.consumenapp.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dailyapps.consumenapp.adapter.SectionsPagerAdapter
import com.dailyapps.consumenapp.databinding.FragmentDetailBinding
import com.dailyapps.consumenapp.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.dailyapps.consumenapp.model.Github
import com.google.android.material.tabs.TabLayout

class DetailFragment : Fragment() {

    private lateinit var github : Github
    private lateinit var binding: FragmentDetailBinding
    private lateinit var uriWithId: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataArgument()

    }

    override fun onDestroy() {
        super.onDestroy()
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
            val homeFragment = DetailFragmentDirections.actionDetailFragment2ToFavoriteFragment()
            findNavController().navigate(homeFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}
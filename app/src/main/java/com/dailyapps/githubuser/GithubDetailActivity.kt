package com.dailyapps.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dailyapps.githubuser.databinding.ActivityGithubDetailBinding

class GithubDetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_GITHUB = "extra_github"
    }

    private lateinit var binding: ActivityGithubDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGithubDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val github = intent.getParcelableExtra<Github>(EXTRA_GITHUB) as Github
        supportActionBar?.setTitle(github.name)
        binding.txtCompany.text = github.company
        binding.txtUsername.text = github.username
        binding.txtFollowers.text = github.follower
        binding.txtFollowing.text = github.following
        binding.txtName.text = github.name
        binding.txtLocation.text = github.location
        binding.txtRepository.text = "${github.repository} Repository"
        binding.imgPhoto.setImageResource(github.avatar)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId.equals(android.R.id.home)){
            finish();
        }
        return super.onOptionsItemSelected(item)
    }

}
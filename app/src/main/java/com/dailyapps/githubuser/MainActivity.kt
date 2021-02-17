package com.dailyapps.githubuser

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import com.dailyapps.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GithubAdapter
    private lateinit var dataName: Array<String>
    private lateinit var dataUsername: Array<String>
    private lateinit var dataPhoto: TypedArray
    private lateinit var dataCompany: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataFollower: Array<String>
    private lateinit var dataFollowing: Array<String>
    private var githubs = arrayListOf<Github>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listView = binding.lvList
        adapter = GithubAdapter(this)
        listView.adapter = adapter
        prepare()
        addItem()
        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            Toast.makeText(this@MainActivity, githubs[position].name, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, GithubDetailActivity::class.java)
            intent.putExtra(GithubDetailActivity.EXTRA_GITHUB, githubs[position])
            startActivity(intent)
        }
    }

    private fun addItem() {
        for (position in dataName.indices){
            val github = Github(
                dataUsername[position],
                dataName[position],
                dataPhoto.getResourceId(position, -1),
                dataCompany[position],
                dataLocation[position],
                dataRepository[position],
                dataFollower[position],
                dataFollowing[position],
            )
            githubs.add(github)
        }
        adapter.github = githubs
    }

    private fun prepare(){
        dataName = resources.getStringArray(R.array.data_name)
        dataUsername = resources.getStringArray(R.array.data_username)
        dataPhoto = resources.obtainTypedArray(R.array.data_avatar)
        dataCompany = resources.getStringArray(R.array.data_company)
        dataLocation = resources.getStringArray(R.array.data_location)
        dataRepository = resources.getStringArray(R.array.data_repository)
        dataFollower = resources.getStringArray(R.array.data_follower)
        dataFollowing = resources.getStringArray(R.array.data_following)
    }

}
package com.dailyapps.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dailyapps.githubuser.databinding.ItemGithubBinding

class GithubAdapter internal constructor(private val context: Context) : BaseAdapter(){
    internal var github = arrayListOf<Github>()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        var itemView = view
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_github, viewGroup, false)
        }
        val viewHolder = ViewHolder(itemView as View)

        val github = getItem(position) as Github
        viewHolder.bind(github)
        return itemView
    }

    override fun getItem(i: Int): Any = github[i]

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getCount(): Int = github.size

private inner class ViewHolder constructor(private val view: View){
    private val binding = ItemGithubBinding.bind(view)
    internal fun bind(github: Github){
        binding.txtUsername.text = github.username
        binding.txtName.text = github.name
        binding.imgPhoto.setImageResource(github.avatar)
    }
}

}
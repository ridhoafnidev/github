package com.dailyapps.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dailyapps.githubuser.databinding.ItemGithubBinding
import com.dailyapps.githubuser.model.Github

class GithubAdapter() : RecyclerView.Adapter<GithubAdapter.ListViewHolder>(){

    private var onItemClickCallback: OnItemClickCallback?= null
    private val mData = ArrayList<Github>()

    fun setData(item: ArrayList<Github>) {
        mData.clear()
        mData.addAll(item)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(private val binding: ItemGithubBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(github: Github){
            with(binding){
                Glide.with(itemView.context)
                    .load(github.avatar)
                    .apply(RequestOptions().override(55,55))
                    .into(binding.imgPhoto)
                txtName.text = github.name
                txtUsername.text = github.username

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(github) }

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Github)
    }

}

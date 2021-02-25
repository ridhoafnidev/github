package com.dailyapps.githubuser.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dailyapps.githubuser.R
import com.dailyapps.githubuser.fragment.FollowerFragment
import com.dailyapps.githubuser.fragment.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager, private val username: String?) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_follower,
        R.string.tab_following
    )

    private lateinit var followerFragment: FollowerFragment
    private lateinit var followingFragment: FollowingFragment
    private lateinit var bundle: Bundle

    override fun getItem(position: Int): Fragment {
        followerFragment = FollowerFragment()
        followingFragment = FollowingFragment()
        bundle = Bundle()
        bundle.putString(FollowerFragment.EXTRA_DATA, username)
        bundle.putString(FollowingFragment.EXTRA_DATA, username)
        followingFragment.arguments = bundle
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = followerFragment
            1 -> fragment = followingFragment
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

}
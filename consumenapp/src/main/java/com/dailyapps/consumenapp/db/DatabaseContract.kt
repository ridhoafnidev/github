package com.dailyapps.consumenapp.db

import android.net.Uri
import android.provider.BaseColumns

object  DatabaseContract {

    const val AUTHORITY = "com.dailyapps.githubuser"
    const val SCHEME = "content"

    class FavColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val FAVORITE = "favorite"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
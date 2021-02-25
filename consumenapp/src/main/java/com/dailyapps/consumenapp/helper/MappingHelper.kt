package com.dailyapps.consumenapp.helper

import android.database.Cursor
import com.dailyapps.consumenapp.db.DatabaseContract
import com.dailyapps.consumenapp.model.Github
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Github> {
        val favoriteList = ArrayList<Github>()

        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FOLLOWING))
                val favorite = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FAVORITE))
                favoriteList.add(
                    Github(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        favorite
                    )
                )
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(notesCursor: Cursor?): Github {
        var github = Github()
        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FOLLOWING))
                val favorite = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FAVORITE))
                github = Github(username, name, avatar, company, location,repository, followers, following, favorite)
            }
        }
        return github
    }

}
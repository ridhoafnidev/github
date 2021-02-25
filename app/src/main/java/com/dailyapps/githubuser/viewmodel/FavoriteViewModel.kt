package com.dailyapps.githubuser.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dailyapps.githubuser.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.dailyapps.githubuser.helper.MappingHelper
import com.dailyapps.githubuser.model.Github
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel(){
    private val listFavorite = MutableLiveData<ArrayList<Github>>()

    fun loadFavorite(context: Context?) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = context?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteData = deferredFavorite.await()
            listFavorite.postValue(favoriteData)
        }
    }

    fun getFavorite(): LiveData<ArrayList<Github>>{
        return listFavorite
    }
}
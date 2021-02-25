package com.dailyapps.githubuser.viewmodel

import android.content.Context
import android.net.Uri
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

class DetailViewModel : ViewModel() {
    private val favorite = MutableLiveData<Github>()
    private lateinit var uriWithId: Uri

    fun setDetailUser(context: Context?, username: String?){
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" +username)
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = context?.contentResolver?.query(uriWithId, null, null, null, null)
                MappingHelper.mapCursorToObject(cursor)
            }
            val data = deferredFavorite.await()
            favorite.postValue(data)
        }
    }

    fun getDetailUser(): LiveData<Github> {
        return favorite
    }
}
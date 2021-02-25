package com.dailyapps.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dailyapps.githubuser.BuildConfig
import com.dailyapps.githubuser.model.Github
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class GithubViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<Github>>()
    private val listUserDetail = MutableLiveData<ArrayList<Github>>()
    private val githubs = ArrayList<Github>()
    private val listUserSearch = MutableLiveData<ArrayList<Github>>()

    fun setLoginUserGithub(){
        val listLogin = ArrayList<Github>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")

        val url = "https://api.github.com/users"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val login: String = jsonObject.getString("login")
                        val github = Github(
                            username = login,
                            name = "",
                            avatar = "",
                            location = "",
                            company = "",
                            repository = "",
                            followers = "",
                            following = "",
                            favorite = ""
                        )
                        listLogin.add(github)
                    }
                    listUser.postValue(listLogin)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("Exception",errorMessage)
            }
        })
    }

    fun getLoginUserGithub(): LiveData<ArrayList<Github>> {
        return listUser
    }

    fun setDetailUserGithub(username: String?){
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val login: String = jsonObject.getString("login")
                    val name: String = jsonObject.getString("name")
                    val avatar: String = jsonObject.getString("avatar_url")
                    val company: String = jsonObject.getString("company")
                    val location: String = jsonObject.getString("location")
                    val repository: String =  jsonObject.getString("public_repos")
                    val followers: String =  jsonObject.getString("followers")
                    val following: String =  jsonObject.getString("following")
                    val favorite: String =  "0"
                    githubs.add(
                        Github(
                            login,
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
                    listUserDetail.postValue(githubs)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("Error ", errorMessage)
            }
        })
    }

    fun getDetailUserGithub(): LiveData<ArrayList<Github>> {
        return listUserDetail
    }

    fun setSearchUserGithub(query: String){
        val listSerch = ArrayList<Github>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")
        val url = " https://api.github.com/search/users?q=$query"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val dataItem = jsonObject.getJSONArray("items")
                    for (i in 0 until dataItem.length()) {
                        val item = dataItem.getJSONObject(i)
                        val username: String = item.getString("login")
                        val search = Github(
                            username,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                        listSerch.add(search)
                    }
                    listUserSearch.postValue(listSerch)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("Exception",errorMessage)
            }
        })
    }

    fun getSearchUserGethub(): LiveData<ArrayList<Github>> {
        return listUserSearch
    }

}
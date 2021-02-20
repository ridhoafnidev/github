package com.dailyapps.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Github(
    var username: String,
    var name: String,
    var avatar: String,
    var company: String,
    var location: String,
    var repository: String,
    var follower: String,
    var following: String,
): Parcelable
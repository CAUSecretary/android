package com.example.causecretary.ui.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Uncertified(
    val belong: String,
    val certifyImg: String,
    val name: String,
    val userIdx: Int
):Parcelable
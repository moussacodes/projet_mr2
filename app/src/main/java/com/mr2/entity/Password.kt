package com.mr2.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

 @Entity(tableName = "passwords")
 @Parcelize
data class Password(

    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "notes") var notes: String = "",
    @ColumnInfo(name = "updatedDate") var updatedDate: String = "",
    @ColumnInfo(name = "updatedTime") var updatedTime: String = "",

) : Parcelable

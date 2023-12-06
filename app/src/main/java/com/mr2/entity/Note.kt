package com.mr2.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

 @Entity(tableName = "notes")
 @Parcelize
data class Note(

    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "label") var label: String = "",
    @ColumnInfo(name = "date") var date: String = "",
    @ColumnInfo(name = "time") var time: String = "",
    @ColumnInfo(name = "updatedDate") var updatedDate: String = "",
    @ColumnInfo(name = "updatedTime") var updatedTime: String = "",
    @ColumnInfo(name = "body") var body: String = ""
) : Parcelable

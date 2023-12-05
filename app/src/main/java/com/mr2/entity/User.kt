
package com.mr2.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "user")
//Parcelable annotation to make parcelable object
@Parcelize
data class User(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "username") var username: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "access_token") var access_token: String = "",
    @ColumnInfo(name = "refresh_token") var refresh_token: String = "",
    @ColumnInfo(name = "logged_on") var logged_on: String = "",

) : Parcelable

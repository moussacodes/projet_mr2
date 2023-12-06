package com.mr2.api

/**
 * défini les routes (ici, pour créer un compte)
 */

import com.mr2.model.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/local/signup")
    fun signup(@Body userData: UserData): Call<Void>
}
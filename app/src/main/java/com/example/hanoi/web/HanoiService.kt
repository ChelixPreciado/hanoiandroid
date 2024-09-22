package com.example.hanoi.web

import com.example.hanoi.models.HanoiWsEntity
import retrofit2.http.GET

interface HanoiService {

    @GET("/hanoi")
    suspend fun getDisksNumber(): HanoiWsEntity

}
package com.narcis.shoes.model

import io.reactivex.Single
import retrofit2.http.GET

interface ShoesApi {
    @GET("narcis-dpr/PolarizedMacelice/master/shoes.json")
    fun getShoes(): Single<List<ShoeBreed>>
}
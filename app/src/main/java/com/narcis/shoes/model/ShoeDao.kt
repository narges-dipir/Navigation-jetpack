package com.narcis.shoes.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShoeDao {
    @Insert
    suspend fun insertAll(vararg shoes: ShoeBreed): List<Long>

    @Query("SELECT * FROM shoebreed")
    suspend fun getAllShoes(): List<ShoeBreed>

    @Query("SELECT * FROM shoebreed WHERE uuid = :shoeId")
    suspend fun getShoe(shoeId: Int): ShoeBreed

    @Query("DELETE FROM shoebreed")
    suspend fun deleteAllShoes()
}
package com.narcis.shoes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ShoeBreed(
    @ColumnInfo(name = "shoe_id")
    @SerializedName("id")
    val breedId: String?,

    @ColumnInfo(name = "shoe_name")
    @SerializedName("name")
    val shoeBreed: String?,

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifeSpan: String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    val breedGroup: String?,

    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    val bredFor: String?,

    @SerializedName("style")
    val temperament: String?,

    @ColumnInfo(name = "shoe_url")
    @SerializedName("url")
    val imageUrl: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

data class ShoePalette(var color: Int)

data class SmsInfo(
    var to: String,
    var text: String,
    var imageUrl: String?
)
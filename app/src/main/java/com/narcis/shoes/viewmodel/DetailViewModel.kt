package com.narcis.shoes.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.narcis.shoes.model.ShoeBreed
import com.narcis.shoes.model.ShoeDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val shoeLiveData = MutableLiveData<ShoeBreed>()

    fun fetch(uuid: Int) {
        launch {
            val shoe = ShoeDatabase(getApplication()).shoeDao().getShoe(uuid)
            shoeLiveData.value = shoe
        }
    }
}
package com.narcis.shoes.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.narcis.shoes.model.ShoeBreed
import com.narcis.shoes.model.ShoeDatabase
import com.narcis.shoes.model.ShoesApiService
import com.narcis.shoes.util.NotificationsHelper
import com.narcis.shoes.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application): BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val shoesService = ShoesApiService()
    private val disposable = CompositeDisposable()

    val shoes = MutableLiveData<List<ShoeBreed>>()
    val shoesLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val shoes = ShoeDatabase(getApplication()).shoeDao().getAllShoes()
            shoesRetrieved(shoes)
            Toast.makeText(getApplication(), "Shoes retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            shoesService.getShoes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<ShoeBreed>>() {

                    override fun onSuccess(shoeList: List<ShoeBreed>) {
                        storeShoesLocally(shoeList)
                        Toast.makeText(getApplication(), "Shoes retrieved from endpoint", Toast.LENGTH_SHORT).show()
                        NotificationsHelper(getApplication()).createNofitication()
                    }

                    override fun onError(e: Throwable) {
                        shoesLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun shoesRetrieved(shoeList: List<ShoeBreed>) {
        shoes.value = shoeList
        shoesLoadError.value = false
        loading.value = false
    }

    private fun storeShoesLocally(list: List<ShoeBreed>) {
        launch {
            val dao = ShoeDatabase(getApplication()).shoeDao()
            dao.deleteAllShoes()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            shoesRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
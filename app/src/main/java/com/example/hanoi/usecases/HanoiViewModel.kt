package com.example.hanoi.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanoi.models.HanoiWsEntity
import com.example.hanoi.web.HanoiService
import com.example.hanoi.web.RetrofitClient
import kotlinx.coroutines.launch

class HanoiViewModel : ViewModel(), HanoiDataSource {

    private val _data = MutableLiveData<HanoiWsEntity?>()
    val data: LiveData<HanoiWsEntity?> get() = _data

    private val apiService by lazy {
        RetrofitClient.instance.create(HanoiService::class.java)
    }

    override fun getDisks() {
        viewModelScope.launch {
            try {
                val response = apiService.getDisksNumber()
                Log.i("Chelix", "Numero de discos en BE: ${response.discos}")
                _data.value = response
            } catch (e: Exception) {
                Log.e("Hanoi", "Error: ${e.message}")
                _data.value = null
            }
        }
    }

}
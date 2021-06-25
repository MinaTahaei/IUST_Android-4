package com.example.mb2

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CellInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CellInfoRepository
    val allInfos: LiveData<List<CellInfo>>

    init {
        val infoDao = CellInfoDatabase.getDatabase(application, viewModelScope).cellInfoDao()
        repository = CellInfoRepository(infoDao)
        allInfos = repository.allWords


    }

    fun insert(cellInfo: CellInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(cellInfo)
    }
}

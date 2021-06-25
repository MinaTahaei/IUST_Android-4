package com.example.mb2

import androidx.lifecycle.LiveData

class CellInfoRepository(private val cellInfoDao: CellInfoDao) {

    val allWords: LiveData<List<CellInfo>> = cellInfoDao.getAlphabetizedWords()

    suspend fun insert(cellInfo: CellInfo) {
        cellInfoDao.insert(cellInfo)
    }
}
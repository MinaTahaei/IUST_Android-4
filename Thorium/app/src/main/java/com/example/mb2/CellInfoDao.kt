package com.example.mb2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CellInfoDao {
    @Query("SELECT * from cell_info_table ORDER BY time DESC")
    fun getAlphabetizedWords(): LiveData<List<CellInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cell_info: CellInfo)
}
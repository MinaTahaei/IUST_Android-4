package com.example.mb2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [CellInfo::class], version = 1, exportSchema = false)
public abstract class CellInfoDatabase : RoomDatabase(){
    abstract fun cellInfoDao(): CellInfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CellInfoDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CellInfoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CellInfoDatabase::class.java,
                    "cell_info_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
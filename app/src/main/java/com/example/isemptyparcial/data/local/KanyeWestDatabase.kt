package com.example.isemptyparcial.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritoEntity::class], version = 1, exportSchema = false)
abstract class KanyeWestDatabase : RoomDatabase() {
    abstract fun favoritoDao(): FavoritoDao

    companion object {
        @Volatile
        private var instancia: KanyeWestDatabase? = null

        fun obtener(context: Context): KanyeWestDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    KanyeWestDatabase::class.java,
                    "kanyex.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { instancia = it }
            }
    }
}

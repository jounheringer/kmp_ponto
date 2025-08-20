package com.reringuy.dependency_injection

import android.content.Context
import androidx.room.Room
import com.reringuy.database.PontoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PontoDatabase {
        return Room.databaseBuilder(
            context,
            PontoDatabase::class.java,
            "ponto.db"
        ).build()
    }

}
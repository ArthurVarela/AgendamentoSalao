package com.example.agendamentosalao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.agendamentosalao.database.daos.AppointmentDao
import com.example.agendamentosalao.database.daos.UserDao
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.database.models.User
import kotlinx.coroutines.CoroutineScope


@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    //abstract fun userDao(): UserDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {

        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "salon_schedule")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
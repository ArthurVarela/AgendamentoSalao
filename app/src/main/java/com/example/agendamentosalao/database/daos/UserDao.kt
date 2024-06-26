package com.example.agendamentosalao.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.agendamentosalao.database.models.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT COUNT(id) FROM users")
    suspend fun getTotalItems(): Long
}

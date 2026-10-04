package com.example.listatarefas

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TarefaDao {
    @Insert
    suspend fun inserir(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas ORDER BY id ASC")
    suspend fun listar(): List<Tarefa>

    @Delete
    suspend fun excluir(tarefa: Tarefa)
}

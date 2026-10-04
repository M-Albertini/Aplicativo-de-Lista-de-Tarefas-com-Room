package com.example.listatarefas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = TarefaDatabase.obter(applicationContext).tarefaDao()

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(primary = Color(0xFF3F51B5))
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TelaTarefas(dao)
                }
            }
        }
    }
}

@Composable
fun TelaTarefas(dao: TarefaDao) {
    var nome by rememberSaveable { mutableStateOf("") }
    var tarefas by remember { mutableStateOf(emptyList<Tarefa>()) }
    var ocupado by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Busca as tarefas salvas quando a tela é aberta.
    LaunchedEffect(dao) {
        tarefas = withContext(Dispatchers.IO) {
            dao.listar()
        }
        ocupado = false
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lista de tarefas", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome da tarefa") },
            singleLine = true,
            enabled = !ocupado,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val texto = nome.trim()
                if (texto.isNotEmpty() && !ocupado) {
                    ocupado = true
                    scope.launch {
                        tarefas = withContext(Dispatchers.IO) {
                            dao.inserir(Tarefa(nome = texto))
                            dao.listar()
                        }
                        nome = ""
                        ocupado = false
                    }
                }
            },
            enabled = nome.isNotBlank() && !ocupado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Tarefas cadastradas", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (ocupado) {
            Text("Carregando...")
        } else if (tarefas.isEmpty()) {
            Text("Nenhuma tarefa cadastrada.")
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tarefas, key = { it.id }) { tarefa ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(tarefa.nome, modifier = Modifier.weight(1f))
                        TextButton(
                            enabled = !ocupado,
                            onClick = {
                                ocupado = true
                                scope.launch {
                                    tarefas = withContext(Dispatchers.IO) {
                                        dao.excluir(tarefa)
                                        dao.listar()
                                    }
                                    ocupado = false
                                }
                            }
                        ) {
                            Text("Excluir")
                        }
                    }
                }
            }
        }
    }
}

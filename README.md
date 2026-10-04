# Lista de tarefas — Kotlin, Compose e Room

Projeto simples para a atividade de compensação de ausência de Desenvolvimento de Aplicativos Móveis.

## O que o aplicativo faz

- Cadastra uma tarefa pelo nome.
- Mostra as tarefas em uma `LazyColumn`.
- Exclui a tarefa pelo botão **Excluir**.
- Salva os dados localmente com Room.
- Não permite cadastrar um nome vazio ou apenas espaços.
- Não tem edição de tarefas, login ou conexão com servidor.

## Como abrir

1. Extraia o arquivo ZIP.
2. No Android Studio, use **Open** e selecione a pasta `lista-tarefas`, que contém `settings.gradle.kts`.
3. Espere a sincronização do Gradle. No primeiro uso, é necessária internet para baixar as dependências.
4. Se solicitado, instale a plataforma Android SDK 35 e aceite as licenças do SDK.
5. Use JDK 17 ou 21 na configuração do Gradle.
6. Selecione um emulador ou celular com Android 8.0 (API 26) ou superior e clique em **Run**.

O arquivo `local.properties` é específico de cada computador e não acompanha o projeto. O Android Studio configura o caminho do SDK ao abrir o projeto.

## Arquivos principais

Na pasta `app/src/main/java/com/example/listatarefas/`:

- `Tarefa.kt`: entidade com ID automático e nome.
- `TarefaDao.kt`: funções `inserir`, `listar` e `excluir`.
- `TarefaDatabase.kt`: criação do banco Room chamado `tarefas.db`.
- `MainActivity.kt`: tela e operações com coroutines.

## Funcionamento resumido

Ao abrir a tela, `LaunchedEffect` consulta o banco. Ao adicionar ou excluir, uma coroutine executa a operação e busca a lista atualizada. O acesso ao banco fica dentro de `withContext(Dispatchers.IO)`. Depois, o estado da tela é atualizado na thread principal.

Para simplificar a atividade, não foram usados ViewModel, Repository ou injeção de dependências.

## Gerar o APK por terminal

```bash
./gradlew assembleDebug
```

No Windows:

```bat
gradlew.bat assembleDebug
```

O APK é gerado em `app/build/outputs/apk/debug/app-debug.apk`.

## Documentação consultada

- https://developer.android.com/training/data-storage/room/defining-data
- https://developer.android.com/training/data-storage/room/accessing-data

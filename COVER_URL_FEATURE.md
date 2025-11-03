# Feature: Suporte a URL de Capa de Livros

## Resumo das Mudanças Implementadas

### 1. Modelo de Dados (Book.kt)
- Adicionado campo coverUrl: String? para armazenar URLs de capas remotas
- Mantido campo coverUri: String? para imagens locais

### 2. Banco de Dados (BookDatabase.kt)
- Versão do banco atualizada de 1 para 2
- Criada migração MIGRATION_1_2 que adiciona a coluna coverUrl na tabela books
- Migração adicionada ao builder do Room

### 3. ViewModels

#### AddBookViewModel.kt
- Adicionado campo coverUrl: String = "" no AddBookUiState
- Criada função onCoverUrlChange(url: String) para atualizar a URL
- Atualizada função saveBook() para salvar o coverUrl no banco

#### EditBookViewModel.kt
- Adicionado campo coverUrl: String = "" no EditBookUiState
- Atualizada função loadBook() para carregar o coverUrl do banco
- Criada função onCoverUrlChange(url: String) para atualizar a URL
- Atualizada função saveBook() para salvar o coverUrl no banco

### 4. Telas de UI

#### AddBookScreen.kt
- Importado coil.compose.AsyncImage
- Atualizada visualização da capa para usar AsyncImage com suporte a URL e URI
- Adicionado campo OutlinedTextField para entrada de URL da capa
- Prioriza URL sobre URI local quando ambos estão disponíveis

#### EditBookScreen.kt
- Importado coil.compose.AsyncImage
- Atualizada visualização da capa para usar AsyncImage com suporte a URL e URI
- Adicionado campo OutlinedTextField para entrada de URL da capa
- Prioriza URL sobre URI local quando ambos estão disponíveis

#### HomeScreen.kt
- Importado coil.compose.AsyncImage
- Atualizada visualização da capa nos cards de livros para usar AsyncImage
- Removido placeholder temporário e agora exibe imagens reais

#### BookDetailScreen.kt
- Importado coil.compose.AsyncImage
- Atualizada visualização da capa na tela de detalhes para usar AsyncImage
- Removido placeholder temporário e agora exibe imagens reais com ContentScale.Crop

### 5. Permissões (AndroidManifest.xml)
- Permissão INTERNET já estava configurada no manifesto

### 6. Dependências (build.gradle.kts)
- Biblioteca Coil (coil-compose) já estava incluída nas dependências

## Como Funciona

1. Adicionar Livro: O usuário pode adicionar uma URL de capa ou selecionar uma imagem local. A URL tem prioridade na exibição.

2. Editar Livro: O usuário pode atualizar a URL da capa ou trocar por uma imagem local.

3. Visualização: Em todas as telas (Home, Detalhes), o app prioriza exibir a URL da capa. Se não houver URL, exibe a imagem local. Se não houver nenhuma, exibe um ícone de placeholder.

4. Biblioteca Coil: Gerencia automaticamente o cache, carregamento assíncrono e suporte tanto para URLs remotas quanto URIs locais.

5. Migração Automática: Ao rodar o app, o Room detecta a mudança de versão e executa a migração automaticamente, adicionando a nova coluna sem perder dados existentes.

## Prioridade de Exibição

```
coverUrl (se disponível) → coverUri (se disponível) → placeholder icon
```

## Teste das Funcionalidades

Para testar:
1. Execute o app (a migração do banco será aplicada automaticamente)
2. Adicione um novo livro com uma URL de capa (ex: https://covers.openlibrary.org/b/id/240727-L.jpg)
3. Verifique se a imagem é carregada na tela de listagem
4. Abra os detalhes do livro para ver a capa em tamanho maior
5. Edite o livro e troque a URL ou adicione uma imagem local

## Observações

- A biblioteca Coil gerencia cache automaticamente
- Imagens são carregadas de forma assíncrona sem bloquear a UI
- URLs inválidas ou imagens que não carregam mostram placeholder automaticamente
- É possível ter ambos (URL e URI local), mas a URL tem prioridade


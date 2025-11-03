# Documentação do Projeto BookLog

## Visão Geral

BookLog é um aplicativo Android para registro, organização e acompanhamento de leituras de livros.

## Estrutura das Telas

### Tela Home

Lista todos os livros cadastrados com opções de busca, filtro e ordenação.

Filtros disponíveis:
- Status do livro (Para Ler, Lendo, Lido)
- Gênero do livro

Funcionalidades:
- Buscar por título ou autor
- Filtrar por status e gênero
- Ordenar por título, autor, avaliação ou data
- Adicionar novo livro
- Visualizar detalhes do livro

### Tela de Detalhes

Exibe todas as informações do livro selecionado.

Informações apresentadas:
- Capa
- Título
- Autor
- Gênero
- Status
- Avaliação
- Notas
- Datas de criação e atualização

Ações disponíveis:
- Editar livro
- Excluir livro

### Tela de Cadastro/Edição

Formulário para adicionar ou editar um livro.

Campos do formulário:
- Título (obrigatório)
- Autor (obrigatório)
- Gênero (obrigatório)
- Status: Para Ler, Lendo ou Lido (obrigatório)
- Avaliação (0 a 5 estrelas, opcional)
- Capa: Imagem local (URI) ou URL remota (opcional)
- Notas pessoais (opcional)

## Tecnologias

- Kotlin
- Jetpack Compose
- Room Database
- Coil para carregamento de imagens
- Navigation Compose
- Material Design 3

## Arquitetura

O projeto utiliza o padrão MVVM (Model-View-ViewModel) com Repository Pattern para separação de responsabilidades e facilitar a manutenção.

## Modelo de Dados

### Entidade Book

A entidade principal do aplicativo contém os seguintes campos:

- **id**: Long (chave primária, auto-incremento)
- **title**: String (título do livro, obrigatório)
- **author**: String (autor do livro, obrigatório)
- **genre**: String (gênero literário, obrigatório)
- **status**: Enum (PARA_LER, LENDO, LIDO - obrigatório)
- **rating**: Int (avaliação de 0 a 5 estrelas, opcional)
- **coverUri**: String? (URI local da capa, opcional)
- **coverUrl**: String? (URL remota da capa, opcional)
- **notes**: String? (notas pessoais, opcional)
- **createdAt**: Long (timestamp de criação)
- **updatedAt**: Long (timestamp da última atualização)

### Persistência

- Banco de dados: Room (SQLite local)
- Operações: CRUD completo via DAO
- Observação: Flow para atualização reativa da UI
- Sem backend: Todos os dados são armazenados localmente


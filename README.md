# BookLog - Aplicativo de Gerenciamento de Leituras

## Descrição
BookLog é um aplicativo Android nativo desenvolvido em Kotlin com Jetpack Compose que permite aos usuários organizar e acompanhar suas leituras de forma simples e eficiente.

## Tecnologias Utilizadas

### Frontend
- **Kotlin**: Linguagem de programação principal
- **Jetpack Compose**: Framework moderno para UI declarativa
- **Material Design 3**: Design system para uma interface consistente

### Arquitetura
- **MVVM (Model-View-ViewModel)**: Padrão arquitetural para separação de responsabilidades
- **Room Database**: Persistência local de dados
- **Kotlin Coroutines & Flow**: Programação assíncrona e streams reativos
- **Navigation Compose**: Navegação entre telas

### Bibliotecas
- **Coil**: Carregamento eficiente de imagens
- **Lifecycle & ViewModel**: Gerenciamento de ciclo de vida
- **KSP (Kotlin Symbol Processing)**: Processamento de anotações do Room

## Funcionalidades

### Tela Home
- Listagem de todos os livros cadastrados
- Busca por título ou autor
- Filtros por status (Para Ler, Lendo, Lido) e gênero
- Ordenação por título, autor, avaliação ou data de modificação
- Visualização de capas, avaliações e status dos livros

### Adicionar Livro
- Cadastro de novos livros com:
  - Título (obrigatório)
  - Autor (obrigatório)
  - Gênero (obrigatório)
  - Status de leitura
  - Avaliação (0-5 estrelas)
  - Capa do livro (imagem)
  - Notas pessoais

### Detalhes do Livro
- Visualização completa de todas as informações do livro
- Exibição de capa em tamanho maior
- Informações de criação e última atualização
- Opções para editar ou excluir o livro

### Editar Livro
- Atualização de todas as informações do livro
- Alteração de capa
- Atualização de status e avaliação

## Estrutura do Projeto

```
app/src/main/java/com/example/booklog/
├── data/
│   ├── local/
│   │   ├── BookDao.kt              # Interface de acesso aos dados
│   │   ├── BookDatabase.kt         # Configuração do Room Database
│   │   └── Converters.kt           # Conversores de tipos do Room
│   ├── model/
│   │   └── Book.kt                 # Entidade Book e enum BookStatus
│   └── repository/
│       └── BookRepository.kt       # Camada de repositório
├── navigation/
│   ├── BookLogNavGraph.kt          # Configuração de navegação
│   └── Screen.kt                   # Definição de rotas
├── ui/
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt       # UI da tela inicial
│   │   │   └── HomeViewModel.kt    # Lógica da tela inicial
│   │   ├── addbook/
│   │   │   ├── AddBookScreen.kt    # UI de adicionar livro
│   │   │   └── AddBookViewModel.kt # Lógica de adicionar livro
│   │   ├── bookdetail/
│   │   │   ├── BookDetailScreen.kt # UI de detalhes
│   │   │   └── BookDetailViewModel.kt # Lógica de detalhes
│   │   └── editbook/
│   │       ├── EditBookScreen.kt   # UI de edição
│   │       └── EditBookViewModel.kt # Lógica de edição
│   └── theme/
│       └── ...                      # Tema Material 3
└── MainActivity.kt                  # Activity principal

```

## Design

### Paleta de Cores
- **Background Principal**: Branco (#FFFFFF)
- **AppBar**: Preto (#000000)
- **Texto Principal**: Preto (#000000)
- **Texto Secundário**: Cinza Escuro (#424242)
- **Texto Terciário**: Cinza (#757575)
- **Accent**: Amarelo/Dourado (#FFA000) para avaliações
- **Status Colors**:
  - Para Ler: Cinza (#757575)
  - Lendo: Azul (2196F3)
  - Lido: Verde (4CAF50)

## Como Executar

### Requisitos
- Android Studio Hedgehog ou superior
- JDK 11 ou superior
- Android SDK 24 (mínimo) - Android 7.0
- Android SDK 36 (target) - Android 14

### Passos
1. Clone o repositório
2. Abra o projeto no Android Studio
3. Sincronize as dependências do Gradle
4. Execute o aplicativo em um emulador ou dispositivo físico

## Banco de Dados

### Estrutura da Tabela books
- id: Long (Primary Key, auto-incremento)
- title: String (título do livro)
- author: String (autor)
- genre: String (gênero)
- status: Enum (PARA_LER, LENDO, LIDO)
- rating: Int (0-5)
- coverUri: String? (URI da capa local)
- coverUrl: String? (URL da capa remota)
- notes: String? (notas do usuário)
- createdAt: Long (timestamp de criação)
- updatedAt: Long (timestamp de atualização)

## Permissões
- READ_MEDIA_IMAGES: Para selecionar imagens de capa (Android 13+)
- READ_EXTERNAL_STORAGE: Para selecionar imagens de capa (Android 12 e anteriores)
- INTERNET: Para carregamento de imagens pelo Coil

## Características Técnicas

### Boas Práticas Implementadas
- Separação de responsabilidades com MVVM
- Uso de StateFlow para gerenciamento de estado reativo
- Coroutines para operações assíncronas
- Flow para observação de dados do banco
- ViewModelFactory para injeção de dependências
- UI declarativa com Compose
- Validação de dados antes de salvar
- Tratamento de erros

### Performance
- Lazy loading de listas com LazyColumn
- Carregamento eficiente de imagens com Coil
- Queries otimizadas no Room
- Estados de loading para melhor UX

## Melhorias Futuras
- Exportação/importação de dados (JSON/CSV)
- Backup automático
- Estatísticas de leitura
- Meta de leitura anual
- Categorização personalizada
- Sincronização com serviços de nuvem (opcional)
- Busca por ISBN com API externa
- Widget para homescreen
- Modo escuro
- Suporte a múltiplos idiomas

## Licença
Este projeto foi desenvolvido como aplicativo educacional.


# BookLog - Regras e Escopo

## O Problema ou a Oportunidade
Muitas pessoas têm dificuldade em acompanhar suas leituras, lembrar quais livros já leram, quais pretendem ler e organizar suas avaliações e anotações sobre cada obra. O aplicativo BookLog facilita o registro e o acompanhamento das leituras, permitindo ao usuário organizar sua biblioteca pessoal, registrar informações relevantes sobre cada livro e acompanhar seu progresso de leitura. O contexto de uso é para leitores que desejam ter controle e organização sobre suas leituras, avaliações e anotações.

## Público-Alvo
Leitores em geral que desejam organizar e acompanhar suas leituras, sejam estudantes, amantes de livros ou qualquer pessoa interessada em leitura.

## Proposta de Valor
A maneira mais prática de registrar, organizar e acompanhar suas leituras de livros, sem necessidade de conta ou serviço externo.

## 3. Funcionalidades Essenciais (MVP - Mínimo Produto Viável)

a) Abertura e fluxo inicial:
- Não haverá autenticação. Ao abrir o app o usuário será direcionado diretamente para a tela Home.

b) Uso do Banco de Dados (CRUD - Criar, Ler, Atualizar, Deletar):
Principal dado gerenciado: Livros do usuário.

- Criar: Usuário poderá registrar novos livros informando título, autor, gênero, status (lido/não lido), avaliação (até 5 estrelas), URL/URI da capa e notas.
- Ler: Usuário poderá visualizar sua lista de livros, filtrar por gênero e status de leitura.
- Atualizar: Usuário poderá editar informações dos livros, atualizar status de leitura, avaliação e notas.
- Deletar: Usuário poderá excluir livros da sua lista.

Observação sobre imagens: as capas serão armazenadas localmente; o banco armazenará URIs/paths para os ficheiros das capas. Não será usado armazenamento remoto.

c) Outras funcionalidades do MVP:
- Ordenação e filtros simples (por título, autor, status).
- Visualização detalhada de cada livro com notas e avaliação.
- Importação/Exportação básica (opcional, como arquivo local JSON) para backup do usuário.

## 4. Arquitetura e Tecnologias

a) Tecnologia para Desenvolvimento do App (Frontend):
- Escolha: Android nativo
- Linguagem: Kotlin
- UI: Jetpack Compose

b) Persistência de Dados (substitui backend):
- Escolha: Room (AndroidX Room) para persistência local de dados relacionais.
- Modelo sugerido (exemplo): entidade `Book` com campos:
  - `id`: Long (PK, auto-gerado)
  - `title`: String
  - `author`: String
  - `genre`: String
  - `status`: Enum/String (ex.: `LIDO`, `PARA_LER`)
  - `rating`: Int (0-5)
  - `coverUri`: String? (path/URI local)
  - `notes`: String?
  - `createdAt` / `updatedAt`: Long (timestamps, opcionais)

Notas técnicas:
- Usar DAOs do Room para operações CRUD.
- Recomenda-se usar `Flow`/`LiveData` para observar mudanças e integrar com Compose.
- Armazenar arquivos de imagem em armazenamento interno ou `MediaStore` e persistir o caminho/URI no banco.
- Não haverá comunicação com servidor externo; todo o estado é local.

c) Backend / Serviços Externos:
- Não haverá backend. Supabase e outros serviços remotos foram removidos do escopo.

d) Outras bibliotecas recomendadas:
- Kotlin Coroutines
- Jetpack DataStore (opcional, para preferências)
- Coil ou outra biblioteca leve para carregar imagens locais no Compose

## Observações finais
- O foco do MVP é simplicidade e confiabilidade dos dados locais.
- Planejar migrações do Room para evoluções do modelo.
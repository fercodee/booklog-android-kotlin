# Instruções de Build e Execução - BookLog

## Pré-requisitos

### Software Necessário
- Android Studio: Hedgehog (2023.1.1) ou superior
- JDK: 11 ou superior
- Android SDK: 
  - Mínimo: API 24 (Android 7.0)
  - Target: API 36 (Android 14)
- Gradle: 8.13 (incluído no projeto)

### Configuração do Ambiente
1. Instale o Android Studio da página oficial
2. Durante a instalação, certifique-se de incluir:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device (para emulador)

## Clonando o Projeto

```bash
git clone <repository-url>
cd BookLog
```

## Configuração Inicial

### 1. Abrir no Android Studio
1. Abra o Android Studio
2. Selecione File > Open
3. Navegue até a pasta do projeto BookLog
4. Clique em OK

### 2. Sincronizar Gradle
O Android Studio deve automaticamente iniciar o sync do Gradle. Se não:
1. Clique em File > Sync Project with Gradle Files
2. Aguarde o download de todas as dependências

### 3. Verificar SDK
1. Vá em Tools > SDK Manager
2. Certifique-se de ter instalado:
   - Android SDK Platform 36 (Android 14.0)
   - Android SDK Platform 24 (Android 7.0) - mínimo suportado

## Compilando o Projeto

### Via Android Studio (Recomendado)

#### Build Debug
1. Selecione Build > Make Project (Ctrl+F9)
2. Aguarde a compilação

#### Build Release
1. Selecione Build > Generate Signed Bundle / APK
2. Escolha APK
3. Siga o processo para criar/selecionar keystore

### Via Linha de Comando

#### Windows (PowerShell ou CMD)
```powershell
# Debug build
.\gradlew.bat assembleDebug

# Release build (sem assinatura)
.\gradlew.bat assembleRelease

# Build completo (debug + release)
.\gradlew.bat build

# Limpar build anterior
.\gradlew.bat clean
```

#### Linux/Mac
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Build completo
./gradlew build

# Limpar
./gradlew clean
```

## Executando o Aplicativo

### Opção 1: Via Android Studio (Mais Fácil)

#### Em Emulador
1. Clique em Tools > Device Manager
2. Crie um novo dispositivo virtual:
   - Clique em Create Device
   - Escolha um telefone (ex: Pixel 6)
   - Selecione uma imagem do sistema (recomendado: API 34 - Android 14)
   - Clique em Finish
3. Selecione o dispositivo virtual no dropdown superior
4. Clique no botão Run ou pressione Shift+F10

#### Em Dispositivo Físico
1. Habilite Opções do Desenvolvedor no seu Android:
   - Vá em Configurações > Sobre o telefone
   - Toque 7 vezes em "Número da compilação"
2. Habilite Depuração USB:
   - Configurações > Sistema > Opções do desenvolvedor
   - Ative "Depuração USB"
3. Conecte o dispositivo via USB
4. Autorize o computador no dispositivo quando solicitado
5. Selecione seu dispositivo no dropdown do Android Studio
6. Clique em Run

### Opção 2: Via Linha de Comando

#### Instalar em Dispositivo Conectado
```powershell
# Windows
.\gradlew.bat installDebug

# Linux/Mac
./gradlew installDebug
```

#### Instalar e Executar
```powershell
# Windows
.\gradlew.bat installDebug
adb shell am start -n com.example.booklog/.MainActivity

# Linux/Mac
./gradlew installDebug
adb shell am start -n com.example.booklog/.MainActivity
```

## Localizando o APK

Após o build, os APKs estarão em:

### Debug APK
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release APK
```
app/build/outputs/apk/release/app-release.apk
```

## Verificando a Compilação

### Verificar Erros
No Android Studio:
1. Abra a aba Build (Alt+1)
2. Verifique se há erros de compilação

Via linha de comando:
```powershell
.\gradlew.bat build --warning-mode all
```

### Verificar Dependências
```powershell
.\gradlew.bat dependencies
```

### Limpar Cache do Gradle
Se houver problemas:
```powershell
.\gradlew.bat clean
.\gradlew.bat --stop
# Delete: .gradle/ e app/build/
.\gradlew.bat build
```

## Resolução de Problemas Comuns

### Erro: "SDK location not found"
Solução: Crie o arquivo local.properties na raiz do projeto:
```properties
sdk.dir=C\:\\Users\\SeuUsuario\\AppData\\Local\\Android\\Sdk
```

### Erro: "Gradle sync failed"
Soluções:
1. Verifique sua conexão com internet
2. Execute: .\gradlew.bat --stop
3. Invalide cache: File > Invalidate Caches / Restart
4. Reimporte o projeto

### Erro: "Could not resolve dependencies"
Solução:
1. Verifique o arquivo gradle/libs.versions.toml
2. Certifique-se de ter internet para baixar dependências
3. Tente: .\gradlew.bat build --refresh-dependencies

### Erro: "No connected devices"
Solução:
1. Para emulador: inicie-o manualmente no Device Manager
2. Para dispositivo físico: 
   - Verifique o cabo USB
   - Execute `adb devices` para listar dispositivos
   - Revogue/reautorize depuração USB

### Erro de Compilação KSP/Room
**Solução**:
1. Execute clean: `.\gradlew.bat clean`
2. Rebuild: **Build > Rebuild Project**

## 📱 Requisitos do Dispositivo

### Mínimo
- Android 7.0 (API 24)
- ~50 MB de espaço livre
- Permissão para acessar galeria de fotos

### Recomendado
- Android 10.0 (API 29) ou superior
- ~100 MB de espaço livre
- Conexão com internet (apenas para Coil carregar algumas imagens se necessário)

## 🎯 Verificando Instalação

Após instalar, verifique:
1. O ícone do BookLog aparece na lista de apps
2. Ao abrir, você vê a tela Home (pode estar vazia inicialmente)
3. O botão + no canto inferior direito está visível
4. Você consegue adicionar um livro de teste

## 📊 Performance Tips

### Para builds mais rápidos:
1. Aumente a memória do Gradle em `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
org.gradle.parallel=true
org.gradle.caching=true
```

2. Use o Gradle Daemon (já habilitado por padrão)

3. Compile apenas o variant necessário:
```powershell
.\gradlew.bat assembleDebug    # Apenas debug
```

## 🔐 Build de Produção

Para criar um APK de produção assinado:

### 1. Criar Keystore
```bash
keytool -genkey -v -keystore booklog-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias booklog
```

### 2. Configurar signing no `app/build.gradle.kts`
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../booklog-release-key.jks")
            storePassword = "sua-senha"
            keyAlias = "booklog"
            keyPassword = "sua-senha"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // ...
        }
    }
}
```

### 3. Build
```powershell
.\gradlew.bat assembleRelease
```

⚠️ **IMPORTANTE**: Nunca commite o keystore ou senhas no Git!

## ✅ Checklist Final

Antes de considerar o build concluído:
- [ ] Projeto compila sem erros
- [ ] Todos os testes passam (se houver)
- [ ] APK é gerado com sucesso
- [ ] Aplicativo instala no dispositivo/emulador
- [ ] Aplicativo abre sem crashes
- [ ] Funcionalidades principais funcionam:
  - [ ] Adicionar livro
  - [ ] Visualizar lista
  - [ ] Ver detalhes
  - [ ] Editar livro
  - [ ] Excluir livro
  - [ ] Buscar livros
  - [ ] Filtrar e ordenar

## 📞 Suporte

Se encontrar problemas:
1. Verifique a seção de Resolução de Problemas acima
2. Consulte os logs no Android Studio (Logcat)
3. Execute com mais detalhes: `.\gradlew.bat build --info`
4. Verifique a documentação do Android Studio

## 🎉 Sucesso!

Se você chegou até aqui e o app está rodando, parabéns! 
O BookLog está pronto para uso. Aproveite para organizar suas leituras! 📚


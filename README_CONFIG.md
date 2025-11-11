### 1. Clonar o repositório
```bash
git clone https://github.com/JoaoGiatti/PI_IV_ES_TIME12.git
```

---

### 2. Configurar o App (Android)
A **Google Maps API Key** é **necessária para o funcionamento do sistema de corrida**.

#### Como configurar a Google Maps API
1. Acesse o Google Cloud Console.
2. Crie um projeto ou use um já existente.
3. Ative as seguintes APIs obrigatórias:
    - **Maps SDK for Android**
4. Gere uma **API Key**.

#### Aplicando a Key no projeto
- No arquivo `local.properties`, adicione:
```
GOOGLE_MAPS_API_KEY=SUA_CHAVE_AQUI 
```

#### Obtendo a sua SHA1 e SHA256
Você pode usar o comando abaixo para gerar tanto a **SHA1** quanto a **SHA256** do certificado de debug:
```
keytool -list -v -keystore "C:\Users\SEU-USUARIO-AQUI\.android\debug.keystore" -storepass android
```

- No **Firebase**, habilite o login com Google.
- Adicione sua **SHA1** e **SHA256** ao Firebase.
- Baixe o arquivo **google-services.json** e coloque em: ``` PI_IV_ES_TIME12/chase/app ```

---

### 3. Configurar a API (Backend)
- Crie um arquivo `.env` em: ``` PI_IV_ES_TIME12/backend/.env ```
- Adicione:
```
MONGO_URI=<sua_uri_do_mongo>
MONGO_DATABASE=<seu_banco>
```

---
### 4. Executar o projeto
#### API
- Abra a pasta `PI_IV_ES_TIME12/backend` no **IntelliJ**.
- Execute a class **ChaseApplication**.
- Após isso a API deve rodar na porta **8080**

#### SERVIDOR MALIGNO
- Abra a pasta `PI_IV_ES_TIME12/server` no **IntelliJ**.
- Execute a class **Servidor.java**
- Após isso o Servidor rodará na porta padrão **3000**

#### APP
- Abra a pasta `PI_IV_ES_TIME12/chase` no **Android Studio**.
- Execute o app no emulador ou dispositivo físico.
- Após todo o processo o ‘app’ deve rodar normalmente.

---

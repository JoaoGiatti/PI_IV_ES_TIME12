# PI4-GrupoC
Integrative Project IV - Software Engineering 2025.2 - Pontifical Catholic University of Campinas (PUC-Campinas)


<img width=100% src="https://capsule-render.vercel.app/api?type=waving&color=00BFBF&height=120&section=header"/>

<div align="center">
    <img src="https://via.placeholder.com/100x100/00BFBF/FFFFFF?text=CHASE" alt="Chase Logo" width="100" margintop="-30"/>
</div>

<div align="center">
    <h1>Chase - Aplicativo de Corrida Gamificado</h1>
</div>

---

<h2 id="desc"> 📖 Descrição Geral</h2>

**Chase** é um aplicativo mobile que transforma a corrida em uma experiência gamificada e social. Usuários podem criar rotas, competir por melhores tempos, conquistar territórios e interagir com uma comunidade de corredores. O objetivo é combater a monotonia e a falta de motivação na prática de exercícios físicos, especialmente entre iniciantes e corredores ocasionais.

---

<h2 id="func"> 🚀 Funcionalidades</h2>

### 🗺️ Criação e Exploração de Rotas
- Crie rotas personalizadas usando GPS.
- Explore rotas criadas por outros usuários na sua cidade ou durante viagens.

### 🏆 Competição e Rankings
- Competir em rotas de outros usuários (time trial).
- Rankings por segmento com recordes pessoais (PR).
- Sistema de pontuação e badges por conquistas.

### 👥 Social e Comunidade
- Perfis de usuário com estatísticas e conquistas.
- Seguir amigos e comparar desempenho.
- Compartilhar rotas e resultados.

### 🎮 Gamificação
- Sistema de badges por metas (ex: primeiro PR, 5 tentativas concluídas).
- Pontuação por completar rotas.
- Conquista de territórios virtuais.

### 📱 Experiência do Usuário
- Funcionamento offline para registrar corridas sem internet.
- Notificações push para engajamento.
- Interface moderna e intuitiva com Jetpack Compose.

---

<h2 id="arq"> 📐 Arquitetura do Projeto</h2>

Utilizamos uma arquitetura moderna e escalável, combinando tecnologias robustas para frontend, backend e armazenamento.

### 🔹 Mobile (Kotlin + Jetpack Compose)
- Desenvolvimento nativo Android com UI declarativa.
- Integração com GPS e mapas para rastreamento em tempo real.

### 🔹 Backend (Java + Spring Boot)
- API RESTful para gerenciamento de usuários, rotas e rankings.
- Comunicação segura entre app e servidor.

### 🔹 Banco de Dados (MongoDB)
- Armazenamento flexível de dados de rotas, usuários e histórico de corridas.

### 🔹 Serviços Externos
- **Google Maps SDK**: Visualização e criação de rotas.
- **Firebase**: Autenticação, notificações e analytics.

---

<h2 id="obs">📝 Diferenciais</h2>

- **Gamificação Imersiva**: Transforma ruas e parques em arenas de competição.
- **Comunidade Colaborativa**: Rotas criadas e validadas pelos usuários.
- **Progressão Visual**: Acompanhamento claro de evolução e conquistas.
- **Motivação Social**: Competição saudável com amigos e ranking local.

---

<h2 id="tech"> 🛠️ Tecnologias Utilizadas</h2>

| Categoria                 | Tecnologia            | Descrição                                                       |
|--------------------------|-----------------------|------------------------------------------------------------------|
| Mobile Frontend          | Kotlin + Jetpack Compose | UI moderna e reativa para Android.                             |
| Backend                  | Java + Spring Boot    | API robusta e escalável.                                       |
| Banco de Dados           | MongoDB               | Armazenamento NoSQL flexível.                                  |
| Mapas e Geolocalização   | Google Maps SDK       | Criação e exibição de rotas.                                   |
| Autenticação             | Firebase Auth         | Gerenciamento seguro de usuários.                              |
| Notificações             | Firebase Cloud Messaging| Notificações push para engajamento.                            |
| Controle de Versão       | Git                   | Versionamento de código.                                       |
| Design e Prototipagem    | Figma                 | Interface e experiência do usuário.                            |

---

<h2 id="telas">💻 Telas do Sistema</h2>

<div align="center"><h3>Tela Inicial - Mapa de Rotas</h3></div>
<p align="center"><i>Visualize rotas próximas, crie novas rotas e navegue pelo mapa.</i></p>
<div align="center">
    <img src="https://via.placeholder.com/700x400/00BFBF/FFFFFF?text=Mapa+de+Rotas" alt="Tela Mapa" width="700"/>
</div>

<div align="center"><h3>Detalhes da Rota</h3></div>
<p align="center"><i>Informações detalhadas da rota, recordes e opção para competir.</i></p>
<div align="center">
    <img src="https://via.placeholder.com/700x400/008B8B/FFFFFF?text=Detalhes+da+Rota" alt="Detalhes Rota" width="700"/>
</div>

<div align="center"><h3>Perfil do Usuário</h3></div>
<p align="center"><i>Estatísticas, badges conquistados e histórico de corridas.</i></p>
<div align="center">
    <img src="https://via.placeholder.com/700x400/005F5F/FFFFFF?text=Perfil+do+Usuário" alt="Perfil" width="700"/>
</div>

<div align="center"><h3>Competição em Tempo Real</h3></div>
<p align="center"><i>Interface durante a corrida com tracking GPS e métricas.</i></p>
<div align="center">
    <img src="https://via.placeholder.com/700x400/004D4D/FFFFFF?text=Corrida+em+Andamento" alt="Corrida" width="700"/>
</div>

---

<h2 id="colab">🤝 Colaboradores</h2>
Um agradecimento especial à equipe de desenvolvimento do Chase.

<table>
  <!-- Informações sobre a Eduarda -->
  <tr>
    <td align="center" width="150px">
      <a href="https://github.com/eduardanepomuceno" style="color: #ffffff; text-decoration: none;">
        <img src="https://avatars.githubusercontent.com/u/SEU_USUARIO_AQUI" width="100px;" alt="Eduarda Profile Picture" style="border-radius: 50%;"/><br>
        <sub>
          <strong>Eduarda Nepomuceno</strong>
        </sub>
      </a>
    </td>
    <td>
      <h3>DESIGN E DESENVOLVIMENTO MOBILE</h3>
        <p>
            Responsável pela interface do usuário e experiência gamificada.
        </p>
    </td>
  </tr>

  <!-- Informações sobre o Jean -->
  <tr>
    <td align="center" width="150px">
      <a href="https://github.com/jeanyuki" style="color: #ffffff; text-decoration: none;">
        <img src="https://avatars.githubusercontent.com/u/SEU_USUARIO_AQUI" width="100px;" alt="Jean Profile Picture" style="border-radius: 50%;"/><br>
        <sub>
          <strong>Jean Yuki</strong>
        </sub>
      </a>
    </td>
    <td>
      <h3>BACKEND E BANCO DE DADOS</h3>
        <p>
            Desenvolvimento da API e integração com MongoDB.
        </p>
    </td>
  </tr>

  <!-- Informações sobre a Jhenifer -->
  <tr>
    <td align="center" width="150px">
      <a href="https://github.com/jheniferbarbosa" style="color: #ffffff; text-decoration: none;">
        <img src="https://avatars.githubusercontent.com/u/SEU_USUARIO_AQUI" width="100px;" alt="Jhenifer Profile Picture" style="border-radius: 50%;"/><br>
        <sub>
          <strong>Jhenifer Barbosa</strong>
        </sub>
      </a>
    </td>
    <td>
      <h3>DESENVOLVIMENTO MOBILE E TESTES</h3>
        <p>
            Implementação de funcionalidades e garantia de qualidade.
        </p>
    </td>
  </tr>

  <!-- Informações sobre o João -->
  <tr>
    <td align="center" width="150px">
      <a href="https://github.com/joaogiatti" style="color: #ffffff; text-decoration: none;">
        <img src="https://avatars.githubusercontent.com/u/91966589?v=4" width="100px;" alt="João Profile Picture" style="border-radius: 50%;"/><br>
        <sub>
          <strong>João Giatti</strong>
        </sub>
      </a>
    </td>
    <td>
      <h3>FULL-STACK E COORDENAÇÃO</h3>
        <p>
            Desenvolvimento full-stack e coordenação do projeto.
        </p>
    </td>
  </tr>

  <!-- Informações sobre o Thiago -->
  <tr>
    <td align="center" width="150px">
      <a href="https://github.com/thiagovolponi" style="color: #ffffff; text-decoration: none;">
        <img src="https://avatars.githubusercontent.com/u/SEU_USUARIO_AQUI" width="100px;" alt="Thiago Profile Picture" style="border-radius: 50%;"/><br>
        <sub>
          <strong>Thiago Volponi</strong>
        </sub>
      </a>
    </td>
    <td>
      <h3>TESTES E ANÁLISE DE DADOS</h3>
        <p>
            Garantia de qualidade e análise de métricas de uso.
        </p>
    </td>
  </tr>
</table>

<img width=100% src="https://capsule-render.vercel.app/api?type=waving&color=00BFBF&height=120&section=footer"/>
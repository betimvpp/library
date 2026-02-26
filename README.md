# 📚 Livraria — Sistema de Gerenciamento de Biblioteca

Projeto Java desenvolvido como exercício de OOP, implementando um sistema completo de gerenciamento de biblioteca com empréstimos, vendas, cadastro de clientes, autores e livros — tudo persistido em banco de dados SQLite.

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| SQLite (via JDBC) | sqlite-jdbc |
| Build Tool | Maven |

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:

```
com.livraria
├── database/       # Conexão e inicialização do banco de dados
├── exception/      # Exceções customizadas
├── model/          # Entidades (classes de domínio)
│   └── enums/      # Enum Role (ADMIN, CONSUMIDOR)
├── repository/     # Interfaces de acesso a dados
│   └── impl/       # Implementações JDBC
├── service/        # Regras de negócio
├── ui/             # Interface de console (menu interativo)
└── Main.java       # Ponto de entrada
```

---

## 📦 Entidades

### `Usuario` *(abstrata)*
| Campo | Tipo | Descrição |
|---|---|---|
| id | int | Autoincrement |
| cpf | String | Único, 11 dígitos |
| nomeCompleto | String | — |
| dataNascimento | LocalDate | — |
| role | Role | `ADMIN` ou `CONSUMIDOR` |
| email | String | Opcional |

**Subclasses:** `Admin` e `Consumidor`

---

### `Autor`
| Campo | Tipo | Descrição |
|---|---|---|
| id | int | Autoincrement |
| nomeCompleto | String | — |
| cpf | String | Único |
| dataNascimento | LocalDate | — |
| email | String | Opcional |

---

### `Livraria`
| Campo | Tipo | Descrição |
|---|---|---|
| id | int | Autoincrement |
| idAdmin | int | FK → `usuarios.id` (apenas ADMIN) |
| nome | String | — |
| cidade | String | — |

---

### `Livro`
| Campo | Tipo | Descrição |
|---|---|---|
| id | int | Autoincrement |
| idAutor | int | FK → `autores.id` |
| titulo | String | — |
| dataLancamento | LocalDate | — |
| dataAtualizacao | LocalDate | Nullable — preenchido ao atualizar |

---

### `LivroPorLivraria` *(acervo)*
| Campo | Tipo | Descrição |
|---|---|---|
| id | int | Autoincrement |
| idLivraria | int | FK → `livrarias.id` |
| idLivro | int | FK → `livros.id` |
| estoque | int | Total de cópias |
| estoqueDisponivel | int | Cópias disponíveis no momento |

> **Regra:** `estoqueDisponivel = estoque - empréstimos_ativos - vendas`

---

### `Emprestimo`
| Campo | Tipo | Descrição |
|---|---|---|
| id | int | Autoincrement |
| idLivraria | int | FK → `livrarias.id` |
| idLivroPorLivraria | int | FK → `livros_por_livraria.id` |
| idConsumidor | int | FK → `usuarios.id` |
| dataEmprestimo | LocalDate | — |
| dataDevolucao | LocalDate | Nullable — null = ainda em aberto |

---

### `Venda`
| Campo | Tipo | Descrição |
|---|---|---|
| id | int | Autoincrement |
| idLivraria | int | FK → `livrarias.id` |
| idLivroPorLivraria | int | FK → `livros_por_livraria.id` |
| idConsumidor | int | FK → `usuarios.id` |
| dataVenda | LocalDate | — |

---

## ⚙️ Regras de Negócio

- **CPF único** — Usuários e Autores não podem compartilhar CPF; lança `CpfJaCadastradoException` / `AutorCpfJaCadastradoException` (herdam de `CpfDuplicadoException`)
- **Livraria** — Somente um `Admin` pode criar uma livraria
- **Livro** — Somente um `Autor` cadastrado pode publicar livros; somente o Autor original pode atualizar o título
- **Empréstimo** — Só permitido se `estoqueDisponivel > 0`; decrementa o estoque temporariamente (devolução restaura)
- **Venda** — Decrementa `estoque` e `estoqueDisponivel` permanentemente

---

## 🖥️ Menu Interativo

```
╔══════════════════════════════════╗
║         MENU PRINCIPAL           ║
╠══════════════════════════════════╣
║  1. Livros disponíveis           ║
║  2. Realizar empréstimo          ║
║  3. Devolver livro               ║
║  4. Cadastrar novo livro         ║
║  5. Buscar por título            ║
║  6. Buscar por autor             ║
║  7. Listar clientes              ║
║  8. Cadastrar cliente            ║
║  9. Histórico de um cliente      ║
║ 10. Comprar livro                ║
║  0. Sair                         ║
╚══════════════════════════════════╝
```

- **Busca por título** — LIKE case-insensitive no nome do livro
- **Busca por autor** — JOIN com a tabela `autores`, LIKE no nome
- **Histórico** — Lista todos os empréstimos de um cliente com status de devolução
- **Identificação automática** — Nas opções de empréstimo e compra, o sistema identifica o cliente pelo CPF ou faz o cadastro na hora caso não exista

---

## 🚀 Como Executar

### Pré-requisitos
- JDK 17+
- Maven 3.x

### Passos

```bash
# Clone o repositório
git clone https://github.com/betimvpp/library.git
cd library

# Compile e execute
mvn compile exec:java -Dexec.mainClass="com.livraria.Main"
```

> ⚠️ **Na primeira execução**, o banco de dados `livraria.db` será criado automaticamente em `src/main/resources/` com um acervo de demonstração.  
> Se você alterar a estrutura do banco, delete o arquivo `.db` antes de rodar novamente.

---

## 🗃️ Diagrama do Banco de Dados

```
usuarios ─────────────────────┐
  id, cpf*, nome, nasc, role,  │
  email                         │
                                │
autores                         │
  id, nome, cpf*, nasc, email   │
     │                          │
     ▼                          │
   livros                       │
     id, id_autor(fk), titulo,  │
     data_lancamento,           │
     data_atualizacao(nullable) │
          │                     │
          ▼                     ▼
   livros_por_livraria       livrarias
     id, id_livraria(fk),      id, id_admin(fk),
     id_livro(fk),             nome, cidade
     estoque,
     estoque_disponivel
          │
    ┌─────┴─────┐
    ▼           ▼
emprestimos   vendas
  id, id_livraria(fk),
  id_livro_por_livraria(fk),
  id_consumidor(fk),
  data, data_devolucao(nullable)
```

---

## 📁 Exceções Customizadas

```
CpfDuplicadoException  (abstrata)
├── CpfJaCadastradoException       (Usuário)
└── AutorCpfJaCadastradoException  (Autor)
```

# MicroGestor - Sistema de Gestão Financeira para Microempreendedores Informais

> **AEP (Atividade de Estudo Programada) – 2º Bimestre / 2026.2**  
> **Instituição:** UniCesumar (Maringá - PR)  
> **Cursos Integrados:** Engenharia de Software (Esoft) & Análise e Desenvolvimento de Sistemas (ADS)  
> **Alinhamento:** ODS 8 da ONU – Trabalho Decente e Crescimento Econômico  

---

## 👥 Integrantes da Equipe

| Nome Completo | Registro Acadêmico (RA) | Função Principal no Desenvolvimento |
| :--- | :--- | :--- |
| **Eduardo André A. de Sant’Ana** | `25016225-2` | Arquitetura POO, Entidades de Domínio e Relatórios |
| **Kauan Maniezo de Souza** | `25001222-2` | Camada DAO, Transações JDBC e Modelagem MySQL |
| **Patrick Takeo dos Santos Oda** | `25362798-2` | Interface CLI de Console, Validações e Regras de Negócio |

**Repositório Oficial:** [github.com/eduuusantana/aep-microgestor](https://github.com/eduuusantana/aep-microgestor)

---

## 🎯 Sobre o Projeto

O **MicroGestor** é uma aplicação em **Java (Console / Terminal CLI)** projetada para microempreendedores informais do setor alimentício (ex.: venda de doces caseiros, bolos de pote e salgados). O sistema soluciona dores críticas desse público:
1. **Controle de Produtos e Cardápio:** CRUD completo para precificação e gerenciamento.
2. **Registro Rápido de Vendas:** Suporte a múltiplos itens com composição (1:N) e controle de pagamentos (PIX, Dinheiro, Cartão e Fiado).
3. **Registro de Despesas:** Acompanhamento de compras de insumos e embalagens.
4. **Fechamento de Caixa Automatizado:** Cálculo de receitas, despesas, lucro líquido real e valores pendentes no "fiado".

---

## 🏛️ Pilares da Programação Orientada a Objetos (POO) Aplicados

1. **Abstração:**
   - Classe abstrata `TransacaoFinanceira` com o método abstrato `public abstract double processarNoCaixa()`.
2. **Herança:**
   - `Receita` herda de `TransacaoFinanceira`.
   - `Despesa` herda de `TransacaoFinanceira`.
   - `Venda` herda de `Receita` (especialização comercial).
3. **Polimorfismo:**
   - Implementação de `@Override` no método `processarNoCaixa()`:
     - `Receita` retorna `+valorTotal`.
     - `Despesa` retorna `-valorTotal`.
   - A classe de serviço `CaixaService` processa listas heterogêneas de transações sem acoplamento.
4. **Composição (1:N):**
   - A classe `Venda` é composta por uma lista de `ItemVenda` (`List<ItemVenda>`), onde cada item calcula seu subtotal e referencia o `Produto` correspondente.
5. **Encapsulamento e Boas Práticas:**
   - Atributos privados/protegidos, acessíveis apenas via getters/setters com validações estritas (impedindo valores negativos ou nomes em branco).
   - Nomenclatura aderente ao padrão *CamelCase* e convenções da linguagem Java.

---

## 🗄️ Banco de Dados (MySQL / XAMPP)

O sistema utiliza persistência real via **JDBC** conectado ao **MySQL**. O script de criação das tabelas e carga inicial está localizado em:
📂 `database/schema.sql`

### Estrutura das Tabelas:
- **`produto`**: `id` (INT AUTO_INCREMENT PK), `nome` (VARCHAR), `preco_base` (DECIMAL).
- **`transacao`**: `id` (INT AUTO_INCREMENT PK), `tipo_transacao` (VARCHAR), `data_registro` (DATE), `valor_total` (DECIMAL), `forma_pagamento` (VARCHAR), `cliente` (VARCHAR), `fornecedor` (VARCHAR), `categoria` (VARCHAR).
- **`item_venda`**: `id` (INT AUTO_INCREMENT PK), `transacao_id` (FK), `produto_id` (FK), `quantidade` (INT), `preco_unitario` (DECIMAL).

---

## 🚀 Como Configurar e Executar com XAMPP

### 1. Pré-requisitos
- **Java JDK:** Versão 8 ou superior instalado.
- **XAMPP:** Com o módulo **MySQL** ativo.
- Qualquer IDE Java moderna (**IntelliJ IDEA, Eclipse, NetBeans ou VS Code**).

### 2. Configurando o Banco de Dados no XAMPP / phpMyAdmin
1. Abra o **XAMPP Control Panel** e clique em **Start** no módulo **MySQL**.
2. Abra seu navegador e acesse: [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
3. No menu lateral esquerdo, clique em **Novo (New)**.
4. Digite o nome da base de dados: `microgestor_db` e clique em **Criar**.
5. Clique na aba **SQL** no topo da página.
6. Copie e cole todo o conteúdo do arquivo `database/schema.sql` e clique em **Executar (Go)**.

> As credenciais padrão do XAMPP já estão configuradas no arquivo `src/main/resources/db.properties` (usuário: `root`, sem senha).

---

### 3. Executando o Projeto

#### Pela IDE (Recomendado):
1. Abra a pasta do projeto na sua IDE.
2. Aguarde o Maven carregar a dependência `mysql-connector-j`.
3. Navegue até `src/main/java/br/com/microgestor/app/Main.java`.
4. Clique com o botão direito e selecione **Run 'Main.main()'**.

---

## 📋 Divisão de Commits para a Equipe no Git

Para garantir a nota máxima no critério de versionamento em equipe:

1. **Commit 1 - Eduardo André (Model & POO):**
   - Arquivos: `pom.xml`, `.gitignore`, `src/main/java/br/com/microgestor/model/`
   - Mensagem: `feat(model): classes de dominio com abstracao, heranca e composicao`

2. **Commit 2 - Kauan Maniezo (DAO & MySQL):**
   - Arquivos: `database/schema.sql`, `src/main/resources/`, `src/main/java/br/com/microgestor/dao/`
   - Mensagem: `feat(dao): persistencia mysql e conexao jdbc com controle transacional`

3. **Commit 3 - Patrick Takeo (Service, CLI & Documentação):**
   - Arquivos: `src/main/java/br/com/microgestor/service/`, `src/main/java/br/com/microgestor/app/`, `README.md`
   - Mensagem: `feat(cli): menu interativo de console, fechamento de caixa e manual`

# MicroGestor - Sistema de Gestão Financeira para Microempreendedores

Atividade de Estudo Programada (AEP) - 2º Bimestre / 2026.2  
UniCesumar - Centro Universitário Cesumar (Maringá - PR)  
Cursos: Engenharia de Software e Análise e Desenvolvimento de Sistemas  
Alinhamento: ODS 8 - Trabalho Decente e Crescimento Econômico  

---

## 1. Integrantes da Equipe

* **Eduardo André A. de Sant'Ana** - RA: 25016225-2
* **Kauan Maniezo de Souza** - RA: 25001222-2
* **Patrick Takeo dos Santos Oda** - RA: 25362798-2

Repositório: https://github.com/eduuusantana/aep-microgestor

---

## 2. Visão Geral do Sistema

O MicroGestor é uma aplicação desenvolvida em Java com interface via Console (Terminal CLI), voltada para microempreendedores informais do setor de alimentos artesanais (como pequenos produtores de bolos, doces e salgados). 

O sistema visa solucionar a desorganização de registros financeiros em cadernos, a mistura de receitas do negócio com gastos pessoais e as perdas financeiras decorrentes de vendas a prazo ("fiado"). A solução automatiza o fluxo de entradas e saídas, permitindo o acompanhamento do saldo líquido real e dos valores a receber.

### Requisitos Funcionais Contemplados:
* **RF01:** Cadastro, listagem, atualização e exclusão de produtos com nome e preço base (CRUD completo).
* **RF02:** Registro de vendas com composição de múltiplos itens e cálculo automático de subtotais e totais.
* **RF03:** Registro de despesas categorizadas (insumos, embalagens, utilidades e transporte).
* **RF04:** Registro da forma de pagamento utilizada (PIX, Dinheiro, Cartão ou Fiado).
* **RF05:** Relatório consolidado de fechamento de caixa, demonstrando total de entradas, total de saídas, saldo líquido do período e valores pendentes no fiado.

---

## 3. Arquitetura e Orientação a Objetos

O projeto foi estruturado seguindo os princípios da Programação Orientada a Objetos e o padrão de arquitetura em camadas (Model, DAO, Service e App/View):

* **Abstração:** A classe abstrata `TransacaoFinanceira` centraliza os atributos comuns de movimentações financeiras (`id`, `dataRegistro`, `valorTotal`, `formaPagamento`) e declara o método abstrato `public abstract double processarNoCaixa()`.
* **Herança:** As classes `Receita` e `Despesa` herdam diretamente de `TransacaoFinanceira`. A classe `Venda` especializa `Receita`, incorporando o comportamento de comercialização com carrinho.
* **Polimorfismo:** Sobrescrita (`@Override`) do método `processarNoCaixa()`. Instâncias de `Receita` somam valor ao caixa, enquanto instâncias de `Despesa` subtraem valor. O `CaixaService` processa listas polimórficas de transações sem necessidade de acoplamento direto.
* **Composição (1:N):** A classe `Venda` possui uma composição com `ItemVenda` (`List<ItemVenda>`), onde cada item calcula seu subtotal multiplicando quantidade pelo preço unitário e referencia a entidade `Produto`.
* **Encapsulamento:** Todos os atributos possuem visibilidade restrita (private/protected) com métodos de acesso (getters/setters) e validações de integridade de dados (bloqueio de valores negativos e strings vazias).

---

## 4. Banco de Dados

A persistência é realizada em banco de dados relacional MySQL via JDBC. O script de inicialização encontra-se em `database/schema.sql`.

### Esquema Relacional:
* **`produto`:** Armazena o catálogo de itens comercializados (`id`, `nome`, `preco_base`).
* **`transacao`:** Armazena as movimentações financeiras de receitas, vendas e despesas (`id`, `tipo_transacao`, `data_registro`, `valor_total`, `forma_pagamento`, `cliente`, `fornecedor`, `categoria`).
* **`item_venda`:** Tabela associativa que implementa a composição da venda (`id`, `transacao_id`, `produto_id`, `quantidade`, `preco_unitario`), contendo restrição de integridade referencial com exclusão em cascata (`ON DELETE CASCADE`).

---

## 5. Cronograma e Entregas das Sprints

Conforme planejado no documento do 1º Bimestre, o desenvolvimento foi distribuído entre os membros da equipe:

| Sprint | Período | Módulo / Responsabilidade | Integrante Responsável |
| :--- | :--- | :--- | :--- |
| **Sprint 1** | 01/10 a 15/10 | Modelagem da classe `Produto` e implementação das operações de CRUD (`ProdutoDAO`). | Patrick |
| **Sprint 2** | 16/10 a 30/10 | Motor Financeiro: classe abstrata `TransacaoFinanceira`, herança e polimorfismo (`Receita`, `Despesa`, `Venda`, `ItemVenda`). | Kauan |
| **Sprint 3** | 01/11 a 15/11 | Modelagem relacional, script SQL (`schema.sql`) e persistência JDBC com controle transacional (`DatabaseConnection`, `TransacaoDAO`). | Eduardo |
| **Sprint 4** | 16/11 a 25/11 | Fechamento de caixa (`CaixaService`), interface interativa de console (`Main`), testes integrados e documentação final. | Todos |

---

## 6. Instruções de Instalação e Execução

### Pré-requisitos:
* Java JDK versão 8 ou superior instalado.
* Servidor MySQL ativo (porta 3306). Recomendado o uso do XAMPP.
* Maven (opcional, caso execute via linha de comando) ou IDE com suporte a Maven (IntelliJ IDEA, Eclipse, VS Code ou NetBeans).

### Configuração do Banco de Dados:
1. Inicie o serviço MySQL no painel de controle do XAMPP.
2. Acesse a interface administrativa (phpMyAdmin em `http://localhost/phpmyadmin` ou terminal MySQL).
3. Crie a base de dados:
   ```sql
   CREATE DATABASE microgestor_db;
   ```
4. Execute o script contido no arquivo `database/schema.sql` na base criada.

As configurações de conexão encontram-se em `src/main/resources/db.properties` (usuário padrão `root`, sem senha, porta 3306).

### Execução da Aplicação:

#### Via IDE (IntelliJ / Eclipse / VS Code):
1. Abra a pasta do projeto como um projeto Maven.
2. Aguarde o download das dependências declaradas no `pom.xml` (`mysql-connector-j`).
3. Localize e execute o arquivo `src/main/java/br/com/microgestor/app/Main.java`.

#### Via Linha de Comando (Maven):
```bash
mvn clean package
java -jar target/microgestor-app-1.0.0.jar
```

---

## 7. Estrutura de Diretórios

```text
aep-microgestor/
├── pom.xml
├── README.md
├── .gitignore
├── database/
│   └── schema.sql
├── docs/
│   ├── AEP1 - ESOFT4SN.pdf
│   ├── diagrama_banco_der.png
│   └── diagrama_classes_uml.png
└── src/
    └── main/
        ├── resources/
        │   └── db.properties
        └── java/
            └── br/com/microgestor/
                ├── model/
                │   ├── Produto.java
                │   ├── TransacaoFinanceira.java
                │   ├── Receita.java
                │   ├── Despesa.java
                │   ├── Venda.java
                │   └── ItemVenda.java
                ├── dao/
                │   ├── DatabaseConnection.java
                │   ├── ProdutoDAO.java
                │   └── TransacaoDAO.java
                ├── service/
                │   └── CaixaService.java
                └── app/
                    └── Main.java
```

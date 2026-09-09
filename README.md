# Projeto MicroGestor - Sistema de Gestão para Microempreendedores

Projeto interdisciplinar desenvolvido para a Atividade de Estudo Programada (AEP) do 4º Semestre (2026.2).
Cursos Integrados: Engenharia de Software (Esoft) e Análise e Desenvolvimento de Sistemas (ADS) - UniCesumar.

**ODS Vinculada:** ODS 8 - Trabalho Decente e Crescimento Econômico  
**Equipe de Desenvolvimento:**
- Eduardo
- Kauan
- Patrick

---

## 📌 Escopo do Sistema (Requisitos Funcionais)

- **RF01:** O sistema deve permitir o cadastro, consulta, alteração e exclusão de produtos do catálogo (nome e preço base).
- **RF02:** O sistema deve permitir o registro de operações de venda (entradas), vinculando produtos e suas respectivas quantidades.
- **RF03:** O sistema deve permitir o registro de despesas operacionais (saídas), como insumos e matérias-primas.
- **RF04:** O sistema deve permitir identificar a forma de pagamento de cada transação (Dinheiro, Pix, Cartão ou Fiado).
- **RF05:** O sistema deve gerar relatório de fechamento de caixa mensal (Total de Entradas, Total de Saídas e Lucro Líquido).

---

## 📅 Cronograma de Desenvolvimento (2º Bimestre)

| Período / Sprint | Módulo / Épico | Atividade Planejada | Responsável |
| :--- | :--- | :--- | :--- |
| **Sprint 1** (01/10 a 15/10) | Catálogo de Produtos | Modelagem da classe Produto e implementação das operações de CRUD. | Patrick |
| **Sprint 2** (16/10 a 30/10) | Motor Financeiro | Superclasse abstrata TransacaoFinanceira, herança e métodos polimórficos. | Kauan |
| **Sprint 3** (01/11 a 15/11) | Banco de Dados | Script SQL (/database) e conexão JDBC ativa com o banco relacional. | Eduardo |
| **Sprint 4** (16/11 a 25/11) | Relatórios e Testes | Fechamento de caixa, testes de integração entre camadas e documentação final. | Todos |

---

## 📂 Estrutura do Repositório

- `/src`: Código-fonte da aplicação orientada a objetos (2º Bimestre).
- `/docs`: Documentação formal de requisitos e arquitetura (AEP 1º Bimestre).
- `/database`: Scripts de definição de dados (DDL) e carga inicial (DML) do banco de dados.

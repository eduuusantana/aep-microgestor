package br.com.microgestor.dao;

import br.com.microgestor.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    public boolean salvarVenda(Venda venda) {
        String sqlTransacao = "INSERT INTO transacao (tipo_transacao, data_registro, valor_total, forma_pagamento, cliente) " +
                              "VALUES ('VENDA', ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_venda (transacao_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtTransacao = conn.prepareStatement(sqlTransacao, Statement.RETURN_GENERATED_KEYS)) {
                stmtTransacao.setDate(1, Date.valueOf(venda.getDataRegistro()));
                stmtTransacao.setDouble(2, venda.getValorTotal());
                stmtTransacao.setString(3, venda.getFormaPagamento());
                stmtTransacao.setString(4, venda.getCliente());

                int affected = stmtTransacao.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet rs = stmtTransacao.getGeneratedKeys()) {
                    if (rs.next()) {
                        venda.setId(rs.getInt(1));
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
                for (ItemVenda item : venda.getItens()) {
                    stmtItem.setInt(1, venda.getId());
                    stmtItem.setInt(2, item.getProduto().getId());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitario());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar venda: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean salvarDespesa(Despesa despesa) {
        String sql = "INSERT INTO transacao (tipo_transacao, data_registro, valor_total, forma_pagamento, fornecedor, categoria) " +
                     "VALUES ('DESPESA', ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(despesa.getDataRegistro()));
            stmt.setDouble(2, despesa.getValorTotal());
            stmt.setString(3, despesa.getFormaPagamento());
            stmt.setString(4, despesa.getFornecedor());
            stmt.setString(5, despesa.getCategoria());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        despesa.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar despesa: " + e.getMessage());
        }
        return false;
    }

    public List<TransacaoFinanceira> listarTodas() {
        List<TransacaoFinanceira> lista = new ArrayList<>();
        String sql = "SELECT id, tipo_transacao, data_registro, valor_total, forma_pagamento, cliente, fornecedor, categoria " +
                     "FROM transacao ORDER BY data_registro DESC, id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo_transacao");
                LocalDate data = rs.getDate("data_registro").toLocalDate();
                double valor = rs.getDouble("valor_total");
                String formaPgto = rs.getString("forma_pagamento");

                if ("VENDA".equalsIgnoreCase(tipo)) {
                    String cliente = rs.getString("cliente");
                    Venda venda = new Venda(id, data, valor, formaPgto, cliente);
                    carregarItensDaVenda(conn, venda);
                    lista.add(venda);
                } else if ("DESPESA".equalsIgnoreCase(tipo)) {
                    String fornecedor = rs.getString("fornecedor");
                    String categoria = rs.getString("categoria");
                    Despesa despesa = new Despesa(id, data, valor, formaPgto, fornecedor, categoria);
                    lista.add(despesa);
                } else {
                    String cliente = rs.getString("cliente");
                    Receita receita = new Receita(id, data, valor, formaPgto, cliente);
                    lista.add(receita);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar transações: " + e.getMessage());
        }
        return lista;
    }

    private void carregarItensDaVenda(Connection conn, Venda venda) {
        String sql = "SELECT iv.id, iv.quantidade, iv.preco_unitario, p.id as prod_id, p.nome, p.preco_base " +
                     "FROM item_venda iv " +
                     "INNER JOIN produto p ON iv.produto_id = p.id " +
                     "WHERE iv.transacao_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venda.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto(rs.getInt("prod_id"), rs.getString("nome"), rs.getDouble("preco_base"));
                    ItemVenda item = new ItemVenda(rs.getInt("id"), p, rs.getInt("quantidade"), rs.getDouble("preco_unitario"));
                    venda.adicionarItem(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar itens da venda #" + venda.getId() + ": " + e.getMessage());
        }
    }
}

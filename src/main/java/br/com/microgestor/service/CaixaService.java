package br.com.microgestor.service;

import br.com.microgestor.dao.TransacaoDAO;
import br.com.microgestor.model.Despesa;
import br.com.microgestor.model.Receita;
import br.com.microgestor.model.TransacaoFinanceira;

import java.util.List;

public class CaixaService {

    private final TransacaoDAO transacaoDAO;

    public CaixaService() {
        this.transacaoDAO = new TransacaoDAO();
    }

    public CaixaService(TransacaoDAO transacaoDAO) {
        this.transacaoDAO = transacaoDAO;
    }

    public double calcularSaldoLiquido() {
        List<TransacaoFinanceira> transacoes = transacaoDAO.listarTodas();
        double saldo = 0.0;
        for (TransacaoFinanceira t : transacoes) {
            saldo += t.processarNoCaixa();
        }
        return saldo;
    }

    public double calcularTotalReceitas() {
        List<TransacaoFinanceira> transacoes = transacaoDAO.listarTodas();
        double total = 0.0;
        for (TransacaoFinanceira t : transacoes) {
            if (t instanceof Receita) {
                total += t.getValorTotal();
            }
        }
        return total;
    }

    public double calcularTotalDespesas() {
        List<TransacaoFinanceira> transacoes = transacaoDAO.listarTodas();
        double total = 0.0;
        for (TransacaoFinanceira t : transacoes) {
            if (t instanceof Despesa) {
                total += t.getValorTotal();
            }
        }
        return total;
    }

    public double calcularTotalFiadoPendente() {
        List<TransacaoFinanceira> transacoes = transacaoDAO.listarTodas();
        double totalFiado = 0.0;
        for (TransacaoFinanceira t : transacoes) {
            if (t instanceof Receita && "FIADO".equalsIgnoreCase(t.getFormaPagamento())) {
                totalFiado += t.getValorTotal();
            }
        }
        return totalFiado;
    }

    public List<TransacaoFinanceira> obterExtratoGeral() {
        return transacaoDAO.listarTodas();
    }
}

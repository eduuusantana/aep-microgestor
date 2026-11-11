package br.com.microgestor.model;

import java.time.LocalDate;

public class Receita extends TransacaoFinanceira {
    protected String cliente;

    public Receita() {
        super();
    }

    public Receita(double valorTotal, String formaPagamento, String cliente) {
        super(valorTotal, formaPagamento);
        this.cliente = cliente;
    }

    public Receita(Integer id, LocalDate dataRegistro, double valorTotal, String formaPagamento, String cliente) {
        super(id, dataRegistro, valorTotal, formaPagamento);
        this.cliente = cliente;
    }

    @Override
    public double processarNoCaixa() {
        return this.valorTotal;
    }

    @Override
    public String getTipoTransacao() {
        return "RECEITA";
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return String.format("[RECEITA #%d] %s | Cliente: %s | Pgto: %s | +R$ %.2f",
                id != null ? id : 0, dataRegistro, (cliente != null ? cliente : "Balcão"), formaPagamento, valorTotal);
    }
}

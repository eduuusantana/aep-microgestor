package br.com.microgestor.model;

import java.time.LocalDate;

public abstract class TransacaoFinanceira {
    protected Integer id;
    protected LocalDate dataRegistro;
    protected double valorTotal;
    protected String formaPagamento;

    public TransacaoFinanceira() {
        this.dataRegistro = LocalDate.now();
    }

    public TransacaoFinanceira(double valorTotal, String formaPagamento) {
        this.dataRegistro = LocalDate.now();
        setValorTotal(valorTotal);
        this.formaPagamento = formaPagamento;
    }

    public TransacaoFinanceira(Integer id, LocalDate dataRegistro, double valorTotal, String formaPagamento) {
        this.id = id;
        this.dataRegistro = dataRegistro != null ? dataRegistro : LocalDate.now();
        setValorTotal(valorTotal);
        this.formaPagamento = formaPagamento;
    }

    public abstract double processarNoCaixa();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        if (valorTotal < 0) {
            throw new IllegalArgumentException("O valor total da transação não pode ser negativo.");
        }
        this.valorTotal = valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public abstract String getTipoTransacao();
}

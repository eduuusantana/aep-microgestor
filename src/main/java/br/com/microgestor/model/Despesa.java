package br.com.microgestor.model;

import java.time.LocalDate;

public class Despesa extends TransacaoFinanceira {
    private String fornecedor;
    private String categoria;

    public Despesa() {
        super();
    }

    public Despesa(double valorTotal, String formaPagamento, String fornecedor, String categoria) {
        super(valorTotal, formaPagamento);
        this.fornecedor = fornecedor;
        this.categoria = categoria;
    }

    public Despesa(Integer id, LocalDate dataRegistro, double valorTotal, String formaPagamento, String fornecedor, String categoria) {
        super(id, dataRegistro, valorTotal, formaPagamento);
        this.fornecedor = fornecedor;
        this.categoria = categoria;
    }

    @Override
    public double processarNoCaixa() {
        return -this.valorTotal;
    }

    @Override
    public String getTipoTransacao() {
        return "DESPESA";
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return String.format("[DESPESA #%d] %s | Categoria: %s | Fornecedor: %s | Pgto: %s | -R$ %.2f",
                id != null ? id : 0, dataRegistro, (categoria != null ? categoria : "Geral"),
                (fornecedor != null ? fornecedor : "Diversos"), formaPagamento, valorTotal);
    }
}

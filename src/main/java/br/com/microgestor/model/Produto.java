package br.com.microgestor.model;

public class Produto {
    private Integer id;
    private String nome;
    private double precoBase;

    public Produto() {
    }

    public Produto(String nome, double precoBase) {
        this.nome = nome;
        setPrecoBase(precoBase);
    }

    public Produto(Integer id, String nome, double precoBase) {
        this.id = id;
        this.nome = nome;
        setPrecoBase(precoBase);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        if (precoBase < 0) {
            throw new IllegalArgumentException("O preço base não pode ser negativo.");
        }
        this.precoBase = precoBase;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] %-30s | Preço: R$ %.2f", id, nome, precoBase);
    }
}

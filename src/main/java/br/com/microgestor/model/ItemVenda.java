package br.com.microgestor.model;

public class ItemVenda {
    private Integer id;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemVenda() {
    }

    public ItemVenda(Produto produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        setQuantidade(quantidade);
        setPrecoUnitario(precoUnitario);
    }

    public ItemVenda(Integer id, Produto produto, int quantidade, double precoUnitario) {
        this.id = id;
        this.produto = produto;
        setQuantidade(quantidade);
        setPrecoUnitario(precoUnitario);
    }

    public double getSubtotal() {
        return this.quantidade * this.precoUnitario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0) {
            throw new IllegalArgumentException("O preço unitário não pode ser negativo.");
        }
        this.precoUnitario = precoUnitario;
    }

    @Override
    public String toString() {
        String nomeProd = (produto != null) ? produto.getNome() : "Produto Indefinido";
        return String.format("  -> %dx %-28s @ R$ %.2f un = R$ %.2f",
                quantidade, nomeProd, precoUnitario, getSubtotal());
    }
}

package br.com.microgestor.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venda extends Receita {
    private final List<ItemVenda> itens;

    public Venda() {
        super();
        this.itens = new ArrayList<>();
    }

    public Venda(String formaPagamento, String cliente) {
        super(0.0, formaPagamento, cliente);
        this.itens = new ArrayList<>();
    }

    public Venda(Integer id, LocalDate dataRegistro, double valorTotal, String formaPagamento, String cliente) {
        super(id, dataRegistro, valorTotal, formaPagamento, cliente);
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemVenda item) {
        if (item == null) {
            throw new IllegalArgumentException("O item de venda não pode ser nulo.");
        }
        this.itens.add(item);
        recalcularValorTotal();
    }

    public void recalcularValorTotal() {
        double total = 0.0;
        for (ItemVenda item : itens) {
            total += item.getSubtotal();
        }
        this.valorTotal = total;
    }

    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

    @Override
    public String getTipoTransacao() {
        return "VENDA";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[VENDA #%d] %s | Cliente: %s | Pgto: %s | Total: R$ %.2f\n",
                id != null ? id : 0, dataRegistro, (cliente != null && !cliente.isEmpty() ? cliente : "Balcão"),
                formaPagamento, valorTotal));
        for (ItemVenda item : itens) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}

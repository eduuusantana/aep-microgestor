package br.com.microgestor.app;

import br.com.microgestor.dao.DatabaseConnection;
import br.com.microgestor.dao.ProdutoDAO;
import br.com.microgestor.dao.TransacaoDAO;
import br.com.microgestor.model.*;
import br.com.microgestor.service.CaixaService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private static final CaixaService caixaService = new CaixaService(transacaoDAO);

    public static void main(String[] args) {
        exibirBanner();

        boolean rodando = true;
        while (rodando) {
            exibirMenuPrincipal();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    menuGerenciarProdutos();
                    break;
                case 2:
                    registrarVenda();
                    break;
                case 3:
                    registrarDespesa();
                    break;
                case 4:
                    exibirFechamentoDeCaixa();
                    break;
                case 5:
                    testarConexaoBD();
                    break;
                case 0:
                    System.out.println("\n[MicroGestor] Encerrando o sistema. Até logo!");
                    rodando = false;
                    break;
                default:
                    System.out.println("\n[!] Opção inválida. Escolha uma opção do menu.");
            }
        }
    }

    private static void exibirBanner() {
        System.out.println("================================================================================");
        System.out.println("  Gestão Financeira Simplificada para Microempreendedores Informais");
        System.out.println("  UniCesumar - AEP 2026.2 | Engenharia de Software");
        System.out.println("  Equipe: Eduardo Sant'Ana | Kauan Maniezo | Patrick Oda");
        System.out.println("================================================================================");
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n----------------- [ MENU PRINCIPAL ] -----------------");
        System.out.println("  [1] Gerenciar Produtos (Cardápio / Insumos) - CRUD");
        System.out.println("  [2] Registrar Nova Venda");
        System.out.println("  [3] Registrar Nova Despesa (Insumos / Custos)");
        System.out.println("  [4] Fechamento de Caixa & Extrato Detalhado");
        System.out.println("  [5] Testar Conexão com o Banco MySQL (XAMPP)");
        System.out.println("  [0] Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void menuGerenciarProdutos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n----------- [ GESTÃO DE PRODUTOS - CRUD ] -----------");
            System.out.println("  [1] Listar Produtos Cadastrados");
            System.out.println("  [2] Cadastrar Novo Produto");
            System.out.println("  [3] Atualizar Produto Existente");
            System.out.println("  [4] Excluir Produto");
            System.out.println("  [0] Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();
            switch (opcao) {
                case 1:
                    listarProdutos();
                    break;
                case 2:
                    cadastrarProduto();
                    break;
                case 3:
                    atualizarProduto();
                    break;
                case 4:
                    excluirProduto();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("[!] Opção inválida.");
            }
        }
    }

    private static void listarProdutos() {
        System.out.println("\n--- Lista de Produtos Cadastrados ---");
        List<Produto> produtos = produtoDAO.listarTodos();
        if (produtos.isEmpty()) {
            System.out.println("(Nenhum produto cadastrado no momento)");
        } else {
            System.out.printf("%-6s | %-32s | %-12s\n", "ID", "NOME DO PRODUTO", "PREÇO BASE");
            System.out.println("---------------------------------------------------------");
            for (Produto p : produtos) {
                System.out.printf("#%-5d | %-32s | R$ %8.2f\n", p.getId(), p.getNome(), p.getPrecoBase());
            }
        }
    }

    private static void cadastrarProduto() {
        System.out.println("\n--- Cadastrar Novo Produto ---");
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Preço de venda (R$): ");
        double preco = lerDouble();

        try {
            Produto p = new Produto(nome, preco);
            if (produtoDAO.inserir(p)) {
                System.out.println("[SUCESSO] Produto cadastrado com sucesso! ID gerado: " + p.getId());
            } else {
                System.out.println("[ERRO] Não foi possível cadastrar o produto no banco.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO DE VALIDAÇÃO] " + e.getMessage());
        }
    }

    private static void atualizarProduto() {
        System.out.println("\n--- Atualizar Produto ---");
        listarProdutos();
        System.out.print("\nDigite o ID do produto que deseja atualizar: ");
        int id = lerOpcao();

        Produto p = produtoDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("[!] Produto não encontrado com o ID informado.");
            return;
        }

        System.out.print("Novo nome (pressione ENTER para manter '" + p.getNome() + "'): ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) {
            p.setNome(novoNome);
        }

        System.out.print("Novo preço (atual R$ " + String.format("%.2f", p.getPrecoBase()) + ") [digite 0 para manter]: ");
        double novoPreco = lerDouble();
        if (novoPreco > 0) {
            p.setPrecoBase(novoPreco);
        }

        if (produtoDAO.atualizar(p)) {
            System.out.println("[SUCESSO] Produto atualizado com sucesso!");
        } else {
            System.out.println("[ERRO] Falha ao atualizar o produto no banco.");
        }
    }

    private static void excluirProduto() {
        System.out.println("\n--- Excluir Produto ---");
        listarProdutos();
        System.out.print("\nDigite o ID do produto a remover: ");
        int id = lerOpcao();

        Produto p = produtoDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("[!] Produto não encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir '" + p.getNome() + "'? (S/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("S")) {
            if (produtoDAO.deletar(id)) {
                System.out.println("[SUCESSO] Produto excluído com sucesso.");
            } else {
                System.out.println("[ERRO] Não foi possível excluir. Verifique se o produto já está associado a vendas.");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void registrarVenda() {
        System.out.println("\n================ REGISTRAR NOVA VENDA ================");
        List<Produto> produtos = produtoDAO.listarTodos();
        if (produtos.isEmpty()) {
            System.out.println("[!] Cadastre ao menos um produto antes de registrar vendas.");
            return;
        }

        Venda venda = new Venda();

        boolean adicionando = true;
        while (adicionando) {
            listarProdutos();
            System.out.print("\nDigite o ID do produto para adicionar à venda: ");
            int idProd = lerOpcao();

            Produto prod = produtoDAO.buscarPorId(idProd);
            if (prod == null) {
                System.out.println("[!] Produto inválido.");
            } else {
                System.out.print("Quantidade de '" + prod.getNome() + "': ");
                int qtd = lerOpcao();
                if (qtd <= 0) {
                    System.out.println("[!] A quantidade deve ser positiva.");
                } else {
                    ItemVenda item = new ItemVenda(prod, qtd, prod.getPrecoBase());
                    venda.adicionarItem(item);
                    System.out.printf("Item adicionado: %s x %d = R$ %.2f\n", prod.getNome(), qtd, item.getSubtotal());
                }
            }

            System.out.print("\nDeseja adicionar outro produto nesta mesma venda? (S/N): ");
            String mais = scanner.nextLine().trim();
            if (!mais.equalsIgnoreCase("S")) {
                adicionando = false;
            }
        }

        if (venda.getItens().isEmpty()) {
            System.out.println("[!] Venda cancelada: nenhum item inserido.");
            return;
        }

        System.out.printf("\n>>> TOTAL PARCIAL DA VENDA: R$ %.2f <<<\n", venda.getValorTotal());

        System.out.print("Nome do cliente (opcional - pressione ENTER para 'Consumidor Final'): ");
        String cliente = scanner.nextLine().trim();
        venda.setCliente(cliente.isEmpty() ? "Consumidor Final" : cliente);

        System.out.println("\nSelecione a Forma de Pagamento:");
        System.out.println("  [1] PIX");
        System.out.println("  [2] Dinheiro");
        System.out.println("  [3] Cartão de Débito/Crédito");
        System.out.println("  [4] FIADO (Conta / A Receber)");
        System.out.print("Opção: ");
        int opPgto = lerOpcao();

        String formaPgto;
        switch (opPgto) {
            case 1: formaPgto = "PIX"; break;
            case 2: formaPgto = "DINHEIRO"; break;
            case 3: formaPgto = "CARTAO"; break;
            case 4: formaPgto = "FIADO"; break;
            default: formaPgto = "OUTRO"; break;
        }
        venda.setFormaPagamento(formaPgto);

        if (transacaoDAO.salvarVenda(venda)) {
            System.out.println("\n========================================================");
            System.out.println("[SUCESSO] Venda #" + venda.getId() + " registrada e persistida no MySQL!");
            System.out.printf("Total: R$ %.2f | Pagamento: %s | Cliente: %s\n",
                    venda.getValorTotal(), venda.getFormaPagamento(), venda.getCliente());
            System.out.println("========================================================");
        } else {
            System.out.println("[ERRO] Houve uma falha ao persistir a venda no banco de dados.");
        }
    }

    private static void registrarDespesa() {
        System.out.println("\n================ REGISTRAR NOVA DESPESA ================");
        System.out.print("Descrição / Fornecedor (ex: Atacadão, Casa de Embalagens): ");
        String fornecedor = scanner.nextLine().trim();

        System.out.println("Categoria da Despesa:");
        System.out.println("  [1] Insumos e Ingredientes");
        System.out.println("  [2] Embalagens e Descartáveis");
        System.out.println("  [3] Gás / Energia / Transporte");
        System.out.println("  [4] Outros Custos");
        System.out.print("Opção: ");
        int catOp = lerOpcao();

        String categoria;
        switch (catOp) {
            case 1: categoria = "INSUMOS"; break;
            case 2: categoria = "EMBALAGENS"; break;
            case 3: categoria = "UTILIDADES_TRANSPORTE"; break;
            default: categoria = "OUTROS"; break;
        }

        System.out.print("Valor total gasto (R$): ");
        double valor = lerDouble();

        System.out.println("Forma de Pagamento utilizada:");
        System.out.println("  [1] PIX");
        System.out.println("  [2] Dinheiro");
        System.out.println("  [3] Cartão de Crédito/Débito");
        System.out.print("Opção: ");
        int pgOp = lerOpcao();
        String formaPgto = (pgOp == 1) ? "PIX" : (pgOp == 2) ? "DINHEIRO" : "CARTAO";

        Despesa despesa = new Despesa(valor, formaPgto, fornecedor, categoria);

        if (transacaoDAO.salvarDespesa(despesa)) {
            System.out.println("\n========================================================");
            System.out.println("[SUCESSO] Despesa #" + despesa.getId() + " registrada no MySQL!");
            System.out.printf("Valor: -R$ %.2f | Categoria: %s | Fornecedor: %s\n",
                    despesa.getValorTotal(), despesa.getCategoria(), despesa.getFornecedor());
            System.out.println("========================================================");
        } else {
            System.out.println("[ERRO] Não foi possível salvar a despesa.");
        }
    }

    private static void exibirFechamentoDeCaixa() {
        System.out.println("\n================================================================================");
        System.out.println("                  RELATÓRIO DE FECHAMENTO DE CAIXA & FLUXO                      ");
        System.out.println("================================================================================");

        double totalReceitas = caixaService.calcularTotalReceitas();
        double totalDespesas = caixaService.calcularTotalDespesas();
        double saldoLiquido = caixaService.calcularSaldoLiquido();
        double totalFiado = caixaService.calcularTotalFiadoPendente();

        System.out.printf(" (+) Total de Entradas (Vendas / Receitas): R$ %10.2f\n", totalReceitas);
        System.out.printf(" (-) Total de Saídas (Despesas / Custos):   R$ %10.2f\n", totalDespesas);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf(" (=) SALDO LÍQUIDO DO CAIXA:                R$ %10.2f %s\n",
                saldoLiquido, (saldoLiquido >= 0 ? "[LUCRO]" : "[PREJUÍZO]"));
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf(" (!) Total em Aberto no FIADO (a receber):  R$ %10.2f\n", totalFiado);
        System.out.println("================================================================================");

        System.out.println("\n--- Extrato Analítico de Transações ---");
        List<TransacaoFinanceira> transacoes = caixaService.obterExtratoGeral();
        if (transacoes.isEmpty()) {
            System.out.println("(Nenhuma transação financeira registrada até o momento)");
        } else {
            for (TransacaoFinanceira t : transacoes) {
                System.out.println(t.toString());
            }
        }
    }

    private static void testarConexaoBD() {
        System.out.println("\n--- Testando Conexão com MySQL (XAMPP) ---");
        System.out.println("URL configurada: " + DatabaseConnection.getUrl());
        boolean conectado = DatabaseConnection.testarConexao();
        if (conectado) {
            System.out.println("[SUCESSO] Conexão com o banco de dados MySQL estabelecida com êxito!");
        } else {
            System.out.println("[FALHA] Não foi possível conectar ao MySQL.");
            System.out.println("Dicas para solução:");
            System.out.println(" 1. Abra o painel do XAMPP e clique em 'Start' no MySQL (porta 3306).");
            System.out.println(" 2. Acesse http://localhost/phpmyadmin e crie o banco 'microgestor_db'.");
            System.out.println(" 3. Verifique as credenciais no arquivo 'src/main/resources/db.properties'.");
        }
    }

    private static int lerOpcao() {
        try {
            String linha = scanner.nextLine().trim();
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                String linha = scanner.nextLine().trim().replace(",", ".");
                double valor = Double.parseDouble(linha);
                if (valor < 0) {
                    System.out.print("[!] O valor não pode ser negativo. Digite novamente: ");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("[!] Valor inválido. Digite um número decimal (ex: 8.50): ");
            }
        }
    }
}

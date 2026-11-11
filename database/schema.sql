CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco_base DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_transacao VARCHAR(20) NOT NULL,
    data_registro DATE NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL,
    cliente VARCHAR(100),
    fornecedor VARCHAR(100),
    categoria VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS item_venda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transacao_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (transacao_id) REFERENCES transacao(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

INSERT IGNORE INTO produto (id, nome, preco_base) VALUES 
(1, 'Bolo de Pote Chocolate', 8.50),
(2, 'Brigadeiro Gourmet (Unidade)', 2.50),
(3, 'Coxinha Tradicional de Frango', 6.00),
(4, 'Pão de Mel Caseiro Especial', 5.00),
(5, 'Torta Salgada de Frango (Fatia)', 9.00);

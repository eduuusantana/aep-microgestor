package br.com.microgestor.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String url = "jdbc:mysql://localhost:3306/microgestor_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String user = "root";
    private static String password = "";

    static {
        carregarConfiguracoes();
    }

    private static void carregarConfiguracoes() {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                url = prop.getProperty("db.url", url);
                user = prop.getProperty("db.user", user);
                password = prop.getProperty("db.password", password);
            }
        } catch (Exception e) {
            System.err.println("[AVISO] Não foi possível ler db.properties. Utilizando credenciais padrão.");
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                throw new SQLException("Driver JDBC do MySQL não encontrado no classpath.", ex);
            }
        }
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean testarConexao() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static String getUrl() {
        return url;
    }
}

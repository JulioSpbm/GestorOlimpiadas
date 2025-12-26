package com.laboratorio.olimpiadas.gestaoolimpiadas.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe Singleton responsável por gerir a conexão com a base de dados.
 * Garante que apenas uma instância da conexão esteja ativa em toda a aplicação.
 */
public class DBConnection {

    private static DBConnection instance;
    private final Connection connection;
    private static final String CONFIG_FILE_PATH = "../senhabd/config.txt";

    /**
     * Construtor privado para impedir a criação de múltiplas instâncias.
     * Inicializa a conexão com a base de dados.
     *
     * @throws SQLException caso ocorra algum erro ao estabelecer a conexão.
     */
    private DBConnection() throws SQLException, IOException {
        try {
            String[] dbConfig = getDatabaseConfigFromFile(CONFIG_FILE_PATH);
            String url = dbConfig[0];
            String user = dbConfig[1];
            String password = dbConfig[2];

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha na conexão com a base de dados. Verifique as configurações.",
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o ficheiro de configuração. Verifique o arquivo.",
                    "Erro de Leitura de Arquivo", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    /**
     * Lê as configurações do banco de dados de um ficheiro de texto.
     *
     * @param filePath o caminho do ficheiro de configuração.
     * @return um array de strings contendo [URL, usuário, senha].
     * @throws IOException caso ocorra um erro na leitura do ficheiro.
     */
    private String[] getDatabaseConfigFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String url = reader.readLine().trim();
            String user = reader.readLine().trim();
            String password = reader.readLine().trim();

            if (url.isEmpty() || user.isEmpty() || password.isEmpty()) {
                throw new IOException("O ficheiro de configuração contém informações inválidas ou está incompleto.");
            }

            return new String[]{url, user, password};
        }
    }

    /**
     * Obtém a instância única de DBConnection.
     * Se não existir uma instância, cria uma nova. Caso contrário, retorna a instância existente.
     *
     * @return a instância única de DBConnection
     * @throws SQLException caso ocorra algum erro ao estabelecer a conexão.
     */
    public static DBConnection getInstance() throws SQLException, IOException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Obtém a conexão com a base de dados.
     *
     * @return a conexão ativa com a base de dados.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Fecha a conexão com o banco de dados, se estiver ativa.
     *
     * @throws SQLException caso ocorra algum erro ao encerrar a conexão.
     */
    public static void closeConnection() throws SQLException {
        if (instance != null && instance.connection != null && !instance.connection.isClosed()) {
            instance.connection.close();
        }
        instance = null;
    }
}

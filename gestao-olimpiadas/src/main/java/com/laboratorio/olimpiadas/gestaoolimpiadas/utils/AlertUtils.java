package com.laboratorio.olimpiadas.gestaoolimpiadas.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import java.util.Optional;

/**
 * Classe utilitária para exibir diferentes tipos de alertas em JavaFX.
 * Fornece métodos estáticos para mostrar alertas informativos, de aviso, de erro e de confirmação.
 * Cada alerta pode ser personalizado com um título e uma mensagem.
 */
public class AlertUtils {

    /**
     * Exibe um alerta informativo com o título e a mensagem especificados.
     *
     * @param title   o título do alerta
     * @param message o conteúdo da mensagem do alerta
     */
    public static void showInfoAlert(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    /**
     * Exibe um alerta de aviso com o título e a mensagem especificados.
     *
     * @param title   o título do alerta
     * @param message o conteúdo da mensagem do alerta
     */
    public static void showWarningAlert(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }

    /**
     * Exibe um alerta de erro com o título e a mensagem especificados.
     *
     * @param title   o título do alerta
     * @param message o conteúdo da mensagem do alerta
     */
    public static void showErrorAlert(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    /**
     * Exibe um alerta de confirmação com o título e a mensagem especificados.
     * Retorna verdadeiro se o utilizador clicar em "Sim", e falso se clicar em "Não".
     *
     * @param title   o título do alerta
     * @param message o conteúdo da mensagem do alerta
     * @return boolean indicando a escolha do utilizador (true = Sim, false = Não)
     */
    public static boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonYes = new ButtonType("Sim");
        ButtonType buttonNo = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }

    /**
     * Método auxiliar para exibir um alerta genérico com o tipo, título e mensagem especificados.
     *
     * @param alertType o tipo de alerta a exibir (INFORMATION, WARNING, ERROR ou CONFIRMATION)
     * @param title     o título do alerta
     * @param message   o conteúdo da mensagem do alerta
     */
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Local;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.LocalService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controlador da interface gráfica para criação de novos locais.
 */
public class LocalController {

    @FXML
    public AnchorPane localCreatePane;
    @FXML
    public AnchorPane localEditPane;
    @FXML
    public TextField fldID;
    @FXML
    public TextField fldName;
    @FXML
    public TextField fldAddress;
    @FXML
    public TextField fldCapacity;
    @FXML
    public TextField fldCity;
    @FXML
    public TextField fldYearConstruction;
    @FXML
    public ComboBox<String> fldTipoLocal;
    public static boolean isCreateMode = false;


    private TableView<Local> tableViewLocais;
    private LocalService localService;

    public LocalController() {
        this.localService = new LocalService();
    }

    public void inicializarTabela(TableView<Local> tableViewLocais) {
        this.tableViewLocais = tableViewLocais;
    }

    /**
     * Inicializa a configuração da interface gráfica ao abrir a vista.
     */
    public void initialize(){
        configurarRestricoesDeCampos();
        if(isCreateMode){
            carregarComBox();
        }
    }

    /**
     * Ação do botão para adicionar um novo local. Verifica se todos os campos estão preenchidos
     * e chama o serviço para inserir o local.
     */
    @FXML
    public void btnAddLocal() {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        boolean success = LocalService.insertLocal(
                fldName.getText(),
                fldAddress.getText(),
                fldCity.getText(),
                fldCapacity.getText(),
                fldYearConstruction.getText(),
                fldTipoLocal.getValue()
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Local inserido com sucesso!");
            closeCurrentWindowCreate();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Local não foi inserido!");
        }
    }
    @FXML
    public void btnEditLocal() {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }
        boolean success = LocalService.updateLocal(
                Integer.parseInt(fldID.getText()),
                fldName.getText(),
                fldAddress.getText(),
                fldCity.getText(),
                fldCapacity.getText(),
                fldYearConstruction.getText(),
                fldTipoLocal.getValue()
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Local editado com sucesso!");
            closeCurrentWindowEdit();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Local não foi editado!");
        }
    }

    /**
     * Verifica se todos os campos obrigatórios foram preenchidos.
     * @return true se todos os campos estiverem preenchidos; caso contrário, false.
     */
    private boolean areFieldsValid() {
        try {
            return fldName.getText() != null &&
                    fldAddress.getText() != null && fldCity.getText() != null &&
                    fldCapacity.getText() != null && fldName.getText() != null &&
                    fldYearConstruction.getText() != null;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * Configura as restrições de entrada para campos numéricos, permitindo apenas dígitos nos campos
     * de capacidade e ano de construção.
     */
    private void configurarRestricoesDeCampos() {
        fldYearConstruction.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                fldYearConstruction.setText(oldValue);
            }
        });

        fldCapacity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                fldCapacity.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void carregarDados() {
        if (tableViewLocais != null) {
            ObservableList<Local> locais = FXCollections.observableArrayList(localService.listarLocais());
            tableViewLocais.setItems(locais);
        }
    }

    public void carregarComBox() {
        ObservableList<String> locais = FXCollections.observableArrayList("Exterior", "Interior");
        fldTipoLocal.setItems(locais);
    }

    /**
     * Fecha a janela atual do formulário de criação de locais.
     */
    private void closeCurrentWindowCreate() {
        Stage currentStage = (Stage) localCreatePane.getScene().getWindow();
        EventHandler<WindowEvent> closeHandler = currentStage.getOnCloseRequest();
        if (closeHandler != null) {
            closeHandler.handle(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        currentStage.close();
    }

    private void closeCurrentWindowEdit() {
        Stage currentStage = (Stage) localEditPane.getScene().getWindow();
        EventHandler<WindowEvent> closeHandler = currentStage.getOnCloseRequest();
        if (closeHandler != null) {
            closeHandler.handle(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        currentStage.close();
    }

    public void setLocal(Local selectedLocal) {
        fldID.setText(String.valueOf(selectedLocal.getId()));
        fldName.setText(selectedLocal.getNome());
        fldAddress.setText(selectedLocal.getMorada());
        fldCity.setText(selectedLocal.getCidade());
        fldCapacity.setText(String.valueOf(selectedLocal.getCapacidade()));
        fldYearConstruction.setText(String.valueOf(selectedLocal.getAnoConstrucao()));
    }

    public static void setCreateMode() {
        isCreateMode = true;
    }
}

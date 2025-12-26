package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.EquipaDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Equipa;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Modalidade;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Participacao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.AtletaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.EquipaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.ModalidadeService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para a interface de gestão de Equipa.
 */
public class EquipaController {

    @FXML
    private AnchorPane equipaCreatePane;
    @FXML
    private AnchorPane equipaEditPane;
    @FXML
    private TextField fldName;
    @FXML
    private ComboBox<String> cmboxCountry;
    @FXML
    private ComboBox<String> cmboxModalitie;
    @FXML
    private TextField fldYearFoundation;
    @FXML
    private ComboBox<String> cmboxGender;
    private TableView<Equipa> tableViewEquipas;
    private EquipaService equipaService;

    @FXML
    private TableView<Equipa> tableViewImportTeam;
    @FXML
    private TableColumn<Equipa, String> colNomeTeam, colPaisTeam, colGeneroTeam, colSportTeam,colResultTeam;
    @FXML
    private TableColumn<Equipa, Integer> colYearTeam,colAnoTeam;
    @FXML
    private TableView<Participacao> tableViewParticipacoesTeam;
    @FXML
    private Button btnImportTeam;

    private EquipaDAO equipaDAO = new EquipaDAO();

    private static boolean isCreateTeam = false;

    public EquipaController() {
        this.equipaService = new EquipaService();
    }

    /**
     * Método para configurar a referência da TableView do FXML
     */
    public void inicializarTabela(TableView<Equipa> tableViewEquipas) {
        this.tableViewEquipas = tableViewEquipas;
    }

    public static void setCreateTeamMode() {
        isCreateTeam = true;
    }

    /**
     * Inicializa o controlador da Equipa carregando as opções e configurando restrições.
     */
    public void initialize() throws SQLException {
        if (isCreateTeam) {
            carregarOpcoes();
            configurarRestricoesDeCampos();
        }
    }

    /**
     * Método executado ao clicar no botão de adicionar equipa.
     */
    @FXML
    public void btnAddTeam() throws SQLException {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        boolean success = EquipaService.insertEquipa(
                fldName.getText(),
                cmboxCountry.getValue(),
                cmboxModalitie.getValue(),
                cmboxGender.getValue(),
                Integer.parseInt(fldYearFoundation.getText())
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Equipa inserida com sucesso!");
            closeCurrentWindowCreate();
            carregarDados();
        } else {
            AlertUtils.showErrorAlert("Erro!", "A equipa não foi inserida!");
        }
    }

    /**
     * Mostrar os dados das equipas
     * @param equipas
     */
    public void mostrarDados(List<Equipa> equipas) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/import_teams.fxml"));
            loader.setController(this);
            SplitPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dados das Modalidades");
            stage.initModality(Modality.APPLICATION_MODAL);

            configurarTabelaEquipas(equipas);
            configurarTabelaParticipacoes();
            configurarListeners();

            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para amostrar os dados importados dos atletas.
     * @param equipas Lista de Atletas.
     */
    private void configurarTabelaEquipas(List<Equipa> equipas) {
        List<Equipa> equipasUnicas = equipas.stream().distinct().toList();

        colNomeTeam.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPaisTeam.setCellValueFactory(new PropertyValueFactory<>("paisNome"));
        colGeneroTeam.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colSportTeam.setCellValueFactory(new PropertyValueFactory<>("modalidadeNome"));
        colYearTeam.setCellValueFactory(new PropertyValueFactory<>("anoFundacao"));

        tableViewImportTeam.getItems().setAll(equipasUnicas);
    }

    /**
     * Metodo para mostrar participacoes e resultado na tabela participacoes.
     */
    private void configurarTabelaParticipacoes() {
        colAnoTeam.setCellValueFactory(new PropertyValueFactory<>("anoParticipacao"));
        colResultTeam.setCellValueFactory(new PropertyValueFactory<>("resultadoNome"));
    }

    /**
     * Configura os listeners para a interação com a tabela e o botão de importação.
     */
    private void configurarListeners() {
        tableViewImportTeam.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                atualizarFormulario(newVal);
            }
        });

        btnImportTeam.setOnAction(e -> {
            try {
                importEquipas(tableViewImportTeam.getItems());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Importa as equipas para o sisstema.
     * @param equipas
     * @throws SQLException
     */
    private void importEquipas(ObservableList<Equipa> equipas) throws SQLException {
        Stage stage = (Stage) tableViewImportTeam.getScene().getWindow();
        EquipaService.importTeam(equipas, stage);
    }

    /**
     * Atualiza o formulário com as participações da equipa.
     * @param equipa
     */
    private void atualizarFormulario(Equipa equipa) {
        tableViewParticipacoesTeam.getItems().setAll(equipa.getParticipacoes());
    }

    /**
     * Método executado ao clicar no botão de editar equipa.
     */
    @FXML
    public void btnEditTeam() throws SQLException {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        boolean success = EquipaService.editEquipa(
                fldName.getText(),
                cmboxCountry.getValue(),
                cmboxModalitie.getValue(),
                cmboxGender.getValue(),
                Integer.parseInt(fldYearFoundation.getText())
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Equipa editada com sucesso!");
            closeCurrentWindowEdit();
        } else {
            AlertUtils.showErrorAlert("Error!", "team não foi editada!");
        }
    }

    /**
     * Verifica todos os campos para ver se estao vazios.
     */
    private boolean areFieldsValid() {
        return fldName.getText() != null && cmboxCountry.getValue() != null &&
                cmboxModalitie.getValue() != null && fldYearFoundation.getText() != null &&
                cmboxGender.getValue() != null;
    }

    /**
     * Carrega as opções de países, modalidades e géneros na interface.
     */
    void carregarOpcoes() throws SQLException {

        ObservableList<String> countries = FXCollections.observableArrayList(EquipaService.getCountries());
        cmboxCountry.setItems(countries);

        ObservableList<String> genderOptions = FXCollections.observableArrayList("Masculino", "Feminino");
        cmboxGender.setItems(genderOptions);

        ObservableList<String> modalidadesOptions = FXCollections.observableArrayList(EquipaService.getModalidades());
        cmboxModalitie.setItems(modalidadesOptions);
    }

    /**
     * Configura restrições para os campos de texto da interface.
     */
    private void configurarRestricoesDeCampos() {
        fldYearFoundation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                fldYearFoundation.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Carrega os dados das equipas e mostra na tabela.
     */
    public void carregarDados() {
        if (tableViewEquipas != null) {
            ObservableList<Equipa> equipas = FXCollections.observableArrayList(equipaService.listarEquipas());
            tableViewEquipas.setItems(equipas);
        }
    }

    /**
     * Fecha a janela atual ou seja equipaCreate.
     */
    private void closeCurrentWindowCreate() {
        Stage currentStage = (Stage) equipaCreatePane.getScene().getWindow();
        EventHandler<WindowEvent> closeHandler = currentStage.getOnCloseRequest();
        if (closeHandler != null) {
            closeHandler.handle(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        currentStage.close();
    }

    /**
     * Fecha a janela atual ou seja equipaEdit.
     */
    private void closeCurrentWindowEdit() {
        Stage currentStage = (Stage) equipaEditPane.getScene().getWindow();
        EventHandler<WindowEvent> closeHandler = currentStage.getOnCloseRequest();
        if (closeHandler != null) {
            closeHandler.handle(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        currentStage.close();
    }

    /**
     * Preenche os campos do formulário com os dados de uma equipa selecionada.
     * @param selectedEquipa
     */
    public void setEquipa(Equipa selectedEquipa) {
        fldName.setText(selectedEquipa.getNome());
        cmboxCountry.setValue(selectedEquipa.getPaisNome());
        cmboxModalitie.setValue(selectedEquipa.getModalidadeNome());
        cmboxGender.setValue(selectedEquipa.getGeneroNome());
        fldYearFoundation.setText(String.valueOf(selectedEquipa.getAnoFundacao()));
    }

    public List<Equipa> obterEquipasFiltradas(int id_modalidade) {
        List<Equipa> equipas = equipaService.listarEquipasFiltradas(id_modalidade);
        return equipas;
    }
}
package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Evento;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Participacao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.AtletaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador Atletas.
 */
public class AtletaController {

    @FXML
    public Pane atletaCreatePane;
    @FXML
    public Pane atletaEditPane;
    @FXML
    public TextField fldID;
    @FXML
    public TextField fldName;
    @FXML
    public TextField fldBirthDate;
    @FXML
    public TextField fldHeigth;
    @FXML
    public TextField fldWeigth;
    @FXML
    public ComboBox<String> cmboxCountry;
    @FXML
    public ComboBox<String> cmboxGender;
    @FXML
    public ComboBox<String> cmboxEstadoAtleta;

    @FXML
    private TableView<Atleta> tableViewAtletas;
    private final AtletaService atletaService;
    @FXML
    private TableView<Atleta> tableViewImportAtletas;
    @FXML
    private TableColumn<Atleta, String> colNome, colPais, colDataNascimento, colGenero, colEstadoAtleta;
    @FXML
    private TableColumn<Atleta, Integer> colAltura, colPeso;
    @FXML
    private TableView<Participacao> tableViewParticipacoes;
    @FXML
    private TableColumn<Participacao, Integer> colAno, colOuro, colPrata, colBronze;
    @FXML
    private Button btnAddParticipacao;

    /**
     * Variavel para verificação se é creação do atleta.
     */
    private static boolean isCreateAthlete = false;

    /**
     * Variavel para verificação se é edição do atleta.
     */
    private static boolean isEditAthlete = false;

    /**
     * Inicialização do AtletaService.
     */
    public AtletaController() {
        this.atletaService = new AtletaService();
    }

    /**
     * Metodo para setar o atleta.
     * @param atleta Atleta.
     */

    /**
     * Inicialização da Tabela para visualizar os Atletas.
     * @param tableViewAtletas Tabela dos Atletas
     */
    public void inicializarTabela(TableView<Atleta> tableViewAtletas) {
        this.tableViewAtletas = tableViewAtletas;
    }

    /**
     * Metodo para defenir se é realmente a criação de um atleta ou não.
     */
    public static void setCreateAthleteMode() {
        isCreateAthlete = true;
    }

    /**
     * Inicializa o controlador da vista de Atleta.
     * Carrega as listas de opções e configura as restrições de entrada.
     * Se o mesmo for para criar atleta.
     * @Execption SQLException
     */
    public void initialize() throws SQLException {
        if (isCreateAthlete) {
            carregarOpcoes();
            configurarRestricoesDeCampos();
        }
    }

    /**
     * Adiciona um novo atleta, validando os campos e inserindo-o na base de dados.
     * @Execption SQLException
     */
    @FXML
    public void btnAddAtleta() throws SQLException, IOException {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        boolean success = AtletaService.insertAtletas(
                fldName.getText(),
                cmboxCountry.getValue(),
                cmboxGender.getValue(),
                fldHeigth.getText(),
                fldWeigth.getText(),
                fldBirthDate.getText()
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Atleta inserido com sucesso!");
            closeCurrentWindowCreate();
            carregarDados();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Atleta não foi inserido!");
        }
    }

    /**
     * Metodo para editar um atleta.
     * @Execption SQLException
     */
    @FXML
    public void btnEditAthlete() throws SQLException, IOException {
        if (!areFieldsValid()) {

            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        boolean success = AtletaService.updateAtleta(
                Integer.valueOf(fldID.getText()),
                fldName.getText(),
                cmboxCountry.getValue(),
                cmboxGender.getValue(),
                fldHeigth.getText(),
                fldWeigth.getText(),
                fldBirthDate.getText(),
                cmboxEstadoAtleta.getValue()
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Atleta atualizado com sucesso!");
            closeCurrentWindowEdit();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Atleta não foi atualizado!");
        }
    }

    /**
     * Preenche os campos da interface com os dados de um atleta.
     */
    public void setAtleta(Atleta atleta) {
        fldID.setText(String.valueOf(atleta.getId()));
        fldName.setText(atleta.getNome());
        cmboxCountry.setValue(atleta.getPaisNome());
        cmboxGender.setValue(atleta.getGeneroNome());
        fldHeigth.setText(String.valueOf(atleta.getAltura()));
        fldWeigth.setText(String.valueOf(atleta.getPeso()));
        fldBirthDate.setText(atleta.getDataNascimento());
        cmboxEstadoAtleta.setValue(atleta.getEstado());
    }

    /**
     * Metodo para verificar se os TextField são validos. que não sejam vazios.
     */
    private boolean areFieldsValid() {
        return fldName.getText() != null && fldBirthDate.getText() != null && fldHeigth.getText() != null &&
                fldWeigth.getText() != null && cmboxCountry.getValue() != null && cmboxGender.getValue() != null;
    }

    /**
     * Metodo para popular as comboBox com paises e generos.
     * @Execption SQLException
     */
    public void carregarOpcoes() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList(AtletaService.getCountries());
        cmboxCountry.setItems(countries);

        ObservableList<String> genderOptions = FXCollections.observableArrayList("Masculino", "Feminino");
        ObservableList<String> estado = FXCollections.observableArrayList("Desativado", "Ativado");
        cmboxGender.setItems(genderOptions);
        cmboxEstadoAtleta.setItems(estado);
    }

    /**
     * Metodo de restricoes de campos para nao premitir caracteres em inteiros.
     */
    private void configurarRestricoesDeCampos() {
        fldHeigth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) fldHeigth.setText(oldValue);
        });
        fldWeigth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) fldWeigth.setText(oldValue);
        });
    }
    /**
     * Metodo para popular as tabelas com dados dos atletas.
     */
    public void carregarDados() {
        if (tableViewAtletas != null) {
            ObservableList<Atleta> atletas = FXCollections.observableArrayList(atletaService.listarAtletas());
            tableViewAtletas.setItems(atletas);
        }
    }

    /**
     * Metodo para inicializar fxml visualizar importação dos atletas.
     * @param atletas Lista de Atletas.
     */
    public void mostrarDados(List<Atleta> atletas) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/import_athlete.fxml"));

            loader.setController(this);
            SplitPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dados dos Atletas");
            stage.initModality(Modality.APPLICATION_MODAL);

            configurarTabelaAtletas(atletas);
            configurarTabelaParticipacoes();
            configurarListeners();

            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    /**
     * Metodo para amostrar os dados importados dos atletas.
     * @param atletas Lista de Atletas.
     */
    private void configurarTabelaAtletas(List<Atleta> atletas) {
        List<Atleta> atletasUnicos = atletas.stream().distinct().toList();

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("paisNome"));
        colDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colEstadoAtleta.setCellValueFactory(new PropertyValueFactory<>("EstadoAtleta"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colAltura.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " cm");
                }
            }
        });
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colPeso.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " kg");
                }
            }
        });

        tableViewImportAtletas.getItems().setAll(atletasUnicos);
    }

    /**
     * Metodo para amostrar os dados importados das particiapações dos atletas.
     */
    private void configurarTabelaParticipacoes() {
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoParticipacao"));
        colOuro.setCellValueFactory(new PropertyValueFactory<>("ouro"));
        colPrata.setCellValueFactory(new PropertyValueFactory<>("prata"));
        colBronze.setCellValueFactory(new PropertyValueFactory<>("bronze"));
    }

    /**
     * Metodo para amostrar os dados importados das particicpações de cada atleta selecionado.
     */
    private void configurarListeners() {
        tableViewImportAtletas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                atualizarFormulario(newVal);
            }
        });

        btnAddParticipacao.setOnAction(e -> {
            try {
                importAtletas(tableViewImportAtletas.getItems());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Metodo para atualizar cada particiapação quando o atleta é selecionado.
     * @param atleta Atleta.
     */
    private void atualizarFormulario(Atleta atleta) {
        tableViewParticipacoes.getItems().setAll(atleta.getParticipacoes());
    }

    /**
     * Metodo para importar Atletas.
     * @param atletas Lista de Atletas.
     * @exception SQLException SQL exception
     */
    private void importAtletas(ObservableList<Atleta> atletas) throws SQLException {
        Stage stage = (Stage) tableViewParticipacoes.getScene().getWindow();
        AtletaService.importAtletas(atletas, stage);
    }

    /**
     * Fecha a janela atual ou seja equipaCreate.
     */
    private void closeCurrentWindowCreate() {
        Stage currentStage = (Stage) atletaCreatePane.getScene().getWindow();
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
        Stage currentStage = (Stage) atletaEditPane.getScene().getWindow();
        EventHandler<WindowEvent> closeHandler = currentStage.getOnCloseRequest();
        if (closeHandler != null) {
            closeHandler.handle(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        currentStage.close();
    }

}

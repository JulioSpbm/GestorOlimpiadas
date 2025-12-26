package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.ModalidadeDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Modalidade;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.AtletaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.ModalidadeService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ModalidadeController {

    @FXML
    public Pane modaliteCreatePane;
    @FXML
    public Pane modaliteEditPane;
    @FXML
    public ComboBox<String> cmboxTipo;
    @FXML
    public TextField fldDescription;
    @FXML
    public TextField flsMinParticipants;
    @FXML
    public TextField flsNumGames;
    @FXML
    public TextField fldRules;
    @FXML
    public TextField fldName;
    @FXML
    public TextField fldID;
    @FXML
    public ComboBox<String> cmboxScore;
    @FXML
    public ComboBox<String> cmboxEstado;
    @FXML
    public ComboBox<String> cmboxGender;
    @FXML
    private TableView<Modalidade> tableViewModalidades;

    @FXML
    private TableColumn<Atleta, String> colModType,colModRecordName,colModRecordAno,colModRecordRecord,colModWinnerName,colModWinnerAno,colModWinnerRecord,colModName,colModGenre,colModDescription,colModScore,colModWinner,colModRules,colModNumJogos;
    @FXML
    private TableColumn<Atleta, Integer> colModMinParti;

    @FXML
    private TableView<Modalidade> tableViewImportModalidades;

    @FXML
    private Button btnImportModalidades;

    private ModalidadeService modalidadeService;

    public ModalidadeController() {
        this.modalidadeService = new ModalidadeService();
    }

    private static boolean isCreateModalidade = false;

    public static void setCreateAthleteMode() {
        isCreateModalidade = true;
    }

    /**
     * Inicializa o controlador da vista de Modalidade. Este método é automaticamente chamado pelo JavaFX ao carregar a interface.
     * Carrega as listas de opções para os campos de seleção e configura as restrições de entrada para os campos numéricos.
     */
    public void initialize(){
        if (isCreateModalidade) {
            carregarOpcoes();
            configurarRestricoesDeCampos();
        }
    }

    /**
     * Método acionado ao clicar no botão "Adicionar Modalidade".
     * Valida os campos do formulário e, se todos os campos estiverem preenchidos corretamente, tenta inserir a modalidade na base de dados.
     * Exibe uma mensagem de sucesso caso a inserção seja bem-sucedida ou uma mensagem de erro caso contrário.
     */
    @FXML
    public void btnAddModalitie() {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        boolean success = ModalidadeService.insertModalidade(
                cmboxTipo.getValue(),
                cmboxGender.getValue(),
                fldName.getText(),
                fldDescription.getText(),
                flsMinParticipants.getText(),
                cmboxScore.getValue(),
                flsNumGames.getText(),
                fldRules.getText()
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Modalidade inserida com sucesso!");
            closeCurrentWindowCreate();
            carregarDados();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Modalidade não foi inserida!");
        }
    }

    /**
     * Método acionado ao clicar no botão "Editar Modalidade".
     * Valida os campos do formulário e, se todos os campos estiverem preenchidos corretamente, tenta editar a modalidade na base de dados.
     * Exibe uma mensagem de sucesso caso a inserção seja bem-sucedida ou uma mensagem de erro caso contrário.
     */

    private int idModalidade;

    @FXML
    public void btnEditModalitie() {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }
        boolean success = ModalidadeService.editModalidade(
                idModalidade,
                cmboxTipo.getValue(),
                cmboxGender.getValue(),
                fldName.getText(),
                fldDescription.getText(),
                flsMinParticipants.getText(),
                cmboxScore.getValue(),
                flsNumGames.getText(),
                fldRules.getText(),
                cmboxEstado.getValue()
                );
        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Modalidade atualizado com sucesso!");
            closeCurrentWindowEdit();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Modalidade não foi atualizado!");
        }
    }

    /**
     * Verifica se todos os campos obrigatórios foram preenchidos.
     * @return true se todos os campos estiverem preenchidos, caso contrário false.
     */
    private boolean areFieldsValid() {
        return cmboxTipo.getValue() != null && fldDescription.getText() != null &&
                flsMinParticipants.getText() != null  && flsNumGames.getText() != null  &&
                fldRules.getText() != null  && fldName.getText() != null  &&
                cmboxScore.getValue() != null && cmboxGender.getValue() != null;
    }

    /**
     * Carrega as opções para os ComboBoxes da interface.
     * Define as listas de opções para o tipo de modalidade, género e pontuação, recuperando as pontuações da base de dados.
     */
    void carregarOpcoes() {
        ObservableList<String> tipo = FXCollections.observableArrayList("Individual", "Equipa");
        ObservableList<String> genero = FXCollections.observableArrayList("Masculino", "Feminino");
        ObservableList<String> estado = FXCollections.observableArrayList("Desativado", "Ativado");
        ObservableList<String> scores = FXCollections.observableArrayList(ModalidadeDAO.getScores());
        cmboxTipo.setItems(tipo);
        cmboxGender.setItems(genero);
        cmboxScore.setItems(scores);
        cmboxEstado.setItems(estado);
    }

    /**
     * Configura as restrições de entrada para campos numéricos, permitindo apenas dígitos nos campos de participantes mínimos
     * e número de jogos.
     * Este método adiciona ouvintes aos campos de texto para garantir que apenas números são inseridos.
     */
    private void configurarRestricoesDeCampos() {
        flsMinParticipants.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                flsMinParticipants.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        flsNumGames.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                flsNumGames.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void inicializarTabela(TableView<Modalidade> tableViewModalidades) {
        this.tableViewModalidades = tableViewModalidades;
    }

    public void carregarDados() {
        if (tableViewModalidades != null) {
            ObservableList<Modalidade> modalidades = FXCollections.observableArrayList(modalidadeService.listarModalidades());
            tableViewModalidades.setItems(modalidades);
        }
    }

    /**
     * Fecha a janela atual do formulário de criação de modalidades.
     */
    private void closeCurrentWindowCreate() {
        Stage currentStage = (Stage) modaliteCreatePane.getScene().getWindow();
        EventHandler<WindowEvent> closeHandler = currentStage.getOnCloseRequest();
        if (closeHandler != null) {
            closeHandler.handle(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        currentStage.close();
    }

    /**
     * Metodo para inicializar fxml visualizar importação dos atletas.
     * @param modalidades Lista de Atletas.
     */
    public void mostrarDados(List<Modalidade> modalidades) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/import_modalidades.fxml"));
            loader.setController(this);
            SplitPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dados das Modalidades");
            stage.initModality(Modality.APPLICATION_MODAL);

            configurarTabelaModalidades(modalidades);
            configurarListeners();

            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo para amostrar os dados importados dos atletas.
     * @param modalidades Lista de Atletas.
     */
    private void configurarTabelaModalidades(List<Modalidade> modalidades) {
        List<Modalidade> modalidadesUnicas = modalidades.stream().distinct().toList();

        colModType.setCellValueFactory(new PropertyValueFactory<>("tipoNome"));
        colModName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colModGenre.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colModDescription.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colModMinParti.setCellValueFactory(new PropertyValueFactory<>("minParticipantes"));
        colModScore.setCellValueFactory(new PropertyValueFactory<>("pontuacaoTipo"));
        colModNumJogos.setCellValueFactory(new PropertyValueFactory<>("numJogosNome"));
        colModRecordName.setCellValueFactory(new PropertyValueFactory<>("recordeOlimpicoNome"));
        colModRecordAno.setCellValueFactory(new PropertyValueFactory<>("recordeOlimpicoAno"));
        colModRecordRecord.setCellValueFactory(new PropertyValueFactory<>("recordeOlimpicoRecord"));
        colModWinnerName.setCellValueFactory(new PropertyValueFactory<>("vencedorOlimpicoNome"));
        colModWinnerAno.setCellValueFactory(new PropertyValueFactory<>("vencedorOlimpicoAno"));
        colModWinnerRecord.setCellValueFactory(new PropertyValueFactory<>("vencedorOlimpicoRecord"));


        colModRules.setCellValueFactory(new PropertyValueFactory<>("regras"));

        tableViewImportModalidades.getItems().setAll(modalidadesUnicas);
    }

    /**
     * Configura os listeners.
     */
    private void configurarListeners() {
        btnImportModalidades.setOnAction(e -> {
            try {
                importModalidades(tableViewImportModalidades.getItems());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void importModalidades(ObservableList<Modalidade> modalidades) throws SQLException {
        Stage stage = (Stage) tableViewImportModalidades.getScene().getWindow();
        ModalidadeService.importModalidades(modalidades, stage);
    }

    public void setModalidade(Modalidade selectedModalidade) {
        idModalidade = selectedModalidade.getId();
        fldName.setText(selectedModalidade.getNome());
        fldDescription.setText(selectedModalidade.getDescricao());
        flsMinParticipants.setText(String.valueOf(selectedModalidade.getMinParticipantes()));
        flsNumGames.setText(String.valueOf(selectedModalidade.getNumJogos()));
        fldRules.setText(selectedModalidade.getRegras());
        cmboxTipo.setValue(selectedModalidade.getTipoNome());
        cmboxGender.setValue(selectedModalidade.getGeneroNome());
        cmboxScore.setValue(selectedModalidade.getMedidaPontuacaoNome());
        cmboxEstado.setValue(selectedModalidade.getEstado());
    }

    public List<Modalidade> obterModalidadesSemEvento() {
        List<Modalidade> modalidades = modalidadeService.listarModalidadesSemEvento();
        return modalidades;
    }

    /**
     * Fecha a janela atual ou seja equipaEdit.
     */
    private void closeCurrentWindowEdit() {
        Stage currentStage = (Stage) modaliteEditPane.getScene().getWindow();
        EventHandler<WindowEvent> closeHandler = currentStage.getOnCloseRequest();
        if (closeHandler != null) {
            closeHandler.handle(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        currentStage.close();
    }

    public void addEquipa(int idModalidade, int idEquipa) {
        modalidadeService.addModalidadeEquipa(idModalidade,idEquipa);
    }
}
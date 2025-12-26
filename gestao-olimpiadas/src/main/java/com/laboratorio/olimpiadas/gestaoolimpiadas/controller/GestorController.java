package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.InscricaoService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AuthenticateUserUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.XMLImporterUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GestorController {

    public Label lblTotalModalities;
    public Label lblTotalMatches;
    @FXML
    private AnchorPane homePane;
    @FXML
    private AnchorPane eventsPane;
    @FXML
    private AnchorPane athletesPane;
    @FXML
    private AnchorPane teamPane;
    @FXML
    private AnchorPane modalitiesPane;
    @FXML
    private AnchorPane localsPane;
    @FXML
    private AnchorPane approvesPane;
    @FXML
    private Label lblName;
    @FXML
    private Button btnXmlHistory;
    @FXML
    private Button btnCreateModalitie;
    @FXML
    private Button btnCreateAthlete;
    @FXML
    private Button btnCreateLocal;
    @FXML
    private Button btnCreateTeam;
    @FXML
    private Button btnCreateEvent;
    @FXML
    private Button btnEditAthlete;
    @FXML
    private Button btnEditLocal;
    @FXML
    private Button btnEditTeam;
    @FXML
    private Button btnEditModalitie;
    @FXML
    private Button btnEditEvent;
    @FXML
    private Button btnCreateMatches;
    @FXML
    private AnchorPane btnModalitiesOpen;
    @FXML
    private AnchorPane btnMatchesOpen;
    @FXML
    private Button btnManageClients;
    @FXML
    private Button btnManageGames;

    // TableView Local
    @FXML
    private TableView<Local> tableViewLocais;
    @FXML
    private TableColumn<Local, String> colNomeLocal;
    @FXML
    private TableColumn<Local, String> colMorada;
    @FXML
    private TableColumn<Local, String> colCidade;
    @FXML
    private TableColumn<Local, Integer> colCapacidade;
    @FXML
    private TableColumn<Local, Integer> colAnoConstrucao;

    LocalController localController;

    // TableView Evento
    @FXML
    private TableView<Evento> tableViewEventos;
    @FXML
    private TableColumn<Evento, String> colNomeEvento;
    @FXML
    private TableColumn<Evento, Integer> colAnoEdicao;
    @FXML
    private TableColumn<Evento, Date> colDataInicio;
    @FXML
    private TableColumn<Evento, Date> colDataFim;
    @FXML
    private TableColumn<Evento, String> colPais;
    @FXML
    private TableColumn<Evento, Image> colMascotLogotipo;
    @FXML
    private Button btnRecordsTeamList;

    private EventoController eventoController;

    // TableView Atleta
    @FXML
    private TableView<Atleta> tableViewAtletas;
    @FXML
    private TableColumn<Atleta, Integer> colIdUtilizador;
    @FXML
    private TableColumn<Atleta, Integer> colPaisAtleta;
    @FXML
    private TableColumn<Atleta, Date> colDataNascimentoAtleta;
    @FXML
    private TableColumn<Atleta, Boolean> colGeneroAtleta;
    @FXML
    private TableColumn<Atleta, Boolean> colEstadoAtleta;
    @FXML
    private TableColumn<Atleta, Integer> colAltura;
    @FXML
    private TableColumn<Atleta, Integer> colPeso;
    @FXML
    private TableColumn<AtletaRecorde, String> colModalidadeAthlete;
    @FXML
    private  Button btnRecordsAthleteList;

    private static AtletaController atletaController;
    private static ModalidadeController modalidadeController;
    private static EquipaController equipaController;
    private static InscricaoController inscricaoController;
    private static ResultadoController resultController;

    // TableView Modalidades
    @FXML
    private TableView<Modalidade> tableViewModalidades;
    @FXML
    private TableColumn<Modalidade, String> colNome;
    @FXML
    private TableColumn<Modalidade, String> colDescricao;
    @FXML
    private TableColumn<Modalidade, Integer> colNumMinParticipantes;
    @FXML
    private TableColumn<Modalidade, Integer> colNumJogos;
    @FXML
    private TableColumn<Modalidade, Integer> colPontuacao;
    @FXML
    private TableColumn<Modalidade, Integer> colGenero;
    @FXML
    private TableColumn<Modalidade, Integer> colTipo;
    @FXML
    private TableColumn<Modalidade, Float> colRecordOlimpico;
    @FXML
    private TableColumn<Modalidade, String> colRegras;
    @FXML
    private TableColumn<Modalidade, String> colEstado;
    @FXML
    private Button btnRecordsModalityList;


    // TableView Equipas
    @FXML
    private TableView<Equipa> tableViewEquipas;
    @FXML
    private TableColumn<Equipa, String> colPaisEquipa;
    @FXML
    private TableColumn<Equipa, String> colModalidade;
    @FXML
    private TableColumn<Equipa, Boolean> colGeneroEquipa;
    @FXML
    private TableColumn<Equipa, String> colNomeEquipa;
    @FXML
    private TableColumn<Equipa, Integer> colAnofundacao;

    // TableView Inscricao
    @FXML
    private TableView<Inscricao> tableViewInscricao;
    @FXML
    private TableColumn<Inscricao, String> colAtletaInscricao;
    @FXML
    private TableColumn<Inscricao, String> colEquipaInscricao;
    @FXML
    private TableColumn<Inscricao, String> colModalidadeInscricao;
    @FXML
    private TableColumn<Inscricao, String> colEventoInscricao;
    @FXML
    private TableColumn<Inscricao, String> colEstadoInscricao;
    @FXML
    private CheckBox checkAthletes;
    @FXML
    private CheckBox checkTeams;
    @FXML
    private CheckBox checkApproveds;
    @FXML
    private CheckBox checkRefuseds;
    public ComboBox cmboxEvents;
    private InscricaoService inscricaoService = new InscricaoService();

    @FXML
    private Hyperlink hplChangePassword;

    private XMLImporterController xmlimporterController;
    @FXML
    public TableView<XML> tableViewHist;
    @FXML
    public TableColumn<XML,String> colHistData;
    @FXML
    public TableColumn<XML,String> colHistUtilizador;
    @FXML
    public TableColumn<XML,String> colHistFicheiro;



    private String authenticatedUser;

    /**
     * Define o utilizador autenticado e atualiza a interface com o nome.
     * @param num_mecanografico
     * @throws SQLException
     */
    public void setAuthenticatedUser(String num_mecanografico) throws SQLException {
        this.authenticatedUser = num_mecanografico;
        AuthenticateUserUtils.setNumMecanografico(num_mecanografico);
        String name = UtilizadorDAO.getNomeUserByNumMecan(AuthenticateUserUtils.getNumMecanografico());
        lblName.setText(name);
    }

    @FXML
    public void initialize() throws SQLException {
        // Inicializar Controller's
        localController = new LocalController();
        eventoController = new EventoController();
        atletaController = new AtletaController();
        modalidadeController = new ModalidadeController();
        equipaController = new EquipaController();
        resultController = new ResultadoController();
        xmlimporterController = new XMLImporterController();
        inscricaoController = new InscricaoController();

        //HyperLink change password
        hplChangePassword.setOnAction(event -> {
            try {
                openChangePasswordWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Configurar colunas sem adicionar ao TableView novamente
        configurarColunas();

        // Carregar os dados na tabela
        localController.inicializarTabela(tableViewLocais);
        localController.carregarDados();
        eventoController.inicializarTabela(tableViewEventos);
        eventoController.carregarDados();
        atletaController.inicializarTabela(tableViewAtletas);
        atletaController.carregarDados();
        modalidadeController.inicializarTabela(tableViewModalidades);
        modalidadeController.carregarDados();
        equipaController.inicializarTabela(tableViewEquipas);
        equipaController.carregarDados();
        inscricaoController.inicializarTabela(tableViewInscricao);
        inscricaoController.carregarDados();
        xmlimporterController.inicializarTabela(tableViewHist);
        xmlimporterController.carregarDados();

        onShowHome();

        List<String> eventos = new ArrayList<>(inscricaoService.listarInscricoes().stream()
                .map(Inscricao::getEvento)
                .distinct()
                .sorted()
                .toList());

        eventos.add(0, "Todos");

        cmboxEvents.setItems(FXCollections.observableArrayList(eventos));
        cmboxEvents.setValue("Todos");

        // Adicionar Listeners aos CheckBoxes
        cmboxEvents.setOnAction(event -> aplicarFiltros());
        checkAthletes.setOnAction(event -> aplicarFiltros());
        checkTeams.setOnAction(event -> aplicarFiltros());
        checkApproveds.setOnAction(event -> aplicarFiltros());
        checkRefuseds.setOnAction(event -> aplicarFiltros());
        // Carregar dados iniciais
        aplicarFiltros();
        updateTotalModalitiesLabel();
        updateTotalMatchesLabel();
    }

    /**
     * Configura as colunas da tabela, associando-as aos respectivos campos.
     */
    private void configurarColunas() {
        //colLocais
        colNomeLocal.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
        colAnoConstrucao.setCellValueFactory(new PropertyValueFactory<>("anoConstrucao"));
        //colEventos
        colDataInicio.setCellValueFactory(new PropertyValueFactory<>("data_inicio"));
        colDataFim.setCellValueFactory(new PropertyValueFactory<>("data_fim"));
        colNomeEvento.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAnoEdicao.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("nomePais"));

        colMascotLogotipo.setCellValueFactory(event -> {
            String imageBytes = event.getValue().getImagemMascote();
            if (imageBytes != null) {
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(imageBytes);
                    Image image = new Image(new ByteArrayInputStream(decodedBytes));
                    return new SimpleObjectProperty<>(image);
                } catch (Exception e) {
                    return new SimpleObjectProperty<>(null);
                }
            } else {
                return new SimpleObjectProperty<>(null); // Caso não haja imagem, retorna null
            }
        });
        colMascotLogotipo.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);

                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        //colAtleta
        colIdUtilizador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPaisAtleta.setCellValueFactory(new PropertyValueFactory<>("paisNome"));
        colDataNascimentoAtleta.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colGeneroAtleta.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colEstadoAtleta.setCellValueFactory(new PropertyValueFactory<>("estado"));
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
        //colModalidade
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colNumMinParticipantes.setCellValueFactory(new PropertyValueFactory<>("minParticipantes"));
        colNumJogos.setCellValueFactory(new PropertyValueFactory<>("numJogos"));
        colPontuacao.setCellValueFactory(new PropertyValueFactory<>("pontuacaoTipo"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoNome"));
        colRecordOlimpico.setCellValueFactory(new PropertyValueFactory<>("recordeOlimpico"));
        colRegras.setCellValueFactory(new PropertyValueFactory<>("regras"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        //colEquipas
        colNomeEquipa.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPaisEquipa.setCellValueFactory(new PropertyValueFactory<>("paisNome"));
        colModalidade.setCellValueFactory(new PropertyValueFactory<>("modalidadeNome"));
        colGeneroEquipa.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colAnofundacao.setCellValueFactory(new PropertyValueFactory<>("anoFundacao"));
        //colInscicoes
        colAtletaInscricao.setCellValueFactory(new PropertyValueFactory<>("utilizador"));
        colEquipaInscricao.setCellValueFactory(new PropertyValueFactory<>("equipa"));
        colModalidadeInscricao.setCellValueFactory(new PropertyValueFactory<>("modalidade"));
        colEventoInscricao.setCellValueFactory(new PropertyValueFactory<>("evento"));
        colEstadoInscricao.setCellValueFactory(new PropertyValueFactory<>("estado"));
        //colHist
        colHistData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colHistUtilizador.setCellValueFactory(new PropertyValueFactory<>("id_utilizador"));
        colHistFicheiro.setCellValueFactory(new PropertyValueFactory<>("ficheiro"));

        btnRecordsModalityList.setOnAction(event -> onShowModalidadeRecordsList());
        btnRecordsAthleteList.setOnAction(event -> onShowRecordAthleteList());
        btnRecordsTeamList.setOnAction(event -> onShowRecordTeamsList());
    }


    private void showPane(AnchorPane pane) {
        homePane.setVisible(false);
        eventsPane.setVisible(false);
        athletesPane.setVisible(false);
        teamPane.setVisible(false);
        modalitiesPane.setVisible(false);
        localsPane.setVisible(false);
        approvesPane.setVisible(false);

        pane.setVisible(true);
    }

    @FXML
    private TextArea fileContentArea;

    @FXML
    public void onShowHome() {
        showPane(homePane);
        tableViewHist.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int index = tableViewHist.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    XML selectedRow = tableViewHist.getItems().get(index);

                    String fileContent = readFile("./historicoXML/"+selectedRow.getFicheiro());
                    fileContentArea.setText(fileContent);
                }
            }
        });
        btnModalitiesOpen.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                try {
                    openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/generate_results.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnMatchesOpen.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                try {
                    openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/matches_list.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnManageClients.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                try {
                    openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/manage-clients.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnManageGames.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                try {
                    openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/manage-games.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Lê o conteúdo de um arquivo de texto e retorna como uma string.
     * @param filePath
     * @return
     */
    private String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            content.append("Erro ao ler o ficheiro.");
        }
        return content.toString();
    }

    /**
     * Exibe o painel de eventos e configura a ação para criar um novo evento.
     */
    @FXML
    public void onShowEvents() {
        showPane(eventsPane);

        tableViewEventos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Evento eventoSelecionado = tableViewEventos.getSelectionModel().getSelectedItem();
                if (eventoSelecionado != null) {
                    onEventDoubleClick(eventoSelecionado);
                }
            }
        });

        btnCreateEvent.setOnAction(event -> {
            try {
                Stage stage = openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/create_event.fxml");
                stage.setOnCloseRequest(closeEvent -> {
                    eventoController.inicializarTabela(tableViewEventos);
                    eventoController.carregarDados();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void onEventDoubleClick(Evento eventoSelecionado) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Selecione uma modalidade");
        alert.setHeaderText("Escolha uma das opções abaixo");

        ComboBox<Modalidade> comboBoxModalidades = new ComboBox<>();
        List<Modalidade> modalidades = modalidadeController.obterModalidadesSemEvento();
        comboBoxModalidades.getItems().addAll(modalidades);
        comboBoxModalidades.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Modalidade item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
        comboBoxModalidades.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Modalidade item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        Label labelModalidade = new Label("Escolha uma Modalidade:");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(labelModalidade, comboBoxModalidades);

        alert.getDialogPane().setContent(vbox);

        ButtonType btnAdicionar = new ButtonType("Adicionar");
        alert.getButtonTypes().add(btnAdicionar);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnAdicionar) {
                Modalidade modalidadeSelecionada = comboBoxModalidades.getSelectionModel().getSelectedItem();

                if (modalidadeSelecionada != null) {
                    int idModalidade = modalidadeSelecionada.getId();

                    eventoController.addModalidade(eventoSelecionado.getId(), idModalidade);
                    eventoController.inicializarTabela(tableViewEventos);
                }
            }
        });
    }



    /**
     * Configura a ação para editar um evento selecionado na tabela de eventos.
     */
    @FXML
    public void onEditEvent() {
        btnEditEvent.setOnAction(event -> {
            Evento selectedEvento = tableViewEventos.getSelectionModel().getSelectedItem();
            if (selectedEvento != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/edit_event.fxml"));
                    Parent root = loader.load();
                    EventoController controller = loader.getController();
                    controller.isEdit();
                    controller.setEvento(selectedEvento);
                    controller.carregarOpcoes();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(closeEvent -> {
                        eventoController.carregarDados();
                        eventoController.inicializarTabela(tableViewEventos);
                    });
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertUtils.showErrorAlert("Error", "Select an event to edit.");
            }
        });
    }

    /**
     * Configura a ação para editar um evento selecionado na tabela de eventos.
     */
    @FXML
    public void onCreateMatches() {
        btnCreateMatches.setOnAction(event -> {
            Evento selectedEvento = tableViewEventos.getSelectionModel().getSelectedItem();
            if (selectedEvento != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/create_matches.fxml"));
                    Parent root = loader.load();
                    EventoController controller = loader.getController();
                    controller.isMatch();
                    controller.setEventoMatches(selectedEvento.getId());
                    controller.carregarOpcoesMatches();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(closeEvent -> {
                        eventoController.carregarDados();
                        eventoController.inicializarTabela(tableViewEventos);
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertUtils.showErrorAlert("Error", "Select an event to create match.");
            }
        });
    }

    /**
     * Exibe o painel de atletas e configura a ação para criar um novo atleta.
     */
    @FXML
    public void onShowAthletes() {
        showPane(athletesPane);
        btnCreateAthlete.setOnAction(event -> {
            try {
                AtletaController.setCreateAthleteMode();
                Stage stage = openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/create_athlete.fxml");
                stage.setOnCloseRequest(closeEvent -> {
                    atletaController.inicializarTabela(tableViewAtletas);
                    atletaController.carregarDados();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Configura ações de interação para editar atleta
     */
    @FXML
    public void onEditAthlete() {
        btnEditAthlete.setOnAction(event -> {
            Atleta selectedAtleta = tableViewAtletas.getSelectionModel().getSelectedItem();
            if (selectedAtleta != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/edit_athlete.fxml"));
                    Parent root = loader.load();
                    AtletaController controller = loader.getController();
                    controller.setAtleta(selectedAtleta);
                    controller.carregarOpcoes();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(closeEvent -> {
                        atletaController.carregarDados();
                        atletaController.inicializarTabela(tableViewAtletas);
                    });
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertUtils.showErrorAlert("Error", "Select an athlete to edit.");
            }
        });
    }


    @FXML
    public void onShowTeams() {
        showPane(teamPane);
        btnCreateTeam.setOnAction(event -> {
            try {
                EquipaController.setCreateTeamMode();
                Stage stage = openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/create_team.fxml");
                stage.setOnCloseRequest(closeEvent -> {
                    equipaController.inicializarTabela(tableViewEquipas);
                    equipaController.carregarDados();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void onShowApprovals() {
        showPane(approvesPane);
    }

    /**
     * Configura a ação para editar uma equipa selecionada na tabela de equipas.
     */
    @FXML
    public void onEditTeam() {
        btnEditTeam.setOnAction(event -> {
            Equipa selectedEquipa = tableViewEquipas.getSelectionModel().getSelectedItem();
            if (selectedEquipa != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/edit_team.fxml"));
                    Parent root = loader.load();
                    EquipaController controller = loader.getController();
                    controller.setEquipa(selectedEquipa);
                    controller.carregarOpcoes(); // Call carregarOpcoes method

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(closeEvent -> {
                        equipaController.inicializarTabela(tableViewEquipas);
                        equipaController.carregarDados();
                    });
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertUtils.showErrorAlert("Error", "Select a team to edit.");
            }
        });
    }

    /**
     * Criar modalidade
     */
    @FXML
    public void onShowModalities(){
        showPane(modalitiesPane);
        tableViewModalidades.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Modalidade modalidadeSelecionada = tableViewModalidades.getSelectionModel().getSelectedItem();
                if (modalidadeSelecionada != null) {
                    onModalidadeDoubleClick(modalidadeSelecionada);
                }
            }
        });
        btnCreateModalitie.setOnAction(event -> {
            try {
                ModalidadeController.setCreateAthleteMode();
                Stage stage = openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/create_modalitie.fxml");
                stage.setOnCloseRequest(closeEvent -> {
                    modalidadeController.inicializarTabela(tableViewModalidades);
                    modalidadeController.carregarDados();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void onModalidadeDoubleClick(Modalidade modalidadeSelecionada) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Selecione uma equipa");
        alert.setHeaderText("Escolha uma das opções abaixo");

        ComboBox<Equipa> comboBoxEquipa = new ComboBox<>();
        List<Equipa> equipas = equipaController.obterEquipasFiltradas(modalidadeSelecionada.getId());

        if (equipas == null || equipas.isEmpty()) {
            comboBoxEquipa.setDisable(true);
            comboBoxEquipa.setPromptText("Não existe equipas");
        } else {
            comboBoxEquipa.getItems().addAll(equipas);
            comboBoxEquipa.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Equipa item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getNome());
                }
            });
            comboBoxEquipa.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Equipa item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getNome());
                }
            });
        }

        Label labelEquipa = new Label("Escolha uma Equipa:");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(labelEquipa, comboBoxEquipa);

        alert.getDialogPane().setContent(vbox);

        ButtonType btnAdicionar = new ButtonType("Adicionar");
        alert.getButtonTypes().add(btnAdicionar);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnAdicionar && !comboBoxEquipa.isDisabled()) {
                Equipa equipaSelecionada = comboBoxEquipa.getSelectionModel().getSelectedItem();

                if (equipaSelecionada != null) {
                    int idEquipa = equipaSelecionada.getId();

                    modalidadeController.addEquipa(modalidadeSelecionada.getId(), idEquipa);
                    modalidadeController.inicializarTabela(tableViewModalidades);
                }
            }
        });
    }


    /**
     * editar modalidade
     */
    @FXML
        public void onEditModalitie() {
        btnEditModalitie.setOnAction(event -> {
            Modalidade selectedModalidade = tableViewModalidades.getSelectionModel().getSelectedItem();
            if (selectedModalidade != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/edit_modalitie.fxml"));
                    Parent root = loader.load();
                    ModalidadeController controller = loader.getController();
                    controller.setModalidade(selectedModalidade);
                    controller.carregarOpcoes();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(closeEvent -> {
                        modalidadeController.inicializarTabela(tableViewModalidades);
                        modalidadeController.carregarDados();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertUtils.showErrorAlert("Error", "Select a modality to edit.");
            }
        });
    }

    /**
     * Criar Local
     */
    @FXML
    public void onShowLocals() {
        showPane(localsPane);
        btnCreateLocal.setOnAction(event -> {
            try {
                localController.setCreateMode();
                Stage stage = openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/create_local.fxml");
                stage.setOnCloseRequest(closeEvent -> {
                    localController.inicializarTabela(tableViewLocais);
                    localController.carregarDados();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Editar Local
     */
    @FXML
    public void onEditLocal() {
        btnEditLocal.setOnAction(event -> {
            Local selectedLocal = tableViewLocais.getSelectionModel().getSelectedItem();
            if (selectedLocal != null) {
                try {
                    localController.setCreateMode();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/edit_local.fxml"));
                    Parent root = loader.load();
                    LocalController controller = loader.getController();
                    controller.setLocal(selectedLocal);
                    controller.setCreateMode();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(closeEvent -> {
                        localController.carregarDados();
                        localController.inicializarTabela(tableViewLocais);
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertUtils.showErrorAlert("Error", "Select a local to edit.");
            }
        });
    }



    private Stage openNewWindow(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
        return newStage;
    }

    public void btnImportAthlete() {
        XMLImporterUtils.importarXMLAtletas();
    }
    public void btnImportModalitie() {
        XMLImporterUtils.importarXMLModalitie();
    }
    public void btnImportTeam() {
        XMLImporterUtils.importarXMLTeam();
    }

    /**
     * Filtra as inscrições exibidas na TableView com base nos valores dos checkboxes
     * de atletas e equipas.
     */
    @FXML
    public void aplicarFiltros() {
        List<Inscricao> todasInscricoes = inscricaoService.listarInscricoes();

        boolean filtrarAprovados = checkApproveds.isSelected();
        boolean filtrarRecusados = checkRefuseds.isSelected();
        boolean filtrarApenasAtletas = checkAthletes.isSelected();
        boolean filtrarApenasEquipas = checkTeams.isSelected();
        String eventoSelecionado = (String) cmboxEvents.getValue();

        // Aplicar filtros
        List<Inscricao> inscricoesFiltradas = todasInscricoes.stream()
                .filter(inscricao -> {
                    boolean estadoValido = (!filtrarAprovados && !filtrarRecusados) ||
                            (filtrarAprovados && "Aprovado".equals(inscricao.getEstado())) ||
                            (filtrarRecusados && "Reprovado".equals(inscricao.getEstado()));

                    boolean tipoValido = (!filtrarApenasAtletas && !filtrarApenasEquipas) ||
                            (filtrarApenasAtletas && inscricao.getEquipa() == null) ||
                            (filtrarApenasEquipas && inscricao.getUtilizador() == null);

                    boolean eventoValido = "Todos".equals(eventoSelecionado) ||
                            eventoSelecionado.equals(inscricao.getEvento());

                    return estadoValido && tipoValido && eventoValido;
                })
                .toList();

        tableViewInscricao.setItems(FXCollections.observableArrayList(inscricoesFiltradas));
    }

    /**
     * Verifica se uma inscrição foi selecionada na TableView e tenta atualizar
     * o estado para "Aprovado".
     */
    @FXML
    private void onApprove() {
        Inscricao selecionada = tableViewInscricao.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            int idSelecionado = selecionada.getId();
            if (inscricaoService.atualizarEstado(idSelecionado, "Aprovado")) {
                inscricaoController.carregarDados();
                AlertUtils.showInfoAlert("Aprovado", "A inscrição foi aprovada com sucesso.");
            } else {
                AlertUtils.showErrorAlert("Erro", "Não foi possível aprovar a inscrição.");
            }
        } else {
            AlertUtils.showWarningAlert("Seleção inválida", "Por favor, selecione uma inscrição.");
        }
    }

    /**
     * Verifica se uma inscrição foi selecionada na TableView e tenta atualizar
     * o estado para "Recusado".
     */
    @FXML
    private void onRefuse() {
        Inscricao selecionada = tableViewInscricao.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            int idSelecionado = selecionada.getId();
            if (inscricaoService.atualizarEstado(idSelecionado, "Recusado")) {
                inscricaoController.carregarDados();
                AlertUtils.showInfoAlert("Recusado", "A inscrição foi recusada com sucesso.");
            } else {
                AlertUtils.showErrorAlert("Erro", "Não foi possível recusar a inscrição.");
            }
        } else {
            AlertUtils.showWarningAlert("Seleção inválida", "Por favor, selecione uma inscrição.");
        }
    }

    /**
     * Realiza o logout do utilizador atual e redireciona para a tela de login.
     * @throws RuntimeException se ocorrer um erro ao carregar a tela de login.
     */
    @FXML
    public void onShowLogout() {
        try {
            Stage currentStage = (Stage) homePane.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/login-view.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao tentar fazer logout: " + e.getMessage());
        }
    }

    @FXML
    private void openChangePasswordWindow() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/change_password.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ChangePasswordController controller = loader.getController();
            controller.setAuthenticatedUser(this.authenticatedUser, 1);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar a tela de alteração de senha.");
        }
    }

    public void updateTotalModalitiesLabel() {
        try {
            int totalModalities = resultController.getNumberOfInscriptions();

            lblTotalModalities.setText(String.valueOf(totalModalities));

        } catch (SQLException e) {

        }
    }

    public void updateTotalMatchesLabel() {
        try {
            int totalMatches = eventoController.getNumberOfMatches();

            lblTotalMatches.setText(String.valueOf(totalMatches));

        } catch (SQLException e) {

        }
    }


    @FXML
    public void onShowModalidadeRecordsList() {
        try {
            List<ModalidadeRecorde> modalidadesComRecordes = ModalidadeDAO.getModalidadesComRecordes();
            showModalidadesRecordsList(modalidadesComRecordes);
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar a lista de recordes.");
        }
    }
    private void showModalidadesRecordsList(List<ModalidadeRecorde> modalidadesComRecordes) throws IOException {
        // Carregar o FXML para a nova janela
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/modality_record.fxml"));
        Parent root = loader.load();

        // Obter o controlador e passar os dados das modalidades com recordes
        RecordModalityController controller = loader.getController();
        controller.setModalidades(modalidadesComRecordes);

        // Configurar e exibir a nova janela
        Stage stage = new Stage();
        stage.setTitle("Lista de Recordes Olímpicos");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onShowRecordAthleteList() {
        try{
            List<AtletaRecorde> atheletsComRecordes = AtletaDAO.getAthleteComRecordes();
            showRecordAthleteList(atheletsComRecordes);
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar a lista de recordes.");
        }
    }
    private void showRecordAthleteList (List<AtletaRecorde> atletasComRecordes) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/athlete_record.fxml"));
        Parent root = loader.load();

        RecordAthleteController controller = loader.getController();
        controller.setAtletas(atletasComRecordes);

        Stage stage = new Stage();
        stage.setTitle("Lista de Recordes Olímpicos");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onShowRecordTeamsList() {
        try{
            List<EquipaRecorde> teamsComRecordes = EquipaDAO.getTeamComRecordes();
            showRecordTeamsList(teamsComRecordes);
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar a lista de recordes.");
        }
    }
    private void showRecordTeamsList (List<EquipaRecorde> teamsComRecordes) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/team_record.fxml"));
        Parent root = loader.load();

        RecordTeamController controller = loader.getController();
        controller.setEquipas(teamsComRecordes);

        Stage stage = new Stage();
        stage.setTitle("Lista de Recordes Olímpicos");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
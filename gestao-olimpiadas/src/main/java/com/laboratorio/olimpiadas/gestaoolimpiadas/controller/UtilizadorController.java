package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.AtletaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.EventoService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.PartidaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.UtilizadorService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.YearMonth;
import java.util.concurrent.LinkedTransferQueue;

public class UtilizadorController {
    @FXML
    private Label fotoPerfil;
    @FXML
    private Label lblName;
    @FXML
    private AnchorPane homePane;
    @FXML
    private AnchorPane profilePane;
    @FXML
    private AnchorPane historyPane;
    @FXML
    private TableView<AtletaRecorde> tableViewHistory;
    @FXML
    private TableColumn<AtletaRecorde, String> colHiModalidade;
    @FXML
    private TableColumn<AtletaRecorde, Integer> colHiAno;
    @FXML
    private TableColumn<AtletaRecorde, Float> colHiRecorde;
    @FXML
    private TableColumn<AtletaRecorde, Integer> colHiMedalha;
    @FXML
    private AnchorPane registrationsPane;
    @FXML
    private TableView<Evento> tableViewRegEventos;
    @FXML
    private TableColumn<Evento, String> colRegName;
    @FXML
    private TableColumn<Evento, String> colRegCountry;
    @FXML
    private TableColumn<Evento, String> colRegYear;

    @FXML
    private TableView<Modalidade> tableViewRegModalidades;
    @FXML
    private TableColumn<Modalidade, String> colRegModName;
    @FXML
    private TableColumn<Modalidade, String> colRegModGenre;
    @FXML
    private TableColumn<Modalidade, String> colRegModType;
    @FXML
    private TableView<Equipa> tableViewRegEquipas;
    @FXML
    private TableColumn<Equipa, String> colRegEqiName;
    @FXML
    private TableColumn<Equipa, String> colRegEqiGenre;
    @FXML
    private TableColumn<Equipa, String> colRegEqiPais;

    private final UtilizadorService utilizadorService;
    private final EventoService eventoService;
    private final AtletaService atletaService;

    /*calendario*/
    @FXML
    private AnchorPane calendarPane;
    @FXML
    private Button prevMonthButton;
    @FXML
    private Button nextMonthButton;
    @FXML
    private Label monthYearLabel;
    @FXML
    private GridPane calendarGrid;
    private YearMonth currentYearMonth;
    private String authenticatedUser;

    @FXML
    public void initialize() throws SQLException {
        colRegName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colRegCountry.setCellValueFactory(new PropertyValueFactory<>("nomePais"));
        colRegYear.setCellValueFactory(new PropertyValueFactory<>("ano"));
        carregarEventos();

        currentYearMonth = YearMonth.now();
        updateCalendar(currentYearMonth);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem removerItem = new MenuItem("Remover");
        removerItem.setOnAction(event -> {
            try {
                utilizadorService.setFotoUserByNumMecanografico(this.authenticatedUser, null);
                loadFotoPerfil();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().add(removerItem);

        fotoPerfil.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(fotoPerfil, event.getScreenX(), event.getScreenY());
            } else {
                mostrarDialogoEdicaoFoto();
            }
        });

        tableViewRegEventos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Evento selectedEvento = tableViewRegEventos.getSelectionModel().getSelectedItem();
                if (selectedEvento != null) {
                    inicializarModalidades(selectedEvento);
                }
            }
        });

        colHiModalidade.setCellValueFactory(new PropertyValueFactory<>("nome_modalidade"));
        colHiAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colHiRecorde.setCellValueFactory(new PropertyValueFactory<>("recorde"));
        colHiMedalha.setCellValueFactory(new PropertyValueFactory<>("num_medalhas"));
    }

    private void mostrarDialogoEdicaoFoto() {
        FileChooser fileChooser = new FileChooser();

        // Filtros para tipos de ficheiros de imagem
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Imagens (JPG, PNG)", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Selecionar o ficheiro
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                if (mostrarDialogoConfirmacaoImagem(image)) {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    byte[] fileData = new byte[(int) selectedFile.length()];
                    fileInputStream.read(fileData);
                    fileInputStream.close();
                    String imageBytes = Base64.getEncoder().encodeToString(fileData);

                    utilizadorService.setFotoUserByNumMecanografico(this.authenticatedUser, imageBytes);
                    loadFotoPerfil();
                    AlertUtils.showInfoAlert("Sucesso", "Imagem guardada com sucesso!");
                } else {
                    AlertUtils.showInfoAlert("Cancelado", "A escolha da imagem foi cancelada.");
                }
            } catch (IOException e) {
                AlertUtils.showErrorAlert("Erro ao carregar a imagem", "Não foi possível carregar a imagem selecionada.");
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean mostrarDialogoConfirmacaoImagem(Image image) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Pré-visualizar Imagem");
        dialog.setHeaderText("Deseja guardar esta imagem?");
        dialog.setContentText("Confirme a sua escolha.");

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        dialog.setGraphic(imageView);

        ButtonType guardarButton = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelarButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(guardarButton, cancelarButton);

        return dialog.showAndWait().filter(button -> button == guardarButton).isPresent();
    }

    /**
     * Inicialização do UtilizadorService, EventoService e AtletaService.
     */
    public UtilizadorController() {
        this.utilizadorService = new UtilizadorService();
        this.eventoService = new EventoService();
        this.atletaService = new AtletaService();
    }

    /**
     * Atualiza o nome do utilizador.
     *
     * @param numMecanografico
     * @throws SQLException
     */
    public void setAuthenticatedUser(String numMecanografico) throws SQLException {
        this.authenticatedUser = numMecanografico;

        if (lblName != null) {
            String name = utilizadorService.getNomeUserByNumMecan(this.authenticatedUser);
            lblName.setText(name);
        }
    }

    /**
     * Carrega a tabela de eventos.
     */

    public void carregarEventos() {
        if (tableViewRegEventos != null) {
            ObservableList<Evento> eventos = FXCollections.observableArrayList(eventoService.listarEventos());
            tableViewRegEventos.setItems(eventos);
        }
    }

    void carregarRecorde() {
        if (tableViewHistory != null) {
            ObservableList<AtletaRecorde> recordes = FXCollections.observableArrayList(atletaService.listarHistoricoAtleta(this.authenticatedUser));
            tableViewHistory.setItems(recordes);
        }
    }

    /**
     * Inicializa o stage das modalidades com base no evento selecionado.
     *
     * @param evento Evento
     */

    public void inicializarModalidades(Evento evento) {
        try {
            ObservableList<Modalidade> modalidades = FXCollections.observableArrayList(
                    eventoService.listarModalidadesPorEvento(evento.getId())
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/athlete-modalidades-view.fxml"));
            loader.setController(this);
            SplitPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dados dos Atletas");
            stage.initModality(Modality.APPLICATION_MODAL);

            carregarModalidades(modalidades, evento.getId());

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar as modalidades.");
        }
    }

    /**
     * Inicializa os dados das modalidades com base na modalidade slecionado e o evento.
     *
     * @param modalidade Modalidade
     * @param eventoId   Id do evento
     */

    private void carregarModalidades(ObservableList<Modalidade> modalidade, int eventoId) {
        colRegModName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colRegModGenre.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colRegModType.setCellValueFactory(new PropertyValueFactory<>("tipoNome"));

        tableViewRegModalidades.setItems(modalidade);

        tableViewRegModalidades.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Modalidade selectedModalidade = tableViewRegModalidades.getSelectionModel().getSelectedItem();
                if (selectedModalidade != null) {
                    if (Objects.equals(selectedModalidade.getTipoNome(), "Individual")) {
                        Atleta atleta = atletaService.getAtletaByNumMecanografico(this.authenticatedUser);
                        if (Objects.equals(selectedModalidade.getGeneroNome(), atleta.getGeneroNome())) {
                            if (AlertUtils.showConfirmationAlert("Confirmação", "Deseja inscrever-se na modalidade " + selectedModalidade.getNome() + "?")) {
                                utilizadorService.inscreverModalidade(selectedModalidade.getId(), eventoId, atleta.getId());
                            }
                        } else {
                            AlertUtils.showErrorAlert("Error", "O seu género não é compatível com a modalidade.");
                        }
                    } else if (Objects.equals(selectedModalidade.getTipoNome(), "Coletivo")) {
                        inicializarEquipas(selectedModalidade, eventoId);
                    }
                }
            }
        });
    }

    /**
     * Inicializa o stage das equipas com base na modalidade selecionado.
     *
     * @param selectedModalidade modalidade
     */

    private void inicializarEquipas(Modalidade selectedModalidade, int eventoId) {
        try {
            ObservableList<Equipa> equipas = FXCollections.observableArrayList(
                    eventoService.listarEquipasByIdModalidadeEvento(selectedModalidade, eventoId)
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/athlete-equipas-view.fxml"));
            loader.setController(this);
            SplitPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dados dos Atletas");
            stage.initModality(Modality.APPLICATION_MODAL);

            carregarEquipas(selectedModalidade.getId(), equipas, eventoId);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar as modalidades.");
        }
    }

    /**
     * Inicializa os dados das equipas com base na modalidade selecionado e o evento.
     *
     * @param modalidadeId Modalidade ID
     * @param equipas      Lista das equipas
     * @param eventoID     Id do evento
     */

    private void carregarEquipas(int modalidadeId, ObservableList<Equipa> equipas, int eventoID) {
        colRegEqiName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colRegEqiGenre.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colRegEqiPais.setCellValueFactory(new PropertyValueFactory<>("paisNome"));

        tableViewRegEquipas.setItems(equipas);

        tableViewRegEquipas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Equipa selectedEquipa = tableViewRegEquipas.getSelectionModel().getSelectedItem();
                if (selectedEquipa != null) {
                    Atleta atleta = atletaService.getAtletaByNumMecanografico(this.authenticatedUser);
                    if (Objects.equals(selectedEquipa.getPaisNome(), atleta.getPaisNome())) {
                        if (Objects.equals(selectedEquipa.getGeneroNome(), atleta.getGeneroNome())) {
                            if (AlertUtils.showConfirmationAlert("Confirmação", "Deseja inscrever-se na equipa " + selectedEquipa.getNome() + "?")) {
                                utilizadorService.inscreverEquipa(modalidadeId, eventoID, selectedEquipa.getId(), atleta.getId());
                            }
                        } else {
                            AlertUtils.showErrorAlert("Error", "O seu genero não é compatível com a equipa.");
                        }
                    } else {
                        AlertUtils.showErrorAlert("Error", "O seu pais não é compatível com a equipa.");
                    }
                }
            }
        });
    }

    public void loadFotoPerfil() throws SQLException, IOException {
        Image image = null;
        String imageBytes = utilizadorService.getFotoUserByNumMecanografico(this.authenticatedUser);
        if (imageBytes != null) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(imageBytes);
                image = new Image(new ByteArrayInputStream(decodedBytes));
            } catch (IllegalArgumentException e) {
                System.err.println("Erro ao decodificar imagem em Base64: " + e.getMessage());
            }
        } else {
            fotoPerfil.setGraphic(null);
            fotoPerfil.setText("💪");
        }

        if (image != null) {
            ImageView imageView = new ImageView(image);
            Rectangle clip = new Rectangle(66, 66);
            clip.setArcWidth(66);
            clip.setArcHeight(66);
            imageView.setClip(clip);
            imageView.setFitWidth(66);
            imageView.setFitHeight(66);
            imageView.setPreserveRatio(false);
            fotoPerfil.setText("");
            fotoPerfil.setGraphic(imageView);
        } else {
            fotoPerfil.setGraphic(null);
            fotoPerfil.setText("💪");
        }

    }

    /**
     * Função para visualizar o pane de acordo com o selecionado.
     *
     * @param pane Pane selecionado
     */

    private void showPane(AnchorPane pane) {
        homePane.setVisible(false);
        profilePane.setVisible(false);
        historyPane.setVisible(false);
        registrationsPane.setVisible(false);
        if (calendarPane != null) {
            calendarPane.setVisible(false);
        }
        pane.setVisible(true);
    }

    @FXML
    public void onShowHome() {
        showPane(homePane);
    }

    @FXML
    public void onShowProfile() {
        showPane(profilePane);
    }

    @FXML
    public void onShowHistory() {
        showPane(historyPane);
    }

    @FXML
    public void onShowTeams() {
        showPane(registrationsPane);
    }

    /**
     * Realiza o logout do utilizador atual e redireciona para a tela de login.
     *
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

    /**
     * Carrega a tela de alteração da palavra passe.
     */
    @FXML
    public void onShowChangePassword() {
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


    @FXML
    public void onShowCalendar() throws SQLException {
        showPane(calendarPane);
        updateCalendar(currentYearMonth);
    }
    @FXML
    private void onPrevMonth() throws SQLException {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar(currentYearMonth);
    }

    @FXML
    private void onNextMonth() throws SQLException {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar(currentYearMonth);
    }

    private void updateCalendar(YearMonth currentYearMonth) throws SQLException {
        calendarGrid.getChildren().clear();
        List<Partida> listaProvas = PartidaService.getAllMatchesByUtilizador(this.authenticatedUser);

        String[] diasSemana = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"};
        for (int i = 0; i < diasSemana.length; i++) {
            Label diaLabel = new Label(diasSemana[i]);
            diaLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            calendarGrid.add(diaLabel, i, 0);
        }

        int primeiroDiaDoMes = this.currentYearMonth.atDay(1).getDayOfWeek().getValue();
        int totalDias = this.currentYearMonth.lengthOfMonth();

        monthYearLabel.setText(this.currentYearMonth.getMonth().toString() + " " + this.currentYearMonth.getYear());

        int dia = 1;
        for (int linha = 1; linha <= 6; linha++) {
            for (int coluna = 0; coluna < 7; coluna++) {
                if (linha == 1 && coluna < primeiroDiaDoMes - 1) {
                    continue;
                }
                if (dia > totalDias) {
                    break;
                }

                LocalDate dataAtual = this.currentYearMonth.atDay(dia);
                Label diaLabel = new Label(String.valueOf(dia));

                List<Partida> partidasNoDia = listaProvas.stream()
                        .filter(partida -> partida.getData_hora().toLocalDateTime().toLocalDate().isEqual(dataAtual))
                        .toList();

                if (!partidasNoDia.isEmpty()) {
                    diaLabel.setStyle("-fx-border-color: lightgray; -fx-font-size: 20px; -fx-alignment: center; -fx-padding: 10; -fx-background-color: lightgreen;");

                    diaLabel.setOnMouseClicked(event -> {
                        StringBuilder detalhes = new StringBuilder("Partidas no dia " + dataAtual + ":\n\n");
                        partidasNoDia.forEach(partida -> detalhes.append(
                                "Prova: " + partida.getEventoNome() + "\n" +
                                        "Hora: " + partida.getData_hora().toLocalDateTime().toLocalTime() + "\n" +
                                        "Local: " + partida.getLocalNome() + "\n\n"));

                        AlertUtils.showInfoAlert("Detalhes das Partidas", detalhes.toString());
                    });
                } else {
                    diaLabel.setStyle("-fx-border-color: lightgray; -fx-font-size: 20px; -fx-alignment: center; -fx-padding: 10;");
                }

                calendarGrid.add(diaLabel, coluna, linha);

                dia++;
            }
        }
    }

    @FXML
    private TextField fldName;
    @FXML
    private TextField fldCountry;
    @FXML
    private TextField fldGender;
    @FXML
    private TextField fldHeight;
    @FXML
    private TextField fldWeight;
    @FXML
    private TextField fldYearsParticipation;

    /**
     * Carrega os dados do utilizador autenticado e preenche os campos de perfil.
     */
    private void loadUserProfile() {
        try {
            Atleta atleta = atletaService.getAtletadetails(this.authenticatedUser);

            if (atleta != null) {
                fldName.setText(atleta.getNome());
                fldCountry.setText(atleta.getPaisNome());
                fldGender.setText(atleta.getGenero() ? "Feminino" : "Masculino");
                fldHeight.setText(String.valueOf(atleta.getAltura()));
                fldWeight.setText(String.valueOf(atleta.getPeso()));

                fldYearsParticipation.setText(
                        atleta.getAnosParticipacao() != null && !atleta.getAnosParticipacao().isEmpty()
                                ? String.join(", ", atleta.getAnosParticipacao().stream().map(String::valueOf).toList())
                                : "Nenhum ano registrado"
                );
            } else {
                AlertUtils.showErrorAlert("Erro", "Nenhum atleta encontrado para o utilizador autenticado.");
            }
        } catch (Exception e) {
            AlertUtils.showErrorAlert("Erro", "Não foi possível carregar os dados do perfil.");
            e.printStackTrace();
        }
    }

    /**
     * Exibe o painel de perfil e carrega as informações do utilizador.
     */
    @FXML
    public void onShowProfile1() {
        loadUserProfile();
        showPane(profilePane);
    }
}

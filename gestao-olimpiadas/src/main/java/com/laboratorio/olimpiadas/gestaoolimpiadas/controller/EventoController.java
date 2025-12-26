package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.EventoService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.LocalService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.PartidaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.ResultadoService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

public class EventoController {
    @FXML
    public AnchorPane eventCreatePane;
    @FXML
    public AnchorPane eventEditPane;
    @FXML
    public TextField fldName;
    @FXML
    public TextField fldYear;
    @FXML
    public DatePicker dpStartDate;
    @FXML
    public DatePicker dpEndDate;
    @FXML
    public ComboBox<String> cmboxCountry;
    @FXML
    public TextField fldMascotName;
    @FXML
    public TextField fldMascotDescription;
    @FXML
    public ImageView imageViewMascot;
    private int idEvento;
    private int idModalidade = -1;
    private String modalidadeNome;
    boolean isEdit = false;
    boolean isMatch = false;
    private String imageBytes;
    private TableView<Evento> tableViewEventos;
    private EventoService eventoService;
    private LocalService localService;

    // Create Matches

    @FXML
    public TableView<Modalidade> tableMModalidades;
    @FXML
    public TableColumn<Modalidade, String> colMNomeModalidade;
    @FXML
    public TableColumn<Modalidade, String> colMGenero;
    @FXML
    public TableColumn<Modalidade, String> colMTipoPontuacao;

    @FXML
    public TableView<Equipa> tableMEquipas;
    @FXML
    public TableColumn<Equipa, String> colMNomeEquipa;
    @FXML
    public TableView<Atleta> tableMAtletas;
    @FXML
    public TableColumn<Atleta, String> colMNomeAtleta;
    @FXML
    public ComboBox<Local> cmbMLocal;
    @FXML
    public DatePicker dpMData;
    @FXML
    private ComboBox<String> cmbMHora;
    @FXML
    private ComboBox<String> cmbMMinuto;


    @FXML
    public DatePicker dpMDataFim;
    @FXML
    private ComboBox<String> cmbMHoraFim;
    @FXML
    private ComboBox<String> cmbMMinutoFim;


    public EventoController() {
        this.eventoService = new EventoService();
    }

    public void inicializarTabela(TableView<Evento> tableViewEventos) {
        this.tableViewEventos = tableViewEventos;
    }

    public void isEdit(){
        isEdit = true;
    }

    public void isMatch(){
        isMatch = true;
    }

    @FXML
    public void initialize() throws SQLException {
        if(isEdit) {
            carregarOpcoes();
            configurarRestricoesDeCampos();
            imageViewMascot.setOnMouseClicked(event -> escolherImagemMascote());
        }
        localService = new LocalService();
    }

    public void btnAddEvent() throws FileNotFoundException {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        if (imageBytes == null) {
            AlertUtils.showErrorAlert("Erro", "A imagem do mascote não foi carregada.");
            return;
        }

        boolean success = eventoService.insertEvento(
                fldName.getText(),
                fldYear.getText(),
                dpStartDate.getValue(),
                dpEndDate.getValue(),
                cmboxCountry.getValue(),
                fldMascotName.getText(),
                fldMascotDescription.getText(),
                this.imageBytes
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Evento inserido com sucesso!");
            closeCurrentWindowCreate();
            carregarDados();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Evento não foi inserido!");
        }
    }

    /**
     * Configurar botão de editar Evento.
     */
    @FXML
    public void btnEditEvent() {
        if (!areFieldsValid()) {
            AlertUtils.showErrorAlert("Erro de Validação", "Todos os campos devem ser preenchidos.");
            return;
        }

        boolean success = EventoService.updateEvento(
                idEvento,
                fldName.getText(),
                fldYear.getText(),
                dpStartDate.getValue(),
                dpEndDate.getValue(),
                cmboxCountry.getValue(),
                fldMascotName.getText(),
                fldMascotDescription.getText(),
                this.imageBytes
        );

        if (success) {
            AlertUtils.showInfoAlert("Sucesso!", "Evento editado com sucesso!");
            closeCurrentWindowEdit();
            carregarDados();
        } else {
            AlertUtils.showErrorAlert("Erro!", "Evento não foi editado!");
        }
    }

    @FXML
    public void escolherImagemMascote() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Imagens (JPG, PNG)", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                byte[] fileData = new byte[(int) selectedFile.length()];
                fileInputStream.read(fileData);
                fileInputStream.close();
                this.imageBytes = Base64.getEncoder().encodeToString(fileData);

                Image image = new Image(selectedFile.toURI().toString());
                imageViewMascot.setImage(image);
            } catch (IOException e) {
                AlertUtils.showErrorAlert("Erro ao carregar a imagem", "Não foi possível carregar a imagem selecionada.");
                e.printStackTrace();
            }
        }
    }

    private boolean areFieldsValid() {
        return fldName.getText() != null && fldYear.getText() != null && dpStartDate.getValue() != null &&
                dpEndDate.getValue() != null && cmboxCountry.getValue() != null && fldMascotName.getText() != null
                && fldMascotDescription.getText() != null;
    }

    void carregarOpcoes() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList(EventoService.getCountries());
        cmboxCountry.setItems(countries);
    }

    private void configurarRestricoesDeCampos() {
        fldYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) fldYear.setText(oldValue);
        });
    }

    private void closeCurrentWindowCreate() {
        Stage currentStage = (Stage) eventCreatePane.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void closeCurrentWindowEdit() {
        Stage currentStage = (Stage) eventEditPane.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }


    /**
     * Mostra os dados od evento.
     * @param evento
     */
    public void setEvento(Evento evento) {
        idEvento = evento.getId();
        fldName.setText(evento.getNome());
        fldYear.setText(String.valueOf(evento.getAno()));
        dpStartDate.setValue(evento.getDataInicio());
        dpEndDate.setValue(evento.getDataFim());
        cmboxCountry.setValue(evento.getPais());
        Evento mascote = eventoService.listarMascote(evento.getIdMascote());
        String imageBytes = mascote.getImagemMascote();
        if (imageBytes != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(imageBytes);
            Image image = new Image(new ByteArrayInputStream(decodedBytes));
            imageViewMascot.setImage(image);
        } else {
            imageViewMascot.setImage(null);
        }
        fldMascotName.setText(mascote.getDescricao_mascote());
        fldMascotDescription.setText(mascote.getDescricaoMascote());
    }

    public void carregarDados() {
        if (tableViewEventos != null) {
            ObservableList<Evento> eventos = FXCollections.observableArrayList(eventoService.listarEventos());
            tableViewEventos.setItems(eventos);
        }
    }

    public void addModalidade(int idEvent, int idModalidade) {
        eventoService.addModalidade(idEvent,idModalidade);

    }

    public void setEventoMatches(int id) {
        idEvento = id;
    }

    public void addInfo() throws ParseException {

        if (idModalidade != -1) {

            int idLocal = cmbMLocal.getValue().getId();

            //Data Inicio

            LocalDate data = dpMData.getValue();

            String horaString = cmbMHora.getValue();
            int hora = Integer.parseInt(horaString);

            String minutoString = cmbMMinuto.getValue();
            int minuto = Integer.parseInt(minutoString);

            LocalDateTime dataHora = LocalDateTime.of(data, java.time.LocalTime.of(hora, minuto));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dataHoraInicio = dataHora.format(formatter);

            //Data Fim

            LocalDate dataFim = dpMDataFim.getValue();

            String horaStringFim = cmbMHoraFim.getValue();
            int horaFim = Integer.parseInt(horaStringFim);

            String minutoStringFim = cmbMMinutoFim.getValue();
            int minutoFim = Integer.parseInt(minutoStringFim);

            LocalDateTime dataHoraFim = LocalDateTime.of(dataFim, java.time.LocalTime.of(horaFim, minutoFim));

            String dataHoraFimm = dataHoraFim.format(formatter);

            int capacidade = eventoService.getCapacidade(idModalidade);

            eventoService.addMatchApi(cmbMLocal.getValue().getNome(),modalidadeNome,dataHoraInicio,dataHoraFimm,capacidade,idEvento);
            eventoService.addMatch(idEvento,idModalidade,idLocal,dataHoraInicio);
        }else{
            AlertUtils.showErrorAlert("Erro","Selecione uma modalidade!");
        }
   }

    public void deleteInfo(ActionEvent actionEvent) {
    }

    public void setModalidadesMatches() {
        ObservableList<Modalidade> modalidades = FXCollections.observableArrayList(eventoService.listarModalidadesPorEvento(idEvento));
        tableMModalidades.setItems(modalidades);

        tableMModalidades.setRowFactory(tv -> {
            TableRow<Modalidade> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Modalidade selectedModalidade = row.getItem();
                    idModalidade = selectedModalidade.getId();
                    modalidadeNome = selectedModalidade.getNome();
                    setEquipasMatches(selectedModalidade);
                    setAletaMatches(selectedModalidade);
                }
            });
            return row;
        });
    }

    public void setCmbMLocal(){
        ObservableList<Local> locais = FXCollections.observableArrayList(localService.listarLocais());
        cmbMLocal.setItems(locais);
        cmbMLocal.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Local item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome());
                }
            }
        });

        cmbMLocal.setConverter(new StringConverter<Local>() {
            @Override
            public String toString(Local object) {
                return object == null ? "" : object.getNome();
            }

            @Override
            public Local fromString(String string) {
                return null;
            }
        });
    }

    public void setEquipasMatches(Modalidade modalidadeSelecionada) {
        ObservableList<Equipa> equipas = FXCollections.observableArrayList(eventoService.listarEquipasByIdModalidadeEvento(modalidadeSelecionada,idEvento));
        tableMEquipas.setItems(equipas);
    }

    public void setAletaMatches(Modalidade modalidadeSelecionada) {
        ObservableList<Atleta> atletas = FXCollections.observableArrayList(eventoService.listarAtletasByIdModalidadeEvento(modalidadeSelecionada,idEvento));
        tableMAtletas.setItems(atletas);
    }


    public void carregarOpcoesMatches() {
        colMNomeModalidade.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMGenero.setCellValueFactory(new PropertyValueFactory<>("generoNome"));
        colMTipoPontuacao.setCellValueFactory(new PropertyValueFactory<>("medidaPontuacaoNome"));
        colMNomeEquipa.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMNomeAtleta.setCellValueFactory(new PropertyValueFactory<>("nome"));
        setCmbMLocal();
        setModalidadesMatches();
    }

    public int getNumberOfMatches() throws SQLException {
        ObservableList<Partida> partidas = FXCollections.observableArrayList(PartidaService.getAllMatches());
        return partidas.size();
    }
}

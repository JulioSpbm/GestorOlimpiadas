package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Resultado;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.ResultadoService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ResultadoController {

    @FXML
    private Button btnResults;

    @FXML
    private TableView<Resultado> tableViewGenerateResults;

    @FXML
    private TableColumn<Resultado, String> colResName;

    @FXML
    private TableColumn<Resultado, String> colResGenre;

    @FXML
    private TableColumn<Resultado, String> colResType;

    @FXML
    private TableColumn<Resultado, String> colResScore;

    @FXML
    private TableColumn<Resultado, Integer> colResInscriptions;

    @FXML
    public void initialize() throws SQLException {
        colResName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colResGenre.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colResType.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colResScore.setCellValueFactory(new PropertyValueFactory<>("pontuacao"));
        colResInscriptions.setCellValueFactory(new PropertyValueFactory<>("numeroInscricoes"));

        ObservableList<Resultado> resultados = FXCollections.observableArrayList(ResultadoService.getModalidadeInscricoes());
        tableViewGenerateResults.setItems(resultados);

        tableViewGenerateResults.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Resultado resultado = tableViewGenerateResults.getSelectionModel().getSelectedItem();
                if (resultado != null) {
                    List<Resultado> resultados1 = ResultadoService.getResultadoByModalidade(resultado.getIdModalidade());
                    StringBuilder dadosAcumulados = new StringBuilder();
                    if (resultados1 != null && !resultados1.isEmpty()) {
                        String nomeModalidade = resultados1.get(0).getNomeModalidade();
                        dadosAcumulados.append("Modalidade: ").append(nomeModalidade).append("\n\n");

                        for (Resultado resultado2 : resultados1) {
                            dadosAcumulados.append("Atleta: ").append(resultado2.getNomeAtleta()).append("\n")
                                    .append("Equipa: ").append(resultado2.getNomeEquipa()).append("\n")
                                    .append("Medalha: ").append(resultado2.getNomeMedalha()).append("\n")
                                    .append("Data do Resultado: ").append(resultado2.getDataResultado()).append("\n")
                                    .append("Pontuação: ").append(resultado2.getPontuacao()).append("\n")
                                    .append("Evento: ").append(resultado2.getNomeEvento()).append("\n\n");
                        }
                        AlertUtils.showInfoAlert("Resultados", dadosAcumulados.toString());
                    }
                }
            }
        });

        btnResults.setOnAction(event -> {
            try {
                handleBtnResultsClick();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Verifica se há modalidades selecionadas na tabela, dependendo do tipo de modalidade selecionada,
     * chama o método apropriado para gerar os resultados.
     * @throws SQLException
     */
    private void handleBtnResultsClick() throws SQLException {
        List<Resultado> selectedResults = tableViewGenerateResults.getSelectionModel().getSelectedItems();

        if (selectedResults.isEmpty()) {
            AlertUtils.showErrorAlert("Error", "Please select a modality.");
        } else {
            for (Resultado resultado : selectedResults) {
                if (resultado.getTipo().equals("Individual")) {
                    generateIndividualResults(resultado);
                } else if (resultado.getTipo().equals("Team")) {
                    generateTeamResults(resultado);
                }
            }
        }
    }

    private void atribuirMedalhas(List<Resultado> resultados) {
        resultados.sort((r1, r2) -> Double.compare(r2.getPontuacaoIndividual(), r1.getPontuacaoIndividual()));

        if (resultados.size() > 0) resultados.get(0).setIdMedalha(1);
        if (resultados.size() > 1) resultados.get(1).setIdMedalha(2);
        if (resultados.size() > 2) resultados.get(2).setIdMedalha(3);
        if (resultados.size() > 3) resultados.get(3).setIdMedalha(4);
    }

    /**
     * Gera e insere os resultados individuais para uma modalidade. O método cria resultados baseados no tipo de
     * modalidade e os valores gerados para cada atleta.
     * @param resultado
     * @throws SQLException
     */
    private void generateIndividualResults(Resultado resultado) throws SQLException {
        List<String> ids = resultado.getIds();
        List<Resultado> resultadosDetalhados = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            double valorResultado = 0;
            String unidade = "";

            switch (resultado.getPontuacao()) {
                case "Time":
                    valorResultado = generateTimeResult();
                    break;
                case "Points":
                    valorResultado = generatePointResult();
                    break;
                case "Distance":
                    valorResultado = generateDistanceResult();
                    break;
                default:
                    valorResultado = Math.random() * 100;
                    break;
            }

            Resultado detalhe = new Resultado();

            detalhe.setIdModalidade(resultado.getIdModalidade());
            detalhe.setIdAtleta(Integer.parseInt(ids.get(i)));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            detalhe.setDataResultado(LocalDate.now().format(formatter));
            detalhe.setPontuacaoIndividual(valorResultado);
            detalhe.setIdEvento(resultado.getIdEvento());


            resultadosDetalhados.add(detalhe);
        }

        atribuirMedalhas(resultadosDetalhados);

        for (Resultado detalhe : resultadosDetalhados) {
            boolean success = ResultadoService.saveResultadoIndividual(detalhe);
            if (success){
                AlertUtils.showInfoAlert("Sucesso!", "Resultado inserido com sucesso!");
            }else{
                AlertUtils.showErrorAlert("Erro!", "Resultado não foi inserido!");
            }
        }
    }

    /**
     * Gere e insere os resultados das equipas para uma modalidade.
     * @param resultado
     */
    private void generateTeamResults(Resultado resultado) {
        List<String> ids = resultado.getIds();
        List<Resultado> resultadosEquipes = new ArrayList<>();

        // Criar uma lista de equipas com suas pontuações e vitórias
        class EquipaPontuacao {
            int idEquipa;
            double pontuacao;
            int vitorias = 0;

            EquipaPontuacao(int idEquipa, double pontuacao) {
                this.idEquipa = idEquipa;
                this.pontuacao = pontuacao;
            }
        }

        List<EquipaPontuacao> equipas = new ArrayList<>();
        for (String id : ids) {
            double valorResultado = (int) (Math.random() * 10 + 1);
            equipas.add(new EquipaPontuacao(Integer.parseInt(id), valorResultado));
        }

        // Comparar todas as equipas entre si
        for (int i = 0; i < equipas.size(); i++) {
            for (int j = i + 1; j < equipas.size(); j++) {
                EquipaPontuacao equipa1 = equipas.get(i);
                EquipaPontuacao equipa2 = equipas.get(j);

                if (equipa1.pontuacao > equipa2.pontuacao) {
                    equipa1.vitorias++;
                } else if (equipa2.pontuacao > equipa1.pontuacao) {
                    equipa2.vitorias++;
                }
            }
        }

        // Ordenar as equipas pelo número de vitórias (descendente)
        equipas.sort((e1, e2) -> Integer.compare(e2.vitorias, e1.vitorias));

        // Atribuir medalhas
        for (int i = 0; i < equipas.size(); i++) {
            EquipaPontuacao equipa = equipas.get(i);
            Resultado detalhe = new Resultado();

            detalhe.setIdModalidade(resultado.getIdModalidade());
            detalhe.setIdEquipa(equipa.idEquipa);
            detalhe.setPontuacaoTeam(equipa.pontuacao);
            detalhe.setDataResultado(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            detalhe.setIdEvento(resultado.getIdEvento());

            // Atribuir medalhas
            switch (i) {
                case 0 -> detalhe.setIdMedalha(1); // Ouro
                case 1 -> detalhe.setIdMedalha(2); // Prata
                case 2 -> detalhe.setIdMedalha(3); // Bronze
                default -> detalhe.setIdMedalha(4); // Sem medalha
            }

            resultadosEquipes.add(detalhe);
        }

        // Salvar os resultados
        for (Resultado detalhe : resultadosEquipes) {
            boolean success = ResultadoService.saveResultadoEquipa(detalhe);
            if (success) {
                AlertUtils.showInfoAlert("Sucesso!", "Resultado inserido com sucesso!");
            } else {
                AlertUtils.showErrorAlert("Erro!", "Resultado não foi inserido!");
            }
        }
    }


    /**
     * Gera um resultado aleatório para uma modalidade de tempo
     * @return
     */
    private double generateTimeResult() {
        return Math.random() * 6 + 9; // Entre 9 e 15 segundos
    }

    /**
     * Gera um resultado aleatório para uma modalidade de distancia
     * @return
     */
    private double generateDistanceResult() {
        return Math.random() * 5 + 5; // Entre 5m e 10m
    }


    /**
     * Gera um resultado aleatório de pontos para uma modalidade.
     * O valor gerado estará no intervalo de 50 a 150 pontos.
     * @return
     */
    private double generatePointResult() {
        return Math.random() * 100 + 50; // Entre 50 e 150 pontos
    }


    /**
     * Obtém o número de resultados registados.
     * Consulta os dados da modalidade e retorna o número de inscrições registadas.
     * @return
     * @throws SQLException
     */
    public int getNumberOfInscriptions() throws SQLException {
        ObservableList<Resultado> resultados = FXCollections.observableArrayList(ResultadoService.getModalidadeInscricoes());
        return resultados.size();
    }
}

package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.EventoDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

public class EventoService {

    private static final String USERNAME = "EG3";
    private static final String PASSWORD = "7?bo?PL03S";

    private static final EventoDAO eventoDAO = new EventoDAO();

    /**
     * Insere um novo evento na base de dados.
     * @param nome Nome do evento.
     * @param ano Ano do evento.
     * @param startDate Data de início do evento.
     * @param endDate Data de fim do evento.
     * @param pais Nome do país anfitrião.
     * @param nomeMascote Nome da mascote.
     * @param descricaoMascote Descrição da mascote.
     * @param imageMascote Caminho do arquivo da foto da mascote.
     * @return true se o evento for inserido com sucesso, caso contrário false.
     */
    public static boolean insertEvento(String nome, String ano, LocalDate startDate, LocalDate endDate, String pais, String nomeMascote, String descricaoMascote, String imageMascote) throws FileNotFoundException {
        LocalDate hoje = LocalDate.now();

        if (startDate.isBefore(hoje) && endDate.isBefore(hoje)) {
            AlertUtils.showErrorAlert("Erro", "A data não pode ser no passado.");
            return false;
        }

        if (endDate.isBefore(startDate)) {
            AlertUtils.showErrorAlert("Erro", "A data de fim não pode ser anterior à data de início.");
            return false;
        }

        int anoo = Integer.parseInt(ano);
        int idPais = eventoDAO.getPaisIdByName(pais);

        if(idPais == -1){
            AlertUtils.showErrorAlert("Aviso!", "Nenhum país encontrado com o nome: " + pais);
            return false;
        }

        Olimpiada olimpiada = new Olimpiada(anoo,idPais);

        Evento evento = new Evento(olimpiada, nome, ano, nomeMascote, descricaoMascote, imageMascote, Date.valueOf(startDate), Date.valueOf(endDate));

        return eventoDAO.createEvento(evento);
    }

    /**
     * Edita um evento.
     *
     * @param idEvento
     * @param nome
     * @param ano
     * @param startDate
     * @param endDate
     * @param pais
     * @param nomeMascote
     * @param descricaoMascote
     * @return
     */
    public static boolean updateEvento(int idEvento, String nome, String ano, LocalDate startDate, LocalDate endDate, String pais, String nomeMascote, String descricaoMascote, String foto_mascote) {
        LocalDate hoje = LocalDate.now();

        if (startDate.isBefore(hoje) && endDate.isBefore(hoje)) {
            AlertUtils.showErrorAlert("Erro", "A data não pode ser no passado.");
            return false;
        }

        if (endDate.isBefore(startDate)) {
            AlertUtils.showErrorAlert("Erro", "A data de fim não pode ser anterior à data de início.");
            return false;
        }

        int anoo = Integer.parseInt(ano);
        int idPais = eventoDAO.getPaisIdByName(pais);

        if(idPais == -1){
            AlertUtils.showErrorAlert("Aviso!", "Nenhum país encontrado com o nome: " + pais);
            return false;
        }

        Olimpiada olimpiada = new Olimpiada(anoo,idPais);

        Evento evento = new Evento(olimpiada, nome, ano, nomeMascote, descricaoMascote, foto_mascote, Date.valueOf(startDate), Date.valueOf(endDate));
        evento.setId(idEvento);

        return eventoDAO.updateEvento(evento);
    }

    /**
     * Verifica se a data é valida.
     * @return true or false.
     */
    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lista todas as mascotes atraves do ID.
     * @param idMascote
     * @return
     */
    public static Evento listarMascote(int idMascote) {
        return eventoDAO.getMascoteById(idMascote);
    }

    /**
     * Obtém todos os países da base de dados.
     * @return Lista de países.
     */
    public static List<String> getCountries() throws SQLException {
        return eventoDAO.getAllCountries();
    }

    /**
     * Obtém todos os eventos da base de dados.
     * @return Lista de eventos.
     */

    public List<Evento> listarEventos() {
        return eventoDAO.listarEventos();
    }

    /**
     * Obtém todas as modalidade da base de dados pelo o evento.
     * @param id Evento id
     * @return Lista de modalidades.
     */

    public List<Modalidade> listarModalidadesPorEvento(int id) {
       return eventoDAO.getModalidadesByIdEvent(id);
    }

    /**
     * Obtém todas as equipas da base de dados pelo o evento e modalidade.
     * @param modalidade Modalidade
     * @param eventoId Evento id
     * @return Lista de equipas.
     */

    public List<Equipa> listarEquipasByIdModalidadeEvento(Modalidade modalidade, int eventoId) {
        return eventoDAO.getEquipasByIdModalidadeEvento(modalidade.getId(),eventoId,modalidade.getGeneroNome());
    }

    public List<Atleta> listarAtletasByIdModalidadeEvento(Modalidade modalidade, int eventoId) {
        return eventoDAO.getAtletasByIdModalidadeEvento(modalidade.getId(),eventoId,modalidade.getGeneroNome());
    }

    public List<Equipa> listarEquipasByIdModalidade(Modalidade modalidade, int eventoId) {
        return eventoDAO.getEquipasByIdModalidadeEvento(modalidade.getId(),eventoId, modalidade.getGeneroNome());
    }

    public void addModalidade(int idEvent, int idModalidade) {
        eventoDAO.addModalidade(idEvent,idModalidade);
    }

    public void addMatch(int idEvento, int idModalidade, int idLocal, String dataHoraFormatada) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date parsedDate = dateFormat.parse(dataHoraFormatada);
        Timestamp timestamp = new Timestamp(parsedDate.getTime());

        eventoDAO.addMatch(idEvento, idModalidade, idLocal, timestamp);

    }

    public int getCapacidade(int idModalidade){
        return eventoDAO.getCapacidadeLocalPorModalidade(idModalidade);
    }

    public void addMatchApi(String local,String sport,String startDate,String endDate,int capacity,int eventId) {
        try {

            String jsonBody = String.format(
                    "{\"StartDate\":\"%s\",\"EndDate\":\"%s\", \"Location\":\"%s\", \"Sport\":\"%s\", \"Capacity\":%d, \"EventId\":%d, \"Active\":\"true\"}",
                    startDate.replace(" ", "T"), endDate.replace(" ", "T"), local, sport, capacity, eventId
            );

            HttpClient client = HttpClient.newHttpClient();
            String auth = USERNAME + ":" + PASSWORD;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authorizationHeader = "Basic " + encodedAuth;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://services.inapa.com/opo/api/game"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorizationHeader)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                AlertUtils.showInfoAlert("Info", "Prova criada com sucesso!");
            } else {
                AlertUtils.showWarningAlert("Erro", "Falha ao criar prova!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
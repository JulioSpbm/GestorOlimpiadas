package com.laboratorio.olimpiadas.gestaoolimpiadas.xml;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XMLImporter {

    public static List<Atleta> importXMLAtletas(Document doc) {
        List<Atleta> atletas = new ArrayList<>();

        try {
            NodeList athleteList = doc.getElementsByTagName("athlete");

            for (int i = 0; i < athleteList.getLength(); i++) {
                Node athleteNode = athleteList.item(i);

                if (athleteNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element athleteElement = (Element) athleteNode;

                    String nome = athleteElement.getElementsByTagName("name").item(0).getTextContent();
                    String pais = athleteElement.getElementsByTagName("country").item(0).getTextContent();
                    String genero = athleteElement.getElementsByTagName("genre").item(0).getTextContent();
                    int altura = Integer.parseInt(athleteElement.getElementsByTagName("height").item(0).getTextContent());
                    int peso = Integer.parseInt(athleteElement.getElementsByTagName("weight").item(0).getTextContent());
                    String dataNascimento = athleteElement.getElementsByTagName("dateOfBirth").item(0).getTextContent();

                    List<Participacao> participacoes = new ArrayList<>();

                    NodeList participationList = athleteElement.getElementsByTagName("participation");

                    if (participationList.getLength() > 0) {
                        for (int j = 0; j < participationList.getLength(); j++) {
                            Element participationElement = (Element) participationList.item(j);
                            int anoParticipacao = Integer.parseInt(participationElement.getElementsByTagName("year").item(0).getTextContent());
                            int ouro = Integer.parseInt(participationElement.getElementsByTagName("gold").item(0).getTextContent());
                            int prata = Integer.parseInt(participationElement.getElementsByTagName("silver").item(0).getTextContent());
                            int bronze = Integer.parseInt(participationElement.getElementsByTagName("bronze").item(0).getTextContent());

                            Participacao participacao = new Participacao(anoParticipacao, ouro, prata, bronze);
                            participacoes.add(participacao);
                        }
                    }

                    Date dataNascimentoo = Date.valueOf(dataNascimento);

                    Atleta atleta = new Atleta();
                    atleta.setNome(nome);
                    atleta.setPaisNome(pais);
                    atleta.setDataNascimento(dataNascimentoo);
                    atleta.setGeneroNome(genero);
                    atleta.setAltura(altura);
                    atleta.setPeso(peso);
                    atleta.setParticipacoes(participacoes);
                    atletas.add(atleta);

                    atletas.add(atleta);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return atletas;
    }

    public static List<Modalidade> importXMLModalitie(Document doc) {
        List<Modalidade> modalidades = new ArrayList<>();

        try {
            NodeList modalidadeList = doc.getElementsByTagName("sport");

            for (int i = 0; i < modalidadeList.getLength(); i++) {
                Node modalidadeNode = modalidadeList.item(i);

                if (modalidadeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element sportElement = (Element) modalidadeNode;

                    String tipo = getElementText(sportElement, "type");
                    String genero = getElementText(sportElement, "genre");
                    String nome = getElementText(sportElement, "name");
                    String descricao = getElementText(sportElement, "description");
                    int minParticipantes = Integer.parseInt(getElementText(sportElement, "minParticipants"));
                    String medidaPontuacao = getElementText(sportElement, "scoringMeasure");
                    String numJogo = getElementText(sportElement, "oneGame");

                    try (Connection connection = DBConnection.getInstance().getConnection()) {
                        adicionarPontuacaoSeNaoExistir(connection, medidaPontuacao);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    RecordeModalidade recordeOlimpico = null;
                    NodeList recordList = sportElement.getElementsByTagName("olympicRecord");
                    if (recordList.getLength() > 0) {
                        Element recordElement = (Element) recordList.item(0);
                        int anoRecorde = Integer.parseInt(recordElement.getElementsByTagName("year").item(0).getTextContent());
                        String titularRecorde = recordElement.getElementsByTagName("holder").item(0).getTextContent();

                        String tempoRecorde = null;
                        String medalhasRecorde = null;

                        Node timeNode = recordElement.getElementsByTagName("time").item(0);
                        Node medalsNode = recordElement.getElementsByTagName("medals").item(0);

                        if (timeNode != null && timeNode.getTextContent() != null && !timeNode.getTextContent().isEmpty()) {
                            tempoRecorde = timeNode.getTextContent();
                        } else if (medalsNode != null && medalsNode.getTextContent() != null && !medalsNode.getTextContent().isEmpty()) {
                            medalhasRecorde = medalsNode.getTextContent();
                        }

                        if (Objects.equals(tipo, "Individual")) {
                            recordeOlimpico = new RecordeModalidade();
                            recordeOlimpico.setUtilizadorNome(titularRecorde);
                            recordeOlimpico.setEquipaNome(null);
                            recordeOlimpico.setNum_medalhasNome(medalhasRecorde);
                            recordeOlimpico.setRecord_olimpicoNome(tempoRecorde);
                            recordeOlimpico.setAno_recorde(anoRecorde);
                        } else if (Objects.equals(tipo, "Collective")) {
                            recordeOlimpico = new RecordeModalidade();
                            recordeOlimpico.setUtilizadorNome(null);
                            recordeOlimpico.setEquipaNome(titularRecorde);
                            recordeOlimpico.setNum_medalhasNome(medalhasRecorde);
                            recordeOlimpico.setRecord_olimpicoNome(tempoRecorde);
                            recordeOlimpico.setAno_recorde(anoRecorde);
                        }
                    }

                    VencedorOlimpico vencedorOlimpico = null;
                    NodeList winnerList = sportElement.getElementsByTagName("winnerOlympic");
                    if (winnerList.getLength() > 0) {
                        Element winnerElement = (Element) winnerList.item(0);
                        int anoVencedor = Integer.parseInt(winnerElement.getElementsByTagName("year").item(0).getTextContent());
                        String titularRecorde = winnerElement.getElementsByTagName("holder").item(0).getTextContent();

                        String tempoVencedor = null;
                        String medalhaVencedor = null;

                        Node timeNode = winnerElement.getElementsByTagName("time").item(0);
                        Node medalsNode = winnerElement.getElementsByTagName("medal").item(0);

                        if (timeNode != null && timeNode.getTextContent() != null && !timeNode.getTextContent().isEmpty()) {
                            tempoVencedor = timeNode.getTextContent();
                        } else if (medalsNode != null && medalsNode.getTextContent() != null && !medalsNode.getTextContent().isEmpty()) {
                            medalhaVencedor = medalsNode.getTextContent();
                        }

                        if (Objects.equals(tipo, "Individual")) {
                            vencedorOlimpico = new VencedorOlimpico();
                            vencedorOlimpico.setUtilizadorNome(titularRecorde);
                            vencedorOlimpico.setEquipaNome(null);
                            vencedorOlimpico.setMedalhaNome(medalhaVencedor);
                            vencedorOlimpico.setResultadoNome(tempoVencedor);
                            vencedorOlimpico.setAno_vencedor(anoVencedor);
                        } else if (Objects.equals(tipo, "Collective")) {
                            vencedorOlimpico = new VencedorOlimpico();
                            vencedorOlimpico.setUtilizadorNome(null);
                            vencedorOlimpico.setEquipaNome(titularRecorde);
                            vencedorOlimpico.setMedalhaNome(medalhaVencedor);
                            vencedorOlimpico.setResultadoNome(tempoVencedor);
                            vencedorOlimpico.setAno_vencedor(anoVencedor);
                        }
                    }

                    List<String> regras = new ArrayList<>();
                    NodeList ruleList = sportElement.getElementsByTagName("rule");

                    for (int j = 0; j < ruleList.getLength(); j++) {
                        String regra = ruleList.item(j).getTextContent();
                        regras.add(regra);
                    }

                    StringBuilder regraTexto = new StringBuilder();
                    for (String regra : regras) {
                        if (regraTexto.length() > 0) {
                            regraTexto.append(" - ");
                        }
                        regraTexto.append(regra);
                    }

                    String resultadoFinal = regraTexto.toString();

                    Modalidade modalidade = new Modalidade();

                    modalidade.setTipoNome(tipo);
                    modalidade.setGeneroNome(genero);
                    modalidade.setNome(nome);
                    modalidade.setDescricao(descricao);
                    modalidade.setMinParticipantes(minParticipantes);
                    modalidade.setPontuacaoTipo(medidaPontuacao);
                    modalidade.setNumJogosNome(numJogo);
                    modalidade.setRecordeOlimpico(recordeOlimpico);
                    modalidade.setVencedorOlimpico(vencedorOlimpico);
                    modalidade.setRegras(resultadoFinal);

                    modalidades.add(modalidade);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return modalidades;
    }

    /**
     * Insere uma nova medida de pontuação na tabela, somente se ela não existir.
     *
     * @param connection        Conexão com o banco de dados.
     * @param novaPontuacao     Nova medida de pontuação a ser inserida.
     * @return true se a pontuação foi inserida, false se já existia.
     */
    public static boolean adicionarPontuacaoSeNaoExistir(Connection connection, String novaPontuacao) {
        String verificaPontuacaoSQL = "SELECT COUNT(*) FROM tab_pontuacao WHERE medida_pontuacao = ?";
        String inserePontuacaoSQL = "INSERT INTO tab_pontuacao (medida_pontuacao) VALUES (?)";

        try {
            try (PreparedStatement verificaStmt = connection.prepareStatement(verificaPontuacaoSQL)) {
                verificaStmt.setString(1, novaPontuacao);
                try (ResultSet rs = verificaStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
            try (PreparedStatement insereStmt = connection.prepareStatement(inserePontuacaoSQL)) {
                insereStmt.setString(1, novaPontuacao);
                int linhasAfetadas = insereStmt.executeUpdate();
                return linhasAfetadas > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar medida de pontuação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public static List<Equipa> importXMLTeams(Document doc) {
        List<Equipa> equipas = new ArrayList<>();

        try {
            NodeList teamList = doc.getElementsByTagName("team");

            for (int i = 0; i < teamList.getLength(); i++) {
                Node teamNode = teamList.item(i);

                if (teamNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element teamElement = (Element) teamNode;

                    Equipa equipa = new Equipa();
                    equipa.setNome(getElementText(teamElement, "name"));
                    equipa.setPaisNome(getElementText(teamElement, "country"));
                    equipa.setGeneroNome(getElementText(teamElement, "genre"));
                    equipa.setModalidadeNome(getElementText(teamElement, "sport"));
                    equipa.setAnoFundacao(Integer.parseInt(getElementText(teamElement, "foundationYear")));

                    NodeList participationList = teamElement.getElementsByTagName("participation");
                    List<Participacao> participacoes = new ArrayList<>();

                        for (int j = 0; j < participationList.getLength(); j++) {
                            Node participationNode = participationList.item(j);

                            if (participationNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element participationElement = (Element) participationNode;

                                Participacao participacao = new Participacao();
                                participacao.setAnoParticipacao(Integer.parseInt(getElementText(participationElement, "year")));
                                participacao.setResultadoNome(getElementText(participationElement, "result"));

                                participacoes.add(participacao);
                            }
                        }

                    equipa.setParticipacoes(participacoes);
                    equipas.add(equipa);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return equipas;
    }

    private static String getElementText(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null && node.getTextContent() != null) {
                return node.getTextContent().trim();
            }
        }
        return "";
    }


    public static Document validateAndParseXML(File xmlFile, String xsdPath) throws Exception {
        URL schemaFileURL = XMLImporter.class.getClassLoader().getResource(xsdPath);
        if (schemaFileURL == null) {
            throw new FileNotFoundException("Ficheiro XSD não encontrado no classpath: " + xsdPath);
        }

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(schemaFileURL);
        Validator validator = schema.newValidator();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        dbFactory.setSchema(schema);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        try {
            validator.validate(new StreamSource(xmlFile));
            return dBuilder.parse(xmlFile);
        } catch (SAXException e) {
            throw new SAXException("Erro de validação do XML: " + e.getMessage(), e);
        }
    }

    public static String getDocumentRootElementName(File xmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            return doc.getDocumentElement().getNodeName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveHistoryXml(int idUtilizador, String nomeFicheiro, byte[] ficheiro) {
        String sql = "{CALL sp_inserir_historico_xml(?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idUtilizador);
            stmt.setString(2, nomeFicheiro);
            stmt.setBytes(3, ficheiro);

            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao inserir histórico XML: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

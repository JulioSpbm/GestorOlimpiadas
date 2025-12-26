package com.laboratorio.olimpiadas.gestaoolimpiadas.utils;

import com.laboratorio.olimpiadas.gestaoolimpiadas.controller.AtletaController;
import com.laboratorio.olimpiadas.gestaoolimpiadas.controller.EquipaController;
import com.laboratorio.olimpiadas.gestaoolimpiadas.controller.ModalidadeController;
import com.laboratorio.olimpiadas.gestaoolimpiadas.xml.XMLImporter;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XMLImporterUtils {

    private static final String SCHEMA_PATH_ATLETAS = "com/laboratorio/olimpiadas/gestaoolimpiadas/schemas/athletes_xsd.xml";
    private static final String SCHEMA_PATH_MODALIDADES = "com/laboratorio/olimpiadas/gestaoolimpiadas/schemas/sports_xsd.xml";
    private static final String SCHEMA_PATH_EQUIPAS = "com/laboratorio/olimpiadas/gestaoolimpiadas/schemas/teams_xsd.xml";

    private static final AtletaController atletaController = new AtletaController();
    private static final ModalidadeController modalidadeController = new ModalidadeController();
    private static final EquipaController equipaController = new EquipaController();

    private static File ATLETAS_XML;
    private static File MODALIDADES_XML;
    private static File EQUIPAS_XML ;

    private static File escolherFicheiroXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha o seu ficheiro!");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Ficheiros XML", "*.xml")
        );
        return fileChooser.showOpenDialog(null);
    }

    private static void processarXML(File xmlFile, String elementoRaiz, String schemaPath, Runnable importacao) {
        try {
            if (xmlFile != null) {
                String tipo = XMLImporter.getDocumentRootElementName(xmlFile);
                if (elementoRaiz.equals(tipo)) {
                    Document doc = XMLImporter.validateAndParseXML(xmlFile, schemaPath);
                    importacao.run();
                }
            }
        } catch (Exception e) {
            AlertUtils.showErrorAlert("XML","Ficheiro XML não é valido!");
            throw new RuntimeException(e);
        }
    }

    public static void importarXMLAtletas() {
        File xmlFile = escolherFicheiroXML();
        processarXML(xmlFile, "athletes", SCHEMA_PATH_ATLETAS, () -> {
            Document doc = null;
            try {
                doc = XMLImporter.validateAndParseXML(xmlFile, SCHEMA_PATH_ATLETAS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            setAtletasXml(xmlFile);
            try {
                atletaController.mostrarDados(XMLImporter.importXMLAtletas(doc));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void importarXMLModalitie() {
        File xmlFile = escolherFicheiroXML();
        processarXML(xmlFile, "sports", SCHEMA_PATH_MODALIDADES, () -> {
            Document doc = null;
            try {
                doc = XMLImporter.validateAndParseXML(xmlFile, SCHEMA_PATH_MODALIDADES);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            setModalidadesXml(xmlFile);
            modalidadeController.mostrarDados(XMLImporter.importXMLModalitie(doc));
        });
    }

    public static void importarXMLTeam() {
        File xmlFile = escolherFicheiroXML();
        processarXML(xmlFile, "teams", SCHEMA_PATH_EQUIPAS, () -> {
            Document doc = null;
            try {
                doc = XMLImporter.validateAndParseXML(xmlFile, SCHEMA_PATH_EQUIPAS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            setEquipasXml(xmlFile);
            equipaController.mostrarDados(XMLImporter.importXMLTeams(doc));
        });
    }

    public static String salvarXmlNoDiretorio(File file) {
        if (file != null) {
            String nomeFicheiroOriginal = file.getName();
            String extensao = "";
            int i = nomeFicheiroOriginal.lastIndexOf('.');
            if (i > 0) {
                extensao = nomeFicheiroOriginal.substring(i);
                nomeFicheiroOriginal = nomeFicheiroOriginal.substring(0, i);
            }
            String sufixo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nomeFicheiroUnico = nomeFicheiroOriginal + "_" + sufixo + extensao;

            String diretorioDestino = "historicoXML/";

            Path pathDestino = Path.of(diretorioDestino);

            try {
                if (!Files.exists(pathDestino)) {
                    Files.createDirectories(pathDestino);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return nomeFicheiroOriginal;
            }

            Path destino = pathDestino.resolve(nomeFicheiroUnico);

            try {
                Files.copy(file.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                return nomeFicheiroUnico;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void salvarHistoricoXmlAtleta(int id_utilizador) {
        File file = getAtletasXml();
        if (file != null) {
            String nomeFicheiro = file.getName();
            byte[] Ficheiro = lerFicheiroComoBytes(file);
            if (Ficheiro != null) {
                XMLImporter.saveHistoryXml(id_utilizador, nomeFicheiro, Ficheiro);
            } else {
                AlertUtils.showErrorAlert("Erro!", "Não foi possível ler o conteúdo do ficheiro de atletas.");
            }
        } else {
            AlertUtils.showErrorAlert("Erro!", "Nenhum ficheiro de atletas selecionado.");
        }
    }

    public static void salvarHistoricoXmlModalidades(int id_utilizador) {
        File file = getModalidadesXml();
        if (file != null) {
            String nomeFicheiro = file.getName();
            byte[] Ficheiro = lerFicheiroComoBytes(file);
            if (Ficheiro != null) {
                XMLImporter.saveHistoryXml(id_utilizador, nomeFicheiro, Ficheiro);
            } else {
                AlertUtils.showErrorAlert("Erro!", "Não foi possível ler o conteúdo do ficheiro de modalidades.");
            }
        } else {
            AlertUtils.showErrorAlert("Erro!", "Nenhum ficheiro de modalidades selecionado.");
        }
    }

    public static void salvarHistoricoXmlEquipas(int id_utilizador) {
        File file = getEquipasXml();
        if (file != null) {
            String nomeFicheiro = file.getName();
            byte[] Ficheiro = lerFicheiroComoBytes(file);
            if (Ficheiro != null) {
                XMLImporter.saveHistoryXml(id_utilizador, nomeFicheiro, Ficheiro);
            } else {
                AlertUtils.showErrorAlert("Erro!", "Não foi possível ler o conteúdo do ficheiro de equipas.");
            }
        } else {
            AlertUtils.showErrorAlert("Erro!", "Nenhum ficheiro de equipas selecionado.");
        }
    }

    private static byte[] lerFicheiroComoBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao ler o ficheiro: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static File getAtletasXml() {
        return ATLETAS_XML;
    }

    public static void setAtletasXml(File file) {
        ATLETAS_XML = file;
    }

    public static File getModalidadesXml() {
        return MODALIDADES_XML;
    }

    public static void setModalidadesXml(File file) {
        MODALIDADES_XML = file;
    }

    public static File getEquipasXml() {
        return EQUIPAS_XML;
    }

    public static void setEquipasXml(File file) {
        EQUIPAS_XML = file;
    }

}

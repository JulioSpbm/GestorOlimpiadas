module com.laboratorio.olimpiadas.gestaoolimpiadas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires javafx.base;
    requires java.net.http;
    requires org.json;
    requires jdk.accessibility;


    opens com.laboratorio.olimpiadas.gestaoolimpiadas.model to javafx.base;
    opens com.laboratorio.olimpiadas.gestaoolimpiadas.controller to javafx.fxml;
    exports com.laboratorio.olimpiadas.gestaoolimpiadas.controller;
    exports com.laboratorio.olimpiadas.gestaoolimpiadas.model;
    exports com.laboratorio.olimpiadas.gestaoolimpiadas;
}
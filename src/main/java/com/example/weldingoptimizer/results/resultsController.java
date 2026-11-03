package com.example.weldingoptimizer.results;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.btpoptimizer.model.optimizationResult;
import com.example.weldingoptimizer.SceneUtil;
import com.example.weldingoptimizer.resultDataHolder;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class resultsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label headingLabel;

    @FXML
    private TableView<optimizationResult> resultsTable;

    @FXML
    private TableColumn<optimizationResult, String> wCol;

    @FXML
    private TableColumn<optimizationResult, String> vCol;

    @FXML
    private TableColumn<optimizationResult, String> nCol;

    @FXML
    private TableColumn<optimizationResult, String> sCol;

    @FXML
    private TableColumn<optimizationResult, String> gCol;

    @FXML
    private TableColumn<optimizationResult, String> pCol;

    @FXML
    private TableColumn<optimizationResult, String> hCol;

    @FXML
    private TableColumn<optimizationResult, String> iCol;

    @FXML
    private TableColumn<optimizationResult, String> heatCol;

    @FXML
    private Label emptyHeading;

    @FXML
    private Label emptyMessage;

    @FXML
    void back(ActionEvent event) {
        SceneUtil.newScene("landing/landingView.fxml", event);
    }

    @FXML
    void initialize() {
        wCol.setCellValueFactory(cd -> format(cd.getValue().getParameters().getWireFeedRate(), 2));
        vCol.setCellValueFactory(cd -> format(cd.getValue().getParameters().getArcVoltage(), 2));
        nCol.setCellValueFactory(cd -> format(cd.getValue().getParameters().getNozzleDistance(), 3));
        sCol.setCellValueFactory(cd -> format(cd.getValue().getParameters().getWeldingSpeed(), 2));
        gCol.setCellValueFactory(cd -> format(cd.getValue().getParameters().getGasFlowRate(), 2));
        pCol.setCellValueFactory(cd -> format(cd.getValue().getPredictedPenetration(), 4));
        hCol.setCellValueFactory(cd -> format(cd.getValue().getReinforcementHeight(), 4));
        iCol.setCellValueFactory(cd -> format(cd.getValue().getCurrent(), 2));
        heatCol.setCellValueFactory(cd -> format(cd.getValue().getHeatInput(), 6));

        if (resultDataHolder.isShowEmptyState()) {
            resultsTable.setVisible(false);
            emptyHeading.setVisible(true);
            emptyMessage.setVisible(true);
            emptyMessage.setText(resultDataHolder.getStatusMessage());
            headingLabel.setText("Results");
        } else {
            resultsTable.setVisible(true);
            emptyHeading.setVisible(false);
            emptyMessage.setVisible(false);
            resultsTable.setItems(FXCollections.observableArrayList(resultDataHolder.getResults()));
            styleTableHeader();
        }
    }

    private static SimpleStringProperty format(double value, int decimals) {
        return new SimpleStringProperty(String.format("%." + decimals + "f", value));
    }

    private void styleTableHeader() {
        Platform.runLater(() -> {
            for (Node n : resultsTable.lookupAll(".column-header-background")) {
                n.setStyle("-fx-background-color: #0468BF;");
            }
            for (Node n : resultsTable.lookupAll(".column-header .label")) {
                n.setStyle("-fx-text-fill: white;");
            }
            Node filler = resultsTable.lookup(".filler");
            if (filler != null) {
                filler.setStyle("-fx-background-color: #0468BF;");
            }
        });
    }
}

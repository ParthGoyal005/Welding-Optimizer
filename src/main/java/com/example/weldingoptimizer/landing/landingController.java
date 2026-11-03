package com.example.weldingoptimizer.landing;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.weldingoptimizer.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class landingController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void openPredict(ActionEvent event) {
        SceneUtil.newScene("predict/predictView.fxml", event);
    }

    @FXML
    void openOptimize(ActionEvent event) {
        SceneUtil.newScene("optimize/optimizeView.fxml", event);
    }

    @FXML
    void initialize() {
    }
}

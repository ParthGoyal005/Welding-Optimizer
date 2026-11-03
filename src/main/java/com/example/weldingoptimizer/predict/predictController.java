package com.example.weldingoptimizer.predict;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.btpoptimizer.config.parameterRanges;
import com.example.btpoptimizer.model.weldingModel;
import com.example.weldingoptimizer.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class predictController {

    private final weldingModel model = new weldingModel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField wireFeedField;

    @FXML
    private TextField voltageField;

    @FXML
    private TextField nozzleField;

    @FXML
    private TextField speedField;

    @FXML
    private TextField gasField;

    // Inline red validation labels (not suggest-scene fallbacks) — see FXML comment.
    @FXML
    private Label wireFeedError;

    @FXML
    private Label voltageError;

    @FXML
    private Label nozzleError;

    @FXML
    private Label speedError;

    @FXML
    private Label gasError;

    @FXML
    private Label resultLabel;

    @FXML
    void predict(ActionEvent event) {
        hideErrors();
        resultLabel.setText("");

        Double w = parseAndValidate(wireFeedField, wireFeedError,
                parameterRanges.W_MIN, parameterRanges.W_MAX,
                "Enter a number between 6350 and 10160");
        if (w == null) {
            return;
        }

        Double v = parseAndValidate(voltageField, voltageError,
                parameterRanges.V_MIN, parameterRanges.V_MAX,
                "Enter a number between 16 and 25");
        if (v == null) {
            return;
        }

        Double n = parseAndValidate(nozzleField, nozzleError,
                parameterRanges.N_MIN, parameterRanges.N_MAX,
                "Enter a number between 12 and 18");
        if (n == null) {
            return;
        }

        Double s = parseAndValidate(speedField, speedError,
                parameterRanges.S_MIN, parameterRanges.S_MAX,
                "Enter a number between 406 and 610");
        if (s == null) {
            return;
        }

        Double g = parseAndValidate(gasField, gasError,
                parameterRanges.G_MIN, parameterRanges.G_MAX,
                "Enter a valid gas flow rate (range pending)");
        if (g == null) {
            return;
        }

        double p = model.predictPenetration(w, v, n, s, g);
        resultLabel.setText(String.format("Predicted penetration: %.4f mm", p));
    }

    @FXML
    void back(ActionEvent event) {
        SceneUtil.newScene("landing/landingView.fxml", event);
    }

    private Double parseAndValidate(TextField field, Label errorLabel,
                                    double min, double max, String message) {
        String raw = field.getText() == null ? "" : field.getText().trim();
        if (raw.isEmpty()) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            return null;
        }
        try {
            double value = Double.parseDouble(raw);
            if (value < min || value > max) {
                errorLabel.setText(message);
                errorLabel.setVisible(true);
                return null;
            }
            return value;
        } catch (NumberFormatException ex) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            return null;
        }
    }

    private void hideErrors() {
        wireFeedError.setVisible(false);
        voltageError.setVisible(false);
        nozzleError.setVisible(false);
        speedError.setVisible(false);
        gasError.setVisible(false);
    }

    @FXML
    void initialize() {
        hideErrors();
        resultLabel.setText("");
    }
}

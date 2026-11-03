package com.example.weldingoptimizer.optimize;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.btpoptimizer.config.parameterRanges;
import com.example.btpoptimizer.model.optimizationResult;
import com.example.btpoptimizer.optimizer.searchStrategy;
import com.example.btpoptimizer.optimizer.weldingOptimizer;
import com.example.weldingoptimizer.SceneUtil;
import com.example.weldingoptimizer.resultDataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class optimizeController {

    private final weldingOptimizer optimizer = new weldingOptimizer();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField targetField;

    @FXML
    private TextField toleranceField;

    @FXML
    private TextField maxResultsField;

    // Inline red validation labels (not suggest-scene fallbacks) — see FXML comment.
    @FXML
    private Label targetError;

    @FXML
    private Label toleranceError;

    @FXML
    private Label maxResultsError;

    @FXML
    void findParameters(ActionEvent event) {
        hideErrors();

        Double target = parsePositive(targetField, targetError,
                "Enter a positive target penetration");
        if (target == null) {
            return;
        }

        Double tolerance = parseInRange(toleranceField, toleranceError, 0.0, 100.0,
                "Enter a tolerance between 0 and 100");
        if (tolerance == null) {
            return;
        }

        Integer maxResults = parseMaxResults();
        if (maxResults == null) {
            return;
        }

        try {
            List<optimizationResult> results = optimizer.optimize(
                    target,
                    tolerance,
                    maxResults,
                    searchStrategy.ANALYTIC_SOLVE_N);

            if (results.isEmpty()) {
                resultDataHolder.setEmptyState(
                        "No parameter sets matched the target within range and constraints.\n"
                                + "Try a different target penetration or tolerance.");
            } else {
                resultDataHolder.setResults(results);
            }
            SceneUtil.newScene("results/resultsView.fxml", event);
        } catch (UnsupportedOperationException ex) {
            String detail = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
            String message;
            if (detail.contains("reinforcement")) {
                message = "Backend calculation not yet available — reinforcement height equation pending.";
            } else if (detail.contains("current")) {
                message = "Backend calculation not yet available — welding current equation pending.";
            } else {
                message = "Backend calculation not yet available — " + ex.getMessage();
            }
            resultDataHolder.setEmptyState(message);
            SceneUtil.newScene("results/resultsView.fxml", event);
        }
    }

    @FXML
    void back(ActionEvent event) {
        SceneUtil.newScene("landing/landingView.fxml", event);
    }

    private Integer parseMaxResults() {
        String raw = maxResultsField.getText() == null ? "" : maxResultsField.getText().trim();
        if (raw.isEmpty()) {
            return parameterRanges.DEFAULT_MAX_RESULTS;
        }
        try {
            int value = Integer.parseInt(raw);
            if (value < 1 || value > parameterRanges.MAX_RESULTS_UPPER_BOUND) {
                maxResultsError.setText("Enter 1–50");
                maxResultsError.setVisible(true);
                return null;
            }
            return value;
        } catch (NumberFormatException ex) {
            maxResultsError.setText("Enter 1–50");
            maxResultsError.setVisible(true);
            return null;
        }
    }

    private Double parsePositive(TextField field, Label errorLabel, String message) {
        String raw = field.getText() == null ? "" : field.getText().trim();
        if (raw.isEmpty()) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            return null;
        }
        try {
            double value = Double.parseDouble(raw);
            if (value <= 0) {
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

    private Double parseInRange(TextField field, Label errorLabel,
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
        targetError.setVisible(false);
        toleranceError.setVisible(false);
        maxResultsError.setVisible(false);
    }

    @FXML
    void initialize() {
        hideErrors();
        if (toleranceField.getText() == null || toleranceField.getText().isBlank()) {
            toleranceField.setText(String.valueOf((int) parameterRanges.DEFAULT_TOLERANCE_PERCENT));
        }
        if (maxResultsField.getText() == null || maxResultsField.getText().isBlank()) {
            maxResultsField.setText(String.valueOf(parameterRanges.DEFAULT_MAX_RESULTS));
        }
    }
}

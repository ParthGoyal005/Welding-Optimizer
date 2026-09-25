package com.example.weldingoptimizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Navigation helpers copied from the Expert System (Welding) project —
 * same method signatures so screens can merge into that app later with minimal rework.
 * Scene size follows the loaded FXML root's prefWidth/prefHeight (600x400 by default;
 * results uses a wider canvas).
 */
public class SceneUtil {
    public static void newScene(String directory, ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(directory));
            Parent root = fxmlLoader.load();
            double width = root.prefWidth(-1);
            double height = root.prefHeight(-1);
            if (width <= 0) {
                width = 600;
            }
            if (height <= 0) {
                height = 400;
            }
            javafx.scene.Scene scene = new javafx.scene.Scene(root, width, height);
            stage.setTitle("Weld Penetration Optimizer");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    public static void newScene2(String directory, MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(directory));
            Parent root = fxmlLoader.load();
            double width = root.prefWidth(-1);
            double height = root.prefHeight(-1);
            if (width <= 0) {
                width = 600;
            }
            if (height <= 0) {
                height = 400;
            }
            javafx.scene.Scene scene = new javafx.scene.Scene(root, width, height);
            stage.setTitle("Weld Penetration Optimizer");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    public static void closeWindow(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();   // closes the current window
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }
}

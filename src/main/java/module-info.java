module com.example.weldingoptimizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports com.example.btpoptimizer.config;
    exports com.example.btpoptimizer.model;
    exports com.example.btpoptimizer.optimizer;

    opens com.example.weldingoptimizer to javafx.fxml;
    exports com.example.weldingoptimizer;

    opens com.example.weldingoptimizer.landing to javafx.fxml;
    exports com.example.weldingoptimizer.landing;

    opens com.example.weldingoptimizer.predict to javafx.fxml;
    exports com.example.weldingoptimizer.predict;

    opens com.example.weldingoptimizer.optimize to javafx.fxml;
    exports com.example.weldingoptimizer.optimize;

    opens com.example.weldingoptimizer.results to javafx.fxml;
    exports com.example.weldingoptimizer.results;
}

package com.example.btpoptimizer.model;

/**
 * One ranked optimizer hit: parameters plus all derived metrics so callers
 * (e.g. a future JavaFX UI) need not recompute anything.
 */
public final class optimizationResult {

    private final weldingParameters parameters;
    private final double predictedPenetration;
    private final double deviationFromTarget;
    private final double reinforcementHeight;
    private final double current;
    private final double heatInput;

    public optimizationResult(weldingParameters parameters,
                              double predictedPenetration,
                              double deviationFromTarget,
                              double reinforcementHeight,
                              double current,
                              double heatInput) {
        this.parameters = parameters;
        this.predictedPenetration = predictedPenetration;
        this.deviationFromTarget = deviationFromTarget;
        this.reinforcementHeight = reinforcementHeight;
        this.current = current;
        this.heatInput = heatInput;
    }

    public weldingParameters getParameters() {
        return parameters;
    }

    public double getPredictedPenetration() {
        return predictedPenetration;
    }

    public double getDeviationFromTarget() {
        return deviationFromTarget;
    }

    public double getReinforcementHeight() {
        return reinforcementHeight;
    }

    public double getCurrent() {
        return current;
    }

    public double getHeatInput() {
        return heatInput;
    }
}

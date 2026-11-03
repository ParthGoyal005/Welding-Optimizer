package com.example.btpoptimizer.model;

/**
 * Immutable GMAW process parameter set (W, V, N, S, G).
 */
public final class weldingParameters {

    private final double wireFeedRate;
    private final double arcVoltage;
    private final double nozzleDistance;
    private final double weldingSpeed;
    private final double gasFlowRate;

    public weldingParameters(double wireFeedRate,
                             double arcVoltage,
                             double nozzleDistance,
                             double weldingSpeed,
                             double gasFlowRate) {
        this.wireFeedRate = wireFeedRate;
        this.arcVoltage = arcVoltage;
        this.nozzleDistance = nozzleDistance;
        this.weldingSpeed = weldingSpeed;
        this.gasFlowRate = gasFlowRate;
    }

    public double getWireFeedRate() {
        return wireFeedRate;
    }

    public double getArcVoltage() {
        return arcVoltage;
    }

    public double getNozzleDistance() {
        return nozzleDistance;
    }

    public double getWeldingSpeed() {
        return weldingSpeed;
    }

    public double getGasFlowRate() {
        return gasFlowRate;
    }
}

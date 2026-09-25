package com.example.btpoptimizer.model;

/**
 * Pure calculation model for GMAW Al 5083 penetration and related quantities.
 * No I/O — safe to call from a future JavaFX controller.
 */
public final class weldingModel {

    public weldingModel() {
    }

    /**
     * Governing regression equation (hardcoded exactly as derived):
     * P = 3.69 + 0.91*W − 0.14*V + 0.14*N − 0.31*S − 0.06*G + 0.11*(W*N) − 0.14*(V*N)
     *
     * @return predicted weld penetration P (mm)
     */
    public double predictPenetration(double w, double v, double n, double s, double g) {
        return 3.69
                + 0.91 * w
                - 0.14 * v
                + 0.14 * n
                - 0.31 * s
                - 0.06 * g
                + 0.11 * (w * n)
                - 0.14 * (v * n);
    }

    public double predictPenetration(weldingParameters p) {
        return predictPenetration(
                p.getWireFeedRate(),
                p.getArcVoltage(),
                p.getNozzleDistance(),
                p.getWeldingSpeed(),
                p.getGasFlowRate());
    }

    /**
     * Reinforcement height h (mm) as a function of process parameters.
     *
     * TODO: replace this placeholder with the real reinforcement-height equation
     * when the project owner provides it. Do not invent coefficients.
     */
    public double computeReinforcementHeight(double w, double v, double n, double s, double g) {
        throw new UnsupportedOperationException(
                "reinforcement height equation not yet provided");
    }

    public double computeReinforcementHeight(weldingParameters p) {
        return computeReinforcementHeight(
                p.getWireFeedRate(),
                p.getArcVoltage(),
                p.getNozzleDistance(),
                p.getWeldingSpeed(),
                p.getGasFlowRate());
    }

    /**
     * Welding current I (A) as a function of process parameters.
     *
     * TODO: replace this placeholder with the real current-from-parameters
     * relationship when the project owner provides it. Do not invent coefficients.
     */
    public double computeCurrent(double w, double v, double n, double s, double g) {
        throw new UnsupportedOperationException(
                "welding current equation not yet provided");
    }

    public double computeCurrent(weldingParameters p) {
        return computeCurrent(
                p.getWireFeedRate(),
                p.getArcVoltage(),
                p.getNozzleDistance(),
                p.getWeldingSpeed(),
                p.getGasFlowRate());
    }

    /**
     * Heat input (kJ/mm): HeatInput = (V * I * 60) / (S * 1000),
     * where I comes from {@link #computeCurrent(double, double, double, double, double)}.
     */
    public double computeHeatInput(double w, double v, double n, double s, double g) {
        double current = computeCurrent(w, v, n, s, g);
        return (v * current * 60.0) / (s * 1000.0);
    }

    public double computeHeatInput(weldingParameters p) {
        return computeHeatInput(
                p.getWireFeedRate(),
                p.getArcVoltage(),
                p.getNozzleDistance(),
                p.getWeldingSpeed(),
                p.getGasFlowRate());
    }
}

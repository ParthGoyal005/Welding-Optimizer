package com.example.btpoptimizer.model;

import com.example.btpoptimizer.config.parameterRanges;

/**
 * Pure calculation model for GMAW Al 5083 penetration, reinforcement height,
 * welding current, and heat input.
 * No I/O — safe to call from JavaFX controllers.
 */
public final class weldingModel {

    public weldingModel() {
    }

    /**
     * Governing regression equation for penetration:
     * P = 0.576144
     *     + 0.000188976*W
     *     + 0.124444*V
     *     + 0.100370*N
     *     - 0.00303922*S
     *     - 0.0133333*G
     *     + 0.0000192476*(W*N)
     *     - 0.0103704*(V*N)
     *
     * @param w wire feed rate (mm/min)
     * @param v arc voltage (V)
     * @param n nozzle distance (mm)
     * @param s welding speed (mm/min)
     * @param g gas flow rate (L/min)
     * @return predicted weld penetration P (mm)
     */
    public double predictPenetration(double w, double v, double n, double s, double g) {
        return 0.576144
                + 0.000188976 * w
                + 0.124444 * v
                + 0.100370 * n
                - 0.00303922 * s
                - 0.0133333 * g
                + 0.0000192476 * (w * n)
                - 0.0103704 * (v * n);
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
     * Reinforcement height H (mm) as a function of process parameters:
     * H = 8.1234 - 0.037187*S - 0.14219*V + 0.15625*W - 0.00469*G + 0.01875*N - 0.01*theta
     *
     * In the empirical regression model, W is in m/min and S is in cm/min.
     * When parameters are passed in mm/min (standard project units), they are converted.
     */
    public double computeReinforcementHeight(double w, double v, double n, double s, double g) {
        return computeReinforcementHeight(w, v, n, s, g, parameterRanges.DEFAULT_TORCH_ANGLE_DEG);
    }

    public double computeReinforcementHeight(double w, double v, double n, double s, double g, double theta) {
        double w_m_min = (w > 100.0) ? (w / 1000.0) : w;
        double s_cm_min = (s > 100.0) ? (s / 10.0) : s;
        return 8.1234
                - 0.037187 * s_cm_min
                - 0.14219 * v
                + 0.15625 * w_m_min
                - 0.00469 * g
                + 0.01875 * n
                - 0.01 * theta;
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
     * Welding current I (A) as a function of wire feed speed:
     * I = 18.59*W - 10.47
     *
     * In the linear fit, W is in m/min.
     * When W is passed in mm/min (standard project units, 6350 - 10160), it is converted to m/min.
     */
    public double computeCurrent(double w, double v, double n, double s, double g) {
        return computeCurrent(w);
    }

    public double computeCurrent(double w) {
        double w_m_min = (w > 100.0) ? (w / 1000.0) : w;
        return 18.59 * w_m_min - 10.47;
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

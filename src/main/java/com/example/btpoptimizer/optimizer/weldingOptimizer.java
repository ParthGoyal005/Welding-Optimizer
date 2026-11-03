package com.example.btpoptimizer.optimizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.btpoptimizer.config.parameterRanges;
import com.example.btpoptimizer.model.optimizationResult;
import com.example.btpoptimizer.model.weldingModel;
import com.example.btpoptimizer.model.weldingParameters;

/**
 * Search → reinforcement-height filter → rank by heat input → cap top-N.
 */
public final class weldingOptimizer {

    private final weldingModel model;

    public weldingOptimizer() {
        this(new weldingModel());
    }

    public weldingOptimizer(weldingModel model) {
        this.model = model;
    }

    public List<optimizationResult> optimize(double targetPenetration, double tolerancePercent) {
        return optimize(
                targetPenetration,
                tolerancePercent,
                parameterRanges.DEFAULT_MAX_RESULTS,
                searchStrategy.ANALYTIC_SOLVE_N);
    }

    public List<optimizationResult> optimize(double targetPenetration,
                                             double tolerancePercent,
                                             int maxResults,
                                             searchStrategy strategy) {
        int cappedMax = clampMaxResults(maxResults);
        List<weldingParameters> candidates = findMatchingCandidates(
                targetPenetration, tolerancePercent, strategy);

        List<optimizationResult> survivors = new ArrayList<>();
        for (weldingParameters params : candidates) {
            double height = model.computeReinforcementHeight(params);
            if (height > parameterRanges.MAX_REINFORCEMENT_HEIGHT_MM) {
                continue;
            }

            double predicted = model.predictPenetration(params);
            double current = model.computeCurrent(params);
            double heatInput = model.computeHeatInput(params);

            survivors.add(new optimizationResult(
                    params,
                    predicted,
                    predicted - targetPenetration,
                    height,
                    current,
                    heatInput));
        }

        survivors.sort(Comparator.comparingDouble(optimizationResult::getHeatInput));

        if (survivors.size() > cappedMax) {
            return new ArrayList<>(survivors.subList(0, cappedMax));
        }
        return survivors;
    }

    /**
     * Step 1 only: combinations that satisfy the governing equation / tolerance,
     * before reinforcement-height filtering and heat-input ranking.
     */
    public List<weldingParameters> findMatchingCandidates(double targetPenetration,
                                                          double tolerancePercent,
                                                          searchStrategy strategy) {
        if (strategy == searchStrategy.GRID_TOLERANCE) {
            return generateGridToleranceCandidates(targetPenetration, tolerancePercent);
        }
        return generateAnalyticCandidates(targetPenetration);
    }

    /**
     * Analytic solve for N (only variable in both interaction terms):
     * N = (target − 3.69 − 0.91*W + 0.14*V + 0.31*S + 0.06*G)
     *     / (0.14 + 0.11*W − 0.14*V)
     */
    private List<weldingParameters> generateAnalyticCandidates(double target) {
        List<weldingParameters> out = new ArrayList<>();

        double[] wValues = linspace(parameterRanges.W_MIN, parameterRanges.W_MAX, parameterRanges.W_STEPS);
        double[] vValues = linspace(parameterRanges.V_MIN, parameterRanges.V_MAX, parameterRanges.V_STEPS);
        double[] sValues = linspace(parameterRanges.S_MIN, parameterRanges.S_MAX, parameterRanges.S_STEPS);
        double[] gValues = linspace(parameterRanges.G_MIN, parameterRanges.G_MAX, parameterRanges.G_STEPS);

        for (double w : wValues) {
            for (double v : vValues) {
                for (double s : sValues) {
                    for (double g : gValues) {
                        double denominator = 0.14 + 0.11 * w - 0.14 * v;
                        if (Math.abs(denominator) < parameterRanges.DENOMINATOR_EPSILON) {
                            continue;
                        }

                        double numerator = target - 3.69 - 0.91 * w + 0.14 * v + 0.31 * s + 0.06 * g;
                        double n = numerator / denominator;

                        if (n < parameterRanges.N_MIN || n > parameterRanges.N_MAX) {
                            continue;
                        }

                        out.add(new weldingParameters(w, v, n, s, g));
                    }
                }
            }
        }
        return out;
    }

    private List<weldingParameters> generateGridToleranceCandidates(double target,
                                                                    double tolerancePercent) {
        List<weldingParameters> out = new ArrayList<>();
        double lower = target * (1.0 - tolerancePercent / 100.0);
        double upper = target * (1.0 + tolerancePercent / 100.0);

        double[] wValues = linspace(parameterRanges.W_MIN, parameterRanges.W_MAX, parameterRanges.W_STEPS);
        double[] vValues = linspace(parameterRanges.V_MIN, parameterRanges.V_MAX, parameterRanges.V_STEPS);
        double[] nValues = linspace(parameterRanges.N_MIN, parameterRanges.N_MAX, parameterRanges.N_STEPS);
        double[] sValues = linspace(parameterRanges.S_MIN, parameterRanges.S_MAX, parameterRanges.S_STEPS);
        double[] gValues = linspace(parameterRanges.G_MIN, parameterRanges.G_MAX, parameterRanges.G_STEPS);

        for (double w : wValues) {
            for (double v : vValues) {
                for (double n : nValues) {
                    for (double s : sValues) {
                        for (double g : gValues) {
                            double p = model.predictPenetration(w, v, n, s, g);
                            if (p >= lower && p <= upper) {
                                out.add(new weldingParameters(w, v, n, s, g));
                            }
                        }
                    }
                }
            }
        }
        return out;
    }

    private static int clampMaxResults(int maxResults) {
        if (maxResults < 1) {
            return 1;
        }
        return Math.min(maxResults, parameterRanges.MAX_RESULTS_UPPER_BOUND);
    }

    /** Inclusive linspace with {@code steps} points (at least 2). */
    static double[] linspace(double min, double max, int steps) {
        int n = Math.max(2, steps);
        double[] values = new double[n];
        if (n == 1) {
            values[0] = min;
            return values;
        }
        double step = (max - min) / (n - 1);
        for (int i = 0; i < n; i++) {
            values[i] = min + step * i;
        }
        return values;
    }
}

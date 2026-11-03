package com.example.btpoptimizer.optimizer;

/**
 * How candidate parameter sets that match the target penetration are generated.
 */
public enum searchStrategy {
    /** Solve analytically for N at each W,V,S,G grid point (preferred / default). */
    ANALYTIC_SOLVE_N,
    /** Brute-force 5-variable grid; keep points whose P is within tolerance. */
    GRID_TOLERANCE
}

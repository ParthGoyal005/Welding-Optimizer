package com.example.btpoptimizer.config;

/**
 * Single place for operating ranges, plate constraint, and optimizer defaults.
 * Plain Java constants — no config framework.
 */
public final class parameterRanges {

    private parameterRanges() {
    }

    // --- Wire feed rate W (mm/min) ---
    public static final double W_MIN = 6350.0;
    public static final double W_MAX = 10160.0;

    // --- Arc voltage V (V) ---
    public static final double V_MIN = 16.0;
    public static final double V_MAX = 25.0;

    // --- Nozzle / contact tip-to-plate distance N (mm) ---
    public static final double N_MIN = 12.0;
    public static final double N_MAX = 18.0;

    // --- Welding speed S (mm/min) ---
    public static final double S_MIN = 406.0;
    public static final double S_MAX = 610.0;

    // --- Shielding gas flow rate G (L/min) ---
    public static final double G_MIN = 7.0;
    public static final double G_MAX = 26.0;

    // --- Torch angle theta (degrees) for reinforcement height ---
    public static final double DEFAULT_TORCH_ANGLE_DEG = 70.0;

    // --- Plate / reinforcement hard constraint ---
    public static final double PLATE_THICKNESS_MM = 12.0;
    public static final double MAX_REINFORCEMENT_HEIGHT_RATIO = 0.50;
    /** h must be ≤ this value (mm). */
    public static final double MAX_REINFORCEMENT_HEIGHT_MM =
            PLATE_THICKNESS_MM * MAX_REINFORCEMENT_HEIGHT_RATIO; // 6.0 mm

    // --- Grid step counts for search (modest for a small project) ---
    public static final int W_STEPS = 12;
    public static final int V_STEPS = 10;
    public static final int N_STEPS = 10;
    public static final int S_STEPS = 12;
    public static final int G_STEPS = 10;

    // --- Optimizer defaults ---
    public static final double DEFAULT_TOLERANCE_PERCENT = 5.0;
    public static final int DEFAULT_MAX_RESULTS = 10;
    public static final int MAX_RESULTS_UPPER_BOUND = 50;

    /** Skip analytic N solve when |denominator| is below this. */
    public static final double DENOMINATOR_EPSILON = 1e-9;
}

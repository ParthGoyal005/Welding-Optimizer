package com.example.weldingoptimizer;

import java.util.Collections;
import java.util.List;

import com.example.btpoptimizer.model.optimizationResult;

/**
 * Tiny static hand-off between optimize → results screens.
 * Small app; no DI / navigation framework needed.
 */
public final class resultDataHolder {

    private static List<optimizationResult> results = Collections.emptyList();
    private static String statusMessage;
    private static boolean showEmptyState;

    private resultDataHolder() {
    }

    public static void setResults(List<optimizationResult> next) {
        results = next == null ? Collections.emptyList() : List.copyOf(next);
        showEmptyState = false;
        statusMessage = null;
    }

    public static void setEmptyState(String message) {
        results = Collections.emptyList();
        showEmptyState = true;
        statusMessage = message;
    }

    public static List<optimizationResult> getResults() {
        return results;
    }

    public static boolean isShowEmptyState() {
        return showEmptyState;
    }

    public static String getStatusMessage() {
        return statusMessage;
    }
}

# GMAW Al 5083 Penetration Optimizer (`btp_optimizer`)

JavaFX frontend + plain-Java backend for reverse-engineering GMAW process parameters that achieve a target weld penetration on Aluminium 5083, then ranking feasible sets by minimum heat input (subject to a reinforcement-height hard constraint).

Styled to match the existing Expert System for Welding (`Welding.zip`) — Scene Builder `AnchorPane` layouts, `#0468BF` primary buttons, inline red validation — so screens can be folded into that project later with minimal rework. Not merged yet.

No Spring, no REST, no JUnit.

## Build & run

Requires **JDK 24**. Maven Wrapper is included. Set `JAVA_HOME` to your JDK.

```bash
# Windows
$env:JAVA_HOME = "C:\Program Files\Java\jdk-24"
.\mvnw.cmd clean javafx:run

# macOS / Linux
./mvnw clean javafx:run
```

## Screens

| Screen | Size | Package |
|---|---|---|
| Landing | 600×400 | `com.example.weldingoptimizer.landing` |
| Predict Penetration | 600×400 | `com.example.weldingoptimizer.predict` |
| Find Optimal Parameters | 600×400 | `com.example.weldingoptimizer.optimize` |
| Results (table) | 900×500 | `com.example.weldingoptimizer.results` |

Navigation uses `SceneUtil` (same signatures as the Expert System). Controllers call `weldingModel` / `weldingOptimizer` directly.

## Governing equation

Hardcoded exactly (do not re-derive):

```
P = 3.69 + 0.91*W − 0.14*V + 0.14*N − 0.31*S − 0.06*G + 0.11*(W*N) − 0.14*(V*N)
```

| Symbol | Meaning | Unit |
|---|---|---|
| P | Weld penetration (output) | mm |
| W | Wire feed rate | mm/min |
| V | Arc voltage | V |
| N | Nozzle (contact tip-to-plate) distance | mm |
| S | Welding speed | mm/min |
| G | Shielding gas flow rate | (unit TBD) |

## Valid operating ranges

| Parameter | Min | Max |
|---|---|---|
| W (Wire Feed Rate) | 6350 | 10160 |
| V (Arc Voltage) | 16 | 25 |
| N (Nozzle Distance) | 12 | 18 |
| S (Welding Speed) | 406 | 610 |
| G (Gas Flow Rate) | **15** (placeholder) | **25** (placeholder) |

All ranges live in `com.example.btpoptimizer.config.parameterRanges`.

## Optimization pipeline

1. Generate candidates that hit the target penetration (analytic N solve, or grid + tolerance).
2. Discard any candidate with reinforcement height `h > 30%` of plate thickness (12 mm → `h ≤ 3.6 mm`).
3. Rank survivors by heat input ascending: `HeatInput = (V * I * 60) / (S * 1000)`.
4. Return up to `maxResults` (default 10, hard cap 50).

## Package layout

| Package | Role |
|---|---|
| `com.example.btpoptimizer.config` | Ranges and defaults (`parameterRanges`) |
| `com.example.btpoptimizer.model` | Pure calc (`weldingModel`, parameters, results) |
| `com.example.btpoptimizer.optimizer` | Search / filter / rank (`weldingOptimizer`) |
| `com.example.weldingoptimizer` | `HelloApplication`, `SceneUtil`, result hand-off |
| `com.example.weldingoptimizer.*` | Per-screen controllers + FXML |

## Known gaps

1. Gas Flow Rate's real operating range (currently placeholder).
2. The reinforcement-height equation (not yet implemented — throws on call).
3. The current-from-parameters relationship needed for heat input (not yet implemented — throws on call).

Until (2) and (3) are filled in, Optimize → Results shows a suggest-style empty state (e.g. “Backend calculation not yet available — reinforcement height equation pending.”) instead of crashing. Drop the real formulas into `weldingModel.computeReinforcementHeight(...)` and `weldingModel.computeCurrent(...)` — nothing else needs to change.

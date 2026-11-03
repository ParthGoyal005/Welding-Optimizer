# GMAW Al 5083 Penetration Optimizer (`btp_optimizer`)

JavaFX frontend + plain-Java backend for reverse-engineering GMAW process parameters that achieve a target weld penetration on Aluminium 5083, then ranking feasible sets by minimum heat input (subject to a reinforcement-height hard constraint).

Styled to match the existing Expert System for Welding (`Welding.zip`) — Scene Builder `AnchorPane` layouts, `#0468BF` primary buttons, inline red validation — so screens can be folded into that project later with minimal rework.

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

## Governing equations

### 1. Penetration ($P$)
```
P = 0.576144 + 0.000188976*W + 0.124444*V + 0.100370*N - 0.00303922*S - 0.0133333*G + 0.0000192476*(W*N) - 0.0103704*(V*N)
```

### 2. Reinforcement Height ($H$)
```
H = 8.1234 - 0.037187*S - 0.14219*V + 0.15625*W - 0.00469*G + 0.01875*N - 0.01*theta
```
*(In empirical regression units: $W$ in m/min, $S$ in cm/min, $\theta$ torch angle in degrees; converted internally from project units mm/min).*

### 3. Welding Current ($I$)
```
I = 18.59*W - 10.47
```
*(In empirical regression units: $W$ in m/min; converted internally from project units mm/min).*

### 4. Heat Input
```
HeatInput = (V * I * 60) / (S * 1000)   [kJ/mm]
```

## Process parameters

| Symbol | Meaning | Unit | Min | Max |
|---|---|---|---|---|
| W | Wire feed rate | mm/min | 6350 | 10160 |
| V | Arc voltage | V | 16 | 25 |
| N | Nozzle (contact tip-to-plate) distance | mm | 12 | 18 |
| S | Welding speed | mm/min | 406 | 610 |
| G | Shielding gas flow rate | L/min | 7 | 26 |

All ranges live in `com.example.btpoptimizer.config.parameterRanges`.

## Optimization pipeline

1. Generate candidates that hit the target penetration within tolerance (analytic N solve across tolerance window, or grid search).
2. Discard any candidate with reinforcement height `h > 50%` of plate thickness (12 mm → `h ≤ 6.0 mm`).
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

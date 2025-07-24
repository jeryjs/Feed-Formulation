# Feed Formulation
This is a technically-driven project focused on solving the complex problem of feed formulation, for livestock or animal husbandry. With a strong foundation in Kotlin (88%) and Python (12%), the codebase blends modern Android/mobile application capabilities with data-driven problem-solving logic.
> **Note:** The primary development of this project happens on other branches. The `main` branch is not actively updated.

## Overview

Feed Formulation is a Kotlin-based Android application designed to address the complex problem of animal feed optimization. It leverages advanced linear programming techniques (specifically the Simplex method) to help nutritionists, researchers, and farm managers formulate cost-effective, nutritionally balanced rations for livestock.

This project was made in collaboration with ICAR-NIANP, and provides a user-friendly interface enabling practical application in the field. The codebase is structured to support extensibility, robust data handling, and precise calculations, making it a valuable tool for both applied and research contexts.

## Key Features

- **Linear Programming Solver:** Implements a custom *Dual-Simplex* algorithm to optimize feed mixtures based on user-specified nutritional requirements (Dry Matter, Crude Protein, Total Digestible Nutrients).
- **Customizable Feed Inputs:** Users can add, edit, import, and export feed ingredient profiles using JSON.
- **Dynamic Nutrient Calculation:** Nutrient needs are calculated from livestock parameters such as body weight, milk yield, milk fat, and physiological state.
- **User-Friendly Interface:** Intuitive fragments for feed management, nutrient selection, and organizational information.
- **Extensible Architecture:** ViewModel-driven design for clear separation of UI and business logic, supporting maintainability and testing.

## Technical Architecture

- **Core Logic:**  
  - `Simplex.kt` encapsulates the custom Dual-Simplex algorithm, translating the feed-mixing optimization problem into a solvable set of equations, and outputs ingredient proportions that satisfy all constraints.
  - `Nutrients.kt` computes nutritional requirements from animal parameters, using domain-specific formulas.
  - `FeedsViewModel.kt` and related UI fragments manage the lifecycle, persistence, and display of feed data, supporting import/export and reset operations.
- **Data Flow:**  
  - User inputs are captured in ViewModels, processed for nutrient requirements, and then passed into the solver.
  - Results are rendered in real-time, providing actionable rations while enforcing constraints and nutritional targets.

## Problem Solving Approach

This project tackles a real-world, NP-hard optimization problem: how to blend available feedstuffs to meet complex dietary requirements at minimum cost or with available resources. By embedding a full-featured Simplex solver, the app efficiently navigates the solution space, ensuring practical, scientifically sound recommendations.

This approach demonstrates both algorithmic rigor and practical engineering. The codebase is clear, modular, and ready for adaptation to other domains requiring constraint-based optimization.

## Preview

![image5](https://github.com/user-attachments/assets/d35858e9-9d15-4e89-bb2d-4e20cef6b957)
![image4](https://github.com/user-attachments/assets/bc2d9ae9-cd3c-4ce6-8647-c40deeae5282)
![image3](https://github.com/user-attachments/assets/7cf7531d-e500-4b3a-be99-f63eda00f339)
![image2](https://github.com/user-attachments/assets/798eb235-210a-4c54-a669-2c401cffac99)
![image1](https://github.com/user-attachments/assets/262414ef-3c17-46cb-abf6-b68d5f3068ff)

## Getting Started

*Note: The main branch does not reflect the latest changes. See other branches for the most recent code.*

### Prerequisites

- Android Studio (latest stable)
- Kotlin 1.8+
- Minimum SDK as specified in the project

### Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/jeryjs/Feed-Formulation.git
   ```
2. Switch to the most active development branch:
   ```bash
   git checkout <branch-name>
   ```
3. Open in Android Studio and run on an emulator or device.

---

![icar nianp footer](https://github.com/jeryjs/Feed_Chart/blob/main/app/src/main/res/drawable/icar_nianp_header.png)

# Sudoku

A native Android Sudoku game built in Java, featuring a from-scratch puzzle generator, a backtracking solver, and a short interactive tutorial that teaches the rules before the player starts.

![Language](https://img.shields.io/badge/Language-Java-F05138?logo=java&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Min%20SDK](https://img.shields.io/badge/Min%20SDK-24-blue)

---

## Overview

This project implements a complete Sudoku puzzle game without any third-party game or UI libraries: the board is generated and validated with a constraint-checking backtracking algorithm written from scratch, and the 9x9 grid is built and rendered programmatically rather than laid out statically in XML.

<p align="center">
  <img width="230" alt="Main menu" src="https://github.com/user-attachments/assets/4638794f-134a-4dbf-8361-3afcf0586b0f" />
  <img width="230" alt="Gameplay" src="https://github.com/user-attachments/assets/e959fa62-e268-434d-85a5-c283e6a04710" />
  <img width="230" alt="Win screen" src="https://github.com/user-attachments/assets/6635497b-2f42-4631-b8e3-52d7c8fa7538" />
</p>

---

## Core Features

- **Procedural puzzle generation** — fills the diagonal 3x3 boxes randomly, solves the remaining board with backtracking, then removes cells to reach the target difficulty.
- **Three difficulty levels** — Easy, Medium, and Hard, mapped to a fixed number of empty cells (20 / 35 / 50) rather than a fixed visual layout.
- **Backtracking solver** — a recursive constraint solver checks row, column, and 3x3-box validity at each step; exposed in-app as a one-tap "Solve" action.
- **Live validation** — checking a solution re-runs the same row/column/box constraint checks against the player's current entries, independent of the generator.
- **Dynamically built grid** — the 9x9 board is constructed at runtime based on screen density and width, rather than a fixed XML grid.
- **Guided onboarding** — a two-screen animated tutorial (`LearnActivity`, `LearnActivity2`) demonstrates the row/column/box constraints before handing off to gameplay.

---

## Architecture

| Component | Responsibility |
|---|---|
| `MainActivity` | Entry point; routes into the tutorial flow |
| `LearnActivity` / `LearnActivity2` | Animated, non-interactive walkthrough of Sudoku's row, column, and box rules |
| `GameActivity` | Game state, board generation, input handling, validation, and the solver |

Game state (`board`, `isFixed`) and rendering (`cellViews`) are kept as parallel structures inside `GameActivity`, with the grid view rebuilt whenever a new puzzle is generated.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | Android Views, XML layouts (`ConstraintLayout`, `GridLayout`) |
| Build | Gradle (Kotlin DSL) |
| IDE | Android Studio |

No third-party dependencies beyond AndroidX (`appcompat`, `material`, `constraintlayout`).

---

## Requirements

- Android Studio (current stable release)
- Android SDK 24+ (Android 7.0)
- Java 11

---

## Running the Project

```bash
git clone https://github.com/ekampagiouklis/SudokuForAndroidOnJava.git
```

1. Open the cloned folder in Android Studio.
2. Let Gradle sync.
3. Run on a device or emulator (Shift+F10).

---

## Author

**Evangelos Kampagiouklis**
[GitHub](https://github.com/ekampagiouklis) · [LinkedIn](https://www.linkedin.com/in/evangelos-kampagiouklis-27676b247/)

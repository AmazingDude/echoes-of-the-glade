# Echoes of the Glade

Echoes of the Glade is a 2D wave defense game inspired by Brotato, developed as a second-semester project using only Java, Swing, and AWT. The project showcases advanced game programming fundamentals, including a machine-independent game loop, image-based level design, and modular architecture, all without third-party libraries or frameworks.

## Features

- **Wave Defense Gameplay:** Survive against waves of enemies, with each wave increasing in difficulty and enemy count.
- **Machine-Independent Game Loop:** The main game loop ensures consistent gameplay and timing, regardless of machine speed or frame rate.
- **Image-Based Level Design:** Level layouts are loaded from image files, where each pixel’s red channel determines the tile type to place, making level editing intuitive and visual.
- **Modular Structure:** The code separates concerns into modules like game state management (`Menu`, `Playing`), rendering (`GamePanel`, `GameWindow`), entities (`Player`, `Slime`, etc.), and utilities.
- **Custom Audio Support:** Includes an internal audio player for music and sound effects, enhancing the overall immersion experience.
- **Custom Font Support:** The game loads and uses custom fonts for an improved UI experience.
- **Menu System:** A graphical menu with interactive buttons, supporting mouse and keyboard input.
- **Player and Enemy Management:** Robust systems for handling player health, enemy spawning, hitboxes, and wave progression.

## Controls

**In-Game:**
- `W` — Move up
- `A` — Move left
- `S` — Move down
- `D` — Move right
- `J` — Attack
- `ESC` — Pause and return to Main Menu (or, on Game Over, return to Main Menu)

**Menu:**
- Mouse — Navigate and select menu options
- `ENTER` — Start the game

## How It Works

- **Game Loop:** The `Game` class manages the main loop, updates, and rendering. The loop separates updates per second (UPS) and frames per second (FPS) for smooth, consistent gameplay.
- **Level Loading:** The `LevelHandler` and `Level` classes read image files to interpret level layouts, with red pixel values mapping to specific tile types.
- **Waves:** The `WaveManager` controls the timing and escalation of enemy waves, with a timer and dynamic enemy count per wave.
- **Player Mechanics:** The player class manages movement, attacking, health, and animations, with logic for handling death and respawn.
- **Menus and States:** Multiple game states (menu, playing, quit) are managed centrally, with transitions handled by UI buttons and input events.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher

### Running the Game

1. Clone the repository:
    ```sh
    git clone https://github.com/AmazingDude/echoes-of-the-glade.git
    ```
2. Navigate to the project directory and compile:
    ```sh
    cd echoes-of-the-glade
    javac -d bin game-module/src/**/*.java
    ```
3. Run the game:
    ```sh
    java -cp bin main.MainClass
    ```

## Project Structure

```
game-module/
  └── src/
      ├── main/         # Main game loop, window, panel, and entry point
      ├── entities/     # Player, enemies, and entity management
      ├── gamestates/   # Menu, Playing, and game state logic
      ├── levels/       # Level loading and handling
      ├── utils/        # Utilities (helper methods, loading, etc.)
      └── audio/        # Audio management (music and effects)
res/                    # Resources: images, level files, audio, fonts
bin/                    # Compiled .class files
README.md               # Project documentation
```

## Inspiration

This game is inspired by Brotato, incorporating similar wave-based survival mechanics and fast-paced gameplay, while exploring classic Java desktop game development.

## Contributing

This project is for educational purposes and not actively maintained for external contributions. Forks and modifications are welcome for learning and experimentation.

## License

[MIT License](LICENSE)

## Credits

Developed by Rehan Haider as a second-semester project.

---

*Echoes of the Glade* — Survive the waves and defend the glade!

# 🐍 Snake A* Pathfinding Algorithm

An intelligent Snake AI implementation that uses the **A\*** (A-star) pathfinding algorithm to navigate a game environment and reach the apple efficiently while avoiding dynamic threats and static obstacles.

This project was developed using a precompiled Java-based game engine provided by the **University of the Witwatersrand** as part of a coursework challenge.

---

## 🎯 Objective

To design a Snake AI agent that:
- Finds the shortest and safest path from the snake’s head to the apple
- Avoids **obstacles** like:
  - Other snakes (3 opponents)
  - Zombie snakes (which pursue the nearest snake)
  - Maze walls / borders (game boundaries)
- Prevents **head-to-head collisions**
- Escapes **zombie snakes** intelligently

---

## 🧠 Key Concepts

- **A\* Search Algorithm**: Used to calculate the optimal path from the snake’s head to the apple
- **Heuristics**: Modified to prioritize both distance and threat avoidance
- **Dynamic Threat Handling**: Accounts for real-time changes (e.g., zombie snake movement and collisions)
- **Collision Avoidance**: Logic for avoiding walls, enemy snakes, and strategic sacrifices

---

## 🛠 Technologies Used

- **Java**
- **JAR-based Game Engine** (precompiled and provided by the University of the Witwatersrand)
- Basic Java graphics and state handling




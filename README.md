# Repository: SubGame
## Description
This repository contains the implementation of a console-based Battleship game in Java. The game allows a player to play against the computer by taking turns to attack each other's ships on a 10x10 grid.

## Features
- Interactive console-based gameplay.
- Player vs Computer mode.
- Different levels of difficulty.
- Randomized ship placement for the computer.
- Input validation for player moves and ship placement.

## Usage
1. Run the game

    java SubGame
    
2. Follow the on-screen instructions to play the game.

## Game Rules
1. Starting the Game:
  - Enter a seed for the random number generator.
  - Choose the game difficulty level (1 to 7).

2. Ship Placement:
  - The player places their ships by entering the starting coordinates and direction.
  - The computer places its ships randomly.

3. Taking Turns:
  - The player and the computer take turns to attack.
  - Enter coordinates for your attack.
  - If a ship is hit, it will be marked on the board.
  - The game continues until all ships of either the player or the computer are destroyed.
  - 
### Example Gameplay

    Welcome To The Great Battleship Console Game!
    Enter seed:
    12345
    Please enter level:
    3
    Enter location for Battleship of size 2:
    2 3 0 1
    ...
    Congratulations! You are the winner of this great battle!

# ~~~~~ Have fun, and GOOD LUCK! ~~~~~

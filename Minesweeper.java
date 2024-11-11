import java.util.ArrayList;
import java.util.Random;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import tester.Tester;

//represents a cell in the MineSweeper game 

class Cell {
  boolean hasMine;
  boolean isRevealed;
  boolean hasFlag;
  int neighboringMines;
  ArrayList<Cell> neighbors;

  // Constructor
  public Cell(boolean hasMine) {
    this.hasMine = hasMine;
    isRevealed = false;
    hasFlag = false;
    neighboringMines = 0;
    neighbors = new ArrayList<>();
  }


  /* TEMPLATE:
   * 
   * fields:
   * ... this.hasMine ...            -- boolean
   * ... this.isRevealed ...         -- boolean
   * ... this.hasFlag ...            -- boolean
   * ... this.neighboringMines ...   -- int
   * ... this.neighbors ...          -- ArrayList<Cell>
   * 
   * methods:
   * ... this.addNeighbor(Cell) ...                      -- void
   * ... this.countNeighborMines() ...                  -- int
   * ... this.revealCell() ...                          -- void
   * ... this.placeFlag() ...                           -- void
   * ... this.drawCell() ...                            -- WorldImage
   * ... this.getColor() ...                            -- Color
   * 
   * methods for fields:
   * ... this.neighbors.addNeighbor(Cell) ...           -- void
   * ... this.neighbors.countNeighborMines() ...       -- int
   * ... this.neighbors.revealCell() ...               -- void
   * ... this.neighbors.placeFlag() ...                -- void
   * ... this.neighbors.drawCell() ...                 -- WorldImage
   * ... this.neighbors.getColor() ...                 -- Color
   * 
   * ... this.drawCell() ...                           -- WorldImage
   * ... this.getColor() ...                           -- Color
   */



  // Add the given cell as a neighbor
  public void addNeighbor(Cell newNeighbor) {
    neighbors.add(newNeighbor);
  }

  // Count the number of neighboring cells with mines
  public int countNeighborMines() {
    int count = 0;
    for (Cell currentNeighbor : neighbors) {
      if (currentNeighbor.hasMine) {
        count++;
      }
    }
    return count;
  }

  // Reveal the cell and propagate if necessary
  public void revealCell() {
    if (!hasFlag) {
      isRevealed = true;
      if (neighboringMines == 0) {
        for (Cell currentNeighbor : neighbors) {
          if (!currentNeighbor.isRevealed && !currentNeighbor.hasMine) {
            currentNeighbor.revealCell();
          }
        }
      }
    }
  }

  // Toggle the flag status of the cell
  public void placeFlag() {
    if (!isRevealed) {
      hasFlag = !hasFlag;
    }
  }

  // Draw the cell based on its state
  public WorldImage drawCell() {
    if (isRevealed) {
      if (hasMine) {
        return new RectangleImage(28, 28, OutlineMode.SOLID, Color.RED);
      }
      else {
        if (neighboringMines > 0) {
          return new OverlayImage(new TextImage(Integer.toString(neighboringMines), 20, getColor()),
              new RectangleImage(28, 28, OutlineMode.SOLID, Color.DARK_GRAY));
        }
        else {
          return new RectangleImage(28, 28, OutlineMode.SOLID, Color.LIGHT_GRAY);
        }
      }
    }
    else {
      if (hasFlag) {
        return new RectangleImage(28, 28, OutlineMode.SOLID, Color.ORANGE);
      }
      else {
        return new RectangleImage(28, 28, OutlineMode.SOLID, Color.CYAN);
      }
    }
  }

  // Determine the color of the cell based on neighboring mines
  public Color getColor() {
    if (neighboringMines == 1) {
      return Color.CYAN;
    }
    if (neighboringMines == 2) {
      return Color.GREEN;
    }
    if (neighboringMines == 3) {
      return Color.RED;
    }
    if (neighboringMines == 4) {
      return Color.BLUE;
    }
    if (neighboringMines == 5) {
      return Color.PINK;
    }
    if (neighboringMines == 6) {
      return Color.YELLOW;
    }
    return Color.WHITE;
  }
}

//The MineSweeper game 
class MinesweeperGame extends World {
  int rows;
  int cols;
  int numMines;
  ArrayList<ArrayList<Cell>> board;
  Random objRandom;
  WorldScene lastScene;

  // Constructor
  public MinesweeperGame(int rows, int cols, int numMines) {
    this.rows = rows;
    this.cols = cols;
    this.numMines = numMines;
    board = new ArrayList<>();
    objRandom = new Random();
  }

  /* TEMPLATE:
   * 
   * fields:
   * ... this.rows ...                 -- int
   * ... this.cols ...                 -- int
   * ... this.numMines ...            -- int
   * ... this.board ...                -- ArrayList<ArrayList<Cell>>
   * ... this.objRandom ...            -- Random
   * ... this.lastScene ...            -- WorldScene
   * 
   * methods:
   * ... this.makeScene() ...                            -- WorldScene
   * ... this.makeBackground() ...                      -- WorldImage
   * ... this.initBoard() ...                           -- void
   * ... this.countNeighborMinesForAll() ...            -- void
   * ... this.onMouseClicked(Posn, String) ...          -- void
   * ... this.updateGameStatus() ...                    -- void
   * ... this.lastScene(String) ...                     -- WorldScene
   * ... this.onTick() ...                              -- void
   * ... this.drawGame(WorldScene) ...                  -- void
   * 
   * methods for fields:
   * ... this.board.makeScene() ...                     -- WorldScene
   * ... this.board.makeBackground() ...               -- WorldImage
   * ... this.board.initBoard() ...                    -- void
   * ... this.board.countNeighborMinesForAll() ...     -- void
   * ... this.board.onMouseClicked(Posn, String) ...   -- void
   * ... this.board.updateGameStatus() ...             -- void
   * ... this.board.lastScene(String) ...              -- WorldScene
   * ... this.board.onTick() ...                       -- void
   * ... this.board.drawGame(WorldScene) ...           -- void
   * 
   * ... this.lastScene(String) ...                    -- WorldScene
   */


  // Create the initial scene with the game board
  public WorldScene makeScene() {
    WorldScene newWorldScene = new WorldScene(cols * 30, rows * 30);
    newWorldScene.placeImageXY(makeBackground(), cols * 30 / 2, rows * 30 / 2);
    drawGame(newWorldScene);
    return newWorldScene;
  }

  // Create the background image for the game board
  public WorldImage makeBackground() {
    return new RectangleImage(cols * 30, rows * 30, OutlineMode.SOLID, Color.BLACK);
  }

  // Initialize the game board with mines and neighboring counts
  public void initBoard() {
    ArrayList<Posn> minePositions = new ArrayList<>();

    for (int z = 0; z < rows; z++) {
      ArrayList<Cell> newRow = new ArrayList<>();
      for (int y = 0; y < cols; y++) {
        newRow.add(new Cell(false));
      }
      board.add(newRow);
    }

    while (minePositions.size() < numMines) {
      int randomRow = objRandom.nextInt(rows);
      int randomColumn = objRandom.nextInt(cols);
      Posn newPosition = new Posn(randomRow, randomColumn);
      if (!minePositions.contains(newPosition)) {
        minePositions.add(newPosition);
        Cell randomCell = board.get(randomRow).get(randomColumn);
        randomCell.hasMine = true;
      }
    }

    countNeighborMinesForAll();
  }

  // Count neighboring mines for all cells in the board
  public void countNeighborMinesForAll() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Cell currentCell = board.get(i).get(j);
        for (int x = i - 1; x <= i + 1; x++) {
          for (int y = j - 1; y <= j + 1; y++) {
            if (x >= 0 && x < rows && y >= 0 && y < cols) {
              currentCell.addNeighbor(board.get(x).get(y));
            }
          }
        }
        currentCell.neighboringMines = currentCell.countNeighborMines();
      }
    }
  }

  // Process mouse clicks to reveal cells or place flags
  public void onMouseClicked(Posn pos, String button) {
    int cellXPos = pos.x / 30;
    int cellYPos = pos.y / 30;
    if (cellXPos >= 0 && cellXPos < cols && cellYPos >= 0 && cellYPos < rows) {
      Cell clickedCell = board.get(cellYPos).get(cellXPos);
      if (button.equals("LeftButton")) {
        if (!clickedCell.hasFlag) {
          clickedCell.revealCell();
          updateGameStatus();
        }
      }
      else if (button.equals("RightButton")) {
        clickedCell.placeFlag();
      }
    }
  }

  // Update the game status based on revealed cells and flags
  public void updateGameStatus() {
    boolean hasLost = false;
    boolean hasWon = true;
    for (int z = 0; z < rows; z++) {
      for (int y = 0; y < cols; y++) {
        Cell currentCell = board.get(z).get(y);
        if (currentCell.isRevealed && currentCell.hasMine) {
          hasLost = true;
        }
        if (!currentCell.isRevealed && !currentCell.hasMine) {
          hasWon = false;
        }
      }
    }
    if (hasLost) {
      endOfWorld("You hit a mine! Game Over!");
    }
    else if (hasWon) {
      endOfWorld("Congratulations! You've won!");
    }
  }

  // Create the final scene with the game over message
  public WorldScene lastScene(String message) {
    WorldScene newWorldScene = makeScene();
    newWorldScene.placeImageXY(new RectangleImage(300, 100, OutlineMode.SOLID, Color.RED),
        cols * 15, rows * 15);
    newWorldScene.placeImageXY(new TextImage(message, 20, FontStyle.BOLD, Color.BLACK), cols * 15,
        rows * 15);
    lastScene = newWorldScene;
    return newWorldScene;
  }

  public void onTick() {
    // No action needed on tick
    // The game state is updated based on player interactions and not time-dependent
    // events.
  }

  public void drawGame(WorldScene scene) {
    for (int z = 0; z < rows; z++) {
      for (int y = 0; y < cols; y++) {
        scene.placeImageXY(board.get(z).get(y).drawCell(), y * 30 + 15, z * 30 + 15);
      }
    }
  }

}

//Examples and Tests
class Examples {
  // Create two example cells
  public void testCell(Tester t) {
    Cell cell1 = new Cell(false);
    // Cell with mine
    Cell cell2 = new Cell(true);

    // Tests for original state of cells
    t.checkExpect(cell1.hasMine, false);
    t.checkExpect(cell1.isRevealed, false);
    t.checkExpect(cell1.hasFlag, false);
    t.checkExpect(cell1.neighboringMines, 0);
    t.checkExpect(cell2.hasMine, true);
    t.checkExpect(cell2.isRevealed, false);
    t.checkExpect(cell2.hasFlag, false);
    t.checkExpect(cell2.neighboringMines, 0);

    // checks cell size before adding neighbors
    t.checkExpect(cell1.neighbors.size(), 0);
    t.checkExpect(cell2.neighbors.size(), 0);

    // new cell neighboring cell1 without a mine
    Cell neighbor1 = new Cell(false);
    // new cell neighboring cell2 without a mine
    Cell neighbor2 = new Cell(true);
    // Adds neighbor(s) to each mine
    cell1.addNeighbor(neighbor1);
    cell1.addNeighbor(neighbor2);
    cell2.addNeighbor(neighbor1);

    // cell that contains another cell1 and its neighbors
    Cell cell3 = new Cell(true);
    cell3.addNeighbor(cell1);

    // checks the amount of neighbors that cell has
    t.checkExpect(cell1.neighbors.size(), 2);
    t.checkExpect(cell2.neighbors.size(), 1);
    t.checkExpect(cell3.neighbors.size(), 1);

    t.checkExpect(cell1.countNeighborMines(), 1);
    t.checkExpect(cell2.countNeighborMines(), 0);
    t.checkExpect(cell3.countNeighborMines(), 0);

    // Tests that check if you can place a flag
    cell1.placeFlag();
    t.checkExpect(cell1.hasFlag, true);
    cell2.placeFlag();
    t.checkExpect(cell2.hasFlag, true);
    t.checkExpect(cell3.hasFlag, false);

    cell1.placeFlag();
    t.checkExpect(cell1.hasFlag, false); // Tests that you can remove a flag
    cell1.revealCell();
    t.checkExpect(cell1.isRevealed, true);
    cell1.revealCell();
    t.checkExpect(cell1.isRevealed, true); // Tests that you cannot hide a cell after displaying
    cell2.placeFlag();
    t.checkExpect(cell1.hasFlag, false); // Tests that you can remove a flag
    cell2.revealCell();
    t.checkExpect(cell2.isRevealed, true);

    // Tests for the visuals of cells
    t.checkExpect(cell1.drawCell(),
        new RectangleImage(28, 28, OutlineMode.SOLID, Color.LIGHT_GRAY));
    // Cell is revealed and contains mine
    t.checkExpect(cell2.drawCell(), new RectangleImage(28, 28, OutlineMode.SOLID, Color.RED));

    // Color for plain not revealed Cell
    Cell plainCell = new Cell(false);
    t.checkExpect(plainCell.drawCell(), new RectangleImage(28, 28, OutlineMode.SOLID, Color.CYAN));

    // Checks that color changes when a flag is placed
    plainCell.placeFlag();
    t.checkExpect(plainCell.drawCell(),
        new RectangleImage(28, 28, OutlineMode.SOLID, Color.ORANGE));
  }

  public void testMinesweeperGame(Tester t) {
    MinesweeperGame game = new MinesweeperGame(1, 2, 1);

    game.initBoard();
    // Tests that when all cells without a mine are revealed the, the user has won the game
    game.board.get(0).get(0).hasMine = false;
    game.board.get(0).get(0).isRevealed = true;
    game.board.get(0).get(1).hasMine = true;
    game.updateGameStatus();
    t.checkExpect(game.lastScene, game.lastScene("Congratulations! You've won!"));

    game = new MinesweeperGame(4, 4, 4);

    for (int z = 0; z < game.rows; z++) {
      ArrayList<Cell> newRow = new ArrayList<>();
      for (int y = 0; y < game.cols; y++) {
        newRow.add(new Cell(false));
      }
      game.board.add(newRow);
    }

    // Tests that when the player hits a mine, the game ends
    game.board.get(0).get(1).hasMine = true;
    game.board.get(0).get(1).isRevealed = true;
    game.updateGameStatus();
    t.checkExpect(game.lastScene, game.lastScene("You hit a mine! Game Over!"));

    t.checkExpect(game.board.size(), 4);
    t.checkExpect(game.board.get(0).get(0).neighboringMines, 0);

    game.initBoard();

    // Mouse click test for creating a flag
    game.onMouseClicked(new Posn(30, 30), "RightButton");
    t.checkExpect(game.board.get(1).get(1).hasFlag, true);
    // Mouse click test for revealing a cell
    game.onMouseClicked(new Posn(0, 0), "LeftButton");
    t.checkExpect(game.board.get(0).get(0).isRevealed, true);

    WorldScene scene = game.makeScene();
    t.checkExpect(scene.height, 120);
    t.checkExpect(scene.width, 120);
  }

  public void testBigBang(Tester t) {
    MinesweeperGame newGame = new MinesweeperGame(16, 30, 99);
    newGame.initBoard();
    newGame.bigBang(30 * 30, 16 * 30, 0.1);
  }

}

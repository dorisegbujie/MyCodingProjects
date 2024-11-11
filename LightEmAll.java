import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.RotateImage;
import javalib.worldimages.StarImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import javalib.worldimages.WorldImage;
import tester.Tester;



//game state and main game class
class LightEmAll extends World {

  ArrayList<ArrayList<GamePiece>> board;
  ArrayList<GamePiece> nodes;
  ArrayList<Edge> mst;
  int width; 
  int height; 
  int powerRow; 
  int powerCol; 
  int radius; 
  Random rand; 
  int score;
  int gameEnd;
  int time; 
  public static int CELL_SIZE = 40; 

  int maxScore = 20; 
  int maxTime = 240; 

  // the default gameplay constructor
  LightEmAll(int width, int height) {
    this(width, height, 3);
  }

  // constructor for making different types of boards
  LightEmAll(int width, int height, int genType) {
    this(width, height, genType, new Random());
  }

  // constructor for generating different types of boards but you pass in a random
  LightEmAll(int width, int height, int genType, Random rand) {
    if (genType == -1) { 
      this.rand = rand;
      this.width = width;
      this.height = height;
      this.powerRow = 0;
      this.powerCol = 0;
      this.board = this.generateBoard();
      this.nodes = this.grabAllNodes();
      this.mst = generateMST(generateAllPossibleEdges(this.board));
      this.score = 0;
      this.radius = (this.calcDiameter() / 2) + 1;
      this.gameEnd = 0;
      this.time = 0;
    }
    else if (genType == 1) { 
      this.rand = rand;
      this.width = width;
      this.height = height;
      this.powerRow = 0;
      this.powerCol = 0;
      this.board = this.generateBoard();
      this.nodes = this.grabAllNodes();
      this.mst = generateMST(generateAllPossibleEdges(this.board));
      this.score = 0;
      generateManualConnections();
      updateAllNeighbors();
      this.radius = (this.calcDiameter() / 2) + 1;
      updatePower(this.board);
      this.gameEnd = 0;
      this.time = 0;
    }
    else if (genType == 2) { 
      this.rand = rand;
      this.width = width;
      this.height = height;
      this.powerRow = 0;
      this.powerCol = 0;
      this.board = this.generateBoard();
      this.nodes = this.grabAllNodes();
      this.mst = generateMST(generateAllPossibleEdges(this.board));
      this.score = 0;
      generateFractalConnections(new Posn(0, 0), this.board);
      updateAllNeighbors();
      this.radius = (this.calcDiameter() / 2) + 1;
      updatePower(this.board);
      this.gameEnd = 0;
      this.time = 0;
    }

    else if (genType == 3) { 
      this.rand = rand;
      this.width = width;
      this.height = height;
      this.powerRow = 0;
      this.powerCol = 0;
      this.board = this.generateBoard();
      this.nodes = this.grabAllNodes();
      this.mst = generateMST(generateAllPossibleEdges(this.board));
      this.score = 0;
      this.radius = (this.calcDiameter() / 2) + 1;
      generateEdgeConnections();
      updateAllNeighbors();
      this.radius = (this.calcDiameter() / 2) + 1;
      randomizeGrid(this.nodes);
      updatePower(this.board);
      this.gameEnd = 0;
      this.time = 0;
    }
  }

  /* Template:
   * Fields:
   * ...this.board...             -- ArrayList<ArrayList<GamePiece>>
   * ...this.nodes...             -- ArrayList<GamePiece>
   * ...this.mst...               -- ArrayList<Edge>
   * ...this.width...             -- int
   * ...this.height...            -- int
   * ...this.powerRow...          -- int
   * ...this.powerCol...          -- int
   * ...this.radius...            -- int
   * ...this.rand...              -- Random
   * ...this.score...             -- int
   * 
   * Methods:
   * ...this.grabAllNodes()...                    -- ArrayList<GamePiece>
   * ...this.generateManualConnections()...        -- void
   * ...this.generateFractalConnections(Posn, ArrayList<ArrayList<GamePiece>>)... -- void
   * ...this.determineSplitType(ArrayList<ArrayList<GamePiece>>)... -- int
   * ...this.splitBoard(int, ArrayList<ArrayList<GamePiece>>)...  
   * -- ArrayList<ArrayList<ArrayList<GamePiece>>>
   * ...this.buildU(ArrayList<ArrayList<GamePiece>>)...          -- void
   * ...this.generateBoard()...                   -- ArrayList<ArrayList<GamePiece>>
   * ...this.onMouseClicked(Posn, String)...      -- void
   * ...this.updateAllNeighbors()...              -- void
   * ...this.locatePiece(Posn)...                  -- GamePiece
   * ...this.makeScene()...                       -- WorldScene
   * ...this.restartGame()...                     -- void
   * ...this.updatePower(ArrayList<ArrayList<GamePiece>>)... -- void
   * ...this.onKeyEvent(String)...                 -- void
   * 
   * Methods for Fields:
   * ...this.nodes.grabAllNodes()...                    -- ArrayList<GamePiece>
   * ...this.nodes.generateManualConnections()...        -- void
   * ...this.nodes.generateFractalConnections(Posn, ArrayList<ArrayList<GamePiece>>)... -- void
   * ...this.nodes.determineSplitType(ArrayList<ArrayList<GamePiece>>)... -- int
   * ...this.nodes.splitBoard(int, ArrayList<ArrayList<GamePiece>>)...  
   * -- ArrayList<ArrayList<ArrayList<GamePiece>>>
   * ...this.nodes.buildU(ArrayList<ArrayList<GamePiece>>)...          -- void
   * ...this.nodes.generateBoard()...                   -- ArrayList<ArrayList<GamePiece>>
   * ...this.nodes.onMouseClicked(Posn, String)...      -- void
   * ...this.nodes.updateAllNeighbors()...              -- void
   * ...this.nodes.locatePiece(Posn)...                  -- GamePiece
   * ...this.nodes.makeScene()...                       -- WorldScene
   * ...this.nodes.restartGame()...                     -- void
   * ...this.nodes.updatePower(ArrayList<ArrayList<GamePiece>>)... -- void
   * ...this.nodes.onKeyEvent(String)...                 -- void
   * 
   * ...this.mst...             -- ArrayList<Edge>
   * ...this.width...           -- int
   * ...this.height...          -- int
   * ...this.rand...            -- Random
   * ...this.score...           -- int
   */

  // will grab all the boards cells, left to right, then top to bottom
  public ArrayList<GamePiece> grabAllNodes() {
    ArrayList<GamePiece> allNodes = new ArrayList<GamePiece>();
    for (int c = 0; c < this.width; c++) {
      for (int r = 0; r < this.height; r++) {
        allNodes.add(this.board.get(c).get(r));
      }
    }
    return allNodes;
  }

  // creates manual connections
  public void generateManualConnections() {
    int middleColIndex = (int) Math.floor(this.width / 2);
    for (int c = 0; c < this.width; c++) {
      for (int r = 0; r < this.height; r++) {
        if (c == 0) {
          this.board.get(c).get(r).left = false;
          this.board.get(c).get(r).right = true;
          this.board.get(c).get(r).top = false;
          this.board.get(c).get(r).bottom = false;
        }
        else if (c == middleColIndex) { 
          this.board.get(c).get(r).left = true;
          this.board.get(c).get(r).right = true;
          this.board.get(c).get(r).top = true;
          this.board.get(c).get(r).bottom = true;
        }
        else if ((c + 1) == this.width) { 
          this.board.get(c).get(r).left = true;
          this.board.get(c).get(r).right = false;
          this.board.get(c).get(r).top = false;
          this.board.get(c).get(r).bottom = false;
        }
        else { // all other columns
          this.board.get(c).get(r).left = true;
          this.board.get(c).get(r).right = true;
          this.board.get(c).get(r).top = false;
          this.board.get(c).get(r).bottom = false;
        }
      }
    }
  }

  // generates a fractal board
  public void generateFractalConnections(Posn lastKnownPosition,
      ArrayList<ArrayList<GamePiece>> currentBoard) {
    ArrayList<ArrayList<ArrayList<GamePiece>>> splits = 
        new ArrayList<ArrayList<ArrayList<GamePiece>>>();
    int splitType = determineSplitType(currentBoard);
    if (splitType == 0) { 
      int colCount = currentBoard.size();
      int rowCount = currentBoard.get(0).size();
      if (colCount == 2 && rowCount == 2) {
        buildU(currentBoard);
      }
      if (colCount == 2 && rowCount == 3) { 
        buildU(currentBoard);
      }
      if (colCount == 3 && rowCount == 2) { 
        buildU(currentBoard);
        currentBoard.get(1).get(0).bottom = true;
        currentBoard.get(1).get(1).top = true;
      }
      if (colCount == 3 && rowCount == 3) { 
        buildU(currentBoard);
        currentBoard.get(1).get(0).bottom = true;
        currentBoard.get(1).get(1).top = true;
        currentBoard.get(1).get(1).bottom = true;
        currentBoard.get(1).get(2).top = true;
      }
    }
    else if (splitType == 1) {
      buildU(currentBoard);
      splits = splitBoard(splitType, currentBoard);
      generateFractalConnections(new Posn(0, 0), splits.get(0));
      generateFractalConnections(new Posn(1, 0), splits.get(1));
      generateFractalConnections(new Posn(0, 1), splits.get(2));
      generateFractalConnections(new Posn(0, 1), splits.get(3));
    }
    else if (splitType == 2) { 
      splits = splitBoard(splitType, currentBoard); 
      int bottomRow = splits.get(0).get(0).size() - 1;
      int rightCol = splits.get(0).size() - 1;
      if (lastKnownPosition.x == 0) { 

        splits.get(0).get(0).get(bottomRow).bottom = true;
        splits.get(1).get(0).get(0).top = true; 
      }
      if (lastKnownPosition.x == 1) { 
        splits.get(0).get(rightCol).get(bottomRow).bottom = true; 
        splits.get(1).get(rightCol).get(0).top = true; 
      }
      generateFractalConnections(new Posn(0, 0), splits.get(0));
      generateFractalConnections(new Posn(0, 1), splits.get(1));
    }
    else if (splitType == 3) { 
      buildU(currentBoard);
      splits = splitBoard(splitType, currentBoard); 
      generateFractalConnections(new Posn(0, 0), splits.get(0));
      generateFractalConnections(new Posn(1, 0), splits.get(1));
    }
  }

  // splits the board in the desired manner
  public ArrayList<ArrayList<ArrayList<GamePiece>>> splitBoard(int splitType,
      ArrayList<ArrayList<GamePiece>> boardToSplit) {
    ArrayList<ArrayList<ArrayList<GamePiece>>> ret = 
        new ArrayList<ArrayList<ArrayList<GamePiece>>>();

    int splitCol = boardToSplit.size() / 2 + ((boardToSplit.size() % 2 == 0) ? 0 : 1);
    int splitRow = boardToSplit.get(0).size() / 2 + ((boardToSplit.get(0).size() % 2 == 0) ? 0 : 1);
    if (splitType == 1) { 
      ArrayList<ArrayList<GamePiece>> quad1 = new ArrayList<ArrayList<GamePiece>>();
      ArrayList<ArrayList<GamePiece>> quad2 = new ArrayList<ArrayList<GamePiece>>();
      ArrayList<ArrayList<GamePiece>> quad3 = new ArrayList<ArrayList<GamePiece>>();
      ArrayList<ArrayList<GamePiece>> quad4 = new ArrayList<ArrayList<GamePiece>>();
      for (int c = 0; c < splitCol; c++) {
        quad1.add(new ArrayList<GamePiece>(boardToSplit.get(c).subList(0, splitRow)));
        quad3.add(new ArrayList<GamePiece>(
            boardToSplit.get(c).subList(splitRow, boardToSplit.get(0).size())));
      }
      for (int c = splitCol; c < boardToSplit.size(); c++) {
        quad2.add(new ArrayList<GamePiece>(boardToSplit.get(c).subList(0, splitRow)));
        quad4.add(new ArrayList<GamePiece>(
            boardToSplit.get(c).subList(splitRow, boardToSplit.get(0).size())));
      }
      ret.add(quad1);
      ret.add(quad2);
      ret.add(quad3);
      ret.add(quad4);
    }
    if (splitType == 2) { 
      ArrayList<ArrayList<GamePiece>> top = new ArrayList<ArrayList<GamePiece>>();
      ArrayList<ArrayList<GamePiece>> bottom = new ArrayList<ArrayList<GamePiece>>();
      for (int c = 0; c < boardToSplit.size(); c++) {
        top.add(new ArrayList<GamePiece>(boardToSplit.get(c).subList(0, splitRow)));
        bottom.add(new ArrayList<GamePiece>(
            boardToSplit.get(c).subList(splitRow, boardToSplit.get(0).size())));
      }
      ret.add(top);
      ret.add(bottom);
    }
    if (splitType == 3) { 
      ArrayList<ArrayList<GamePiece>> left = new ArrayList<ArrayList<GamePiece>>(
          boardToSplit.subList(0, splitCol));
      ArrayList<ArrayList<GamePiece>> right = new ArrayList<ArrayList<GamePiece>>(
          boardToSplit.subList(splitCol, boardToSplit.size()));
      ret.add(left);
      ret.add(right);
    }
    return ret;
  }

  public int determineSplitType(ArrayList<ArrayList<GamePiece>> currentBoard) {
    int colCount = currentBoard.size();
    int rowCount = currentBoard.get(0).size();
    if (colCount < 4 && rowCount < 4) { 
      return 0;
    }
    else if (colCount >= 4 && rowCount >= 4) { 
      return 1;
    }
    else if (colCount < 4 && rowCount >= 4) { 
      return 2;
    }
    else if (colCount >= 4 && rowCount < 4) { 
      return 3;
    }
    return -1; 
  }

  // makes the u pattern on the passed in board
  public void buildU(ArrayList<ArrayList<GamePiece>> currentBoard) {
    for (int r = 0; r < currentBoard.get(0).size(); r++) { 
      if ((r != (currentBoard.get(0).size() - 1)) && r != 0) { 
        currentBoard.get(0).get(r).top = true;
        currentBoard.get(0).get(r).bottom = true;
        currentBoard.get(currentBoard.size() - 1).get(r).top = true;
        currentBoard.get(currentBoard.size() - 1).get(r).bottom = true;
      }
      else if (r == 0) { // top row
        currentBoard.get(0).get(r).bottom = true;
        currentBoard.get(currentBoard.size() - 1).get(r).bottom = true;
      }
      else if (r == (currentBoard.get(0).size() - 1)) { 
        for (int c = 1; c < currentBoard.size() - 1; c++) { 
          currentBoard.get(c).get(r).left = true;
          currentBoard.get(c).get(r).right = true;
        }
        currentBoard.get(0).get(r).top = true;
        currentBoard.get(0).get(r).right = true;
        currentBoard.get(currentBoard.size() - 1).get(r).top = true;
        currentBoard.get(currentBoard.size() - 1).get(r).left = true;
      }
    }

  }

  // builds the board, does not create connections or powerStation
  public ArrayList<ArrayList<GamePiece>> generateBoard() {
    ArrayList<ArrayList<GamePiece>> genBoard = new ArrayList<ArrayList<GamePiece>>();
    for (int c = 0; c < this.width; c++) {
      genBoard.add(new ArrayList<GamePiece>());
      for (int r = 0; r < this.height; r++) {
        genBoard.get(c).add(new GamePiece(r, c, false, false, false, false));
      }
    }

    return genBoard;

  }

  // takes in a grid of gamepieces and rotates each piece by a random integer
  public void randomizeGrid(ArrayList<GamePiece> nodes) {
    for (GamePiece node : nodes) {
      int numRotations = this.rand.nextInt(4);
      for (int i = 0; i < numRotations; i++) {
        node.rotatePiece(1);
      }
    }
  }

  // handles clicks
  public void onMouseClicked(Posn mouse, String button) {
    GamePiece clicked = locatePiece(mouse);
    if (button.equals("LeftButton")) { 
      clicked.rotatePiece(1);
      this.score++; 
    }
    else if (button.equals("RightButton")) { 
      clicked.rotatePiece(-1);
      this.score++; 
    }
    updateAllNeighbors();
    updatePower(this.board);
    checkGameEnd(this.nodes, this.score, this.time);
  }

  // adds all the neighbors to each cell of the game board
  public void updateAllNeighbors() {

    for (GamePiece g : nodes) {
      g.updateNeighbor("top", null);
      g.updateNeighbor("right", null);
      g.updateNeighbor("bottom", null);
      g.updateNeighbor("left", null);
    }
    for (int c = 0; c < this.width; c++) {
      int left = c - 1;
      int right = c + 1;
      for (int r = 0; r < this.height; r++) {
        int top = r - 1;
        int bottom = r + 1;
        if (top >= 0) {
          this.board.get(c).get(r).updateNeighbor("top", this.board.get(c).get(top));
        }
        if (bottom < this.height) {
          this.board.get(c).get(r).updateNeighbor("bottom", this.board.get(c).get(bottom));
        }
        if (left >= 0) {
          this.board.get(c).get(r).updateNeighbor("left", this.board.get(left).get(r));
        }
        if (right < this.width) {
          this.board.get(c).get(r).updateNeighbor("right", this.board.get(right).get(r));
        }
      }
    }
  }

  // finds the cell at the given posn
  public GamePiece locatePiece(Posn mouse) {
    int row = (int) Math.floor(mouse.y / LightEmAll.CELL_SIZE);
    int col = (int) Math.floor(mouse.x / LightEmAll.CELL_SIZE);
    return this.board.get(col).get(row);
  }

  // draws the scene
  public WorldScene makeScene() {
    int boardWidth = this.width * LightEmAll.CELL_SIZE;
    int boardHeight = this.height * LightEmAll.CELL_SIZE;
    WorldScene gameScene = new WorldScene(0, 0);
    WorldImage scoreBoard = new OverlayImage(
        new TextImage(Integer.toString(this.score), LightEmAll.CELL_SIZE, Color.GREEN),
        new OverlayImage(
            new RectangleImage(3 * CELL_SIZE, (int) 1.2 * CELL_SIZE, OutlineMode.SOLID,
                Color.black),
            new RectangleImage(boardWidth, 2 * CELL_SIZE, OutlineMode.SOLID, Color.lightGray)));
    for (int c = 0; c < this.width; c++) {
      for (int r = 0; r < this.height; r++) {
        gameScene.placeImageXY(
            this.board.get(c).get(r).drawPiece(this.radius)
            .movePinhole((-.5 * LightEmAll.CELL_SIZE), (-.5 * LightEmAll.CELL_SIZE)),
            (c * LightEmAll.CELL_SIZE), (r * LightEmAll.CELL_SIZE));
      }
    }
    gameScene.placeImageXY(scoreBoard, boardWidth / 2, boardHeight + CELL_SIZE);
    gameScene.placeImageXY(new TextImage("Press space to restart.", 10, Color.BLACK),
        boardWidth / 2, boardHeight + (CELL_SIZE / 4));
    gameScene.placeImageXY(
        new TextImage("Time: " + Integer.toString((int) (this.time / 4)), 10, Color.BLACK),
        boardWidth / 2, boardHeight + CELL_SIZE + (3 * (CELL_SIZE / 4)));
    return gameScene;
  }

  // restarts the game
  public void restartGame() {
    LightEmAll newGame = new LightEmAll(this.width, this.height);
    this.board = newGame.board;
    this.nodes = newGame.nodes;
    this.mst = newGame.mst;
    this.width = newGame.width;
    this.height = newGame.height;
    this.powerRow = newGame.powerRow;
    this.powerCol = newGame.powerCol;
    this.radius = newGame.radius;
    this.rand = newGame.rand;
    this.score = newGame.score;
    this.gameEnd = newGame.gameEnd;
    this.time = newGame.time;
  }

  // powers the board
  public void updatePower(ArrayList<ArrayList<GamePiece>> targetBoard) {
    for (GamePiece g : this.nodes) {
      g.powered = 0;
    }
    targetBoard.get(powerCol).get(powerRow).powerStation = true; 
    targetBoard.get(powerCol).get(powerRow).powered = this.radius; 
    targetBoard.get(powerCol).get(powerRow).powerNeighbors(new ArrayList<GamePiece>());
  }

  // grabs the farthest reachable node from the given node
  public GamePiece getFarthestNode(GamePiece startNode) {
    HashMap<GamePiece, Integer> distMap = generateDistanceMap(startNode);
    GamePiece farthestNode = startNode;
    int max = 0;
    for (Map.Entry<GamePiece, Integer> entry : distMap.entrySet()) {
      GamePiece key = entry.getKey();
      Integer value = entry.getValue();
      if (value > max) {
        max = value;
        farthestNode = key;
      }
    }
    return farthestNode;
  }

  // calculates the diameter of this game
  public int calcDiameter() {
    GamePiece farthestFromPower = this.getFarthestNode(this.board.get(powerCol).get(powerRow));
    GamePiece farthestSecond = this.getFarthestNode(farthestFromPower);
    return generateDistanceMap(farthestFromPower).get(farthestSecond) + 1;
  }

  // creates a distance map of all the GamePieces reachable from the passed in GamePiece
  public HashMap<GamePiece, Integer> generateDistanceMap(GamePiece startNode) {
    ArrayList<String> directions = new ArrayList<String>(
        Arrays.asList("left", "right", "top", "bottom"));
    ArrayDeque<GamePiece> queue = new ArrayDeque<GamePiece>();
    ArrayList<GamePiece> seen = new ArrayList<GamePiece>();
    HashMap<GamePiece, Integer> distMap = new HashMap<GamePiece, Integer>();
    queue.addFirst(startNode);
    distMap.put(startNode, 0);
    while (!queue.isEmpty()) {
      GamePiece next = queue.removeFirst();
      if (!seen.contains(next)) {
        seen.add(next);
        for (String dir : directions) { 
          if (next.isConnectedTo(dir) && !seen.contains(next.neighbors.get(dir))) {
            queue.addFirst(next.neighbors.get(dir));
            distMap.put(next.neighbors.get(dir), distMap.get(next) + 1);
          }
        }
      }
    }
    return distMap;
  }

  // handles key events
  public void onKeyEvent(String pressedKey) {
    GamePiece powerStationPiece = this.board.get(powerCol).get(powerRow);
    if (pressedKey.equals("up") && this.powerRow > 0 && powerStationPiece.isConnectedTo("top")) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.powerRow -= 1;
    }
    if (pressedKey.equals("down") && this.powerRow < this.height - 1
        && powerStationPiece.isConnectedTo("bottom")) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.powerRow += 1;
    }
    if (pressedKey.equals("left") && this.powerCol > 0 && powerStationPiece.isConnectedTo("left")) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.powerCol -= 1;
    }
    if (pressedKey.equals("right") && this.powerCol < this.width - 1
        && powerStationPiece.isConnectedTo("right")) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.powerCol += 1;
    }
    if (pressedKey.equals(" ") && this.powerCol < this.width) { // restarts the game
      restartGame();
    }
    updatePower(this.board);
  }

  // will run onTick functions
  public void onTick() {
    this.time++;
    checkGameEnd(this.nodes, this.score, this.time);
  }

  // ends the world and checks win/loss
  public WorldEnd worldEnds() {
    int middleX = (int) (this.width * CELL_SIZE) / 2;
    int middleY = (int) (this.height * CELL_SIZE) / 2;
    WorldScene end = this.getEmptyScene();
    if (this.gameEnd == 1) {
      end.placeImageXY(new TextImage("You Win!", CELL_SIZE, Color.GREEN), middleX, middleY);
      return new WorldEnd(true, end);
    }
    else if (this.gameEnd == -1) {
      end.placeImageXY(new TextImage("You Lose!", CELL_SIZE, Color.RED), middleX, middleY);
      return new WorldEnd(true, end);
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // will check game end state
  public void checkGameEnd(ArrayList<GamePiece> nodes, int currScore, int currTime) {
    boolean win = true;
    boolean loss = true;
    for (GamePiece node : nodes) {
      if (node.powered < 1) {
        win = false;
      }
    }
    if (this.score < maxScore && this.time < maxTime) {
      loss = false;
    }

    if (win) {
      this.gameEnd = 1; 
    }
    else if (loss) {
      this.gameEnd = -1; 
    }
    else {
      this.gameEnd = 0; 
    }
  }

  // creates a list of all the possible edges
  public ArrayList<Edge> generateAllPossibleEdges(ArrayList<ArrayList<GamePiece>> board) {
    ArrayList<Edge> ret = new ArrayList<Edge>();
    for (int c = 0; c < this.width; c++) {
      for (int r = 0; r < this.height; r++) {
        if (c < this.width - 1) {
          ret.add(new Edge(board.get(c).get(r), board.get(c + 1).get(r), this.rand.nextInt(200)));
        }

        if (r < this.height - 1) {
          ret.add(new Edge(board.get(c).get(r), board.get(c).get(r + 1), this.rand.nextInt(200)));
        }
      }
    }
    return ret;
  }

  // calculates the MST given the edges
  public ArrayList<Edge> generateMST(ArrayList<Edge> edges) {
    HashMap<GamePiece, GamePiece> representatives = new HashMap<GamePiece, GamePiece>();
    ArrayList<Edge> ret = new ArrayList<Edge>();
    ArrayDeque<Edge> queue = new ArrayDeque<Edge>();
    ArrayList<Edge> sortedEdges = edges;
    Collections.sort(sortedEdges, new SortByWeight());
    for (Edge e : sortedEdges) {
      queue.addLast(e);
    }

    // setting every GamePiece to have itself as a representative
    for (GamePiece p : this.nodes) {
      representatives.put(p, p);
    }
    // while the work list isn't empty
    while (!queue.isEmpty()) {
      Edge next = queue.removeFirst();
      if (find(representatives, next.fromNode) == find(representatives, next.toNode)) {
        // adding this edge would not cause a cycle
        // so do nothing
      }
      else {
        ret.add(next);
        union(representatives, find(representatives, next.fromNode),
            find(representatives, next.toNode));
      }
    }
    return ret;
  }

  // finds the representative of the given GamePiece
  GamePiece find(HashMap<GamePiece, GamePiece> reps, GamePiece key) {
    if (reps.get(key).equals(key)) {
      return key;
    }
    else {
      return find(reps, reps.get(key));
    }
  }

  // EFFECT: updates the representatives of the hashmap with the given pieces 
  public void union(HashMap<GamePiece, GamePiece> reps, GamePiece from, GamePiece to) {
    reps.put(from, to);
  }

  // makes the initial representatives hashmap for Kruskals
  public HashMap<GamePiece, GamePiece> initRepresentative(ArrayList<GamePiece> nodes) {
    HashMap<GamePiece, GamePiece> ret = new HashMap<GamePiece, GamePiece>();
    for (GamePiece node : nodes) {
      ret.put(node, node);
    }
    return ret;
  }

  // creates all the board connections where the edges are
  public void generateEdgeConnections() {
    for (Edge e : this.mst) {
      e.createConnections();
    }
  }
}

//compares the weight of 2 edges
class SortByWeight implements Comparator<Edge> {

  public int compare(Edge edge1, Edge edge2) {
    return edge1.weight - edge2.weight;
  }
}




class Edge {
  GamePiece fromNode;
  GamePiece toNode;
  int weight;

  Edge(GamePiece fromNode, GamePiece toNode, int weight) {
    this.fromNode = fromNode;
    this.toNode = toNode;
    this.weight = weight;
  }

  /* Template:
   * Fields:
   * ...this.fromNode...       -- GamePiece
   * ...this.toNode...         -- GamePiece
   * ...this.weight...         -- int
   * 
   * * Methods:
   * ...this.createConnections()... -- void
   */


  void createConnections() {
    if (this.fromNode.row == this.toNode.row) {
      this.fromNode.right = true;
      this.toNode.left = true;
    }
    else {
      this.fromNode.bottom = true;
      this.toNode.top = true;
    }
  }
}

//a piece in the game
class GamePiece {
  int row;
  int col;
  boolean left;
  boolean right;
  boolean top;
  boolean bottom;
  boolean powerStation;
  int powered;
  HashMap<String, GamePiece> neighbors;

  GamePiece(int row, int col, HashMap<String, GamePiece> neighbors, boolean left, boolean right,
      boolean top, boolean bottom, boolean powerStation, int powerLevel) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
    this.powerStation = powerStation;
    this.powered = powerLevel;
    this.neighbors = neighbors;
    this.neighbors.put("left", null);
    this.neighbors.put("right", null);
    this.neighbors.put("top", null);
    this.neighbors.put("bottom", null);

  }

  GamePiece(int row, int col, boolean left, boolean right, boolean top, boolean bottom,
      boolean powerStation) {
    this(row, col, new HashMap<String, GamePiece>(), left, right, top, bottom, powerStation, 0);

  }

  // convenience constructor for all inputs but powerStation
  GamePiece(int row, int col, boolean left, boolean right, boolean top, boolean bottom) {
    this(row, col, left, right, top, bottom, false);
  }

  // convenience constructors for non powerStations, up and down connections
  GamePiece(int row, int col) {
    this(row, col, false, false, true, true, false);
  }

  GamePiece() {

  }

  /* Template:
   * Fields:
   * ...this.row...            -- int
   * ...this.col...            -- int
   * ...this.left...           -- boolean
   * ...this.right...          -- boolean
   * ...this.top...            -- boolean
   * ...this.bottom...         -- boolean
   * ...this.powerStation...   -- boolean
   * ...this.powered...        -- int
   * ...this.neighbors...      -- HashMap<String, GamePiece>
   * 
   * Methods:
   * ...this.rotatePiece(int)...           -- void
   * ...this.updateNeighbor(String, GamePiece)... -- void
   * ...this.samePiece(GamePiece)...       -- Object
   * ...this.isConnectedTo(String)...      -- boolean
   * ...this.powerNeighbors(ArrayList<GamePiece>)... -- void
   */

  // draws the GamePiece
  public WorldImage drawPiece(int radius) {
    WorldImage base = new RectangleImage(LightEmAll.CELL_SIZE, LightEmAll.CELL_SIZE,
        OutlineMode.SOLID, Color.darkGray);
    WorldImage connection = new RectangleImage((int) LightEmAll.CELL_SIZE / 8,
        (int) LightEmAll.CELL_SIZE / 2, OutlineMode.SOLID, calcColor(radius)).movePinhole(0,
            (int) LightEmAll.CELL_SIZE / 4);
    if (this.top) {
      base = new OverlayImage(connection, base);
    }
    base = new RotateImage(base, 90.0);
    if (this.left) {
      base = new OverlayImage(connection, base);
    }
    base = new RotateImage(base, 90.0);
    if (this.bottom) {
      base = new OverlayImage(connection, base);
    }
    base = new RotateImage(base, 90.0);
    if (this.right) {
      base = new OverlayImage(connection, base);
    }
    base = new RotateImage(base, 90.0);
    if (this.powerStation) {
      base = new OverlayImage(
          new StarImage((LightEmAll.CELL_SIZE / 2.5), 8, 2, OutlineMode.SOLID, Color.ORANGE), base);
    }
    return base;
  }

  // gradient color functionality
  public Color calcColor(int radius) {
    if (powered > 0) {
      return new Color(255, 255, 0, (255 / radius * this.powered));
    }
    else {
      return Color.GRAY;
    }
  }

  // rotates the GamePiece
  public void rotatePiece(int dir) {
    boolean ogLeft = this.left;
    boolean ogRight = this.right;
    boolean ogTop = this.top;
    boolean ogBottom = this.bottom;
    if (dir > 0) { 
      this.top = ogLeft;
      this.right = ogTop;
      this.bottom = ogRight;
      this.left = ogBottom;
    }
    else if (dir < 0) { 
      this.top = ogRight;
      this.right = ogBottom;
      this.bottom = ogLeft;
      this.left = ogTop;
    }
  }

  // adds this gamePiece to the neighbors
  void updateNeighbor(String location, GamePiece neighbor) {
    this.neighbors.replace(location, neighbor);
  }

  // tests if the passed in piece is the same, mainly used for testing
  public Object samePiece(GamePiece that) {
    return this.row == that.row && this.col == that.col && this.left == that.left
        && this.right == that.right && this.top == that.top && this.bottom == that.bottom
        && this.powerStation == that.powerStation && this.powered == that.powered;
  }

  // checks if this GamePiece is connected to the piece in the given direction
  public boolean isConnectedTo(String direction) {
    if (this.neighbors.get(direction) != null) {
      if (direction.equals("top")) {
        return this.neighbors.get(direction).bottom && this.top;
      }
      if (direction.equals("bottom")) {
        return this.neighbors.get(direction).top && this.bottom;
      }
      if (direction.equals("left")) {
        return this.neighbors.get(direction).right && this.left;
      }
      if (direction.equals("right")) {
        return this.neighbors.get(direction).left && this.right;
      }
    }
    return false;
  }

  // sends power thru the neighbors if possible
  public void powerNeighbors(ArrayList<GamePiece> seen) {
    seen.add(this);
    ArrayList<String> directions = new ArrayList<String>(
        Arrays.asList("left", "right", "top", "bottom"));
    if (this.powered > 0) {
      int neighborPowerLevel = this.powered - 1;
      for (String dir : directions) { 
        if (this.isConnectedTo(dir) && !seen.contains(this.neighbors.get(dir))) {
          this.neighbors.get(dir).powered = neighborPowerLevel;
          this.neighbors.get(dir).powerNeighbors(seen);
        }
      }
    }
  }
}




//examples class
class ExamplesLightEmAll {

  // 2x2 GamePieces
  GamePiece twoGamePiece1;
  GamePiece twoGamePiece2;
  GamePiece twoGamePiece3;
  GamePiece twoGamePiece4;

  ArrayList<GamePiece> mt;
  GamePiece gamePiece1;
  GamePiece gamePiece2;
  GamePiece gamePiece3;
  GamePiece gamePiece4;
  GamePiece gamePiece5;
  GamePiece gamePiece6;
  GamePiece gamePiece7;
  GamePiece gamePiece8;
  GamePiece gamePiece9;
  ArrayList<GamePiece> row1;
  ArrayList<GamePiece> row2;
  ArrayList<GamePiece> row3;
  ArrayList<ArrayList<GamePiece>> grid1;
  ArrayList<GamePiece> grid1Nodes;
  LightEmAll game1;
  LightEmAll game2;

  // making a 3 x 5 grid
  GamePiece gamePiece01;
  GamePiece gamePiece02;
  GamePiece gamePiece03;
  GamePiece gamePiece04;
  GamePiece gamePiece05;
  GamePiece gamePiece06;
  GamePiece gamePiece07;
  GamePiece gamePiece08;
  GamePiece gamePiece09;
  GamePiece gamePiece10;
  GamePiece gamePiece11;
  GamePiece gamePiece12;
  GamePiece gamePiece13;
  GamePiece gamePiece14;
  GamePiece gamePiece15;
  ArrayList<GamePiece> row01;
  ArrayList<GamePiece> row02;
  ArrayList<GamePiece> row03;
  ArrayList<ArrayList<GamePiece>> grid3;
  ArrayList<GamePiece> grid3Nodes;
  LightEmAll game3;

  HashMap<String, GamePiece> gamePiece1Neighbors;
  HashMap<String, GamePiece> gamePiece2Neighbors;
  HashMap<String, GamePiece> gamePiece3Neighbors;
  HashMap<String, GamePiece> gamePiece4Neighbors;
  HashMap<String, GamePiece> gamePiece5Neighbors;
  HashMap<String, GamePiece> gamePiece6Neighbors;
  HashMap<String, GamePiece> gamePiece7Neighbors;
  HashMap<String, GamePiece> gamePiece8Neighbors;
  HashMap<String, GamePiece> gamePiece9Neighbors;

  HashMap<String, GamePiece> twoGamePiece1Neighbors;
  HashMap<String, GamePiece> twoGamePiece2Neighbors;
  HashMap<String, GamePiece> twoGamePiece3Neighbors;
  HashMap<String, GamePiece> twoGamePiece4Neighbors;

  LightEmAll twoByTwoBlank;
  LightEmAll twoByTwo;
  LightEmAll threeByThreeU;
  LightEmAll tenByTen;
  LightEmAll fourByFour;

  ArrayList<Edge> emptyEdges;

  public void initData() {
    this.mt = new ArrayList<GamePiece>();

    this.gamePiece1Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece2Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece3Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece4Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece5Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece6Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece7Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece8Neighbors = new HashMap<String, GamePiece>();
    this.gamePiece9Neighbors = new HashMap<String, GamePiece>();

    this.twoGamePiece1Neighbors = new HashMap<String, GamePiece>();
    this.twoGamePiece2Neighbors = new HashMap<String, GamePiece>();
    this.twoGamePiece3Neighbors = new HashMap<String, GamePiece>();
    this.twoGamePiece4Neighbors = new HashMap<String, GamePiece>();

    this.twoGamePiece1 = new GamePiece(0, 0, this.twoGamePiece1Neighbors, false, false, false, true,
        true, 3);
    this.twoGamePiece2 = new GamePiece(1, 0, this.twoGamePiece2Neighbors, false, true, true, false,
        false, 2);
    this.twoGamePiece3 = new GamePiece(1, 1, this.twoGamePiece3Neighbors, true, false, true, false,
        false, 1);
    this.twoGamePiece4 = new GamePiece(0, 1, this.twoGamePiece4Neighbors, false, false, false, true,
        false, 0);

    this.twoGamePiece1Neighbors.put("bottom", this.twoGamePiece2);
    this.twoGamePiece1Neighbors.put("right", this.twoGamePiece4);
    this.twoGamePiece2Neighbors.put("top", this.twoGamePiece1);
    this.twoGamePiece2Neighbors.put("right", this.twoGamePiece3);
    this.twoGamePiece3Neighbors.put("left", this.twoGamePiece2);
    this.twoGamePiece3Neighbors.put("top", this.twoGamePiece4);
    this.twoGamePiece4Neighbors.put("bottom", this.twoGamePiece3);
    this.twoGamePiece4Neighbors.put("left", this.twoGamePiece1);

    this.gamePiece1 = new GamePiece(0, 0, this.gamePiece1Neighbors, false, false, false, true, true,
        0);
    this.gamePiece2 = new GamePiece(1, 0, this.gamePiece2Neighbors, false, false, false, true,
        false, 0);
    this.gamePiece3 = new GamePiece(2, 0, this.gamePiece3Neighbors, false, false, false, true,
        false, 0);
    this.gamePiece4 = new GamePiece(0, 1, this.gamePiece4Neighbors, false, true, true, true, false,
        0);
    this.gamePiece5 = new GamePiece(1, 1, this.gamePiece5Neighbors, true, true, true, true, false,
        0);
    this.gamePiece6 = new GamePiece(2, 1, this.gamePiece6Neighbors, true, false, true, true, false,
        0);
    this.gamePiece7 = new GamePiece(0, 2, this.gamePiece7Neighbors, false, false, false, true,
        false, 0);
    this.gamePiece8 = new GamePiece(1, 2, this.gamePiece8Neighbors, false, false, true, false,
        false, 0);
    this.gamePiece9 = new GamePiece(2, 2, this.gamePiece9Neighbors, false, false, false, true,
        false, 0);

    this.gamePiece1Neighbors.put("right", gamePiece2);
    this.gamePiece1Neighbors.put("bottom", gamePiece4);
    this.gamePiece2Neighbors.put("left", gamePiece1);
    this.gamePiece2Neighbors.put("right", gamePiece3);
    this.gamePiece2Neighbors.put("bottom", gamePiece5);
    this.gamePiece3Neighbors.put("bottom", gamePiece6);
    this.gamePiece3Neighbors.put("left", gamePiece2);
    this.gamePiece4Neighbors.put("top", gamePiece1);
    this.gamePiece4Neighbors.put("bottom", gamePiece7);
    this.gamePiece4Neighbors.put("right", gamePiece5);
    this.gamePiece5Neighbors.put("left", gamePiece4);
    this.gamePiece5Neighbors.put("top", gamePiece2);
    this.gamePiece5Neighbors.put("right", gamePiece6);
    this.gamePiece5Neighbors.put("bottom", gamePiece8);
    this.gamePiece6Neighbors.put("top", gamePiece3);
    this.gamePiece6Neighbors.put("left", gamePiece5);
    this.gamePiece6Neighbors.put("bottom", gamePiece9);
    this.gamePiece7Neighbors.put("top", gamePiece4);
    this.gamePiece7Neighbors.put("right", gamePiece8);
    this.gamePiece8Neighbors.put("top", gamePiece5);
    this.gamePiece9Neighbors.put("top", gamePiece6);

    this.row1 = new ArrayList<GamePiece>();
    this.row1.add(gamePiece1);
    this.row1.add(gamePiece2);
    this.row1.add(gamePiece3);
    this.row2 = new ArrayList<GamePiece>();
    this.row2.add(gamePiece4);
    this.row2.add(gamePiece5);
    this.row2.add(gamePiece6);
    this.row3 = new ArrayList<GamePiece>();
    this.row3.add(gamePiece7);
    this.row3.add(gamePiece8);
    this.row3.add(gamePiece9);
    this.grid1 = new ArrayList<ArrayList<GamePiece>>();
    this.grid1.add(row1);
    this.grid1.add(row2);
    this.grid1.add(row3);
    this.grid1Nodes = new ArrayList<GamePiece>();
    this.grid1Nodes.add(this.gamePiece1);
    this.grid1Nodes.add(this.gamePiece2);
    this.grid1Nodes.add(this.gamePiece3);
    this.grid1Nodes.add(this.gamePiece4);
    this.grid1Nodes.add(this.gamePiece5);
    this.grid1Nodes.add(this.gamePiece6);
    this.grid1Nodes.add(this.gamePiece7);
    this.grid1Nodes.add(this.gamePiece8);
    this.grid1Nodes.add(this.gamePiece9);
    this.game1 = new LightEmAll(3, 3, -1, new Random(3));
    this.game2 = new LightEmAll(10, 10, -1);

    this.gamePiece01 = new GamePiece(0, 0, false, true, false, false);
    this.gamePiece02 = new GamePiece(1, 0, true, true, false, false);
    this.gamePiece03 = new GamePiece(2, 0, true, true, true, true);
    this.gamePiece04 = new GamePiece(3, 0, true, true, false, false);
    this.gamePiece05 = new GamePiece(4, 0, true, false, false, false);
    this.gamePiece06 = new GamePiece(0, 1, false, true, false, false);
    this.gamePiece07 = new GamePiece(1, 1, true, true, false, false);
    this.gamePiece08 = new GamePiece(2, 1, true, true, true, true, true);
    this.gamePiece09 = new GamePiece(3, 1, true, true, false, false);
    this.gamePiece10 = new GamePiece(4, 1, true, false, false, false);
    this.gamePiece11 = new GamePiece(0, 2, false, true, false, false);
    this.gamePiece12 = new GamePiece(1, 2, true, true, false, false);
    this.gamePiece13 = new GamePiece(2, 2, true, true, true, true);
    this.gamePiece14 = new GamePiece(3, 2, true, true, false, false);
    this.gamePiece15 = new GamePiece(4, 2, true, false, false, false);
    this.row01 = new ArrayList<GamePiece>();
    this.row01.add(gamePiece01);
    this.row01.add(gamePiece02);
    this.row01.add(gamePiece03);
    this.row01.add(gamePiece04);
    this.row01.add(gamePiece05);
    this.row02 = new ArrayList<GamePiece>();
    this.row02.add(gamePiece06);
    this.row02.add(gamePiece07);
    this.row02.add(gamePiece08);
    this.row02.add(gamePiece09);
    this.row02.add(gamePiece10);
    this.row03 = new ArrayList<GamePiece>();
    this.row03.add(gamePiece11);
    this.row03.add(gamePiece12);
    this.row03.add(gamePiece13);
    this.row03.add(gamePiece14);
    this.row03.add(gamePiece15);
    this.grid3 = new ArrayList<ArrayList<GamePiece>>();
    this.grid3.add(row01);
    this.grid3.add(row02);
    this.grid3.add(row03);
    this.grid3Nodes = new ArrayList<GamePiece>();
    this.grid3Nodes.add(this.gamePiece01);
    this.grid3Nodes.add(this.gamePiece02);
    this.grid3Nodes.add(this.gamePiece03);
    this.grid3Nodes.add(this.gamePiece04);
    this.grid3Nodes.add(this.gamePiece05);
    this.grid3Nodes.add(this.gamePiece06);
    this.grid3Nodes.add(this.gamePiece07);
    this.grid3Nodes.add(this.gamePiece08);
    this.grid3Nodes.add(this.gamePiece09);
    this.grid3Nodes.add(this.gamePiece10);
    this.grid3Nodes.add(this.gamePiece11);
    this.grid3Nodes.add(this.gamePiece12);
    this.grid3Nodes.add(this.gamePiece13);
    this.grid3Nodes.add(this.gamePiece14);
    this.grid3Nodes.add(this.gamePiece15);
    this.game3 = new LightEmAll(5, 3, 2);

    this.twoByTwoBlank = new LightEmAll(2, 2, -1);
    this.twoByTwo = new LightEmAll(2, 2, 2);
    this.threeByThreeU = new LightEmAll(3, 3, 2);
    this.tenByTen = new LightEmAll(10, 10, 1);
    this.fourByFour = new LightEmAll(4, 4, 1);
    this.emptyEdges = new ArrayList<Edge>();

  }

  // tests for drawPiece
  void testDrawPiece(Tester t) {
    // these tests are based off the coloring of a 10 radius board
    WorldImage base = new RectangleImage(LightEmAll.CELL_SIZE, LightEmAll.CELL_SIZE,
        OutlineMode.SOLID, Color.darkGray);
    initData();
    t.checkExpect(this.game1.board.get(0).get(0).drawPiece(this.game1.radius), new RotateImage(
        new RotateImage(new RotateImage(new RotateImage(base, 90.0), 90.0), 90.0), 90.0));
    t.checkExpect(this.threeByThreeU.board.get(2).get(0).drawPiece(this.game1.radius),
        new RotateImage(
            new RotateImage(
                new OverlayImage(
                    new RectangleImage(5, 20, OutlineMode.SOLID, new Color(128, 128, 128))
                    .movePinhole(0, 10),
                    new RotateImage(new RotateImage(base, 90.0), 90.0)),
                90.0),
            90.0));
    t.checkExpect(this.threeByThreeU.board.get(0).get(0).drawPiece(this.threeByThreeU.radius),
        new OverlayImage(
            new StarImage((LightEmAll.CELL_SIZE / 2.5), 8, 2, OutlineMode.SOLID, Color.ORANGE),
            new RotateImage(new RotateImage(
                new OverlayImage(
                    new RectangleImage(5, 20, OutlineMode.SOLID, new Color(255, 255, 0, 252))
                    .movePinhole(0, 10),
                    new RotateImage(new RotateImage(base, 90.0), 90.0)),
                90.0), 90.0)));
  }

  // tests for calcColor
  void testCalcColor(Tester t) {
    initData();
    t.checkExpect(this.game1.board.get(0).get(0).calcColor(this.game1.width), Color.gray);
    t.checkExpect(this.game3.board.get(0).get(0).calcColor(this.game3.width), Color.YELLOW);
    t.checkExpect(this.game3.board.get(2).get(0).calcColor(this.game3.width), Color.gray);
    t.checkExpect(this.twoByTwo.board.get(0).get(0).calcColor(3), Color.YELLOW);
    t.checkExpect(this.twoByTwo.board.get(1).get(0).calcColor(3), Color.gray);

  }

  // tests for updateNeighbor
  void testUpdateNeighbor(Tester t) {
    initData();
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("right"), null);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("top"), null);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("bottom"), null);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("left"), null);
    this.game1.nodes.get(0).updateNeighbor("right", this.gamePiece01);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("right"), this.gamePiece01);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("top"), null);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("bottom"), null);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("left"), null);
    this.game1.nodes.get(0).updateNeighbor("bottom", this.gamePiece02);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("right"), this.gamePiece01);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("top"), null);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("bottom"), this.gamePiece02);
    t.checkExpect(this.game1.nodes.get(0).neighbors.get("left"), null);
  }

  // tests for samePiece
  void testSamePiece(Tester t) {
    initData();
    t.checkExpect(this.gamePiece01.samePiece(this.gamePiece01), true);
    t.checkExpect(this.gamePiece01.samePiece(this.gamePiece02), false);
    t.checkExpect(this.gamePiece9.samePiece(this.gamePiece9), true);
    t.checkExpect(this.gamePiece9.samePiece(this.gamePiece10), false);
    t.checkExpect(this.gamePiece04.samePiece(this.gamePiece04), true);
  }

  // tests for isConnectedTo
  void testIsConnecteTo(Tester t) {
    initData();
    t.checkExpect(this.gamePiece1.isConnectedTo("top"), false);
    t.checkExpect(this.gamePiece1.isConnectedTo("bottom"), true);
    t.checkExpect(this.gamePiece1.isConnectedTo("left"), false);
    t.checkExpect(this.gamePiece1.isConnectedTo("right"), false);

    t.checkExpect(this.gamePiece3.isConnectedTo("top"), false);
    t.checkExpect(this.gamePiece3.isConnectedTo("bottom"), true);
    t.checkExpect(this.gamePiece3.isConnectedTo("left"), false);
    t.checkExpect(this.gamePiece3.isConnectedTo("right"), false);

    t.checkExpect(this.gamePiece5.isConnectedTo("top"), true);
    t.checkExpect(this.gamePiece5.isConnectedTo("bottom"), true);
    t.checkExpect(this.gamePiece5.isConnectedTo("left"), true);
    t.checkExpect(this.gamePiece5.isConnectedTo("right"), true);
  }

  // tests for rotatePiece
  void testRotatePiece(Tester t) {
    initData();
    t.checkExpect(this.gamePiece1.left, false);
    t.checkExpect(this.gamePiece1.top, false);
    t.checkExpect(this.gamePiece1.right, false);
    t.checkExpect(this.gamePiece1.bottom, true);

    this.gamePiece1.rotatePiece(1);

    t.checkExpect(this.gamePiece1.left, true);
    t.checkExpect(this.gamePiece1.top, false);
    t.checkExpect(this.gamePiece1.right, false);
    t.checkExpect(this.gamePiece1.bottom, false);

    t.checkExpect(this.gamePiece2.left, false);
    t.checkExpect(this.gamePiece2.top, false);
    t.checkExpect(this.gamePiece2.right, false);
    t.checkExpect(this.gamePiece2.bottom, true);

    this.gamePiece2.rotatePiece(1);

    t.checkExpect(this.gamePiece2.left, true);
    t.checkExpect(this.gamePiece2.top, false);
    t.checkExpect(this.gamePiece2.right, false);
    t.checkExpect(this.gamePiece2.bottom, false);

    t.checkExpect(this.gamePiece3.left, false);
    t.checkExpect(this.gamePiece3.top, false);
    t.checkExpect(this.gamePiece3.right, false);
    t.checkExpect(this.gamePiece3.bottom, true);

    this.gamePiece3.rotatePiece(-1);
    t.checkExpect(this.gamePiece3.left, false);
    t.checkExpect(this.gamePiece3.top, false);
    t.checkExpect(this.gamePiece3.right, true);
    t.checkExpect(this.gamePiece3.bottom, false);
  }

  // tests for powerNeighbors
  void testPowerNeighbors(Tester t) {
    initData();
    t.checkExpect(this.twoByTwo.board.get(0).get(0).powered, 3);
    t.checkExpect(this.twoByTwo.board.get(1).get(0).powered, 0);
    t.checkExpect(this.twoByTwo.board.get(0).get(1).powered, 2);

  }

  // tests for generateBoard
  void testGenerateBoard(Tester t) {
    initData();
    t.checkExpect(this.game1.generateBoard().size(), 3);
    t.checkExpect(this.game1.generateBoard().get(0).size(), 3);
    t.checkExpect(this.game2.generateBoard().size(), 10);
    t.checkExpect(this.game2.generateBoard().get(0).size(), 10);
  }

  // tests for grabAllNodes
  public void testGrabAllNodes(Tester t) {
    initData();
    t.checkExpect(this.game1.grabAllNodes().size(), 9);
    t.checkExpect(this.game1.nodes.size(), 9);
    t.checkExpect(this.game1.grabAllNodes().get(0), this.game1.board.get(0).get(0));
    t.checkExpect(this.game1.grabAllNodes().get(2), this.game1.board.get(0).get(2));
    t.checkExpect(this.game1.grabAllNodes().get(8), this.game1.board.get(2).get(2));
    t.checkExpect(this.game2.nodes.size(), 100);
  }

  // tests for manualConnections
  public void testManualConnections(Tester t) {
    initData();
    t.checkExpect(this.game1.board.get(0).get(0).right, false);
    t.checkExpect(this.game1.board.get(1).get(1).right, false);
    t.checkExpect(this.game1.board.get(2).get(0).right, false);

    this.game1.generateManualConnections();
    t.checkExpect(this.game1.board.get(0).get(0).right, true);
    t.checkExpect(this.game1.board.get(0).get(0).right, true);
    t.checkExpect(this.game1.board.get(1).get(1).right, true);
    t.checkExpect(this.game1.board.get(1).get(1).bottom, true);
    t.checkExpect(this.game1.board.get(1).get(1).top, true);
    t.checkExpect(this.game1.board.get(2).get(0).right, false);
    t.checkExpect(this.game1.board.get(2).get(0).left, true);

  }

  // tests for generateFractalConnections
  void testGenerateFractalConnections(Tester t) {
    initData();
    t.checkExpect(this.game1.board.get(0).get(0).bottom, false);
    t.checkExpect(this.game1.board.get(0).get(1).bottom, false);
    t.checkExpect(this.game1.board.get(0).get(1).top, false);
    t.checkExpect(this.game1.board.get(0).get(2).right, false);
    t.checkExpect(this.game1.board.get(0).get(2).top, false);
    this.game1.generateFractalConnections(new Posn(0, 0), this.game1.board);
    t.checkExpect(this.game1.board.get(0).get(0).bottom, true);
    t.checkExpect(this.game1.board.get(0).get(1).bottom, true);
    t.checkExpect(this.game1.board.get(0).get(1).top, true);
    t.checkExpect(this.game1.board.get(0).get(2).right, true);
    t.checkExpect(this.game1.board.get(0).get(2).top, true);

    t.checkExpect(this.game2.board.get(0).get(0).bottom, false);
    t.checkExpect(this.game2.board.get(0).get(1).bottom, false);
    t.checkExpect(this.game2.board.get(0).get(1).top, false);
    t.checkExpect(this.game2.board.get(0).get(2).right, false);
    t.checkExpect(this.game2.board.get(0).get(2).top, false);
    t.checkExpect(this.game2.board.get(0).get(2).right, false);
    t.checkExpect(this.game2.board.get(0).get(3).top, false);
    t.checkExpect(this.game2.board.get(0).get(3).bottom, false);
    t.checkExpect(this.game2.board.get(1).get(2).right, false);
    t.checkExpect(this.game2.board.get(1).get(2).left, false);
    t.checkExpect(this.game2.board.get(1).get(2).top, false);
    t.checkExpect(this.game2.board.get(3).get(2).right, false);
    t.checkExpect(this.game2.board.get(3).get(2).top, false);
    t.checkExpect(this.game2.board.get(4).get(9).top, false);
    t.checkExpect(this.game2.board.get(4).get(9).right, false);
    t.checkExpect(this.game2.board.get(4).get(9).left, false);
    t.checkExpect(this.game2.board.get(4).get(7).top, false);
    t.checkExpect(this.game2.board.get(4).get(7).left, false);
    t.checkExpect(this.game2.board.get(4).get(7).bottom, false);

    this.game2.generateFractalConnections(new Posn(0, 0), this.game2.board);
    t.checkExpect(this.game2.board.get(0).get(0).bottom, true);
    t.checkExpect(this.game2.board.get(0).get(1).bottom, true);
    t.checkExpect(this.game2.board.get(0).get(1).top, true);
    t.checkExpect(this.game2.board.get(0).get(2).right, true);
    t.checkExpect(this.game2.board.get(0).get(2).top, true);
    t.checkExpect(this.game2.board.get(0).get(2).right, true);
    t.checkExpect(this.game2.board.get(0).get(3).top, true);
    t.checkExpect(this.game2.board.get(0).get(3).bottom, true);
    t.checkExpect(this.game2.board.get(1).get(2).right, true);
    t.checkExpect(this.game2.board.get(1).get(2).left, true);
    t.checkExpect(this.game2.board.get(1).get(2).top, true);
    t.checkExpect(this.game2.board.get(3).get(2).right, true);
    t.checkExpect(this.game2.board.get(3).get(2).top, true);
    t.checkExpect(this.game2.board.get(4).get(9).top, true);
    t.checkExpect(this.game2.board.get(4).get(9).right, true);
    t.checkExpect(this.game2.board.get(4).get(9).left, true);
    t.checkExpect(this.game2.board.get(4).get(7).top, true);
    t.checkExpect(this.game2.board.get(4).get(7).left, true);
    t.checkExpect(this.game2.board.get(4).get(7).bottom, true);

  }

  // tests for splitBoard
  void testSplitBoard(Tester t) {
    initData();
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(4, 4, 2).board).size(), 4);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(4, 4, 2).board).get(0).size(), 2);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(4, 4, 2).board).get(0).get(0).size(), 2);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(4, 4, 2).board).get(1).get(0).size(), 2);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(4, 4, 2).board).get(2).get(0).size(), 2);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(4, 4, 2).board).get(3).get(0).size(), 2);
    t.checkExpect(this.game2.splitBoard(2, new LightEmAll(2, 4, 2).board).size(), 2);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).size(), 4);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(0).size(), 4);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(1).size(), 3);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(2).size(), 4);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(3).size(), 3);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(0).get(0).size(), 4);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(1).get(0).size(), 4);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(2).get(0).size(), 3);
    t.checkExpect(this.game2.splitBoard(1, new LightEmAll(7, 7, 2).board).get(3).get(0).size(), 3);

  }

  // tests for determineSplitType
  void testDetermineSplitType(Tester t) {
    initData();
    t.checkExpect(this.game2.determineSplitType(game2.board), 1);
    t.checkExpect(this.game2.determineSplitType(new LightEmAll(4, 4, 1).board), 1);
    t.checkExpect(this.game2.determineSplitType(new LightEmAll(3, 4, 1).board), 2);
    t.checkExpect(this.game2.determineSplitType(new LightEmAll(10, 2, 1).board), 3);
    t.checkExpect(this.game2.determineSplitType(new LightEmAll(2, 10, 1).board), 2);
    t.checkExpect(this.game2.determineSplitType(new LightEmAll(7, 7, 1).board), 1);
  }

  // tests for buildU
  void testBuildU(Tester t) {
    initData();
    t.checkExpect(this.game2.board.get(0).get(0).bottom, false);
    t.checkExpect(this.game2.board.get(9).get(0).bottom, false);
    t.checkExpect(this.game2.board.get(0).get(9).top, false);
    t.checkExpect(this.game2.board.get(9).get(9).top, false);
    t.checkExpect(this.game2.board.get(0).get(4).top, false);
    t.checkExpect(this.game2.board.get(9).get(4).top, false);
    t.checkExpect(this.game2.board.get(0).get(4).top, false);
    t.checkExpect(this.game2.board.get(9).get(4).top, false);
    this.game2.buildU(this.game2.board);
    t.checkExpect(this.game2.board.get(0).get(0).bottom, true);
    t.checkExpect(this.game2.board.get(9).get(0).bottom, true);
    t.checkExpect(this.game2.board.get(0).get(9).top, true);
    t.checkExpect(this.game2.board.get(9).get(9).top, true);
    t.checkExpect(this.game2.board.get(0).get(4).top, true);
    t.checkExpect(this.game2.board.get(9).get(4).top, true);
    t.checkExpect(this.game2.board.get(0).get(4).top, true);
    t.checkExpect(this.game2.board.get(9).get(4).top, true);
    initData();
    t.checkExpect(this.twoByTwoBlank.board.get(0).get(0).bottom, false);
    t.checkExpect(this.twoByTwoBlank.board.get(1).get(0).bottom, false);
    t.checkExpect(this.twoByTwoBlank.board.get(0).get(1).top, false);
    t.checkExpect(this.twoByTwoBlank.board.get(1).get(1).top, false);
    this.twoByTwoBlank.buildU(twoByTwoBlank.board);
    t.checkExpect(this.twoByTwoBlank.board.get(0).get(0).bottom, true);
    t.checkExpect(this.twoByTwoBlank.board.get(1).get(0).bottom, true);
    t.checkExpect(this.twoByTwoBlank.board.get(0).get(1).top, true);
    t.checkExpect(this.twoByTwoBlank.board.get(1).get(1).top, true);
  }

  // tests for onMouseClicked
  void testOnMouseClicked(Tester t) {
    initData();
    t.checkExpect(this.game3.board.get(0).get(0).top, false);
    t.checkExpect(this.game3.board.get(0).get(0).right, false);
    t.checkExpect(this.game3.board.get(0).get(0).bottom, true);
    t.checkExpect(this.game3.board.get(0).get(0).left, false);
    t.checkExpect(this.game3.score, 0);

    this.game3.onMouseClicked(new Posn(0, 0), "LeftButton");
    t.checkExpect(this.game3.board.get(0).get(0).top, false);
    t.checkExpect(this.game3.board.get(0).get(0).right, false);
    t.checkExpect(this.game3.board.get(0).get(0).bottom, false);
    t.checkExpect(this.game3.board.get(0).get(0).left, true);
    t.checkExpect(this.game3.score, 1);

    this.game3.onMouseClicked(new Posn(0, 0), "RightButton");
    t.checkExpect(this.game3.board.get(0).get(0).top, false);
    t.checkExpect(this.game3.board.get(0).get(0).right, false);
    t.checkExpect(this.game3.board.get(0).get(0).bottom, true);
    t.checkExpect(this.game3.board.get(0).get(0).left, false);
    t.checkExpect(this.game3.score, 2);

    this.game3.onMouseClicked(new Posn(0, 0), "RightButton");
    t.checkExpect(this.game3.board.get(0).get(0).top, false);
    t.checkExpect(this.game3.board.get(0).get(0).right, true);
    t.checkExpect(this.game3.board.get(0).get(0).bottom, false);
    t.checkExpect(this.game3.board.get(0).get(0).left, false);
    t.checkExpect(this.game3.score, 3);

  }

  // tests for updateAllNeighbors
  public void testUpdateAllNeighbors(Tester t) {
    initData();
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("top"), null);
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("bottom"), null);
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("left"), null);
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("right"), null);

    initData();
    this.game1.updateAllNeighbors();
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("top"), null);
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("left"), null);
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("right"),
        this.game1.board.get(1).get(0));
    t.checkExpect(this.game1.board.get(0).get(0).neighbors.get("bottom"),
        this.game1.board.get(0).get(1));

  }

  // tests for locatePiece
  void testLocatePiece(Tester t) {
    initData();
    t.checkExpect(this.game1.locatePiece(new Posn(0, 0)),
        new GamePiece(0, 0, false, false, false, false));
    t.checkExpect(this.game2.locatePiece(new Posn(0, 0)),
        new GamePiece(0, 0, false, false, false, false));
    t.checkExpect(this.game1.locatePiece(new Posn(1, 1)),
        new GamePiece(0, 0, false, false, false, false));
    t.checkExpect(this.game1.locatePiece(new Posn(0, 2)),
        new GamePiece(0, 0, false, false, false, false));
    t.checkExpect(this.game1.locatePiece(new Posn(1, 1)),
        new GamePiece(0, 0, false, false, false, false));
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(0, 0)), this.twoGamePiece1);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(0, 1)), this.twoGamePiece1);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(1, 0)), this.twoGamePiece1);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(1, 1)), this.twoGamePiece1);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(2, 2)), this.twoGamePiece1);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(1, 41)), this.twoGamePiece2);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(41, 41)), this.twoGamePiece3);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(41, 1)), this.twoGamePiece4);
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(0, 0)), this.twoByTwo.board.get(0).get(0));
    t.checkExpect(this.twoByTwo.locatePiece(new Posn(1, 1)), this.twoByTwo.board.get(0).get(0));
  }

  // tests for restartGame
  void testRestartGame(Tester t) {
    initData();
    this.game1.onMouseClicked(new Posn(1, 1), "LeftButton");
    t.checkExpect(this.game1.score, 1);
    t.checkExpect(this.game1.time, 0);
    this.game1.onTick();
    this.game1.onTick();
    this.game1.onTick();
    this.game1.onTick();
    t.checkExpect(this.game1.time, 4);
    this.game1.restartGame();
    t.checkExpect(this.game1.score, 0);
    t.checkExpect(this.game1.time, 0);

    this.game2.onMouseClicked(new Posn(1, 1), "RightButton");
    t.checkExpect(this.game2.score, 1);
    t.checkExpect(this.game2.time, 0);
    this.game2.onTick();
    t.checkExpect(this.game2.time, 1);
    this.game2.restartGame();
    t.checkExpect(this.game2.score, 0);
    t.checkExpect(this.game2.time, 0);
  }

  // tests for updatePower
  void testUpdatePower(Tester t) {
    initData();
    t.checkExpect(this.twoByTwo.board.get(1).get(0).powerStation, false);
    this.twoByTwo.updatePower(this.twoByTwo.board);
    t.checkExpect(this.twoByTwo.board.get(0).get(0).powerStation, true);

  }

  // tests for generateRandomGrid
  void testGenerateRandomGrid(Tester t) {
    initData();
    this.game1.generateFractalConnections(new Posn(0, 0), this.game1.board);
    t.checkExpect(this.game1.nodes.get(0).left, false);
    t.checkExpect(this.game1.nodes.get(0).top, false);
    t.checkExpect(this.game1.nodes.get(0).right, false);
    t.checkExpect(this.game1.nodes.get(0).bottom, true);
    t.checkExpect(this.game1.nodes.get(4).left, false);
    t.checkExpect(this.game1.nodes.get(4).top, true);
    t.checkExpect(this.game1.nodes.get(4).right, false);
    t.checkExpect(this.game1.nodes.get(4).bottom, true);
    t.checkExpect(this.game1.nodes.get(8).left, true);
    t.checkExpect(this.game1.nodes.get(8).top, true);
    t.checkExpect(this.game1.nodes.get(8).right, false);
    t.checkExpect(this.game1.nodes.get(8).bottom, false);
    t.checkExpect(this.game1.nodes.get(2).left, false);
    t.checkExpect(this.game1.nodes.get(2).top, true);
    t.checkExpect(this.game1.nodes.get(2).right, true);
    t.checkExpect(this.game1.nodes.get(2).bottom, false);
    t.checkExpect(this.game1.nodes.get(6).left, false);
    t.checkExpect(this.game1.nodes.get(6).top, false);
    t.checkExpect(this.game1.nodes.get(6).right, false);
    t.checkExpect(this.game1.nodes.get(6).bottom, true);
    this.game1.randomizeGrid(this.game1.nodes);

    t.checkExpect(this.game1.nodes.get(0).left, false);
    t.checkExpect(this.game1.nodes.get(0).top, false);
    t.checkExpect(this.game1.nodes.get(0).right, true);
    t.checkExpect(this.game1.nodes.get(0).bottom, false);
    t.checkExpect(this.game1.nodes.get(4).left, true);
    t.checkExpect(this.game1.nodes.get(4).top, false);
    t.checkExpect(this.game1.nodes.get(4).right, true);
    t.checkExpect(this.game1.nodes.get(4).bottom, false);
    t.checkExpect(this.game1.nodes.get(8).left, true);
    t.checkExpect(this.game1.nodes.get(8).top, true);
    t.checkExpect(this.game1.nodes.get(8).right, false);
    t.checkExpect(this.game1.nodes.get(8).bottom, false);
    t.checkExpect(this.game1.nodes.get(2).left, false);
    t.checkExpect(this.game1.nodes.get(2).top, true);
    t.checkExpect(this.game1.nodes.get(2).right, true);
    t.checkExpect(this.game1.nodes.get(2).bottom, false);
    t.checkExpect(this.game1.nodes.get(6).left, false);
    t.checkExpect(this.game1.nodes.get(6).top, false);
    t.checkExpect(this.game1.nodes.get(6).right, true);
    t.checkExpect(this.game1.nodes.get(6).bottom, false);

  }

  // tests for onKeyEvent
  void testOnKeyEvent(Tester t) {
    initData();
    t.checkExpect(this.twoByTwo.powerRow, 0);
    this.twoByTwo.onKeyEvent("down");
    t.checkExpect(this.twoByTwo.powerRow, 1);
    this.twoByTwo.onKeyEvent("right");
    t.checkExpect(this.twoByTwo.powerCol, 1);

    t.checkExpect(this.game3.powerRow, 0);
    this.game3.onKeyEvent("down");
    t.checkExpect(this.game3.powerRow, 1);
    this.game3.onKeyEvent("down");
    t.checkExpect(this.game3.powerRow, 2);
    this.game3.onKeyEvent("right");
    t.checkExpect(this.game3.powerCol, 1);
  }

  // tests for checkGameEnd
  void testCheckGameEnd(Tester t) {
    initData();
    t.checkExpect(this.game2.gameEnd, 0);
    this.game2.checkGameEnd(this.game2.nodes, this.game2.score, this.game2.time);
    t.checkExpect(this.game2.gameEnd, 0);

    initData();
    t.checkExpect(this.game2.gameEnd, 0);
    this.game2.time = this.game2.maxTime - 1;
    this.game2.checkGameEnd(this.game2.nodes, this.game2.score, this.game2.time);
    t.checkExpect(this.game2.gameEnd, 0);

    initData();
    t.checkExpect(this.game2.gameEnd, 0);
    this.game2.time = this.game2.maxTime;
    this.game2.checkGameEnd(this.game2.nodes, this.game2.score, this.game2.time);
    t.checkExpect(this.game2.gameEnd, -1);

    initData();
    t.checkExpect(this.game2.gameEnd, 0);
    this.game2.time = this.game2.maxTime + 1;
    this.game2.checkGameEnd(this.game2.nodes, this.game2.score, this.game2.time);
    t.checkExpect(this.game2.gameEnd, -1);

    initData();
    t.checkExpect(this.game2.gameEnd, 0);
    this.game2.score = this.game2.maxScore - 1;
    this.game2.checkGameEnd(this.game2.nodes, this.game2.score, this.game2.score);
    t.checkExpect(this.game2.gameEnd, 0);

    initData();
    t.checkExpect(this.game2.gameEnd, 0);
    this.game2.score = this.game2.maxScore;
    this.game2.checkGameEnd(this.game2.nodes, this.game2.score, this.game2.score);
    t.checkExpect(this.game2.gameEnd, -1);

    initData();
    t.checkExpect(this.game2.gameEnd, 0);
    this.game2.score = this.game2.maxScore + 1;
    this.game2.checkGameEnd(this.game2.nodes, this.game2.score, this.game2.score);
    t.checkExpect(this.game2.gameEnd, -1);

  }

  // tests for onTick
  void testOnTick(Tester t) {
    initData();
    t.checkExpect(this.game1.time, 0);
    this.game1.onTick();
    t.checkExpect(this.game1.time, 1);
    this.game1.onTick();
    t.checkExpect(this.game1.time, 2);
    initData();
    t.checkExpect(this.game1.gameEnd, 0);
    this.game1.time = 99999998;
    this.game1.onTick();
    t.checkExpect(this.game1.time, 99999999);
    t.checkExpect(this.game1.gameEnd, -1);
  }

  // tests for getFarthestNode
  void testGetFarthestNode(Tester t) {
    initData();
    t.checkExpect(this.threeByThreeU.getFarthestNode(this.threeByThreeU.nodes.get(0)),
        this.threeByThreeU.board.get(2).get(0));
    t.checkExpect(this.fourByFour.getFarthestNode(this.fourByFour.board.get(0).get(0)),
        this.fourByFour.board.get(0).get(3));
    t.checkExpect(this.fourByFour.getFarthestNode(this.fourByFour.board.get(1).get(0)),
        this.fourByFour.board.get(0).get(3));
  }

  // tests for calcDiameter
  void testCalcDiameter(Tester t) {
    initData();
    t.checkExpect(this.threeByThreeU.calcDiameter(), 7);
    t.checkExpect(this.fourByFour.calcDiameter(), 8);
    t.checkExpect(this.tenByTen.calcDiameter(), 20);
  }

  // tests for generateDistanceMap
  void testGenerateDistanceMap(Tester t) {
    initData();
    t.checkExpect(this.threeByThreeU.generateDistanceMap(this.threeByThreeU.nodes.get(0))
        .get(this.threeByThreeU.nodes.get(0)), 0);
    t.checkExpect(this.tenByTen.generateDistanceMap(this.tenByTen.nodes.get(0))
        .get(this.tenByTen.board.get(0).get(2)), 12);
    t.checkExpect(this.tenByTen.generateDistanceMap(this.tenByTen.nodes.get(0))
        .get(this.tenByTen.board.get(9).get(0)), 9);
    t.checkExpect(this.threeByThreeU.generateDistanceMap(this.threeByThreeU.nodes.get(0))
        .get(this.threeByThreeU.board.get(0).get(0)), 0);
    t.checkExpect(this.threeByThreeU.generateDistanceMap(this.threeByThreeU.nodes.get(0))
        .get(this.threeByThreeU.board.get(0).get(2)), 2);
    t.checkExpect(this.threeByThreeU.generateDistanceMap(this.threeByThreeU.nodes.get(0))
        .get(this.threeByThreeU.board.get(2).get(0)), 6);
  }

  // tests for worldEnds
  void testWorldEnds(Tester t) {
    initData();
    int middleX = (int) (this.game1.width * LightEmAll.CELL_SIZE) / 2;
    int middleY = (int) (this.game1.height * LightEmAll.CELL_SIZE) / 2;
    WorldScene winScene = this.game1.getEmptyScene();
    winScene.placeImageXY(new TextImage("You Win!", LightEmAll.CELL_SIZE, Color.GREEN), middleX,
        middleY);
    WorldScene loseScene = this.game1.getEmptyScene();
    loseScene.placeImageXY(new TextImage("You Lose!", LightEmAll.CELL_SIZE, Color.GREEN), middleX,
        middleY);
    t.checkExpect(this.game1.worldEnds(), new WorldEnd(false, this.game1.makeScene()));
    this.game1.gameEnd = 1;
    t.checkExpect(this.game1.worldEnds(), new WorldEnd(true, winScene));
    initData();
    t.checkExpect(this.game1.worldEnds(), new WorldEnd(false, this.game1.makeScene()));
    this.game1.gameEnd = -1;
    t.checkExpect(this.game1.worldEnds(), new WorldEnd(true, loseScene));
  }

  // tests for generateAllPossibleEdges
  void testGenerateAllPossibleEdges(Tester t) {
    initData();
    t.checkExpect(this.game1.generateAllPossibleEdges(this.game1.board).size(), 12);
    t.checkExpect(this.twoByTwo.generateAllPossibleEdges(this.twoByTwo.board).size(), 4);
  }

  // tests for generateAllPossibleEdges
  void testEdgeS(Tester t) {
    initData();
    this.emptyEdges = this.game1.generateAllPossibleEdges(this.game1.board);
    t.checkExpect(emptyEdges.get(0).weight, 77);
    t.checkExpect(emptyEdges.get(2).weight, 181);
    t.checkExpect(emptyEdges.get(4).weight, 176);
    Collections.sort(this.emptyEdges, new SortByWeight());
    t.checkExpect(emptyEdges.get(0).weight, 6);
    t.checkExpect(emptyEdges.get(1).weight, 14);
    t.checkExpect(emptyEdges.get(2).weight, 77);
    t.checkExpect(emptyEdges.get(3).weight, 86);
    t.checkExpect(emptyEdges.get(4).weight, 92);
  }

  // tests for initRepresentative
  void testInitRepresentative(Tester t) {
    initData();
    t.checkExpect(this.game1.initRepresentative(this.game1.nodes).get(this.game1.nodes.get(0)),
        this.game1.nodes.get(0));
    t.checkExpect(this.game1.initRepresentative(this.game1.nodes).get(this.game1.nodes.get(2)),
        this.game1.nodes.get(2));
    t.checkExpect(this.game1.initRepresentative(this.game1.nodes).get(this.game1.nodes.get(8)),
        this.game1.nodes.get(8));
    t.checkExpect(this.game1.initRepresentative(this.game1.nodes).get(this.game1.nodes.get(1)),
        this.game1.nodes.get(1));

  }

  // tests for generateEdgeConnections
  void testGenerateEdgeConnections(Tester t) {
    initData();
    t.checkExpect(this.game1.board.get(0).get(0).right, false);
    t.checkExpect(this.game1.board.get(0).get(1).left, false);
    t.checkExpect(this.game1.board.get(0).get(0).bottom, false);
    this.game1.generateEdgeConnections();
    t.checkExpect(this.game1.board.get(0).get(0).right, true);
    t.checkExpect(this.game1.board.get(0).get(1).left, false);
    t.checkExpect(this.game1.board.get(0).get(0).bottom, true);
  }

  // tests for bigBang, will render the game
  void testBigBang(Tester t) {
    initData();
    LightEmAll game = new LightEmAll(10, 10, 3);
    game.bigBang(game.width * LightEmAll.CELL_SIZE,
        (game.height * LightEmAll.CELL_SIZE) + (LightEmAll.CELL_SIZE * 2), 0.25);
  }

}




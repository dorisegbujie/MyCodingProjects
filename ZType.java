import tester.*;                
import javalib.worldimages.*;   
import javalib.funworld.*;     
import java.awt.Color;          
import java.util.Random;



// interface for constants in the ZType game
interface IZType {
  //represents the height of the game screen
  int height = 600;

  //represents the width of the game screen
  int width = 600;

  // letters for making random strings
  String alphabet = "abcdefghijklmnopqrstuvwxyz";

  // number of ticks before adding a new word
  int tickCount = 30;

  //how far a word moves per tick
  int distance = 5;

}


// Represents a ZType game world
class ZTypeWorld extends World implements IZType {

  ILoWord words;
  Random rand;
  int tc;
  boolean isGameOver;

  // The constructor for use in "real" games
  ZTypeWorld(ILoWord words) {
    this(new Random(), words, IZType.tickCount, false);
  }

  // The constructor for use in testing, with a specified Random object
  ZTypeWorld(Random rand, ILoWord words, int tc, boolean isGameOver) {
    this.rand = rand;
    this.words = words;
    this.tc = tc;
    this.isGameOver = isGameOver;
  }

  /* TEMPLATE:
   * fields:
   * ... this.words ...           -- ILoWord
   * ... this.rand ...            -- Random
   * ... this.tc ...              -- int
   * ... this.isGameOver ...      -- boolean
   * 
   * methods:
   * ... this.makeScene() ...     -- WorldScene
   * ... this.onTick() ...        -- ZTypeWorld
   * ... this.onKeyEvent() ...    -- ZTypeWorld
   * 
   * methods for fields:
   * ... this.words.draw(WorldScene) ...       -- WorldScene
   * ... this.words.pressed(String) ...        -- ILoWord
   * ... this.words.checkAndReduce(String, boolean) ... -- ILoWord
   * ... this.anyActive() ...                  -- boolean
   * ... this.filterOutEmpties() ...           -- ILoWord
   * ... this.moveWord() ...                       -- ILoWord
   * ... this.anyGameOver() ...                -- boolean
   */

  // Draws the current state of the world
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(IZType.width, IZType.height);
    return this.words.draw(scene).placeImageXY(new RectangleImage(200, 
        50, OutlineMode.OUTLINE, Color.pink), 380, 30);
  }


  //Override onTick method to handle periodic addition of new words 
  public ZTypeWorld onTick() {
    return new ZTypeWorld(this.rand, 
        this.words.moveWord(), 
        this.tc + 1,
        this.words.gameOver());
  }


  // Override onKeyEvent method to handle key presses
  public ZTypeWorld onKeyEvent(String key) {
    return new ZTypeWorld(this.rand, 
        this.words.pressHelp(key), 
        this.tc,
        this.words.gameOver() || !this.words.activeChecker());
  }

  //add purpose statement
  public WorldEnd worldEnds() {
    if (this.isGameOver) {
      return new WorldEnd(true, new WorldScene(600, 600)
          .placeImageXY(new TextImage("Game Over", 
              30, Color.red), 140, 140));
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

}

//represents a list of words
interface ILoWord {

  //takes in a WorldScene and draws all of the words in this ILoWord onto the given WorldScene
  WorldScene draw(WorldScene scene); 

  //helper method for when key is pressed
  ILoWord pressHelp(String key);

  // gets rid of the first letter of active word thats starts with key 
  ILoWord checkAndReduce(String key, boolean w); 

  // checks if there is an active word
  boolean activeChecker();

  // filters out words in list that are empty strings
  ILoWord filterOutEmpties();

  // changes the position of words in an ILoWord
  ILoWord moveWord();

  // checks if the game is over
  boolean gameOver();

  // adds a new word to the ILoWord
  ILoWord addNewWord(String word, Random rand);

}

//represents an empty list of words
class MtLoWord implements ILoWord {

  /* TEMPLATE:
   * 
   * methods:
   * ... this.draw(WorldScene) ...          -- WorldScene
   * ... this.pressHelp(String) ...           -- ILoWord
   * ... this.checkAndReduce(String, boolean) ...    -- ILoWord
   * ... this.activeChecker() ...               -- boolean
   * ... this.filterOutEmpties() ...        -- ILoWord
   * ... this.moveWord() ...                    -- ILoWord
   * ... this.gameOver() ...             -- boolean
   * ... this.updateScore() ...             -- int
   * ... this.addNewWord(String Random) ...    -- ILoWord
   */

  //takes in a WorldScene and draws all of the words in this ILoWord onto the given WorldScene
  public WorldScene draw(WorldScene scene) {
    //TEMPLATE: same thing as class template
    return scene;
  }

  //helper method for when key is pressed
  public ILoWord pressHelp(String key) {
    return this;
  }

  //gets rid of the first letter of active word thats starts with key
  public ILoWord checkAndReduce(String key, boolean w) {
    return this;
  }

  //checks if there is an active word
  public boolean activeChecker() {
    return false;
  }

  //filters out words in list that are empty strings
  public ILoWord filterOutEmpties() {
    return this;
  }

  // changes the position of words in an ILoWord
  public ILoWord moveWord() {
    return this;
  }

  // checks if the game is over
  public boolean gameOver() {
    return false;
  }

  //adds a new word to the ILoWord
  public ILoWord addNewWord(String word, Random rand) {
    return new ConsLoWord(
        new InactiveWord(word, rand.nextInt(500) + 60, 60, word.length()),
        this);
  }

}

//Represents a non-empty list of words

class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  /* TEMPLATE:
   * 
   * fields:
   * ... this.first ...       -- IWord
   * ... this.rest ...        -- ILoWord
   * 
   * methods:
   * ... this.draw(WorldScene) ...          -- WorldScene
   * ... this.pressHelp(String) ...           -- ILoWord
   * ... this.checkAndReduce(String, boolean) ...    -- ILoWord
   * ... this.activeChecker() ...               -- boolean
   * ... this.filterOutEmpties() ...        -- ILoWord
   * ... this.moveWord() ...                    -- ILoWord
   * ... this.gameOver() ...             -- boolean
   * ... this.addNewWord(String Random) ...    -- ILoWord
   * 
   * methods for fields:
   * ... this.rest.draw(WorldScene) ...         -- WorldScene
   * ... this.rest.pressHelp(String) ...          -- ILoWord
   * ... this.rest.checkAndReduce(String, boolean) ...   -- ILoWord
   * ... this.rest.findAndReduce(String) ...    -- ILoWord
   * ... this.rest.activeChecker() ...              -- boolean
   * ... this.rest.filterOutEmpties() ...       -- ILoWord
   * ... this.rest.moveWord() ...                   -- ILoWord
   * ... this.rest.gameOver() ...            -- boolean
   * ... this.rest.addNewWord(String Random) ...   -- ILoWord
   * 
   * ... this.first.draw(WorldScene) ...        -- WorldScene
   * ... this.first.isActive() ...              -- boolean
   * ... this.first.checkAndReduce(String) ...  -- IWord
   * ... this.first.isEmpty() ...               -- boolean
   * ... this.first.moveWord() ...                  -- IWord
   * ... this.first.isGameOver() ...            -- boolean
   */

  //takes in a WorldScene and draws all of the words in this ILoWord onto the given WorldScene
  public WorldScene draw(WorldScene scene) {
    //TEMPLATE: same thing as class template
    return this.rest.draw(this.first.draw(scene));
  }

  public ILoWord pressHelp(String key) {
    return this.checkAndReduce(key, !this.activeChecker());
  }

  // gets rid of the first letter of active word thats starts with key
  public ILoWord checkAndReduce(String key, boolean w) {
    if (w) {
      return new ConsLoWord(this.first.checkAndReduce(key, w),
          this.rest.checkAndReduce(key, !this.first.checkAndReduce(key, w).isActive()));
    } else {
      return new ConsLoWord(this.first.checkAndReduce(key, false),
          this.rest.checkAndReduce(key, false));
    }
  }

  // checks if any words in this ILoWord are active
  public boolean activeChecker() {
    return this.first.isActive() || this.rest.activeChecker();
  }

  // filters out empty string
  public ILoWord filterOutEmpties() {
    if (this.first.isEmpty()) {
      return this.rest.filterOutEmpties();
    }
    else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // changes the position of words in this ILoWord
  public ILoWord moveWord() {
    return new ConsLoWord(this.first.moveWord(), this.rest.moveWord()); 
  }

  // checks if the game is over
  public boolean gameOver() {
    return this.first.isGameOver() || this.rest.gameOver();
  }

  // adds new word to end of this ILoWord
  public ILoWord addNewWord(String word, Random rand) {
    return new ConsLoWord(this.first, 
        this.rest.addNewWord(word, rand));
  }

}

//represents a word in the ZType game
interface IWord {

  //takes in a WorldScene and draws a word onto the given WorldScene
  WorldScene draw(WorldScene scene);

  //determines if this IWord is active
  boolean isActive();

  //finds the first word in the list that starts with the given string and makes it active
  IWord checkAndReduce(String str, boolean w); 

  // determines if this IWord contains an empty string
  boolean isEmpty();

  // changes the position of this IWord
  IWord moveWord();

  // determines if this IWord is at the end of the game
  boolean isGameOver();

}

//represents an active word in the ZType game

class ActiveWord implements IWord {
  String word;
  int x;
  int y;
  int length;

  ActiveWord(String word, int x, int y, int length) {
    this.word = word;
    this.x = x;
    this.y = y;
    this.length = length;
  }

  /* TEMPLATE:
   * 
   * fields:
   * ... this.word ...       -- String
   * ... this.x ...          -- int
   * ... this.y ...          -- int
   * ... this.length ...     -- int
   * 
   * methods:
   * ... this.draw(WorldScene) ...              -- WorldScene
   * ... this.isActive() ...                    -- boolean
   * ... this.checkAndReduce(String, boolean)   -- IWord
   * ... this.isEmpty() ...                     -- boolean
   * ... this.moveWord() ...                        -- IWord
   * ... this.isGameOver() ...                  -- boolean
   */

  //takes in a WorldScene and draws an active word onto the given WorldScene 
  public WorldScene draw(WorldScene scene) {
    return scene.placeImageXY(new TextImage(this.word, 15, Color.red), this.x, this.y);
  }

  //determines if this IWord is active
  public boolean isActive() {
    return true;
  }

  //determines if this IWord contains an empty string
  public boolean isEmpty() {
    return this.word.equals("");
  }

  // finds the first word in the list that starts with the given string and makes it active
  public IWord checkAndReduce(String str, boolean w) {
    if (this.word.toLowerCase().startsWith(str.toLowerCase())) {
      return new ActiveWord(word.substring(1), this.x, this.y, this.length);
    }
    else {
      return this;
    }
  }

  //changes the position of this IWord
  public IWord moveWord() {
    return new ActiveWord(this.word, this.x, this.y + IZType.distance, this.length);
  }

  //determines if this IWord is at the end of the game
  public boolean isGameOver() {
    return this.y >= IZType.height;
  }

}


//represents an inactive word in the ZType game

class InactiveWord implements IWord {
  String word;
  int x;
  int y;
  int length;


  InactiveWord(String word, int x, int y, int length) {
    this.word = word;
    this.x = x;
    this.y = y;
    this.length = length;
  }

  /* TEMPLATE:
   * 
   * fields:
   * ... this.word ...       -- String
   * ... this.x ...          -- int
   * ... this.y ...          -- int
   * ... this.length ...     -- int
   * 
   * methods:
   * ... this.draw(WorldScene) ...     -- WorldScene
   * ... this.isActive() ...           -- boolean
   * ... this.checkAndReduce() ...     -- IWord
   * ... this.isEmpty() ...            -- boolean
   * ... this.updatePos() ...          -- IWord
   * ... this.isGameOver() ...         -- boolean
   */

  //takes in a WorldScene and draws an inactive word onto the given WorldScene **
  public WorldScene draw(WorldScene scene) {
    return scene.placeImageXY(new TextImage(this.word, 15, Color.black), this.x, this.y);
  }

  //determines if this IWord is active
  public boolean isActive() {
    return false;
  }

  //determines if this IWord contains an empty string
  public boolean isEmpty() {
    return this.word.equals("");
  }

  //finds the first word in the list that starts with the given string and makes it active
  public IWord checkAndReduce(String str, boolean w) {
    if (w && this.word.toLowerCase().startsWith(str.toLowerCase())) {
      return new ActiveWord(word.substring(1), this.x, this.y, this.length);
    }
    else {
      return this;
    }
  }

  //changes the position of this IWord
  public IWord moveWord() {
    return new InactiveWord(this.word, this.x, this.y + IZType.distance, this.length);
  }

  //determines if this IWord is at the end of the game
  public boolean isGameOver() {
    return this.y >= IZType.height;
  }

}

// Utils class for generating random words

class Utils {
  // Produce a String of six alphabetic characters
  String generateRandomWord(Random rand) {
    return generateRandomWordHelp(rand, 
        IZType.alphabet.substring(rand.nextInt(26)).substring(0, 1));
  }

  // Helper function for generateRandomWord using an accumulator
  String generateRandomWordHelp(Random rand, String s) {
    if (s.length() == 6) {
      return s;
    } 
    else {
      return generateRandomWordHelp(rand,
          s + IZType.alphabet.substring(rand.nextInt(26)).substring(0, 1));
    }
  }
}



// Examples and tests for ZTypeWorld
class ExamplesZType {

  IWord sunset = new InactiveWord("sunset", 50, -30, 8);
  IWord happy = new InactiveWord("happy", 90, -30, 5);
  IWord tiger = new InactiveWord("tiger", 10, -30, 4);
  IWord reveal = new InactiveWord("reveal", 70, -30, 8);
  IWord chair1 = new InactiveWord("chair", 40, -30, 4);
  IWord music = new ActiveWord("music", 80, -30, 6);
  IWord chair2 = new ActiveWord("chair", 20, -30, 4);
  IWord mtWord = new ActiveWord("", 60, -30, 6);
  IWord bye = new InactiveWord("bye", 50, 90, 5);
  IWord movedSunset = new InactiveWord("sunset", 50, -27, 8);
  IWord movedMusic = new ActiveWord("music", 80, -27, 6);

  ILoWord mt = new MtLoWord();
  ILoWord mt2 = new ConsLoWord(new InactiveWord("chair", new Random(54321).nextInt(90) + 5,
      5, 4), this.mt);
  ILoWord list1 = new ConsLoWord(this.sunset,
      new ConsLoWord(this.happy,
          new ConsLoWord(this.tiger, this.mt)));
  ILoWord movedList1 = new ConsLoWord(new InactiveWord("sunset", 50, -27, 8),
      new ConsLoWord(new InactiveWord("happy", 90, -27, 5),
          new ConsLoWord(new InactiveWord("tiger", 10, -27, 4), this.mt)));
  ILoWord pressedList1 = new ConsLoWord(this.sunset,
      new ConsLoWord(this.happy,
          new ConsLoWord(new ActiveWord("iger", 10, -30, 4), this.mt)));
  ILoWord addedList1 = new ConsLoWord(this.sunset,
      new ConsLoWord(this.happy,
          new ConsLoWord(this.tiger,
              new ConsLoWord(new InactiveWord("chair", new Random(54321).nextInt(400) + 50,
                  50, 4), this.mt))));
  ILoWord removeList1 = new ConsLoWord(new InactiveWord("sunset", 50, 91, 8),
      new ConsLoWord(this.happy,
          new ConsLoWord(this.tiger, this.mt)));
  ILoWord list2 = new ConsLoWord(this.reveal,
      new ConsLoWord(this.music,
          new ConsLoWord(this.chair1, mt)));
  ILoWord movedList2 = new ConsLoWord(new InactiveWord("reveal", 70, -27, 8),
      new ConsLoWord(new ActiveWord("music", 80, -27, 6),
          new ConsLoWord(new InactiveWord("chair", 40, -27, 4), this.mt)));
  ILoWord movedList2Add = new ConsLoWord(new InactiveWord("reveal", 70, -27, 8),
      new ConsLoWord(new ActiveWord("music", 80, -27, 6),
          new ConsLoWord(new InactiveWord("chair", 40, -27, 4),
              new ConsLoWord(new InactiveWord("pumko", 13, 50, 4), this.mt))));
  ILoWord mt3 = new ConsLoWord(this.mtWord,
      new ConsLoWord(this.sunset,
          new ConsLoWord(this.happy,
              new ConsLoWord(this.tiger, this.mt))));

  ILoWord mt4 = new ConsLoWord(this.mtWord,
      new ConsLoWord(this.sunset,
          new ConsLoWord(this.mtWord,
              new ConsLoWord(this.happy,
                  new ConsLoWord(this.tiger, this.mt)))));

  ZTypeWorld world1 = new ZTypeWorld(new Random(54321), this.mt, IZType.tickCount, false);
  ZTypeWorld world2 = new ZTypeWorld(new Random(54321), this.list1, IZType.tickCount, false);
  ZTypeWorld world2PressL = new ZTypeWorld(new Random(54321), this.pressedList1, IZType.tickCount, 
      false);

  ZTypeWorld world3 = new ZTypeWorld(new Random(54321), this.list2, 0, true);
  ZTypeWorld world4 = new ZTypeWorld(new Random(54321), this.list2, 0, true);
  ZTypeWorld world5 = new ZTypeWorld(new Random(54321), this.list1, IZType.tickCount, false);

  WorldScene expected1 = new WorldScene(IZType.width, IZType.height)
      .placeImageXY(new TextImage("Score: 0", 20, Color.blue), 60, 20)
      .placeImageXY(new TextImage("Level 1", 20, Color.blue), 50, 20);
  WorldScene expected2 = new WorldScene(IZType.width, IZType.height)
      .placeImageXY(new TextImage("Score: 0", 20, Color.blue), 60, 20)
      .placeImageXY(new TextImage("Level 1", 20, Color.blue), 50, 20)
      .placeImageXY(new TextImage("sunset", 14, Color.black), 50, -30)
      .placeImageXY(new TextImage("happy", 14, Color.black), 90, -30)
      .placeImageXY(new TextImage("tiger", 14, Color.black), 10, -30);
  WorldScene expected3 = new WorldScene(IZType.width, IZType.height)
      .placeImageXY(new TextImage("Score: 0", 20, Color.blue), 60, 20)
      .placeImageXY(new TextImage("Level 1", 20, Color.blue), 250, 20)
      .placeImageXY(new TextImage("reveal", 14, Color.black), 70, -30)
      .placeImageXY(new TextImage("music", 14, Color.red), 80, -30)
      .placeImageXY(new TextImage("chair", 14, Color.black), 40, -30);
  WorldScene gameOver1 = new WorldScene(100, 100)
      .placeImageXY(new TextImage("Game Over",
          20, Color.red), 50, 50)
      .placeImageXY(new TextImage("Score: 0",
          14, Color.blue), 50, 50);
  WorldScene winner1 = new WorldScene(100, 100)
      .placeImageXY(new TextImage("Congrats, You Won!",
          20, Color.red), 50, 50)
      .placeImageXY(new TextImage("Score: 10",
          14, Color.blue), 60, 60);

  Utils utils = new Utils();
  Random rand = new Random();

  WorldScene expectedScene1 = new WorldScene(IZType.width, IZType.height)
      .placeImageXY(new TextImage("active1", 14, Color.red), 20, -20)
      .placeImageXY(new TextImage("inactive1", 14, Color.black), 40, -40);
  WorldScene expectedScene2 = new WorldScene(IZType.width, IZType.height)
      .placeImageXY(new TextImage("active2", 14, Color.red), 30, -30)
      .placeImageXY(new TextImage("inactive2", 14, Color.black), 50, -50);
  WorldScene expectedScene3 = new WorldScene(IZType.width, IZType.height)
      .placeImageXY(new TextImage("active1", 14, Color.red), 20, -20);

  // Test ZTypeWorld methods

  boolean testMakeScene(Tester t) {
    return t.checkExpect(world1.makeScene(), expected1) 
        &&
        t.checkExpect(world2.makeScene(), expected2);
  }

  boolean testOnTick(Tester t) {
    return t.checkExpect(world1.onTick(), new ZTypeWorld(new Random(), new MtLoWord(), 1, false)) 
        &&
        t.checkExpect(world2.onTick(), new ZTypeWorld(new Random(), new ConsLoWord(
            new InactiveWord("sunset", 50, -27, 8),
            new ConsLoWord(new InactiveWord("happy", 90, -27, 5),
                new ConsLoWord(new InactiveWord("tiger", 10, -27, 4), new MtLoWord()))), 1, false));
  }

  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(world1.onKeyEvent("l"), world1) 
        &&
        t.checkExpect(world2.onKeyEvent("i"), new ZTypeWorld(new Random(54321), new ConsLoWord(
            new InactiveWord("sunset", 50, -30, 8),
            new ConsLoWord(new InactiveWord("happy", 90, -30, 5),
                new ConsLoWord(new ActiveWord("iger", 10, -30, 4), new MtLoWord()))), 0, false));
  }

  boolean testWorldEnds(Tester t) {
    return t.checkExpect(world1.worldEnds(), new WorldEnd(false, expected1)) 
        &&
        t.checkExpect(new ZTypeWorld(new Random(54321), new MtLoWord(), 0, true).worldEnds(),
            new WorldEnd(true, gameOver1)) &&
        t.checkExpect(new ZTypeWorld(new Random(54321), new MtLoWord(), 0, false).worldEnds(),
            new WorldEnd(false, expected1));
  }

  // Test ILoWord methods

  boolean testDraw(Tester t) {
    return t.checkExpect(mt.draw(expectedScene1), expectedScene1) 
        &&
        t.checkExpect(list1.draw(expectedScene2), expectedScene2);
  }

  boolean testPressHelp(Tester t) {
    return t.checkExpect(mt.pressHelp("i"), mt) 
        &&
        t.checkExpect(list1.pressHelp("i"), pressedList1);
  }

  boolean testCheckAndReduce(Tester t) {
    return t.checkExpect(mt.checkAndReduce("i", false), mt) 
        &&
        t.checkExpect(list1.checkAndReduce("i", true), pressedList1);
  }

  boolean testActiveChecker(Tester t) {
    return t.checkExpect(mt.activeChecker(), false) 
        &&
        t.checkExpect(list1.activeChecker(), false);
  }

  boolean testFilterOutEmpties(Tester t) {
    return t.checkExpect(mt.filterOutEmpties(), mt) 
        &&
        t.checkExpect(addedList1.filterOutEmpties(), addedList1);
  }

  boolean testMoveWord(Tester t) {
    return t.checkExpect(mt.moveWord(), mt)
        &&
        t.checkExpect(list1.moveWord(), new ConsLoWord(new InactiveWord("sunset", 50, -25, 8),
            new ConsLoWord(new InactiveWord("happy", 90, -25, 5),
                new ConsLoWord(new InactiveWord("tiger", 10, -25, 4), new MtLoWord()))));
  }

  boolean testGameOver(Tester t) {
    return t.checkExpect(mt.gameOver(), false) 
        &&
        t.checkExpect(list1.gameOver(), false);
  }

  boolean testAddNewWord(Tester t) {
    return t.checkExpect(mt.addNewWord("chair", new Random(54321)),
        new ConsLoWord(new InactiveWord("chair", 5, 5, 4), mt))
        &&
        t.checkExpect(list1.addNewWord("chair", new Random(54321)), addedList1);

  }

  // Test IWord methods

  boolean testDrawIWord(Tester t) {
    return t.checkExpect(sunset.draw(expectedScene1), expectedScene1)
        &&
        t.checkExpect(chair1.draw(expectedScene1), expectedScene1);
  }

  boolean testIsActive(Tester t) {
    return t.checkExpect(sunset.isActive(), false) 
        &&
        t.checkExpect(music.isActive(), true);
  }

  boolean testCheckAndReduceIWord(Tester t) {
    return t.checkExpect(sunset.checkAndReduce("s", false), new InactiveWord("unset", 51, -30, 7))
        &&
        t.checkExpect(music.checkAndReduce("m", true), mtWord);
  }

  boolean testIsEmpty(Tester t) {
    return t.checkExpect(sunset.isEmpty(), false) 
        &&
        t.checkExpect(mtWord.isEmpty(), true);
  }

  boolean testMoveWordIWord(Tester t) {
    return t.checkExpect(sunset.moveWord(), new InactiveWord("sunset", 50, -27, 8)) 
        &&
        t.checkExpect(music.moveWord(), new ActiveWord("music", 80, -27, 6));
  }

  boolean testIsGameOver(Tester t) {
    return t.checkExpect(sunset.isGameOver(), false)
        &&
        t.checkExpect(mtWord.isGameOver(), false);
  }


  //Test for generateRandomWord method
  boolean testGenerateRandomWord(Tester t) {
    return t.checkExpect(utils.generateRandomWord(rand).length(), 6)
        && t.checkExpect(utils.generateRandomWord(rand).length(), 6)
        && t.checkExpect(utils.generateRandomWord(rand).length(), 6);
  }

  // Test for generateRandomWordHelp method
  boolean testGenerateRandomWordHelp(Tester t) {
    return t.checkExpect(utils.generateRandomWordHelp(rand, "a").length(), 6)
        && t.checkExpect(utils.generateRandomWordHelp(rand, "b").length(), 6)
        && t.checkExpect(utils.generateRandomWordHelp(rand, "c").length(), 6);
  }

  // big bang
  boolean testBigBang(Tester t) {
    return world2.bigBang(IZType.width, IZType.height, 0.1);
  }

}

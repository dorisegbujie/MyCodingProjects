# Stock Market Application - Design Overview

## Overview
A detailed overview of the design of the Stock Market Application. The application
is structured using the Model-View-Controller (MVC) pattern.

### Design Changes for Part 3

### 1. GUI Controller Addition
- **Modification:** Introduced `GUIController` to handle GUI-based user interactions.
- **Justification:** This change was necessary to provide a graphical user interface for the
application, making it more user-friendly and interactive.

### 2. GUI View Implementation
- **Modification:** Added `GUIView` class to implement the GUI for the application.
- **Justification:** This allows the application to present data and interact with users through a
 graphical interface, improving user experience and accessibility.

### 3. GUI View Interface
- **Modification:** Created `ViewGUI` interface to define methods for GUI views.
- **Justification:** This interface ensures a consistent contract for GUI view implementations,
 promoting modularity and enabling easy updates or changes to the GUI components without affecting
  other parts of the application.

### 4. New GUI View Methods
- **Modification:** Implemented methods in `GUIView` to support buying and selling stocks, displaying
 portfolio composition, saving and loading portfolios, and handling date selection.
- **Justification:** These methods are essential for enabling the full functionality of the application
 within the GUI, ensuring that users can perform all required operations through the graphical interface.

### 5. Integration of GUI Controller and Text-based Controller
- **Modification:** Modified `StockProgram` to integrate both the `StockController` and `GUIController`.
- **Justification:** This allows the application to switch between text-based and GUI-based interfaces
 based on user preferences or command-line arguments, providing flexibility in how users interact with
  the application.

### 6. Enhanced Portfolio Management in GUI
- **Modification:** Added methods in `GUIController` to handle portfolio creation, stock transactions,
 and portfolio composition retrieval through GUI actions.
- **Justification:** These enhancements ensure that all portfolio management features are accessible
 and manageable through the GUI, maintaining feature parity with the text-based interface.

### 7. Improved Error Handling in GUI
- **Modification:** Incorporated error handling in `GUIController` and `GUIView` to manage invalid
 inputs and exceptions gracefully.
- **Justification:** Enhanced error handling improves the robustness and reliability of the application,
 providing users with clear feedback and maintaining application stability.


## Part 2 Design Changes
### 1. Controller Interface Update
- **Modification:** Added `IOException` to the `control` method signature.
- **Justification:** This change was necessary to handle file operations within the controller, such
 as loading and saving portfolios. By including `IOException`, the controller can manage potential
  input/output errors that may occur during these operations, ensuring robust error handling.

### 2. New Features in StockController
- **Modification:** the completeMethod() was changed to be less complex by
removing the methods for each case in the switch statement and creating individual methods for each
- **Justification:** This was done to simplify the completeMethod() method and to make the program
easier to understand

### 3. Portfolio Interface Update
- **Modification:** Added methods: `buyStock`, `sellStock`, `getComposition`, `getValueDistribution`,
 `saveToFile`, `loadFromFile`, `rebalance`, `getStock`, `isBalanced`, and `getDate`. The addStock
 method was made private so that a user can no longer add a stock and now must buy them.
- **Justification:** These methods provide detailed operations for portfolio management, supporting
 new requirements for buying/selling shares, persisting portfolios, and rebalancing.
 They ensure that the Portfolio interface is comprehensive and aligned with the
 application's functionality, facilitating a wide range of portfolio operations.

### 4. PortfolioImpl Class Update
- **Modification:** Implemented new methods from the updated `Portfolio` interface and modified
 existing methods to accommodate new functionalities such as handling transactions and calculating
  value distributions. Additionally, the quant field was changed to accept doubles instead of
  integers so that a user can have a fractional amount of shares.
- **Justification:** The `PortfolioImpl` class needed to be expanded to manage the more complex
 portfolio operations and transactions required by the new features. This ensures that the
  implementation can handle all specified operations efficiently, supporting robust portfolio management.

### 5. StockModel Interface and Implementation Update
- **Modification:** Moved `lookUpStock` method and csv method to a class indicating
that these methods specifically work for the AlphaVantage API.
- **Justification:** This update improves the efficiency of stock data retrieval by caching results,
 reducing redundant API calls, and enhancing performance. It ensures that stock data is retrieved
  promptly and minimizes the load on external APIs. Additionally, creating an interface to
  represent a variety of APIs and moving this method to a specific class will allow the program
  to support a variety of APIs.

### 7. View Interface Update
- **Modification:** A macro was created so that the `formatPortfolioPerformance` method could be
added to the view without modifying the view directly.
- **Justification:** This method is needed for displaying portfolio performance over time in a
 formatted text-based bar chart, enhancing the user's ability to visualize data. It provides a clear
  and concise way to present portfolio performance information to the user.

## Additions
- An additional stockController class was added that extends the StockController class and supports
the new portfolio methods that have been added
- Additionally, a macro was created for the view to represent a text based view of the performance
of a stock as a bar chart as well as another class extending the view being added to
include a text view of the options that have been added

## Design Components

### 1. Model
The Model component handles all data-related logic, representing stock entities and managing
 collections of stocks. It is responsible for data manipulation and business logic.

#### Interfaces and Classes:
- **Stock** (Interface)
  - Represents a stock entity.
  - Methods: `double getPriceAtClose()`, `int getTotalShares()`, `String getTicker()`
  - **Justification:** The `Stock` interface defines the basic attributes and behaviors of a stock,
   providing a standard structure for stock objects.

- **StockImpl** (Implements Stock)
  - Concrete implementation of the Stock interface.
  - Methods: `double getPriceAtClose()`, `int getTotalShares()`, `String getTicker()`
  - **Justification:** The `StockImpl` class provides the actual implementation of the `Stock`
   interface, encapsulating the details of stock properties and behaviors.

- **Portfolio** (Interface)
  - Manages collections of stocks and their operations.
  - Methods: `double calcVal(String date)`,
  `HashMap<String, Integer> portfolioPerformance(String startDate, String endDate)`,
  `void buyStock(String name, HashMap<String, Stock> stocks, double quantity, String date)`,
  `void sellStock(String name, HashMap<String, Stock> stocks, double quantity, String date)`,
  `HashMap<String, Double> getComposition(String date)`,
  `HashMap<String, Double> getValueDistribution(String date)`,
  `void saveToFile(String filename)`, `void loadFromFile(String filename)`,
  `void rebalance(String filename, HashMap<String, Double> targetDistribution, String date)`,
  `List<String> getStock()`, `HashMap<String, Stock> getDate(String date)`
  - **Justification:** The `Portfolio` interface defines the operations required for
  managing a collection of stocks, ensuring that portfolios can be manipulated in a consistent
  and predictable manner.

- **PortfolioImpl** (Implements Portfolio)
  - Concrete implementation of the Portfolio interface.
  - Manages stock transactions and portfolio calculations.
  - Methods: `double calcVal(String date)`,
  `HashMap<String, Integer> portfolioPerformance(String startDate,
  String endDate)`, `void buyStock(String name, HashMap<String, Stock> stocks, double quantity,
  String date)`, `void sellStock(String name, HashMap<String, Stock> stocks, double quantity,
  String date)`, `HashMap<String, Double> getComposition(String date)`, `HashMap<String, Double>
  getValueDistribution(String date)`, `void saveToFile(String filename)`,
  `void loadFromFile(String filename)`, `void rebalance(String filename,
  HashMap<String, Double> targetDistribution, String date)`,
  Double> targetDistribution, String date)`, `HashMap<String, Stock> getDate(String date)`
  - **Justification:** The `PortfolioImpl` class provides the concrete implementation
  for portfolio operations, enabling detailed management of stock collections, transactions,
  and portfolio performance calculations.

- **APICall** (Interface)
  - Define a method to make a call to a specified API
  - Method: `HashMap<String, Stock> lookUpStock(String name)`
  - **Justification:** Defines an opration to be used to look up a specified stock
  using a variety of APIs.

- **AlphaVantAPI** (Implements APICall)
- Concrete implementation of the APICall interface
- Calls the AlphaVantage API and looks up a specified stock
- Methods: `HashMap<String, Stock> lookUpStock(String name)`,
`HashMap<String, Stock> csv(File file)`
- **Justification:** Looks up a specified stock using the AlphaVantage API.

- **StockModel** (Interface)
  - Defines methods for stock data operations.
  - Methods: `double gainOrLoss(HashMap<String, Stock> stock, String startDate, String endDate)`,
  `double xDayMovingAverage(HashMap<String, Stock> stock, String date, int x)`, `ArrayList<String>
  crossovers(HashMap<String, Stock> stock, String startDate, String endDate, int x)`,
  - **Justification:** The `StockModel` interface defines the operations required for
  handling stock data, ensuring that data retrieval and manipulation are performed consistently.

- **StockModelImpl** (Implements StockModel)
  - Concrete implementation of the StockModel interface.
  - Handles stock data retrieval and manipulation.
  - Methods: `double gainOrLoss(HashMap<String, Stock> stock, String startDate,
  String endDate)`, `double xDayMovingAverage(HashMap<String, Stock> stock,
  String date, int x)`, `ArrayList<String> crossovers(HashMap<String, Stock>
  stock, String startDate, String endDate, int x)`
  - **Justification:** The `StockModelImpl` class provides the concrete implementation
  for stock data operations, ensuring efficient and accurate data retrieval and manipulation.

### 2. View
The View component is responsible for displaying stock information, portfolio values,
and user interactions. It provides the user interface and presentation logic.

#### Interfaces and Classes:
- **View** (Interface)
  - Defines methods for displaying data to the user.
  - Methods: `void welcomeMessage()`, `void writeMessage(String message)`,
  `void farewellMessage()`, `void printMenu()`,
  - **Justification:** The `View` interface defines the contract for
  presenting data to the user, ensuring a consistent user interface across different
  implementations.

- **ViewGUI** (Interface) (Extends View, ItemListener, ListSelectionListener)
  - Defines methods for displaying data to the user through a GUI.
  - Methods: `String getRadioDisplayText()`, `String getDate()`, `String saveFileGetText()`,
  `String savePortGetText()`, `String portfolioNameComboGetText()`, `String pNameGetText()`,
  `String tGetText()`, `String t1GetText()`, `void checkPortfolio(ActionEvent arg0)`,
  `void checkBuySell(ActionEvent arg0)`, `void checkComposition(ActionEvent arg0)`,
  `void checkSaveLoadFile(ActionEvent arg0)`, `void portfoliosSetNames(String name)`,
  `void setListener(ActionListener listener)`, `void addItemToPortfolioNames(String name)`
  - **Justification:** The `View` interface defines the contract for
   presenting data to the user, ensuring a consistent user interface across different
   implementations.

- **StocksView** (Implements View)
  - Concrete implementation of the View interface.
  - Handles the presentation logic for the application.
  - Methods: `void welcomeMessage()`, `void writeMessage(String message)`,
  `void farewellMessage()`, `void printMenu()`,
  - **Justification:** The `StocksView` class provides the actual implementation for
  displaying data, ensuring that user interactions are presented clearly and effectively.

- **ViewMacro** (Interface)
  - Provides a method to execute a macro on a view.
  - Methods 'void executeMacro(View v)'
  - **Justification:** Allows the view to accept new methods.

- **ViewWithMacro** (Interface)
  - Executes a method on a ViewMacro.
  - Method: 'void execute(ViewMacro v)'
  - **Justification:** Allows the view to accept new methods.

- **ViewWithMacroImpl** (Implements ViewWithMacro)
  - Implements the method to allow the view to accept a new method.
  - Method: 'void execute(ViewMacro v)'
  - **Justification:** Allows the view to accept new methods.

- **MacroBarChartView** (Implements ViewMacro)
  - Provides a new method to produce a bar chart based on the performance
  of a portfolio.
  - Methods: ` String[] yearMonthDateFormat(String[] keyArray, String pattern)`,
  `void executeMacro(View v)`
  - **Justification:** Allows the view to produce a bar chart.

- **MacroStocksView** (Extends StocksView)
  - Provides an updated menu.
  - Method: `void printMenu()`
  - **Justification:** Supports the new methods, providing an updated
  menu of possible methods the user can call.

- **GUIView** (Extends JFrame) (Implements ViewGUI)
  - Provides a GUI view of the available methods to the user.
  - Method: `void setUpPanelsAfterListener()`, `void setUpMainPanel()`,
  `JPanel setUpPanels(JPanel tPanel)`, `void setUpRadioDisplay()`, `void createPortfolioNames()`,
  `void handleBuySellStock(String text)`, `void savePortfolio(String text)`,
  `JButton buttonCreator(String text, JButton b, JPanel p)`,
  `JLabel createTextFields(JPanel panel, JLabel label, JTextField text, String labelText)`,
  `JComboBox createDate(String[] options, JComboBox combo, JLabel label, JLabel label1)`
  - **Justification:** Provides methods to create the GUI such as text fields, comoboboxes,
  and buttons. Provides methods to perform on portfolios.

### 3. Controller
The Controller component handles user inputs, performs necessary calculations,
and interacts with the Model to retrieve or update data. It coordinates the flow of
information between the Model and View.

#### Interfaces and Classes:
- **Controller** (Interface)
  - Defines the control method for managing user interactions.
  - Methods: `void control() throws IOException`
  - **Justification:** The `Controller` interface defines the contract for
  managing user inputs and coordinating actions between the Model and View,
  ensuring a consistent control flow.

- **StockController** (Implements Controller)
  - Concrete implementation of the Controller interface.
  - Manages user interactions and coordinates between the Model and View.
  - Methods: `void control() throws IOException`, `void completeMethod(Scanner sc,
  HashMap<String, Stock> stocks, String userInstruction)`,
  `String formatMonthDayYear(Scanner sc)` `boolean checkDateFormat(String date)`,
  `void handleGainOrLoss(Scanner sc, HashMap<String, Portfolio> portfolios)`,
  `void handleXDayMovingAvg(Scanner sc, HashMap<String, Portfolio> portfolios)`,
  `void handleCrossovers(Scanner sc, HashMap<String, Portfolio> portfolios)`,
  `void handleCreatePortfolio(Scanner sc, HashMap<String, Portfolio> portfolios)`,
  `void handleCalcPortfolioVal(Scanner sc, HashMap<String, Portfolio> portfolios)`,
  - **Justification:** The `StockController` class provides the concrete implementation
  for managing user interactions, ensuring that all user actions are processed correctly
  and that the appropriate data is retrieved or updated.

- **MacroStockController** (Extends StockController)
  - Adds new functionality to the control, supporting
  new methods that can be performed on a portfolio.
  - Methods: void completeMethod(Scanner sc, HashMap<String, Stock> stocks, String userInstruction)`
  `void handleGetComposition(Scanner sc, HashMap<String, Portfolio> portfolios)`,
    `void handleGetValueDistribution(Scanner sc, HashMap<String, Portfolio> portfolios)`,
    `void handleSavePortfolio(Scanner sc, HashMap<String, Portfolio> portfolios)`,
    `void handleLoadPortfolio(Scanner sc, HashMap<String, Portfolio> portfolios)`,
    `void handleRebalancePortfolio(Scanner sc, HashMap<String, Portfolio> portfolios)`,
    `void handleCreateBarChart(Scanner sc, HashMap<String, Portfolio> portfolios)`,
    `String date(String choice, Scanner sc, String date)`, `String formatMonthYear(Scanner sc)`,
    `String dateFormat(Scanner sc)`, `boolean checkDateFormatForPortfolioPerformance(String date)`,
    `Portfolio addPortfolio(String name, Map<String, Portfolio> portfolios)`,
    `void handleBuyShares(Scanner sc, HashMap<String, Portfolio> portfolios)`,
    `void handleSellShares(Scanner sc, HashMap<String, Portfolio> portfolios)`,
  - **Justification:** The `MacroStockController` class provides the concrete implementation
       for managing user interactions, ensuring that all user actions are processed correctly
       and that the appropriate data is retrieved or updated.

- **GUIController** (Implements ActionListener, Controller)
  - Adds the ability to create a GUI and delegates to the GUI view and the portfolio methods.
  - Methods: `void handleSavePortfolio(String text)`, `void handleGetComposition()`,
  `void handleCreatePortfolio()`, `void performBuySellStock(String text) throws ParseException`
  - **Justification:** Provides the implementation for user interactions, ensuring that all user
  interactions are processed correctly and that the appropriate data is retrieved or updated.



- **StockProgram**
  - Contains the main method to start the application.
  - Methods: `void main(String[] args) throws IOException`
  - **Justification:** The `StockProgram` class serves as the entry point for the application,
  initializing the necessary components and starting the control flow.
package stocks.view;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import javax.swing.event.ListSelectionEvent;

/**
 * Represents a GUI view for the stock program.
 * Supports creating a new portfolio, buying and selling stocks,
 * getting the value and composition of the portfolio, and saving and
 * retrieving portfolios to and from files
 */
public class GUIView extends JFrame implements ViewGUI {
  private JPanel mainPanel;
  private JPanel setDate;
  private JButton submitBuySell;
  private JPanel createPort;
  private JPanel other;

  private JLabel radioDisplay;

  private JLabel l;
  private JLabel l1;
  private JLabel j;
  private JLabel j1;
  private JLabel k;
  private JLabel k1;
  private JComboBox c1;
  private JComboBox c2;
  private JComboBox c3;
  private int setDateExists;
  private JLabel quant;
  private JLabel quant1;
  private JLabel portName;
  private JScrollPane setDateScroll;

  private JList<String> listOfStrings;
  private JList<Integer> listOfIntegers;

  private JTextField t;
  private JTextField t1;

  private String month;
  private String day;
  private String year;

  private int createP;
  private JButton submitPortfolio;
  private JTextField pName;
  private JButton submitComp;
  private JPanel compositionPanel;
  int buySell;
  int compVal;

  private JTextField savePort;
  private JTextField saveFile;
  private JPanel savePortfolioPanel;
  int saveLP;
  private JButton saveButton;
  private ActionListener a;

  private JLabel portfolios;
  private JComboBox portfolioNameCombo;
  private JLabel another;
  private int newPortfolio;

  /**
   * Creates an instance of the GUI view.
   * Initializes variables to be used to identify if components have already been created
   */
  public GUIView() {
    super();

    setTitle("Stock Program");
    setSize(400, 400);
    setDateExists = 0;
    createP = 0;
    buySell = 0;
    compVal = 0;
    saveLP = 0;
    newPortfolio = 0;
  }

  @Override
  public void setListener(ActionListener listener) {
    a = listener;
    setUpPanelsAfterListener();
  }

  /**
   * Sets up all the panels after the listener is created.
   */
  private void setUpPanelsAfterListener() {
    setUpMainPanel();
    setUpRadioDisplay();
    createPort = setUpPanels(createPort);
    setDate = setUpPanels(setDate);
    other = setUpPanels(other);
    compositionPanel = setUpPanels(compositionPanel);
    savePortfolioPanel = setUpPanels(savePortfolioPanel);
    createPort = setUpPanels(createPort);
    createPortfolioNames();
  }

  /**
   * Sets up the panel for the main panel.
   */
  private void setUpMainPanel() {
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);
  }

  /**
   * Sets up the panel for the main panel.
   */
  private JPanel setUpPanels(JPanel tPanel) {
    tPanel = new JPanel();
    tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.PAGE_AXIS));

    mainPanel.add(tPanel);
    tPanel.setVisible(false);
    return tPanel;
  }

  /**
   * Sets up the radio display buttons for the user to select a portfolio method.
   */
  private void setUpRadioDisplay() {
    JPanel radioPanel = new JPanel();
    radioPanel.setBorder(BorderFactory.createTitledBorder(
            "Stocks Program"));

    radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.PAGE_AXIS));

    radioPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT); // have no effect

    JRadioButton[] radioButtons = new JRadioButton[7];

    ButtonGroup rGroup1 = new ButtonGroup();
    radioButtons[0] = new JRadioButton("Create a new portfolio");
    radioButtons[1] = new JRadioButton("Buy a stock");
    radioButtons[2] = new JRadioButton("Sell a stock");
    radioButtons[3] = new JRadioButton("Get the composition of the portfolio");
    radioButtons[4] = new JRadioButton("Get the value of a portfolio");
    radioButtons[5] = new JRadioButton("Save a portfolio");
    radioButtons[6] = new JRadioButton("Retrieve a portfolio");

    for (int i = 0; i < radioButtons.length; i++) {

      radioButtons[i].setActionCommand("RB" + (i + 1));
      radioButtons[i].addActionListener(a);
      rGroup1.add(radioButtons[i]);
      radioPanel.add(radioButtons[i]);

    }

    welcomeMessage();

    radioPanel.add(radioDisplay);
    mainPanel.add(radioPanel);
  }

  /**
   * Creates a label with the existing portfolios.
   */
  private void createPortfolioNames() {
    portfolios = new JLabel("<html> <br/> Existing Portfolios </html>");
    portfolios.setFont(new Font("Serif", Font.PLAIN, 17));
    portfolios.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    mainPanel.add(portfolios);
  }

  @Override
  public void portfoliosSetNames(String name) {
    if (portfolios.getText().contains("</html>")) {
      String tempPName = portfolios.getText().replace("</html>", "");
      portfolios.setText(tempPName + "<br/>" + name + "</html>");
    } else {
      portfolios.setText("<html> " + portfolios.getText() + "<br/>" + name + "</html>");
    }
  }

  @Override
  public void checkBuySell(ActionEvent actionE) {
    if (actionE.getActionCommand().equals("RB2")) {
      radioDisplay.setText("Buy a stock was selected");
    } else if (actionE.getActionCommand().equals("RB3")) {
      radioDisplay.setText("Sell a stock was selected");
    }
    if (buySell == 0 && setDateExists == 0
            && (actionE.getActionCommand().equals("RB2")
            || actionE.getActionCommand().equals("RB3"))) {
      if (actionE.getActionCommand().equals("RB2")) {
        handleBuySellStock("Buy stock was selected");
      } else if (actionE.getActionCommand().equals("RB3")) {
        handleBuySellStock("Sell stock was selected");
      }
      setDate.setVisible(true);
      other.setVisible(true);
    } else if (buySell == 0 && setDateExists == 1
            && (actionE.getActionCommand().equals("RB2")
            || actionE.getActionCommand().equals("RB3"))) {
      t = new JTextField();
      quant = createTextFields(other, quant, t, "Enter amount of stock ");
      t1 = new JTextField();
      quant1 = createTextFields(other, quant1, t1, "Enter stock ticker ");
      if (actionE.getActionCommand().equals("RB2")) {
        submitBuySell = buttonCreator("Submit Information to Buy Stock", submitBuySell, other);
      } else if (actionE.getActionCommand().equals("RB3")) {
        submitBuySell = buttonCreator("Submit Information to Sell Stock", submitBuySell, other);
      }
      setDate.setVisible(true);
      other.setVisible(true);
      buySell = 1;
    } else if (buySell == 1 && setDateExists == 1 && (actionE.getActionCommand().equals("RB2")
            || actionE.getActionCommand().equals("RB3"))) {
      if (actionE.getActionCommand().equals("RB2")) {
        submitBuySell.setText("Submit Information to Buy Stock");
        submitBuySell.setActionCommand("Submit Information to Buy Stock");
      } else if (actionE.getActionCommand().equals("RB3")) {
        submitBuySell.setText("Submit Information to Sell Stock");
        submitBuySell.setActionCommand("Submit Information to Sell Stock");
      }
      setDate.setVisible(true);
      other.setVisible(true);
    } else if (!actionE.getActionCommand().equals("RB2")
            && !actionE.getActionCommand().equals("RB3")) {
      other.setVisible(false);
    }
  }

  @Override
  public void checkComposition(ActionEvent actionE) {
    if (!actionE.getActionCommand().equals("RB4")
            && !actionE.getActionCommand().equals("RB5")) {
      compositionPanel.setVisible(false);
    }
    if (actionE.getActionCommand().equals("RB4")) {
      radioDisplay.setText("Get composition was selected");
    } else if (actionE.getActionCommand().equals("RB5")) {
      radioDisplay.setText("Get value was selected");
    }
    if ((actionE.getActionCommand().equals("RB4")
            || actionE.getActionCommand().equals("RB5"))
            && setDateExists == 0 && compVal == 0) {
      String[] options = {"January", "February", "March", "April", "May", "June", "July",
                          "August", "September", "November", "October", "December"};
      l = new JLabel("Select the month ");
      l1 = new JLabel("");
      c1 = createDate(options, c1, l, l1);
      String[] dayOptions = new String[31];
      for (int i = 0; i < 31; i++) {
        int d = i + 1;
        dayOptions[i] = "" + d;
      }
      j = new JLabel("Select the day ");
      j1 = new JLabel("");
      c2 = createDate(dayOptions, c2, j, j1);
      String[] yearOptions = new String[10];
      int y = 2015;
      for (int i = 0; i < yearOptions.length; i++) {
        yearOptions[i] = "" + y;
        y += 1;
      }
      k = new JLabel("Select the year ");
      k1 = new JLabel("");
      c3 = createDate(yearOptions, c3, k, k1);
      submitComp = buttonCreator("Submit Date", submitComp, compositionPanel);
      setDate.setVisible(true);
      compositionPanel.setVisible(true);
      setDateExists = 1;
      compVal = 1;
    } else if ((actionE.getActionCommand().equals("RB4")
            || actionE.getActionCommand().equals("RB5"))
            && setDateExists == 1 && compVal == 0) {
      submitComp = buttonCreator("Submit Date", submitComp, compositionPanel);
      setDate.setVisible(true);
      compositionPanel.setVisible(true);
      compVal = 1;
    } else if ((actionE.getActionCommand().equals("RB4")
            || actionE.getActionCommand().equals("RB5")) && setDateExists == 1 && compVal == 1) {
      setDate.setVisible(true);
      compositionPanel.setVisible(true);
    }
    if (compVal == 1 && (!actionE.getActionCommand().equals("RB4")
            && !actionE.getActionCommand().equals("RB5"))) {
      compositionPanel.setVisible(false);
    }
  }

  @Override
  public void checkPortfolio(ActionEvent actionE) {
    if (setDateExists == 1 && !actionE.getActionCommand().equals("RB2")
            && !actionE.getActionCommand().equals("RB3")
            && !actionE.getActionCommand().equals("RB4")
            && !actionE.getActionCommand().equals("RB5")) {
      setDate.setVisible(false);
    }
    if (actionE.getActionCommand().equals("RB1")) {
      radioDisplay.setText("Create portfolio was selected");
    }
    if (createP == 1 && !actionE.getActionCommand().equals("RB1")) {
      createPort.setVisible(false);
    }
    if (createP == 0 && actionE.getActionCommand().equals("RB1")) {
      pName = new JTextField();
      portName = createTextFields(createPort, portName, pName, "Enter portfolio name ");
      submitPortfolio = buttonCreator("Submit Portfolio", submitPortfolio, createPort);
      createPort.setVisible(true);
      createP = 1;
    } else if (createP == 1 && actionE.getActionCommand().equals("RB1")) {
      createPort.setVisible(true);
    }
  }

  @Override
  public void checkSaveLoadFile(ActionEvent actionE) {
    if (saveLP == 0 && (actionE.getActionCommand().equals("RB6")
            || actionE.getActionCommand().equals("RB7"))) {
      if (actionE.getActionCommand().equals("RB6")) {
        radioDisplay.setText("Save portfolio was selected");
        savePortfolio("Save Portfolio");
      } else if (actionE.getActionCommand().equals("RB7")) {
        radioDisplay.setText("Load portfolio was selected");
        savePortfolio("Load Portfolio");
      }
      savePortfolioPanel.setVisible(true);
      saveLP = 1;
    }
    if (saveLP == 1 && (actionE.getActionCommand().equals("RB6")
            || actionE.getActionCommand().equals("RB7"))) {
      if (actionE.getActionCommand().equals("RB6")) {
        radioDisplay.setText("Save portfolio was selected");
        saveButton.setText("Save Portfolio");
        saveButton.setActionCommand("Save Portfolio");
      } else if (actionE.getActionCommand().equals("RB7")) {
        radioDisplay.setText("Load portfolio was selected");
        saveButton.setText("Load Portfolio");
        saveButton.setActionCommand("Load Portfolio");
      }
      savePortfolioPanel.setVisible(true);
    }
    if (saveLP == 1 && !actionE.getActionCommand().equals("RB6")
            && !actionE.getActionCommand().equals("RB7")) {
      savePortfolioPanel.setVisible(false);
    }
  }

  @Override
  public void itemStateChanged(ItemEvent itemE) {
    Object e = itemE.getSource();
    if (e == c1) {
      l1.setText(c1.getSelectedItem() + " selected");
      String[] monthsWith31Days = {"January", "March", "May", "July", "August",
                                   "October", "December"};
      String[] monthsWith30Days = {"April", "June", "September", "November"};
      for (String s : monthsWith30Days) {
        String month = l1.getText();
        if (month.contains(s)) {
          if (c2.getItemCount() == 31) {
            c2.removeItem("31");
          } else if (c2.getItemCount() == 29) {
            c2.addItem("30");
          } else if (c2.getItemCount() == 28) {
            c2.addItem("29");
            c2.addItem("30");
          }
          break;
        }
      }
      for (String s : monthsWith31Days) {
        String month = l1.getText();
        if (month.contains(s)) {
          if (c2.getItemCount() == 30) {
            c2.addItem("31");
          } else if (c2.getItemCount() == 29) {
            c2.addItem("30");
            c2.addItem("31");
          } else if (c2.getItemCount() == 28) {
            c2.addItem("29");
            c2.addItem("30");
            c2.addItem("31");
          }
          break;
        }
      }
      if (l1.getText().contains("February")) {
        if (c2.getItemCount() == 31) {
          c2.removeItem("31");
          c2.removeItem("30");
          c2.removeItem("29");
        } else if (c2.getItemCount() == 30) {
          c2.removeItem("30");
          c2.removeItem("29");
        } else if (c2.getItemCount() == 29) {
          c2.removeItem("29");
        }
      }
      month = c1.getSelectedItem().toString();
    } else if (e == c2) {
      j1.setText(c2.getSelectedItem() + " selected");
      day = c2.getSelectedItem().toString();
    } else if (e == c3) {
      k1.setText(c3.getSelectedItem() + " selected");
      year = c3.getSelectedItem().toString();
      Year y = Year.of(Integer.parseInt(year));
      if (y.isLeap() && c1.getSelectedItem().toString().equals("February")) {
        if (c2.getItemCount() == 28) {
          c2.addItem("29");
        }
      }
    } else if (e == portfolioNameCombo) {
      another.setText(portfolioNameCombo.getSelectedItem() + " selected");
    }
  }

  @Override
  public void addItemToPortfolioNames(String name) {
    if (newPortfolio == 0) {
      String[] options1 = new String[0];
      portName = new JLabel("Select portfolio ");
      another = new JLabel("");
      portfolioNameCombo = createDate(options1, portfolioNameCombo, portName, another);
      newPortfolio = 1;
    }
    portfolioNameCombo.addItem(name);
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    JOptionPane.showMessageDialog(GUIView.this,
            "The source object is " + e.getSource(), "Source",
            JOptionPane.PLAIN_MESSAGE);
    JOptionPane.showMessageDialog(GUIView.this,
            "The changing index is " + e.getFirstIndex(), "Index",
            JOptionPane.PLAIN_MESSAGE);
    JOptionPane.showMessageDialog(GUIView.this,
            "The current string item is " + this.listOfStrings.getSelectedValue(),
            "Selected string", JOptionPane.PLAIN_MESSAGE);
    JOptionPane.showMessageDialog(GUIView.this,
            "The current number item is " + this.listOfIntegers.getSelectedValue(),
            "Selected integer", JOptionPane.PLAIN_MESSAGE);
  }

  /**
   * Sets up the text fields and buttons for the buy and sell methods.
   *
   * @param text if a stock is being bought or sold
   */
  private void handleBuySellStock(String text) {
    radioDisplay.setText(text);
    String[] options = {"January", "February", "March", "April", "May", "June", "July", "August",
                        "September", "November", "October", "December"};
    l = new JLabel("Select the month ");
    l1 = new JLabel("");
    c1 = createDate(options, c1, l, l1);
    String[] dayOptions = new String[31];
    for (int i = 0; i < 31; i++) {
      int d = i + 1;
      dayOptions[i] = "" + d;
    }
    j = new JLabel("Select the day ");
    j1 = new JLabel("");
    c2 = createDate(dayOptions, c2, j, j1);
    String[] yearOptions = new String[10];
    int y = 2015;
    for (int i = 0; i < yearOptions.length; i++) {
      yearOptions[i] = "" + y;
      y += 1;
    }
    k = new JLabel("Select the year ");
    k1 = new JLabel("");
    c3 = createDate(yearOptions, c3, k, k1);
    t = new JTextField();
    quant = createTextFields(other, quant, t, "Enter amount of stock ");
    t1 = new JTextField();
    quant1 = createTextFields(other, quant1, t1, "Enter stock ticker ");
    if (text.equals("Buy stock was selected")) {
      submitBuySell = buttonCreator("Submit Information to Buy Stock", submitBuySell, other);
    } else {
      submitBuySell = buttonCreator("Submit Information to Sell Stock", submitBuySell, other);
    }
    setDate.setVisible(true);
    other.setVisible(true);
    setDateExists = 1;
    buySell = 1;
  }

  @Override
  public String getDate() {
    String selectedDate = "";
    try {
      Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).
              parse(c1.getSelectedItem().toString());
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int monthNumber = cal.get(Calendar.MONTH) + 1;
      selectedDate = c3.getSelectedItem().toString() + "-" +
              String.format("%02d", monthNumber) + "-" + String.format("%02d",
              Integer.parseInt(c2.getSelectedItem().toString()));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return selectedDate;
  }

  /**
   * Creates the text, buttons, and text fields for the save and load portfolio methods.
   *
   * @param text if a portfolio is being saved or loaded
   */
  private void savePortfolio(String text) {
    JPanel temp = new JPanel();
    temp.setLayout(new BoxLayout(temp, BoxLayout.PAGE_AXIS));

    JLabel nameLabel = new JLabel("Enter portfolio name");
    JLabel fileLabel = new JLabel("Enter file name");

    savePort = new JTextField(2);
    saveFile = new JTextField(2);

    temp.add(nameLabel);
    temp.add(savePort);
    temp.add(fileLabel);
    temp.add(saveFile);

    saveButton = new JButton(text);
    saveButton.addActionListener(a);
    saveButton.setActionCommand(text);

    temp.add(saveButton);

    savePortfolioPanel.add(temp);

  }

  /**
   * Creates the buttons for each of the methods to be performed.
   *
   * @param text the text to be displayed on the buttons
   * @param b    the button
   * @param p    the panel where the button will be seen
   * @return the created button
   */
  private JButton buttonCreator(String text, JButton b, JPanel p) {
    b = new JButton(text);
    b.addActionListener(a);
    b.setActionCommand(text);
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    panel.add(b);
    p.add(panel);
    return b;
  }

  /**
   * Creates the text fields needed for each of the methods.
   *
   * @param panel     the panel to display the text
   * @param label     the label to show what the text field should be
   * @param text      the text field
   * @param labelText the text to be displayed on the label
   * @return the created label
   */
  private JLabel createTextFields(JPanel panel, JLabel label, JTextField text, String labelText) {
    JPanel temp = new JPanel();
    temp.setLayout(new BoxLayout(temp, BoxLayout.PAGE_AXIS));
    label = new JLabel(labelText);

    temp.add(label);
    temp.add(text);

    panel.add(temp);
    return label;
  }

  /**
   * Creates the date comboboxes.
   *
   * @param options the choices for dates given to the user
   * @param combo   the box needed for each date
   * @param label   the specific date value that the box represents
   * @param label1  the users selection
   * @return the initialized combo box
   */
  private JComboBox createDate(String[] options, JComboBox combo, JLabel label, JLabel label1) {

    combo = new JComboBox(options);

    combo.addItemListener(this);

    JPanel p = new JPanel();

    p.add(label);

    p.add(combo);

    p.add(label1);

    setDate.add(p);

    return combo;
  }

  @Override
  public void welcomeMessage() {
    radioDisplay = new JLabel("Select one to perform functionality on a portfolio.");
  }

  @Override
  public void writeMessage(String message) {
    radioDisplay.setText(message);
  }

  @Override
  public void farewellMessage() {
    radioDisplay.setText("Thank you for using the program.");
  }

  @Override
  public void printMenu() {
    radioDisplay.setText("Select an option.");
  }

  @Override
  public String saveFileGetText() {
    return saveFile.getText();
  }

  @Override
  public String savePortGetText() {
    return savePort.getText();
  }

  @Override
  public String portfolioNameComboGetText() {
    return portfolioNameCombo.getSelectedItem().toString();
  }

  @Override
  public String getRadioDisplayText() {
    return radioDisplay.getText();
  }

  @Override
  public String pNameGetText() {
    return pName.getText();
  }

  @Override
  public String tGetText() {
    return t.getText();
  }

  @Override
  public String t1GetText() {
    return t1.getText();
  }
}

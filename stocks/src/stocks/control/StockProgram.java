package stocks.control;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import stocks.model.APICall;
import stocks.model.StockModelImpl;
import stocks.model.StockModel;
import stocks.view.GUIView;
import stocks.view.MacroStocksView;
import stocks.view.View;
import stocks.model.AlphaVantAPI;
import stocks.view.ViewGUI;

/**
 * Runs the stock program.
 */
public class StockProgram {

  /**
   * Runs the stock program.
   * @param args nothing
   */
  public static void main(String[] args) throws IOException, ParseException {
    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(ap);
    APICall api = new AlphaVantAPI();


    StockController controller = new MacroStockController(rd, model, view, api);

    if (args.length > 0 && args[0].equals("-text")) {
      controller.control();
    } else if (args.length == 0) {
      ViewGUI v = new GUIView();
      Controller guiControl = new GUIController(api, v);
      guiControl.control();
    } else {
      System.out.println("Not a correct input.");
    }
  }
}

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

import stocks.control.StockController;
import stocks.model.APICall;
import stocks.model.AlphaVantAPI;
import stocks.model.StockModel;
import stocks.view.StocksView;
import stocks.view.View;
import stocks.control.Controller;

import static org.junit.Assert.assertEquals;

/**
 * Tests the inputs being sent to the controller.
 */
public class MockControllerTest {
  APICall api = new AlphaVantAPI();

  @Test
  public void testGainLoss() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("1 GOOG 05 29 2024 05 31 2024 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("Start = 2024-05-29, End = 2024-05-31", log.toString());
  }

  @Test
  public void testGainLossApi() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("1 GOOG 05 29 2024 05 31 2024 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("Start = 2024-05-29, End = 2024-05-31", log.toString());
  }

  @Test
  public void testXDayMovingAvg() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("2 GOOG 05 23 2024 3 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("Date = 2024-05-23, x = 3", log.toString());
  }

  @Test
  public void testXDayMovingAvgApi() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("2 GOOG 05 23 2024 3 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("Date = 2024-05-23, x = 3", log.toString());
  }

  @Test
  public void testCrossovers() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("3 GOOG 05 02 2024 05 03 2024 2 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("Start Date = 2024-05-02, End Date = 2024-05-03, x = 2", log.toString());
  }

  @Test
  public void testCrossoversApi() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("3 GOOG 05 02 2024 05 03 2024 2 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("Start Date = 2024-05-02, End Date = 2024-05-03, x = 2", log.toString());
  }

  @Test
  public void testInvalidInstruction() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("20 GOOG 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("", log.toString());
  }

  @Test
  public void testInvalidInstructionApi() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("20 api GOOG 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("", log.toString());
  }

  @Test
  public void testInvalidNum() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("csv gooStock.csv 20 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("", log.toString());
  }

  @Test
  public void testInvalidNumApi() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("api GOOG 20 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("", log.toString());
  }

  @Test
  public void testEmptyInput() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("csv googStock.csv 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("", log.toString());
  }

  @Test
  public void testEmptyInputApi() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("api GOOG 15");
    StringBuilder log = new StringBuilder();
    StockModel model = new MockController(log);
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals("", log.toString());
  }


}

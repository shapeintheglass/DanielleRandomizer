package gui2;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import gui2.controllers.WindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RandomizerGui2 extends Application {

  private static final String GUI2_TEMPLATE_FXML = "template.fxml";
  
  private PrintStream fileStream;

  public static void main(String[] args) {
    Application.launch(RandomizerGui2.class, args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    //setupLogFile();

    FXMLLoader loader = new FXMLLoader(getClass().getResource(GUI2_TEMPLATE_FXML));
    Parent root = loader.load();
 
    
    WindowController controller = (WindowController) loader.getController();
    controller.setStage(stage);

    stage.setTitle(String.format(Gui2Consts.WINDOW_TITLE, Gui2Consts.VERSION));
    stage.setScene(new Scene(root));
    stage.show();
  }

  private void setupLogFile() {
    try {
      File loggerFile = new File(Gui2Consts.LOG_OUTPUT_FILE);
      if (!loggerFile.exists()) {
        loggerFile.createNewFile();
      }
      if (fileStream == null) {
        fileStream = new PrintStream(loggerFile);
      }
      System.setErr(fileStream);
      System.setOut(fileStream);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}

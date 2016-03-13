package watkinsalgo;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;

/**
 *
 * @author Евгений
 */
public class StarterController {
    @FXML
    private Canvas canvas;
    
    @FXML
    private Button buttonDo;
    
    @FXML
    private Button buttonSlowDo;
    
    Executer executer;
    @FXML
    private void actionOncePerformed() {
        executer = new Executer(canvas);
        executer.drawPicture(true);
    }
    
    @FXML
    private void actionSlowPerformed() {
        executer = new Executer(canvas);
        executer.drawPicture(false);
    }
}

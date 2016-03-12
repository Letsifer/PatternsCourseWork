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
    private void actionPerformed() {
        Executer executer = new Executer(canvas);
        executer.drawPicture();
    }
}

package com.sigad.sigad.fx.widgets;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


/**
 * TODO.
 * @author cfoch
 */
public class FXMLController implements Initializable {

    @FXML
    private Label label;

    /**
     * TODO.
     * @param event TODO.
     */
    @FXML
    private void handleButtonAction(final ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    /**
     * TODO.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        // TODO
    }
}

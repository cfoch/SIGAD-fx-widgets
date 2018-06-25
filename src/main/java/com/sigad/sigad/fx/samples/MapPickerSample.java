/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.fx.samples;

import com.sigad.sigad.fx.widgets.MapPicker;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author cfoch
 */
public class MapPickerSample extends Application {

    @Override
    public void start(Stage primaryStage) {
        MapPicker mapPicker = new MapPicker();
        Button btn = new Button();
        TextField latTextField = new TextField();
        TextField lngTextField = new TextField();
        Button btnSetMarkerAt = new Button("Set marker at");
        btn.setText("Say 'Hello World'");

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Double[] latLng;
                latLng = mapPicker.getLatLng();
                if (latLng == null) {
                    System.out.println("Nothing selected.");
                } else {
                    System.out.println(
                            "lat: " + latLng[0] + ", " + "lng: " + latLng[1]);
                }
            }
        });

        btnSetMarkerAt.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Double lat, lng;
                    lat = Double.parseDouble(latTextField.getText());
                    lng = Double.parseDouble(lngTextField.getText());
                    System.out.println(String.format("Set marker at (%s, %s)",
                            lat.toString(), lng.toString()));
                    mapPicker.setMarkerAt(lat, lng);
                } catch (Exception ex) {

                }
            }
        });

        HBox root = new HBox();
        VBox buttonsBox = new VBox();

        mapPicker.getWebView().setPrefSize(400, 400);

        root.getChildren().add(mapPicker);
        root.getChildren().add(buttonsBox);
        buttonsBox.getChildren().add(btn);
        buttonsBox.getChildren().add(latTextField);
        buttonsBox.getChildren().add(lngTextField);
        buttonsBox.getChildren().add(btnSetMarkerAt);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

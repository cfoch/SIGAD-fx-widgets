package com.sigad.sigad.fx.widgets;

import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * TODO.
 * @author cfoch
 */
public class MainApp extends Application {

    /**
     * TODO.
     * @param stage A stage.
     */
    @Override
    public final void start(final Stage stage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();

        try (InputStream inputStream = getClass().getResourceAsStream("/com/sigad/sigad/fx/widgets/map/location.json.schema")) {
          JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
          Schema schema = SchemaLoader.load(rawSchema);
          schema.validate(new JSONObject("{\"hello\" : \"world\"}")); // throws a ValidationException if this object is invalid
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

}

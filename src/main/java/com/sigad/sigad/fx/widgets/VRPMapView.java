/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.fx.widgets;

import com.sigad.sigad.fx.utils.Utils;
import com.sun.javafx.webkit.WebConsoleListener;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * VRPMapView
 * Vista que muestra un mapa de Google Maps y se integra con
 * SimmulatedAnnealingAlgorithm.
 * @author cfoch
 */
public class VRPMapView extends Region {
    private static final String DEFAULT_URL = "/html/map.html";
    private static final String SCHEMA_URL =
            "/com/sigad/sigad/fx/widgets/map/solution.json.schema";

    private static final String CONSOLE_LOG_TMPL = "Console: [%s:%d] %s";

    private static final String SCRIPT_TEMPL_SET_SOLUTION = "setSolution(%s)";
    private static final String SCRIPT_DRAW_SOLUTION = "drawSolution()";
    private static final String SCRIPT_DRAW_ROUTE = "drawRoute(%d,%s)";
    private static final String SCRIPT_DRAW_MARKERS = "drawMarkers()";
    private static final String SCRIPT_CREATE_MARKERS = "createMarkers()";
    private static final String SCRIPT_CLEAR_MARKERS = "clearMarkers()";

    private WebView browser;
    private WebEngine webEngine;
    private String solution;

    public VRPMapView() {
        URL url;
        String OS = System.getProperty("os.name");
        url = getClass().getResource(DEFAULT_URL);

        browser = new WebView();
        webEngine = browser.getEngine();
        // FIXME
        // This is a workaround to avoid issues with fonts in macOS.
        // I am not sure if lastest versions of macOS may have a different
        // name rather than 'Mac OS X' (which is the most probable).
        if (OS.startsWith("Mac OS")) {
            webEngine.setUserAgent(Utils.MAC_OS_X_USER_AGENT);
        }
        webEngine.load(url.toString());
        webEngine.setJavaScriptEnabled(true);

        WebConsoleListener.setDefaultListener(new WebConsoleListener(){
            @Override
            public void messageAdded(WebView webView, String message,
                    int lineNumber, String sourceId) {
                String log;
                log = String.format(CONSOLE_LOG_TMPL, sourceId, lineNumber,
                        message);
                // TODO
                // Use logging system instead.
                System.out.println(log);
            }
        });
        getChildren().add(browser);
    }

    /**
     * @return the solution
     */
    public String getSolution() {
        return solution;
    }

    /**
     * @param solution the solution to set in JSON format according
     *      the solution.json.schema.
     * @return true si la solución es válida, de lo contrario false.
     */
    public boolean setSolution(String solution) {
        boolean valid;
        Class cls = getClass();
        System.out.println(solution);
        this.solution = solution;
        try (InputStream inputStream = cls.getResourceAsStream(SCHEMA_URL)) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            System.out.println("JSON SCHEMA LOADED");
            schema.validate(new JSONArray(solution));
        } catch (Exception ex) {
            Logger.getLogger(VRPMapView.class.getName()).log(Level.SEVERE, null, ex);

            this.solution = null;
        }
        if (this.solution != null) {
            String script;
            script = String.format(SCRIPT_TEMPL_SET_SOLUTION, solution);
            System.out.println(script);
            getWebEngine().executeScript(script);
        }
        return this.solution != null;
    }

    public void clearMarkers() {
        if (this.solution == null) {
            return;
        }
        getWebEngine().executeScript(SCRIPT_CLEAR_MARKERS);
    }

    public void createMarkers() {
        if (this.solution == null) {
            return;
        }
        getWebEngine().executeScript(SCRIPT_CREATE_MARKERS);
    }

    public void drawMarkers() {
        if (this.solution == null) {
            return;
        }
        getWebEngine().executeScript(SCRIPT_DRAW_MARKERS);
    }

    public void drawRoute(int i, boolean clear) {
        if (this.solution == null) {
            return;
        }
        getWebEngine().executeScript(
                String.format(SCRIPT_DRAW_ROUTE, i, "true"));
    }

    public void drawSolution() {
        if (this.solution == null) {
            return;
        }
        getWebEngine().executeScript(SCRIPT_DRAW_SOLUTION);
    }

    public void renderSolution() {
        if (this.solution == null) {
            return;
        }
        getWebEngine().executeScript(SCRIPT_DRAW_SOLUTION);
    }

    /**
     * @return the webEngine
     */
    public WebEngine getWebEngine() {
        return webEngine;
    }

    /**
     * @return the internal WebView
     */
    public WebView getWebView() {
        return browser;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.fx.widgets;

import com.sun.javafx.webkit.WebConsoleListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 *
 * @author cfoch
 */
public class MapPicker extends Region {
    public static class Bridge {
        MapPicker mapPicker;

        public Bridge(MapPicker mapPicker) {
            this.mapPicker = mapPicker;
        }
        public void markerAddedCb() {
            Double [] latlng = mapPicker.getLatLng();
            System.out.println("marker-added");
            // FIXME
            // Old value should be different than null.
            mapPicker.getMarkerAddedProp()
                    .firePropertyChange("marker-added", null, latlng);
        }
    }

    private static final String DEFAULT_URL = "/html/map-picker.html";
    private static final String CONSOLE_LOG_TMPL = "Console: [%s:%d] %s";

    private static final String SCHEMA_URL =
            "/com/sigad/sigad/fx/widgets/map/solution.json.schema";

    private static final String SCRIPT_GET_LAT_LNG = "getLatLng()";
    private static final String SCRIPT_SET_MARKER_AT = "setMarkerAt(%s,%s)";


    private WebView browser;
    private WebEngine webEngine;
    private String solution;
    private PropertyChangeSupport markerAddedProp;

    public MapPicker() {
        URL url;
        url = getClass().getResource(DEFAULT_URL);

        browser = new WebView();
        webEngine = browser.getEngine();
        webEngine.load(url.toString());
        webEngine.setJavaScriptEnabled(true);

        markerAddedProp = new PropertyChangeSupport(this);

        webEngine.getLoadWorker()
                .stateProperty().addListener(
                        (ObservableValue<? extends Worker.State> observable,
                                Worker.State oldValue,
                                Worker.State newValue) -> {
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.setMember("bridge", new Bridge(this));
        });

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

    public Double [] getLatLng() {
        Double [] ret;
        try {
            Double lat, lng;
            JSObject jsobj;
            jsobj = (JSObject) getWebEngine().executeScript(SCRIPT_GET_LAT_LNG);
            lat = (Double) jsobj.getMember("lat");
            lng = (Double) jsobj.getMember("lng");
            ret = new Double []{lat, lng};
        } catch (Exception ex) {
            ret = null;
        }
        return ret;
    }

    public void setMarkerAt(Double lat, Double lng) throws Exception {
        getWebEngine().executeScript(String.format(SCRIPT_SET_MARKER_AT,
                lat, lng));
    }

    public void addMarkerAddedPropListener(PropertyChangeListener listener) {
        getMarkerAddedProp().addPropertyChangeListener(listener);
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

    /**
     * @return the markerAddedProp
     */
    public PropertyChangeSupport getMarkerAddedProp() {
        return markerAddedProp;
    }
}

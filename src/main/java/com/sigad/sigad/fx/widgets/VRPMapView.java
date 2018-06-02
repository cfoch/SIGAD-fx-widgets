/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.fx.widgets;

import com.google.gson.Gson;
import com.grupo1.simulated_annealing.Locacion;
import com.sun.javafx.webkit.WebConsoleListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jgrapht.graph.GraphWalk;

/**
 * VRPMapView
 * Vista que muestra un mapa de Google Maps y se integra con
 * SimmulatedAnnealingAlgorithm.
 * @author cfoch
 */
public class VRPMapView extends Region {
    private static final String DEFAULT_URL = "/html/map.html";

    private static final String CONSOLE_LOG_TMPL = "Console: [%s:%d] %s";

    private static final String SCRIPT_TEMPL_SET_SOLUTION = "setSolution(%s)";
    private static final String SCRIPT_DRAW_SOLUTION = "drawSolution()";
    private static final String SCRIPT_DRAW_ROUTE = "drawRoute(%d,%s)";
    private static final String SCRIPT_DRAW_MARKERS = "drawMarkers()";
    private static final String SCRIPT_CREATE_MARKERS = "createMarkers()";

    private WebView browser;
    private WebEngine webEngine;
    private ArrayList<GraphWalk> solution;

    public VRPMapView() {
        URL url;
        url = getClass().getResource(DEFAULT_URL);

        browser = new WebView();
        webEngine = browser.getEngine();
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

//
//        VRPProblem problem;
//        VRPAlgorithm algorithm;
//        ArrayList<Locacion> check;
//        int i, j;
//
//        //problem = new VRPProblem("datasets/CVRP/augerat/A/A-n80-k10.vrp");
//        Vehiculo.Tipo vehiculoTipo = new Vehiculo.Tipo("Foo", 100);
//        problem = new VRPProblem(16, -12.0431800, -77.0282400, 10000, vehiculoTipo);
//        algorithm = new VRPAlgorithm(problem);
//
//
//        solution = algorithm.solve();
    }

    /**
     * @return the solution
     */
    public ArrayList<GraphWalk> getSolution() {
        return solution;
    }

    /**
     * @param solution the solution to set
     */
    public void setSolution(ArrayList<GraphWalk> solution) {
        this.solution = solution;
    }

    public void createMarkers() {
        getWebEngine().executeScript(SCRIPT_CREATE_MARKERS);
    }

    public void drawMarkers() {
        getWebEngine().executeScript(SCRIPT_DRAW_MARKERS);
    }

    public void drawRoute(int i, boolean clear) {
        getWebEngine().executeScript(
                String.format(SCRIPT_DRAW_ROUTE, i, "true"));
    }

    public void drawSolution() {
        getWebEngine().executeScript(SCRIPT_DRAW_SOLUTION);
    }

    public void setupSolution() {
        int i, j;
        String marshalledSolution, script;
        Gson gson = new Gson();
        ArrayList<ArrayList<Locacion>> pojoSolution = new ArrayList<>();
        for (i = 0; i < solution.size(); i++) {
            GraphWalk walk;
            List<Locacion> locaciones;
            ArrayList<Locacion> pojoRoute = new ArrayList<Locacion>();
            locaciones = solution.get(i).getVertexList();

            for (j = 0; j < locaciones.size(); j++) {
                Locacion locacion = locaciones.get(j);
                Locacion pojoLocacion = new Locacion (locacion.getId(),
                        locacion.getNombre(), locacion.getTipo(),
                        locacion.getX(), locacion.getY());
                pojoRoute.add(pojoLocacion);
            }
            pojoSolution.add(pojoRoute);
        }
        marshalledSolution = gson.toJson(pojoSolution);
        script = String.format(SCRIPT_TEMPL_SET_SOLUTION, marshalledSolution);
        getWebEngine().executeScript(script);
    }

    public void renderSolution() {
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

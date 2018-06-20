/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.fx.utils;

import com.google.gson.Gson;
import com.grupo1.simulated_annealing.Locacion;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.GraphWalk;

/**
 *
 * @author cfoch
 */
public class Utils {
    public static String marshallVRPSolution(ArrayList<GraphWalk> solution) {
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
        return marshalledSolution;
    }

    public static String marshallLocationSolution(
            List<List<Locacion>> solution) {
        int i, j;
        String marshalledSolution, script;
        Gson gson = new Gson();
        ArrayList<ArrayList<Locacion>> pojoSolution = new ArrayList<>();

        for (i = 0; i < solution.size(); i++) {
            GraphWalk walk;
            List<Locacion> locaciones;
            ArrayList<Locacion> pojoRoute = new ArrayList<Locacion>();
            locaciones = solution.get(i);

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
        return marshalledSolution;
    }
}

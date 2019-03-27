/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 *
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 *
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.filmsortierung.filme.rest;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author vibach
 */
@ApplicationPath("RestAPI")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        resources.add(dhbwka.wwi.vertsys.javaee.filmsortierung.filme.rest.SucheFilmRest.class);

        return resources;
    }

}

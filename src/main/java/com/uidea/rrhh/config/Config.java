package com.uidea.rrhh.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class Config {

    private static final String PROPERTIES_FILE = "/db.properties";
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (in == null) {
                throw new IllegalStateException("No se encontro el archivo de configuracion: " + PROPERTIES_FILE);
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Error cargando configuracion de base de datos", e);
        }
    }

    private Config() {
    }

    public static String get(String key) {
        String value = PROPS.getProperty(key);
        return Objects.requireNonNull(value, "Propiedad no encontrada: " + key).trim();
    }

    public static String getDbUrl() {
        return get("db.url");
    }

    public static String getDbUser() {
        return get("db.user");
    }

    public static String getDbPassword() {
        return get("db.password");
    }
}

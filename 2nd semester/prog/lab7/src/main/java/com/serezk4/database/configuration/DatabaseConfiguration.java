package com.serezk4.database.configuration;

public class DatabaseConfiguration {
    public static final String POSTGRES_URL = "jdbc:postgresql://%s:%s/%s".formatted(System.getenv("DB_HOST"), System.getenv("DB_PORT"), System.getenv("DB_NAME"));
    public static final String POSTGRES_USER = System.getenv("DB_USER");
    public static final String POSTGRES_PASSWORD = System.getenv("DB_PASSWORD");
}

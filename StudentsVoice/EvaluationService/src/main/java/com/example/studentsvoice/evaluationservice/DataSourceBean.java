package com.example.studentsvoice.evaluationservice;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
@DataSourceDefinition(name="java:app/jdbc/EvaluationResource",
        className="org.postgresql.ds.PGSimpleDataSource",
        url="jdbc:postgresql://localhost:5432/postgres",
        user="admin",
        password="password",
        databaseName="postgres",
        maxPoolSize = 32,
        minPoolSize = 2
)
public class DataSourceBean {
}

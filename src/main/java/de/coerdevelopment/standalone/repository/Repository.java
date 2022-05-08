package de.coerdevelopment.standalone.repository;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    public static List<Repository> reposities = new ArrayList<>();

    protected SQL sql;
    public String tableName;

    public Repository(String tableName) {
        this.tableName = tableName;
        this.sql = SQL.getSQL();
        reposities.add(this);
    }

}

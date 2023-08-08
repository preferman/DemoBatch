package com.my.demobatch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EntityGenerator {
    public static void main(String[] args) {
        String tableName = "your_table_name";
        String className = generateClassName(tableName);
        String packagePath = "com.example.entities";
        String filePath = "./" + className + ".java";

        try {
            String entityCode = generateEntityClass(tableName, className, packagePath);
            writeToFile(entityCode, filePath);
            System.out.println("Entity class generated successfully!");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/your_database_name";
        String username = "your_username";
        String password = "your_password";
        return DriverManager.getConnection(url, username, password);
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static String generateClassName(String tableName) {
        //
        String[] words = tableName.toLowerCase().split("_");
        StringBuilder className = new StringBuilder();
        for (String word : words) {
            className.append(capitalize(word));
        }
        return className.toString();
    }

    public static String generateFiledName(String filedNameWith_) {
        String[] words = filedNameWith_.toLowerCase().split("_");

        StringBuilder filedName = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                filedName.append(words[i]);
                continue;
            }

            filedName.append(capitalize(words[i]));


        }
        return filedName.toString();
    }

    public static String capitalize(String str) {
        //
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String generateEntityClass(String tableName, String className, String packagePath) throws SQLException {
        StringBuilder codeBuilder = new StringBuilder();
        codeBuilder.append("package ").append(packagePath).append(";\n\n");
        codeBuilder.append("public class ").append(className).append(" {\n\n");

        String query = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '" + tableName + "'";
        ResultSet resultSet = executeQuery(query);
        while (resultSet.next()) {
            String columnName = resultSet.getString("column_name");
            String dataType = resultSet.getString("data_type");
            String javaType = mapDataTypeToJavaType(dataType);
            codeBuilder.append("\tprivate ").append(javaType).append(" ").append(generateFiledName(columnName)).append(";\n");
        }

        codeBuilder.append("\n");

        codeBuilder.append("}");
        return codeBuilder.toString();
    }

    public static String mapDataTypeToJavaType(String dataType) {

        if (dataType.equalsIgnoreCase("int")) {
            return "int";
        } else if (dataType.equalsIgnoreCase("varchar")) {
            return "String";
        } else if (dataType.equalsIgnoreCase("timestamp")) {
            return "java.util.Date";
        } else {
            return "Object";
        }
    }

    public static void writeToFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
package ru.git.ivanv_lab.framework.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    private static final Properties properties=new Properties();

    static {
        try(InputStream inputStream = new FileInputStream("src/main/resources/project.properties")){
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при получении свойств: "+e.getMessage());
        }
    }

    public static final String baseProtocol = properties.getProperty("base.protocol");
    public static final String baseIp = properties.getProperty("base.ip");
    public static final String baseUrl = baseProtocol+baseIp;

    public static final String adminLogin = properties.getProperty("admin.login");
    public static final String adminPassword = properties.getProperty("admin.password");
}

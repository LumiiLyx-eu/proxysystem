package com.luan.proxy.database;

import com.luan.proxy.ProxySystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;

public class MySQL {

    public static String username;
    public static String password;
    public static String database;
    public static String host;
    public static String port;
    public static Connection connection;

    public static boolean isConnected() {
        return connection != null;
    }

    public static void connect() {
        if (!isConnected()) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§aDie MySQL Verbindung wurde erfolgreich aufgebaut§8!"));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void close() {
        if (isConnected()) {
            try {
                connection.close();
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§cDie MySQL Verbindung wurde erfolgreich geschlossen§8!"));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void createTables() {
        if (isConnected()) {
            try {
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS bannedplayers (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Banner VARCHAR(100))");
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS mutedplayers (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), MUTER VARCHAR(100))");
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS beta(BETAKEY varchar(255) UNIQUE, UUID varchar(255), CREATOR varchar(255), DATE varchar(255));");
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS onlinetime (UUID VARCHAR(255), TIME INT(32))");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void update(final String query) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            try {
                preparedStatement.close();
            } catch (SQLException exception1) {
                exception1.printStackTrace();
            }
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static ResultSet getResult(final String query) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static void loadFile() {
        try {
            final File file = new File(ProxySystem.getInstance().getDataFolder(), "config.yml");
            boolean created = true;
            if (!file.exists()) {
                file.createNewFile();
                created = false;
            }
            final Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            if (!created) {
                configuration.set("mysql.Host", "localhost");
                configuration.set("mysql.Port", "3306");
                configuration.set("mysql.Database", "proxy");
                configuration.set("mysql.Username", "admin");
                configuration.set("mysql.Password", "password");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
            }
            host = configuration.getString("mysql.Host");
            port = configuration.getString("mysql.Port");
            database = configuration.getString("mysql.Database");
            username = configuration.getString("mysql.Username");
            password = configuration.getString("mysql.Password");
        } catch (Exception exception) {}
    }
}

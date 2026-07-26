package com.maratonajava.jdbc.repository;

import com.maratonajava.jdbc.conn.ConnectionFactory;
import com.maratonajava.jdbc.dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProducerRepository {
    public static void save(Producer producer) {
        String sql = "INSERT INTO `anime_store`.`producer` (`name`) VALUES ('%s');".formatted(producer.getName());
        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();) {
            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Inserted producer '{}' in the database, rows affected '{}' ", producer.getName(), rowsAffected);
        } catch (SQLException e) {
            log.error("Error while try insert producer '{}'",  producer.getName(), e);
    }
}

    public static void delete(int id) {
        String sql = "DELETE FROM `anime_store`.`producer` WHERE (`id` = '%d');".formatted(id);
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();) {
            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Deleted producer '{}' in the database, rows affected '{}' ", id, rowsAffected);
        } catch (SQLException e) {
            log.error("Error while try insert producer '{}'",  id, e);
        }
    }

    public static void update(Producer producer) {
        String sql = "UPDATE `anime_store`.`producer` SET `name` = '%s' WHERE (`id` = '%d');".formatted(producer.getName(), producer.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();) {
            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Update producer '{}', rows affected '{}' ", producer.getId(), rowsAffected);
        } catch (SQLException e) {
            log.error("Error while trying to update producer '{}'",  producer.getId(), e);
        }
    }

    public static List<Producer> findAll() {
        log.info("Finding all Producers");
        return findByName("");
    }

    public static List<Producer> findByName(String name) {
        log.info("Finding producers by name");
        String sql = "select * from producer WHERE name like '%%%s%%';".formatted(name);
        List<Producer> producersList = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producer producerElement = Producer
                        .builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
                producersList.add(producerElement);
            }
        } catch (SQLException e) {
            log.error("Error while trying to find producers by name", e);
        }
        return producersList;
    }

    public static void showProducerMetaData() {
        log.info("Showing producer metadata");
        String sql = "select * from anime_store.producer";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                log.info("Table name {}", rsMetaData.getTableName(i));
                log.info("Column name {}", rsMetaData.getColumnName(i));
                log.info("Column size {}", rsMetaData.getColumnDisplaySize(i));
                log.info("Column type {}", rsMetaData.getColumnTypeName(i));
            }

        } catch (SQLException e) {
            log.error("Error while trying showed Metadata", e);
        }
    }

    public static void showDriveMetaData() {
        log.info("Showing Driver metadata");
        try (Connection conn = ConnectionFactory.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            if (metaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)) {
                log.info("Support TYPE_FORWARD_ONLY");
                if (metaData.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
                    log.info("And Supports CONCUR_UPDATABLE ");
                }
            }
            if (metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)) {
                log.info("Support TYPE_SCROLL_INSENSITIVE");
                if (metaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                    log.info("And Supports CONCUR_UPDATABLE ");
                }
            }
            if (metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE)) {
                log.info("Supports TYPE_SCROLL_SENSITIVE");
                if (metaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                    log.info("And Supports CONCUR_UPDATABLE ");
                }
            }
        } catch (SQLException e) {
            log.error("Error while trying showed Metadata", e);
        }
    }

}

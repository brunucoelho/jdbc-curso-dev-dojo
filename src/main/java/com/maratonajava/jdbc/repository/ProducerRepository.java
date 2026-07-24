package com.maratonajava.jdbc.repository;

import com.maratonajava.jdbc.conn.ConnectionFactory;
import com.maratonajava.jdbc.dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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


}

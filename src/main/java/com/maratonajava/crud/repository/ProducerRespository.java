package com.maratonajava.crud.repository;


import com.maratonajava.crud.conn.ConnectionFactory;
import com.maratonajava.crud.dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Log4j2
public class ProducerRespository {

    public static List<Producer> findByName(String name) {
        log.info("Finding producers by name '{}'", name);
        String sql = "select * from producer WHERE name like ?;";
        List<Producer> producersList = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementByName(conn, name);
             ResultSet rs = ps.executeQuery();) {

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

    private static PreparedStatement createPrepareStatementByName(Connection conn, String name) throws SQLException {
        String sql = "select * from producer WHERE name like ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }

    public static Optional<Producer> findById(Integer id) {
        log.info("Finding producers by id '{}'", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementById(conn, id);
             ResultSet rs = ps.executeQuery();) {
            if (!rs.next()) return Optional.empty();
            return Optional.of(Producer
                    .builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build());
        } catch (SQLException e) {
            log.error("Error while trying to find producers by id", e);
        }
        return Optional.empty();
    }


    private static PreparedStatement createPrepareStatementById(Connection conn, Integer id) throws SQLException {
        String sql = "select * from producer WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public static void save(Producer producer) {
        log.info("Saving producer '{}'", producer);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementSave(conn, producer);) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to save producer '{}'",  producer.getId(), e);
        }
    }

    private static PreparedStatement createPrepareStatementSave(Connection conn, Producer producer) throws SQLException {
        String sql = "INSERT INTO `anime_store`.`producer` (`name`) VALUES (?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, producer.getName());
        //ps.setInt(2, producer.getId());
        return ps;
    }

    public static void uptade(Producer producer) {
        log.info("Updating producer '{}'", producer);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, producer);) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to update producer '{}'",  producer.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementUpdate(Connection conn, Producer producer) throws SQLException {
        String sql = "UPDATE `anime_store`.`producer` SET `name` = ? WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, producer.getName());
        ps.setInt(2, producer.getId());
        return ps;
    }

    public static void delete(int id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps= createPrepareStatemenDelete(conn, id);) {
            ps.execute();
            log.info("Deleted producer '{}' from the database", id);
        } catch (SQLException e) {
            log.error("Error while try insert producer '{}'",  id, e);
        }
    }

    private static PreparedStatement createPrepareStatemenDelete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM `anime_store`.`producer` WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

}

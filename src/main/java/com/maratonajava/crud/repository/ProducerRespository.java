package com.maratonajava.crud.repository;


import com.maratonajava.crud.conn.ConnectionFactory;
import com.maratonajava.crud.dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

}

package com.maratonajava.crud.repository;


import com.maratonajava.crud.conn.ConnectionFactory;
import com.maratonajava.crud.dominio.Anime;
import com.maratonajava.crud.dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Log4j2
public class AnimeRespository {

    public static List<Anime> findByName(String name) {
        log.info("Finding animes by name '{}'", name);
        String sql = "select * from anime WHERE name like ?;";
        List<Anime> animesList = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementByName(conn, name);
             ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                Producer producer = Producer.builder()
                        .name(rs.getString("producer_name"))
                        .id(rs.getInt("producer_id"))
                        .build();

                Anime animeElement = Anime
                        .builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .episodes(rs.getInt("episodes"))
                        .producer(producer)
                        .build();
                animesList.add(animeElement);
            }
        } catch (SQLException e) {
            log.error("Error while trying to find animes by name", e);
        }
        return animesList;
    }

    private static PreparedStatement createPrepareStatementByName(Connection conn, String name) throws SQLException {
        String sql = """
                SELECT a.id, a.name, a.producer_id, a.episodes, p.name as 'producer_name' FROM anime_store.anime a inner join
                        anime_store.producer p on a.producer_id = p.id
                        where a.name like ?;
                        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }

    public static Optional<Anime> findById(Integer id) {
        log.info("Finding animes by id '{}'", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementById(conn, id);
             ResultSet rs = ps.executeQuery();) {
            if (!rs.next()) return Optional.empty();

            Producer producer = Producer.builder()
                    .name(rs.getString("producer_name"))
                    .id(rs.getInt("producer_id"))
                    .build();

            Anime animeElement = Anime
                    .builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .episodes(rs.getInt("episodes"))
                    .producer(producer)
                    .build();
            return Optional.of(animeElement);
        } catch (SQLException e) {
            log.error("Error while trying to find animes by id", e);
        }
        return Optional.empty();
    }


    private static PreparedStatement createPrepareStatementById(Connection conn, Integer id) throws SQLException {
        String sql = """
                SELECT a.id, a.name, a.producer_id, a.episodes, p.name as 'producer_name' FROM anime_store.anime a inner join
                        anime_store.producer p on a.producer_id = p.id
                        where a.id = ?;
                        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public static void save(Anime anime) {
        log.info("Saving anime '{}'", anime);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementSave(conn, anime);) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to save anime '{}'",  anime.getId(), e);
        }
    }

    private static PreparedStatement createPrepareStatementSave(Connection conn, Anime anime) throws SQLException {
        String sql = "INSERT INTO `anime_store`.`anime` (`name`, `episodes`, `producer_id`) VALUES (?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, anime.getName());
        ps.setInt(2, anime.getEpisodes());
        ps.setInt(3, anime.getProducer().getId());
        return ps;
    }

    public static void uptade(Anime anime) {
        log.info("Updating anime '{}'", anime);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, anime);) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to update anime '{}'",  anime.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementUpdate(Connection conn, Anime anime) throws SQLException {
        String sql = "UPDATE `anime_store`.`anime` SET `name`  = ?, `episodes` = ? WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, anime.getName());
        ps.setInt(2, anime.getEpisodes());
        ps.setInt(3, anime.getId());
        return ps;
    }

    public static void delete(int id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps= createPrepareStatemenDelete(conn, id);) {
            ps.execute();
            log.info("Deleted anime '{}' from the database", id);
        } catch (SQLException e) {
            log.error("Error while try insert anime '{}'",  id, e);
        }
    }

    private static PreparedStatement createPrepareStatemenDelete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM `anime_store`.`anime` WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

}

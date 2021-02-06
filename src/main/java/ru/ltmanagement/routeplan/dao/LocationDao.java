package ru.ltmanagement.routeplan.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_QUERY = "SELECT l.LOC from LOC l " +
            " WHERE  l.LOCATIONTYPE = :type  %s  l.LOCATIONCATEGORY = :category ";
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String DOOR = "DOOR";

    public List<String> getLocations(String locationType, String locationCategory){
        MapSqlParameterSource params = new MapSqlParameterSource("type", locationType)
                .addValue("category", locationCategory);
        String sql = String.format(SELECT_QUERY, DOOR.equalsIgnoreCase(locationCategory) ? OR : AND);
        return jdbcTemplate.query(sql, params, (rs, index) -> rs.getString("LOC"));
    }

}

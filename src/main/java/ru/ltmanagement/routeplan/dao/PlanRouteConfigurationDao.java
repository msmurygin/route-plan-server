package ru.ltmanagement.routeplan.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.routeplan.dto.PlanRouteConfigurationDto;
import ru.ltmanagement.settings.dto.SettingsDto;

import java.util.List;

@Repository
public class PlanRouteConfigurationDao {

    private static final String CONFIG_MAP_KEY_NAME =  "configuration";

    private static final String SCHEMA_NAME = "wmwhse1";

    private static final int DEFAULT_SHIFT_START = 9;

    private static final int DEFAULT_SHIFT_END = 21;

    private static final int NOT_DIRTY_FIELD = 9999;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_PROCEDURE_NAME = "rep_web_plan_route_setting";

    private static final String SELECT_COUNT_SETTINGS = "SELECT " +
            " count(1) from wmwhse1.PLAN_ROUTE_SETTING where USERKEY = :userkey" ;

    private static final String SELECT_DEFAULTS = "SELECT " +
            "TOP 1 SD_BEGIN, SD_END, SN_BEGIN, SN_END FROM PLAN_ROUTE_SETTING" ;

    private static final String UPDATE_QUERY = "UPDATE PLAN_ROUTE_SETTING SET ";

    private static final String UPDATE_QUERY_WITH_PERSONAL_PARAMS = UPDATE_QUERY +
            "NET_NOTSHOW= :hideNotFinishedTask, NET_LASTSMENA = :showLastShiftNotFinishedTask, " +
            "NET_BEETWEN= :showNotFinishedTaskInPeriod, NET_ALL= :showAllNotFinishedTask, PT_NOTSHOW = :hidePlanedTasks, " +
            "PT_NEXTSMENA= :showNextShiftPlanedTasks, PT_BEETWEN= :showPlanedTaskInPeriod WHERE USERKEY = :userkey";

    private static final String INSERT_QUERY_WITH_PERSONAL_PARAMS = "INSERT INTO PLAN_ROUTE_SETTING " +
            "(NET_NOTSHOW, NET_LASTSMENA, NET_BEETWEN, NET_ALL, PT_NOTSHOW, PT_NEXTSMENA, PT_BEETWEN, SD_BEGIN, SD_END," +
            " SN_BEGIN, SN_END )  VALUES (:hideNotFinishedTask , :showLastShiftNotFinishedTask, :showNotFinishedTaskInPeriod, "+
            " :showAllNotFinishedTask ,:hidePlanedTasks, :showNextShiftPlanedTasks, :showPlanedTaskInPeriod, :dayShiftBegin, " +
            " :dayShiftEnd, :nightShiftBegin, :nightShiftEnd, :userkey) ";


    private static final RowMapper<PlanRouteConfigurationDto> DEFAULT_ROW_MAPPER =  (rs, rowNum) ->
            PlanRouteConfigurationDto.builder()
                    .dayShiftBegin(rs.getInt("SD_BEGIN"))
                    .dayShiftEnd(rs.getInt("SD_END"))
                    .nightShiftBegin(rs.getInt("SN_BEGIN"))
                    .nightShiftEnd(rs.getInt("SN_END"))
                    .build();


    private static final RowMapper<PlanRouteConfigurationDto> CONTAINER_MAPPER = (rs, rowNum) ->
            PlanRouteConfigurationDto.builder()
                    .serialKey(rs.getInt("SERIALKEY"))
                    .userKey(rs.getString("USERKEY"))
                    .hideNotFinishedTask(rs.getBoolean("NET_NOTSHOW"))
                    .showLastShiftNotFinishedTask(rs.getBoolean("NET_LASTSMENA"))
                    .showNotFinishedTaskInPeriod(rs.getBoolean("NET_BEETWEN"))
                    .showAllNotFinishedTask(rs.getBoolean("NET_ALL"))
                    .hidePlanedTasks(rs.getBoolean("PT_NOTSHOW"))
                    .showNextShiftPlanedTasks(rs.getBoolean("PT_NEXTSMENA"))
                    .showPlanedTaskInPeriod(rs.getBoolean("PT_BEETWEN"))
                    .dayShiftBegin(rs.getInt("SD_BEGIN"))
                    .dayShiftEnd(rs.getInt("SD_END"))
                    .nightShiftBegin(rs.getInt("SN_BEGIN"))
                    .nightShiftEnd(rs.getInt("SN_END"))
                    .build();



    public PlanRouteConfigurationDao(JdbcTemplate jdbcTemplate,
                                     NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }



    public PlanRouteConfigurationDto getPlanRoutConfiguration(String loginId, boolean isUpdate, int whatUpdate,
                                                              char prm1, int prm2){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("isUpdate", (isUpdate? 1: 0));
        params.addValue("whatUpdate", whatUpdate);
        params.addValue("inputParam1", prm1);
        params.addValue("inputParam2", prm2);
        params.addValue("login_id", loginId);
        PlanRouteConfigurationDto planRouteConfigurationDTO = ((List<PlanRouteConfigurationDto>) simpleJdbcCall
                .returningResultSet(CONFIG_MAP_KEY_NAME, CONTAINER_MAPPER)
                .execute(params).get(CONFIG_MAP_KEY_NAME)).get(0);
        return planRouteConfigurationDTO;
    }

    public boolean isRecordExistByUser(String userKey){
        MapSqlParameterSource params = new MapSqlParameterSource("userkey", userKey);
        return namedParameterJdbcTemplate.queryForObject(SELECT_COUNT_SETTINGS, params, Boolean.class);
    }

    public void updateUserSettings(String login, SettingsDto settingsDto) {
        MapSqlParameterSource params = new MapSqlParameterSource("userkey", login)
                .addValue("hideNotFinishedTask", settingsDto.isHideNotFinishedTask())
                .addValue("showLastShiftNotFinishedTask", settingsDto.isShowLastShiftNotFinishedTask())
                .addValue("showNotFinishedTaskInPeriod", settingsDto.isShowNotFinishedTaskInPeriod())
                .addValue("showAllNotFinishedTask", settingsDto.isShowAllNotFinishedTask())
                .addValue("hidePlanedTasks", settingsDto.isHidePlanedTasks())
                .addValue("showNextShiftPlanedTasks", settingsDto.isShowNextShiftPlanedTasks())
                .addValue("showPlanedTaskInPeriod", settingsDto.isShowPlanedTaskInPeriod());
        namedParameterJdbcTemplate.update(UPDATE_QUERY_WITH_PERSONAL_PARAMS, params);
    }

    public void updateShiftsSettings(SettingsDto settingsDto) {
        StringBuilder sql = new StringBuilder(UPDATE_QUERY);
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (settingsDto.getDayShiftBegin() < NOT_DIRTY_FIELD){
            params.addValue("dayShiftStart", settingsDto.getDayShiftBegin());
            sql.append(" SD_BEGIN = :dayShiftBegin");
        }

        if (settingsDto.getDayShiftEnd() < NOT_DIRTY_FIELD){
            params.addValue("dayShiftEnd", settingsDto.getDayShiftEnd());
            sql.append(", SD_END = :dayShiftEnd");
        }
        if (settingsDto.getNightShiftBegin() < NOT_DIRTY_FIELD){
            params.addValue("nightShiftBegin", settingsDto.getNightShiftBegin());
            sql.append(", SN_BEGIN = :nightShiftBegin");
        }
        if (settingsDto.getNightShiftEnd() < NOT_DIRTY_FIELD){
            params.addValue("nightShiftBegin", settingsDto.getNightShiftEnd());
            sql.append(", SN_END = :nightShiftEnd");
        }
        String updateShiftQuery = sql.toString().replace("SET ,", "SET ");
        if (updateShiftQuery.equalsIgnoreCase(UPDATE_QUERY)) return;

        namedParameterJdbcTemplate.update(updateShiftQuery, params);
    }


    public void insertSettings(String login, PlanRouteConfigurationDto settingsDto) {
        MapSqlParameterSource params = new MapSqlParameterSource("userkey", login)
                .addValue("hideNotFinishedTask", settingsDto.isHideNotFinishedTask())
                .addValue("showLastShiftNotFinishedTask", settingsDto.isShowLastShiftNotFinishedTask())
                .addValue("showNotFinishedTaskInPeriod", settingsDto.isShowNotFinishedTaskInPeriod())
                .addValue("showAllNotFinishedTask", settingsDto.isShowAllNotFinishedTask())
                .addValue("showNextShiftPlanedTasks", settingsDto.isShowNextShiftPlanedTasks())
                .addValue("showPlanedTaskInPeriod", settingsDto.isShowPlanedTaskInPeriod())
                .addValue("dayShiftStart", settingsDto.getDayShiftBegin())
                .addValue("dayShiftEnd", settingsDto.getDayShiftEnd())
                .addValue("nightShiftBegin", settingsDto.getNightShiftBegin())
                .addValue("nightShiftEnd,", settingsDto.getNightShiftEnd());
        namedParameterJdbcTemplate.update(INSERT_QUERY_WITH_PERSONAL_PARAMS, params);
    }




    public PlanRouteConfigurationDto getDefaults(){
        MapSqlParameterSource params = new MapSqlParameterSource();
        List<PlanRouteConfigurationDto> deaults = namedParameterJdbcTemplate.query(SELECT_DEFAULTS, params, DEFAULT_ROW_MAPPER);
        if (!deaults.isEmpty()) { return deaults.get(0); }
        return PlanRouteConfigurationDto.builder()
                .dayShiftBegin(DEFAULT_SHIFT_START)
                .dayShiftEnd(DEFAULT_SHIFT_END)
                .nightShiftBegin(DEFAULT_SHIFT_START)
                .nightShiftEnd(DEFAULT_SHIFT_END)
                .build();

    }
}

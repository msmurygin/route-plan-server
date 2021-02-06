package ru.ltmanagement.common.service.infor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TCPMessageFormatter {

    @Value("${infor.host}")
    private String host;

    private final static String D = "`";
    private final static String SCHEMA = "SCPRD_wmwhse1";


    public String format(String storedProcedureId, String userId, String msg){
        String a1 = storedProcedureId.substring(5,storedProcedureId.length()-2);
        String programFlag = storedProcedureId.substring(storedProcedureId.length()-2);
        String messageId = "2949";
        String message = "EXEC %s " + D + " 1" + D + "%s" + D + "%s" + D + SCHEMA + D + "%s" +
                D + "%s"+ D + host + D + "%s" +"CHECKSUM" + D + "EOS";
        return String.format(message, storedProcedureId, userId, messageId, a1, programFlag, msg);
    }
}

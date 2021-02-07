package ru.ltmanagement.common.service.infor;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Supplier;

@Component
public class InboundMessageProcessor {

    private final static String DELIMITER = "`";

    private static final String NO_ERROR = "No Error" ;

    public String noErrorOrThrow(String inboundString, Supplier<RuntimeException> sup){
       return Arrays.stream(inboundString.split(DELIMITER))
               .filter(item -> item.equalsIgnoreCase(NO_ERROR))
               .findAny()
               .orElseThrow(sup);
    }


}

package ru.ltmanagement.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ltmanagement.exceptions.ApiError;
import ru.ltmanagement.exceptions.CreateInventoryTaskException;
import ru.ltmanagement.exceptions.IllegalArgumentsException;
import ru.ltmanagement.exceptions.InvalidTokenException;
import ru.ltmanagement.exceptions.LoginFailedException;
import ru.ltmanagement.exceptions.OrderAlreadyClosedException;
import ru.ltmanagement.exceptions.OrderAlreadyPickedException;
import ru.ltmanagement.exceptions.OrderCanNotBeClosedException;
import ru.ltmanagement.exceptions.OrderCloseFailedException;
import ru.ltmanagement.exceptions.OrderInProcessException;
import ru.ltmanagement.exceptions.OrderSentToCustomerException;
import ru.ltmanagement.exceptions.ReleaseFailedException;
import ru.ltmanagement.exceptions.RestError;
import ru.ltmanagement.exceptions.RouteCanNotBeClosedException;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { OrderAlreadyClosedException.class,
                                OrderAlreadyPickedException.class,
                                OrderCloseFailedException.class,
                                OrderInProcessException.class,
                                OrderSentToCustomerException.class,
                                ReleaseFailedException.class,
                                RouteCanNotBeClosedException.class,
                                IllegalArgumentsException.class,
                                IllegalArgumentException.class,
                                InvalidTokenException.class,
                                CreateInventoryTaskException.class,
                                OrderCanNotBeClosedException.class})
    protected ApiError handleException(RuntimeException ex) {
        log.warn("Exception during request processing", ex);
        return new RestError(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = { LoginFailedException.class})
    protected ApiError handlingLoginFailed(LoginFailedException ex) {
        log.warn("Exception during request processing", ex);
        return new RestError(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { SQLException.class})
    protected ApiError handleSqlException(SQLException ex) {
        log.warn("Exception during request processing", ex);
        return new RestError(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

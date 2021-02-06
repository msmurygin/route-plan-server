package ru.ltmanagement.common.service.infor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.exceptions.CreateInventoryTaskException;
import ru.ltmanagement.exceptions.LoginFailedException;
import ru.ltmanagement.exceptions.OrderCloseFailedException;
import ru.ltmanagement.exceptions.ReleaseFailedException;
import ru.ltmanagement.ordermanagement.dto.OrderCloseDto;
import ru.ltmanagement.security.JwtProvider;
import ru.ltmanagement.security.controller.response.AuthResponse;
import ru.ltmanagement.user.dao.UserDao;
import ru.ltmanagement.user.dto.UserDto;

import java.util.function.Supplier;

@Service
public class InforClientServiceImpl implements InforClientService {

    @Autowired
    private  TcpClient tcpClient;

    @Autowired
    private  TCPMessageFormatter formatter;

    @Autowired
    private InboundMessageProcessor msgProcessor;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtProvider jwtProvider;
    private static final String[] RUS = {"А","Б","В","Г","Д","Е","Ё","Ж","З","И","Й","К","Л","М","Н","О","П","Р","С","Т","У","Ф","Х","Ц","Ч","Ш","Щ","Ъ","Ы","Ь","Э","Ю","Я"};
    private static final String LOGIN_PROCEDURE =  "NSPRFOT08";
    private static final String ORDER_MGR_PROCEDURE =  "NSPRFPLROUTREP";
    private static final String CLAIMS_PROCESSING_PROCEDURES =  "NSPRFREPPRET1";
    private final static String DELIMITER = "`";

    private final static String RELEASE_ACTION = "0";
    private final static String ALLOCATE_ACTION = "1";
    private final static String SHIP_ACTION = "2";
    private final static String UNALLOCATED_ACTION = "3";
    private final static String CLOSE_ACTION = "4";

    private static final String CLOSE_ALL = " ALL ";
    private final static String LOCALE = "ru_ru";
    private final static String LOGIN_STORED_PROCEDURE_MESSAGE = LOCALE + DELIMITER + "%s" + DELIMITER;
    private final static String RELEASE_STORED_PROCEDURE_MESSAGE = "%s"+ DELIMITER+ "%s" + DELIMITER;

    private final static String LOGIN_ERROR_MESSAGE = "Ошибка входа в систему. Для пользователя: %s";
    private final static String RELEASE_ERROR_MESSAGE = "Ошибка запуска волны %s";
    private final static String ALLOCATE_ERROR_MESSAGE = "Ошибка резервирования заказа %s";
    private final static String SHIP_ERROR_MESSAGE = "Ошибка отгрузки заказа %s";
    private final static String UNALLOCATED_ERROR_MESSAGE = "Ошибка разрезервирования заказа %s";
    private final static String CLOSE_ORDER_ERROR_MESSAGE = "Ошибка закрытия заказа %s";
    private final static String CREATE_INV_TASK_ERROR_MESSAGE = "Ошибка создания задачи на инвернтаризацию с параметрами %s, %s ";
    private final static String SEND_TO_HOST_ERROR_MESSAGE = "Ошибка отправки данных в гавную систему %s ";

    public AuthResponse login(String userId, String password){
        String message = formatter.format(LOGIN_PROCEDURE, userId, String.format(LOGIN_STORED_PROCEDURE_MESSAGE, password));
        String response = tcpClient.send(message);
        msgProcessor.noErrorOrThrow(response, () -> new LoginFailedException(String.format(LOGIN_ERROR_MESSAGE,userId)));
        UserDto userName = userDao.getUserByUserName(userId);
        return new AuthResponse(userName,jwtProvider.generateToken(userId));
    }

    public void release(String waveKey){
        send(ORDER_MGR_PROCEDURE, RELEASE_STORED_PROCEDURE_MESSAGE, RELEASE_ACTION, () -> new ReleaseFailedException(String.format(RELEASE_ERROR_MESSAGE, waveKey)), waveKey);
    }
    public void allocate(String orderKey){
        send(ORDER_MGR_PROCEDURE, RELEASE_STORED_PROCEDURE_MESSAGE, ALLOCATE_ACTION, () -> new ReleaseFailedException(String.format(ALLOCATE_ERROR_MESSAGE, orderKey)), orderKey);
    }
    public void unAllocate(String orderKey){
        send(ORDER_MGR_PROCEDURE, RELEASE_STORED_PROCEDURE_MESSAGE, UNALLOCATED_ACTION, () -> new ReleaseFailedException(String.format(UNALLOCATED_ERROR_MESSAGE, orderKey)), orderKey);
    }
    public void ship(String orderKey){
        send(ORDER_MGR_PROCEDURE, RELEASE_STORED_PROCEDURE_MESSAGE, SHIP_ACTION, () -> new ReleaseFailedException(String.format(SHIP_ERROR_MESSAGE, orderKey)), orderKey);
    }
    public void closeOrder(OrderCloseDto orderCloseDto){
        String params = orderCloseDto.getOrderKey() + CLOSE_ALL + orderCloseDto.getAllowBackOrder()+" "+ orderCloseDto.getStatus()+" "+ orderCloseDto.getBackOrderType();
        send(ORDER_MGR_PROCEDURE,
                RELEASE_STORED_PROCEDURE_MESSAGE,
                CLOSE_ACTION,
                () -> new OrderCloseFailedException(String.format(CLOSE_ORDER_ERROR_MESSAGE, orderCloseDto.getOrderKey())),
                params
        );
    }

    @Override
    public void createTask(String claimsNumber, String sku) {
        UserDto user = userDao.getUser();
        String msg = "0" + DELIMITER + doConvert(claimsNumber) + DELIMITER + sku + DELIMITER;
        String message = formatter.format(CLAIMS_PROCESSING_PROCEDURES, user.getLoginId(), msg);
        String response = tcpClient.send(message);
        msgProcessor.noErrorOrThrow(response, () -> new CreateInventoryTaskException(String.format(CREATE_INV_TASK_ERROR_MESSAGE,claimsNumber, sku)));
    }

    @Override
    public void sendToHost(String claimsNumber) {
        UserDto user = userDao.getUser();
        String msg = "1" + DELIMITER + doConvert(claimsNumber) + DELIMITER + "" + DELIMITER;
        String message = formatter.format(CLAIMS_PROCESSING_PROCEDURES, user.getLoginId(), msg);
        String response = tcpClient.send(message);
        msgProcessor.noErrorOrThrow(response, () -> new CreateInventoryTaskException(String.format(SEND_TO_HOST_ERROR_MESSAGE,claimsNumber)));
    }


    private void send(String procedureName, String tcpFormattedMessage, String action, Supplier<RuntimeException> exception, String params){
        String message = createInforMessage(procedureName, tcpFormattedMessage, action, params);
        String response = tcpClient.send(message);
        msgProcessor.noErrorOrThrow(response, exception);
    }


    public String createInforMessage(String procedureName, String tcpFormattedMessage, String action, String prams){
        String userId = userDao.getUser().getLoginId();
        return formatter.format(procedureName, userId, String.format(tcpFormattedMessage, action , prams));
    }

    public static String doConvert(String number){


        String[] keys = new String[RUS.length];

        for (int i = 0; i < keys.length; i++) {
            String formatted = String.format("%02d", i);
            keys[i] = "P" + formatted;
        }

        for (int i = 0; i < RUS.length; i++) {
            number = number.replaceAll(RUS[i], keys[i]);
        }

        return number;
    }



}

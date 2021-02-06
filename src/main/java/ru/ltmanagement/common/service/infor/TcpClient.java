package ru.ltmanagement.common.service.infor;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

@Component
public class TcpClient {

    @Value("${infor.host}")
    private String host;

    @Value("${infor.port}")
    private int port;

    private static final String UTF8 = "UTF-8";

    @SneakyThrows
    public String send(String message) {
        byte[] buffer = new byte[1024];
        InetAddress ipAddress = InetAddress.getByName(host);
        Socket socket = new Socket(ipAddress, port);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeBytes(message);
        dataOutputStream.flush();
        dataInputStream.read(buffer);
        socket.close();
        return new String(buffer, UTF8);
    }




}
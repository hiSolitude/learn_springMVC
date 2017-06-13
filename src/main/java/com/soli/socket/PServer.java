package com.soli.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Solitude on 2017/6/13.
 */
public class PServer {
    public static void main(String[] args) {
        try {
            //����һ��ServerSocket����8080�˿�
            ServerSocket server = new ServerSocket(8081);
            //�ȴ�����
            Socket socket = server.accept();
            //���յ������ʹ��socket����ͨ�ţ�����BufferedReader���ڶ�ȡ����
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = is.readLine();
            System.out.println("received  from  client"+line);
            //����PrintWriter�����ڷ�������
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println("received data: "+line);
            pw.flush();
            is.close();
            socket.close();
            server.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

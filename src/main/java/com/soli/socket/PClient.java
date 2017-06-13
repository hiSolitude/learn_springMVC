package com.soli.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Solitude on 2017/6/13.
 */
public class PClient {
    public static void main(String[] args) {
        String msg = "Client Data";
        try {
            //����һ��Socket,������ ��8081�˿�����
            Socket socket = new Socket("localhost",8081);
            //ʹ��Socket����PrintWriter��BufferedReeder���ж�ȡ����
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //��������
            pw.println(msg);
            pw.flush();
            //��������
            String line = is.readLine();
            System.out.println("received from server : "+line);
            //�ر���Դ
            pw.close();
            is.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

package com.soli.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.SocketHandler;

/**
 * Created by Solitude on 2017/6/13.
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{
        //����ServerSocketChannel������8081�˿�
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(8081));
        //����Ϊ������ģʽ
        ssc.configureBlocking(false);
        //Ϊsscע��ѡ����
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //����������
        Handler handler = new Handler(1024);
        while (true){
            //�ȴ�����ÿ�εȴ�����3s������3s���̼߳����������У��������0���ǲ���������һֱ����
            if (selector.select(3000)==0){
                System.out.println("�ȴ�����ʱ.......");
                continue;
            }
            System.out.println("��������");
            //��ȡ�������SelectionKey
            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();

            while (keyIter.hasNext()){
                SelectionKey key = keyIter.next();
                try {
                    //���յ���������ʱ
                    if (key.isAcceptable()){
                        handler.handlerAccept(key);
                    }
                    //������
                    if (key.isReadable()){
                        handler.handleRead(key);
                    }
                }catch (IOException ex){
                    keyIter.remove();
                    continue;
                }
                //������ɺ󣬴Ӵ������SelectionKey���������Ƴ���ǰ��ʹ�õ�key
                keyIter.remove();
            }
        }
    }

    private static class Handler{
        private int bufferSize = 1024;
        private String localCharset = "UTF-8";
        public Handler(){}
        public Handler(int bufferSize){
            this(bufferSize,null);
        }
        public Handler(String LocalCharset){
            this(-1,LocalCharset);
        }
        public Handler(int bufferSize,String localCharset){
            if (bufferSize>0)
                this.bufferSize= bufferSize;
            if (localCharset!=null)
                this.localCharset = localCharset;
        }

        public void handlerAccept(SelectionKey key) throws IOException{
            SocketChannel sc = ((ServerSocketChannel)key.channel()).accept();
            sc.configureBlocking(false);
            sc.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
        }

        public void handleRead(SelectionKey key) throws IOException{
            //��ȡchannnel
            SocketChannel sc = (SocketChannel)key.channel();
            //��ȡbuffer������
            ByteBuffer buffer = (ByteBuffer)key.attachment();
            buffer.clear();
            //û�ж���������ر�
            if (sc.read(buffer)==-1){
                sc.close();
            }else {
                //��bufferת��Ϊ ��״̬
                buffer.flip();
                //��buffer�н��յ���ֵ��localCharset��ʽ����󱣴浽receivedString
                String receivedString = Charset.forName(localCharset).newDecoder().decode(buffer).toString();
                System.out.println("received from client: "+receivedString);

                //�������ݸ��ͻ���
                String sendString = "received data :" +receivedString;
                buffer = ByteBuffer.wrap(sendString.getBytes(localCharset));
                sc.write(buffer);
                //�ر�Socket
                sc.close();
            }
        }


    }

}

package com.candy.minatest;

import android.util.Log;


import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;


/**
 * Created by candy on 2015/4/18.
 */
public class MinaThread extends Thread {
    private static final int PORT = 2046;
    private static final String IP = "192.168.1.101";
    private IoSession session = null;
    @Override
    public void run() {
        super.run();
        Log.d("Mina","现在尝试连接服务器");
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(30000);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(),LineDelimiter.WINDOWS.getValue())));
        connector.setHandler(new MinaClientHandler());
        try{
               ConnectFuture future = connector.connect(new InetSocketAddress(IP,PORT));//创建链接
                      future.awaitUninterruptibly();// 等待连接创建完成
                      session = future.getSession();//获得session
                      session.write("start");
                  }catch (Exception e){
                      Log.d("TEST","客户端链接异常...");
                  }
               session.getCloseFuture().awaitUninterruptibly();//等待连接断开
               Log.d("TEST", "客户端断开...");
               connector.dispose();
               super.run();
    }


    public class MinaClientHandler extends IoHandlerAdapter{
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            super.messageReceived(session, message);
            Log.d("收到","消息"+ message);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
        }
    }


}

package com.dex.dex.utils;

import com.neovisionaries.ws.client.*;

import java.io.IOException;

public class socket {
    public void tset() throws WebSocketException, IOException {
        // 1. Create a WebSocketFactory instance.
        WebSocketFactory factory = new WebSocketFactory();

// 2. Set up information about a proxy server.
//    Credentials can be set here.
        ProxySettings settings = factory.getProxySettings();
        settings.setServer("http://proxy.example.com");
        settings.setCredentials("id", "password");

// 3. Connect to a WebSocket endpoint via the proxy.
        WebSocket ws = factory.createSocket("ws://websocket.example.com");

        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket ws, String message) {
                System.out.println(message);
            }
        });
        ws.disconnect();
    }
}

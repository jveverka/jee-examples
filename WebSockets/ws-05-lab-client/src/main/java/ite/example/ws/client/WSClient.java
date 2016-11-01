package ite.example.ws.client;

import java.net.URI;
import java.net.URISyntaxException;

import ite.example.services.ws.dto.RequestMessage;

public class WSClient {
	
	public static void main(String[] args) {
		try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:8080/ws-05-lab/data"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            RequestMessage request = new RequestMessage(System.currentTimeMillis(),0,"Hello from java SE client !");
            System.out.println(request.toString());
            System.out.println(request.toJsonString());
            // send message to websocket
            clientEndPoint.sendMessage(request.toJsonString());

            // wait 5 second for messages from websocket
            Thread.sleep(10000);
            
            clientEndPoint.closeConnection();
            
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
	}
	
}

package no.kreutzer.utils;

import java.io.IOException;

import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketRelay {
    private static final Logger log = LoggerFactory.getLogger(WebsocketRelay.class);
	private Session rpi;
	private Session gui;
	
	public void setRPI(Session session) {
		rpi = session;
		removeOnMessageHandler(session);
		rpi.addMessageHandler(new MessageHandler.Whole<String>() {
		    @Override
		    public void onMessage(String message) {
		    	log.info("fromRPItoGUI called: "+message);
		    	if (gui != null) {
		    		try {
						gui.getBasicRemote().sendText(message);
					} catch (IOException e) {
						log.error(e.getMessage());
					}
		    	} else {
		    		log.error("GUI not connected");
		    	}
		    }
		});
	}
	public void setGUI(Session session) {
		gui = session;
		removeOnMessageHandler(session);
		gui.addMessageHandler(new MessageHandler.Whole<String>() {
		    @Override
		    public void onMessage(String message) {
		    	log.info("fromGUItoRPI called: "+message);
		    	if (rpi != null) {
		    		try {
						rpi.getBasicRemote().sendText(message);
					} catch (IOException e) {
						log.error(e.getMessage());
					}
		    	} else {
		    		log.error("RPI not connected");
		    	}
		    }
		});
	}
	
	private void removeOnMessageHandler(Session session) {
	    for (MessageHandler handler : session.getMessageHandlers()) {
	    	log.info("Handler: "+handler.getClass().toGenericString());
	        if ((handler instanceof MessageHandler.Whole)) {
	        	session.removeMessageHandler(handler);
	        	log.info("removed");
	        }
	    }		
	}
}

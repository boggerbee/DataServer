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
		    		log.warn("GUI not connected");
		    		try {
						rpi.getBasicRemote().sendText("No GUI");
					} catch (IOException e) {
						log.error(e.getMessage());
					}
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
		    		log.warn("RPI not connected");
		    		try {
						gui.getBasicRemote().sendText("No RPI");
					} catch (IOException e) {
						log.error(e.getMessage());
					}
		    	}
		    }
		});
	}
	
	public void closeSession(Session session) {
		if (session.equals(gui)) {
			gui = null;
	    	log.info("gui disconnected");
		} else if (session.equals(rpi)) {
			rpi = null;
	    	log.info("rpi disconnected");
	    	if (gui != null) {
	    		try {
					gui.getBasicRemote().sendText("RPI disconnected");
				} catch (IOException e) {
					log.error(e.getMessage());
				}
	    	}		
	    }
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

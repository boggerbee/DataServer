package no.kreutzer.utils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import restx.factory.Component;

@Component
@ServerEndpoint(value = "/websocket")
public class WebsocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebsocketServer.class);

    private static final String GUEST_PREFIX = "Client";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final WebsocketRelay relay = new WebsocketRelay();

    private final String nickname;
    private Session session;

    public WebsocketServer() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
        log.info("Created: "+nickname);
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        log.info("Websocket connected: "+session.getId());
        try {
			session.getBasicRemote().sendText("WHOAREYOU");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    }

    @OnClose
    public void end() {
        log.info("OnClose called");
        relay.closeSession(session);
    }

    @OnMessage
    public void incoming(String message) {
        log.info("Incoming: "+message);
        if (message.equals("GUI")) {
        	log.info("Is GUI client: "+session.getId());
        	relay.setGUI(session);
        } else if (message.equals("RPI")) {
        	log.info("Is RPI client: "+session.getId());
        	relay.setRPI(session);
        } else {
        	log.error("Unable to set up Relay with message:"+message);
        }
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        log.error("OnError: " + t.toString(), t);
    }

}
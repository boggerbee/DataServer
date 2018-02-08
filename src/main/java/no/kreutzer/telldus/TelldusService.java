package no.kreutzer.telldus;

import java.io.IOException;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import restx.factory.Component;

@Component
public class TelldusService {
    private static final Logger log = LoggerFactory.getLogger(TelldusService.class);
    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    /** Global instance of the JSON factory. */
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    static final String BASE_URL = "https://api.telldus.com/json";
    private HttpRequestFactory requestFactory;
    
    public TelldusService(@Named("consumer.secret") String consumerSecret, @Named("token.secret") String tokenSecret,
                          @Named("public.key") String publicKey, @Named("token") String token) {
        OAuthHmacSigner signer = new OAuthHmacSigner(); 
        signer.clientSharedSecret = consumerSecret;
        signer.tokenSharedSecret = tokenSecret;  
        
        OAuthParameters authorizer = new OAuthParameters(); 
        authorizer.consumerKey = publicKey;
        authorizer.signer = signer; 
        authorizer.token = token; 
        
        requestFactory = HTTP_TRANSPORT.createRequestFactory(authorizer);
    }
    
    public TelldusService(OAuthParameters consumer) {
        requestFactory = HTTP_TRANSPORT.createRequestFactory(consumer);
    }

    public CommandStatus setDeviceStatus(int id,boolean isOn) throws IOException {
        return (CommandStatus) executeAndParse(CommandStatus.class, BASE_URL+"/device/command?id="+id+"&method="+(isOn?1:2));
    }
    
    public DeviceList getDevices() throws IOException {
        return (DeviceList) executeAndParse(DeviceList.class, BASE_URL+"/devices/list");
    }
    
    @SuppressWarnings("unchecked")
    protected Object executeAndParse(@SuppressWarnings("rawtypes") Class c, String u) throws IOException {
        TelldusUrl url = new TelldusUrl(u);
        HttpRequest request = requestFactory.buildGetRequest(url);
        
        JsonObjectParser parser = new JsonObjectParser(JSON_FACTORY);
        request.setParser(parser);
        
        return request.execute().parseAs(c);
    }
}

package no.kreutzer.telldus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OAuth 2 credentials found in the <a href="https://api.telldus.com/keys/showToken">
 * Developer Profile Page</a>.
 *
 * <p>
 * From https://api.telldus.com/keys/showToken
 * 
    Public key: FEHUVEW84RAFR5SP22RABURUPHAFRUNU
    Private key:    ZUXEVEGA9USTAZEWRETHAQUBUR69U6EF
    Token:  f821bfbe3f2b33b0864605c897cdc30705a33f1be
    Token secret:   3f97bca9b90f77fcfbdf8b573ab2b51e
 * </p>
 *
 */
public class OAuth2ClientCredentials {
  private static final Logger log = LoggerFactory.getLogger(OAuth2ClientCredentials.class);

  /** Value of the "API Key". */
  public static final String API_KEY = "FEHUVEW84RAFR5SP22RABURUPHAFRUNU";

  /** Value of the "API Secret". */
  public static final String API_SECRET = "3f97bca9b90f77fcfbdf8b573ab2b51e";

  /** Port in the "Callback URL". */
  public static final int PORT = 8080;

  /** Domain name in the "Callback URL". */
  public static final String DOMAIN = "127.0.0.1";

  public static void errorIfNotSpecified() {
    if (API_KEY.startsWith("Enter ") || API_SECRET.startsWith("Enter ")) {
     log.error(
          "Enter API Key and API Secret from https://api.telldus.com/keys/showToken"
          + " into API_KEY and API_SECRET in " + OAuth2ClientCredentials.class);
    }
  }
}

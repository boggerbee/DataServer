package no.kreutzer.telldus;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class TelldusUrl extends GenericUrl{
      @Key
      private String fields;

      public TelldusUrl(String encodedUrl) {
        super(encodedUrl);
      }

      /**
       * @return the fields
       */
      public String getFields() {
        return fields;
      }

      /**
       * @param fields the fields to set
       */
      public void setFields(String fields) {
        this.fields = fields;
      }
 }
package no.kreutzer.telldus;

import com.google.api.client.util.Key;

public class Device {
    @Key
    public String id;
    @Key
    public String clientDeviceId;
    @Key
    public String name;
    @Key
    public int state;
    @Key
    public String type;
    @Key
    public String client;
    @Key
    public String ClientName;
    @Key
    public String online;
    @Key
    public int editable;
    @Key
    public int ignored;
}

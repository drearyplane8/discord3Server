package com.example.discord2server;

public class Discord2Protocol {

    public static final String CRED_REQUEST = "CanIHaveDBCreds";
    public static final String CLIENT_MESSAGE_SENT = "ISentAMsg";
    public static final String CLIENT_CHECK_MESSAGES = "CheckForMsgs";

    public static final String URL_SEND = "URL\"%s\"";
    public static final String UN_SEND = "UN\"%s\"";
    public static final String PWD_SEND = "PWD\"%s\"";


    public static String[] GetCredsToSend(){
        return new String[]{
            String.format(URL_SEND, Globals.URL),
                String.format(UN_SEND, Globals.USERNAME),
                String.format(PWD_SEND, Globals.PASSWORD)
        };
    }
}

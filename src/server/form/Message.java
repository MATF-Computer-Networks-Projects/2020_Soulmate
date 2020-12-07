package server.form;

/* Abstraction for message sent by one client to another */
public class Message {

    private final String user;
    private final String msg;

    public Message( String contentData ) {
        String[] pairs = contentData.split("&");

        this.user = pairs[0].split("=")[1];
        this.msg  = pairs[1].split("=")[1];
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return msg;
    }
}

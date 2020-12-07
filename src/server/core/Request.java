package server.core;

/* Encapsulates parsed request body */
public class Request {

    private final String method;
    private final String fileRequested;
    private final String contentData;
    private String address = null;


    public Request(String method
                 , String fileRequested
                 , String contentData) {
        this.method = method;
        this.fileRequested = fileRequested;
        this.contentData = contentData;
    }

    public Request(String method, String fileRequested) {
        this(method, fileRequested, null);
    }

    public String getMethod() {
        return method;
    }


    public String getFileRequested() {
        return fileRequested;
    }


    public String getContentData() {
        return contentData;
    }

    public  void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }
}

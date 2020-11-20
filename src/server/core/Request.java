package server.core;

/* Encapsulates parsed request body */
public class Request {

    private final String method;
    private final String fileRequested;
    private final String contentData;


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
}

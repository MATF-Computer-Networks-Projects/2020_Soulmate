package Server;

public class Request {
    private String method;
    private String fileRequested;
    private String contentData;

    public Request(String method
                 , String fileRequested
                 , String contentData) {
        this.method = method;
        this.fileRequested = fileRequested;
        this.contentData = contentData;
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

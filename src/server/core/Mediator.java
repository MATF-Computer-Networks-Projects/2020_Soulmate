package server.core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.StringTokenizer;


/* Communicator between server and client */
public class Mediator {

    private final BufferedReader in;
    private final PrintWriter out;
    private final BufferedOutputStream dataOut;



    public Mediator(BufferedReader in
                  , PrintWriter out
                  , BufferedOutputStream dataOut) throws NullPointerException {

        if(in == null || out == null || dataOut == null)
            throw new NullPointerException("Stream null");

        this.in = in;
        this.out = out;
        this.dataOut = dataOut;
    }



    public Request parseRequest() throws IOException {
        String input = in.readLine();

        if(input.isEmpty()) {
            out.println("400 Failed to get input");
            out.flush();

            return null;
        }

        StringTokenizer parser = new StringTokenizer(input);
        String method = parser.nextToken().toUpperCase();
        String fileRequested = parser.nextToken().toLowerCase();
        
        if(!method.equals("POST")) {
            return new Request(method, fileRequested);
        }

        // parsing length of request body
        int contentLength = 0;
        while(!input.isEmpty()) {
            if(input.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(input.split(" ")[1]);
            }
            input = in.readLine();
        }

        StringBuilder content = new StringBuilder();
        for(int i = 0; i < contentLength; i++) {
            content.append((char)in.read());
        }

        return new Request(method, fileRequested, content.toString());
    }



    public void respond(String content
                      , byte[] fileData) throws IOException{

        int fileLength = fileData.length;

        println("Date: " + new Date());
        println("Content-type: " + content);
        println("Content-length: " + fileLength);
        println();
        flushOut();

        write(fileData, 0, fileLength);
        flushDataOut();
    }

    // Encapsulating stream methods
    public void println(String s) {
        out.println(s);
    }
    public void println() {
        out.println();
    }

    public void flushOut() {
        out.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException {
        dataOut.write(b, off, len);
    }

    public void flushDataOut() throws IOException {
        dataOut.flush();
    }

}

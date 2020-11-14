package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class Server implements Runnable{

    static final File WEB_ROOT = new File("web");
    static final String DEFAULT_FILE = "signup/index.html";
    static final String FILE_NOT_FOUND = "errors/404.html";
    static final String METHOD_NOT_SUPPORTED = "errors/not_supported.html";
    static final int PORT = 8080;

    private static boolean verbose = true;  // Internal debug switch
    private Socket socket;

    Server(Socket s) {
        this.socket = s;
    }

    public static void startServer() throws IOException {
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Listening on port:" + PORT);

        while(true) {

            Server newServer = new Server(ss.accept());
            if(verbose) {
                System.out.println("Connecton opened. (" + new Date() + ")");
            }

            Thread thread = new Thread(newServer);
            thread.start();

        }
    }

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            dataOut = new BufferedOutputStream(socket.getOutputStream());

            String input = in.readLine();

            if(input.isEmpty()) {
                out.println("400 Failed to get input");
                out.flush();

                return;
            }

            StringTokenizer parse = new StringTokenizer(input);

            String method = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();


            if ( checkMethod(method, out, dataOut) ) {

                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }


                String[] fileTree = fileRequested.split("/");
                int idx = fileTree.length - 1;
                String fileName = fileTree[idx];

                StringBuilder fileDir = new StringBuilder();
                for(int i = 0; i < idx; i++) {
                    fileDir.append('/');
                    fileDir.append(fileTree[i]);
                }

                fileDir.insert(0, WEB_ROOT);

                File file = new File(fileDir.toString(), fileName);
                int fileLength = (int) file.length();
                String content = getContentType(fileName);

                if (method.equals("GET")) {

                    byte[] fileData = readFileData(file, fileLength);

                    out.println("HTTP/1.1 200 OK");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println();
                    out.flush();

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if (verbose) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }

            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException e) {
                System.err.println("Error with file not found exception : " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Server error : " + e);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                socket.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }


    /* =============== UTIL PRIVATE METHODS ======================= */

    private boolean checkMethod(String reqMethod
                              , PrintWriter out
                              , BufferedOutputStream dataOut) throws IOException {

        if (!reqMethod.equals("GET") && !reqMethod.equals("HEAD")) {
            if (verbose) {
                System.out.println("501 Not Implemented : " + reqMethod + " method.");
            }

            File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
            int fileLength = (int) file.length();
            String contentMimeType = "text/html";

            byte[] fileData = readFileData(file, fileLength);


            out.println("HTTP/1.1 501 Not Implemented");
            out.println("Date: " + new Date());
            out.println("Content-type: " + contentMimeType);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();

            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();

            return false;
        }

        return true;
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }


    private String getContentType(String fileRequested) {
        /*
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";

         */
        String ext = fileRequested.substring( fileRequested.lastIndexOf('.') );

        switch (ext) {
            case ".htm":
                return "text/html";
            case ".html":
                return "text/html";
            case ".css":
                return "text/css";
            case ".js":
                return "application/js";
            case ".jpg":
                return "image/jpeg";
            default:
                return "text/plain";
        }
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            System.out.println("File " + fileRequested + " not found");
        }
    }


}

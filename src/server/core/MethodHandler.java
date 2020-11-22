package server.core;

import server.form.User;

import java.io.File;
import java.io.IOException;

import static server.GlobalData.*;
import static server.Utils.*;


public class MethodHandler{

    public static void handle(Request request, Mediator mediator) throws IOException {

        String method = request.getMethod();
        String fileRequested = request.getFileRequested();

        if ( checkMethod(method, mediator) ) {

            if (fileRequested.endsWith("/")) {
                fileRequested += DEFAULT_FILE;
            }

            File file = new File(WEB_ROOT, fileRequested);


            switch (method) {
                case "GET":
                    methodGET(file, mediator);
                    break;

                case "POST":
                    methodPOST(file, request.getContentData(), mediator);
                    break;

                default:
                    break;
            }

            if (verbose) {
                System.out.println("File " + fileRequested + " of type " + getContentType(fileRequested) + " returned");
            }

        }
    }

     /*==================================== */
    /* ========= Request methods ========= */
   /*==================================== */

    private static void methodGET(File file
                                , Mediator mediator) throws IOException {

        int fileLength = (int) file.length();
        String content = getContentType(file.getName());

        byte[] fileData = readFileData(file, fileLength);

        mediator.println("HTTP/1.1 200 OK");
        mediator.respond(content, fileData);
    }

    private static void methodPOST(File file
                                 , String contentData
                                 , Mediator mediator) throws IOException {

        User usr = new User(contentData);
        usr.toString();

        if(file.getParent().endsWith("signup")
                || file.getParent().endsWith("login")) {
            // Using because of behaviour
            methodGET(new File(WEB_ROOT, "chat/chat.html"), mediator);
            return;
        }

        mediator.println("HTTP/1.1 501 Not Implemented");
        File f = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
        mediator.respond("text/html", readFileData(f, (int)f.length()));
    }




    /* =============== UTIL PRIVATE METHODS ======================= */

    private static boolean checkMethod(String reqMethod
                                    , Mediator mediator) throws IOException {

        if ( reqMethod.equals("GET") || reqMethod.equals("HEAD") || reqMethod.equals("POST"))
            return true;

        // else
        if (verbose) {
            System.out.println("501 Not Implemented : " + reqMethod + " method.");
        }

        File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
        int fileLength = (int) file.length();
        String content = "text/html";

        byte[] fileData = readFileData(file, fileLength);


        mediator.println("HTTP/1.1 501 Not Implemented");
        mediator.respond(content, fileData);

        return false;

    }


    private static String getContentType(String fileRequested) {
        String ext = fileRequested.substring( fileRequested.lastIndexOf('.') );

        switch (ext) {
            case ".htm":
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
}

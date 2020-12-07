package server.core;

import server.form.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static server.GlobalData.*;
import static server.Utils.*;


public class MethodHandler{

    public static void handle(Request request, Mediator mediator) throws IOException {

        String method = request.getMethod();
        String fileRequested = request.getFileRequested();
        String address = request.getAddress();

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
                    methodPOST(file, address, request.getContentData(), mediator);
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
                                 , String address
                                 , String contentData
                                 , Mediator mediator) throws IOException {


        if(file.getParent().endsWith("signup")) {

            User usr = new User(contentData);
            writeJSONtoFile(usr);
            System.out.println(usr);

            // Using GET because of behaviour
            methodGET(new File(WEB_ROOT, "chat/chat.html"), mediator);

            Server.activeUsers.addUser(usr, address);
            return;
        }

        if(file.getParent().endsWith("login")) {

            String[] pairs = contentData.split("&");
            String mail = java.net.URLDecoder.decode(pairs[0].split("=")[1], "UTF-8");
            String password = pairs[1].split("=")[1];



            User user = loadUserFromJSON(mail, password);

            if(user == null)
            {
                System.err.println("User not found: " + mail + "  :  " + password);
                methodGET(new File(WEB_ROOT, USER_NOT_FOUND), mediator);
            }


            // Using GET because of behaviour
            methodGET(new File(WEB_ROOT, "chat/chat.html"), mediator);

            return;
        }

        if(file.getParent().endsWith("chat")) {

            Message msg = new Message(contentData);

            Server.messages.add(msg);

            String content = getContentType(file.getName());
            StringBuilder allMsgs = new StringBuilder();

            synchronized (Server.messages) {
                for(int i = 0; i < Server.messages.size(); i++) {
                    Message m = Server.messages.get(i);
                    if(m.getUser().equals(msg.getUser()))
                        continue;

                    allMsgs.append("\n" + appendMessageReceiver(m.getMessage()) );
                }
            }

            allMsgs.append("\n" + appendMessageSender(msg.getMessage()) );

            byte[] fileData = allMsgs.toString().getBytes();

            mediator.println("HTTP/1.1 200 OK");
            mediator.respond(content, fileData);

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

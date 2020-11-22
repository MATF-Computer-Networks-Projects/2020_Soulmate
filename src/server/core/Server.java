package server.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Objects;

import static server.GlobalData.*;
import static server.Utils.*;

public class Server implements Runnable{

    private final Socket socket;

    Server(Socket s) {
        this.socket = s;
    }

    @SuppressWarnings("InfiniteLoopStatement")
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
            Mediator mediator = new Mediator(in, out, dataOut);

            /* Parsing input */
            Request req = mediator.parseRequest();
            if(req == null)
                return;

            fileRequested = req.getFileRequested();

            /* Method handling */
            MethodHandler.handle(req, mediator);


        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(new Mediator(in, out, dataOut), fileRequested);
            } catch (IOException e) {
                System.err.println("Error with file not found exception : " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Server error : " + e);

        } finally { // Closing opened streams

            try {

                Objects.requireNonNull(in).close();
                Objects.requireNonNull(out).close();
                Objects.requireNonNull(dataOut).close();
                socket.close();

            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }

}

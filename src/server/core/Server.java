package server.core;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
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

        // System.setProperty("jdk.tls.server.protocols", "TLSv1.2");

        System.setProperty("javax.net.ssl.keyStore", KS_PATH);
        System.setProperty("javax.net.ssl.keyStorePassword", KS_PASS);
        System.setProperty("javax.net.ssl.trustStore", TS_PATH);
        System.setProperty("javax.net.ssl.trustStorePassword", TS_PASS);

        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(PORT);
        // ss.setNeedClientAuth(true);


        String[] supported = ss.getSupportedCipherSuites();
        System.out.println(Arrays.toString(supported));
        // Then get all those which don't require authentication (_anon_)
        String[] anonCipherSuitesSupported = new String[supported.length];
        int numAnonCipherSuitesSupported = 0;
        for (String s : supported) {
            if (s.indexOf("_anon_") > 0)
                anonCipherSuitesSupported[numAnonCipherSuitesSupported++] = s;
        }

        // Combine
        String[] oldEnabled = ss.getEnabledCipherSuites();
        String[] newEnabled = new String[oldEnabled.length + numAnonCipherSuitesSupported];
        System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
        System.arraycopy(anonCipherSuitesSupported, 0, newEnabled, oldEnabled.length, numAnonCipherSuitesSupported);

        ss.setEnabledCipherSuites(newEnabled);


        //ServerSocket ss = new ServerSocket(PORT);
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

        System.out.println("connected - " + socket.getRemoteSocketAddress());

        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
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


        } catch (SSLHandshakeException e) {
            ; // TODO SSLHandshakeException
        } catch (IOException e) {
            // System.err.println("Server error : " + e);
            e.printStackTrace();

        } finally { // Closing opened streams

            try {
                socket.close();

            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed - " + socket.getRemoteSocketAddress() + ".\n");
            }
        }
    }

}

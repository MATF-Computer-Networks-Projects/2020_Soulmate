package Server;

import java.io.*;

import static Server.GlobalData.*;

public class Utils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static byte[] readFileData(File file, int fileLength) throws IOException {
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


    public static void fileNotFound(Mediator mediator
                                  , String fileRequested) throws IOException {

        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        mediator.println("HTTP/1.1 404 File Not Found");
        mediator.respond(content, fileData);

        if (verbose) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

}

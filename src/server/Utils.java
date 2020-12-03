package server;

import server.core.Mediator;

import java.io.*;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.form.User;

import static server.GlobalData.*;

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

    public static void writeJSONtoFile(User user)
    {
        //First Employee
        JSONObject loginDetails = new JSONObject();
        loginDetails.put("Name", user.getName());
        loginDetails.put("Birthday", user.getBirthdate() + "");
        loginDetails.put("Geneder", user.getGender() + "");
        loginDetails.put("Interest", user.getInterest() + "");
        loginDetails.put("Email", user.getEmail());
        loginDetails.put("Password", user.getPassword());
        loginDetails.put("Phone", user.getPhone());


        JSONObject loginObject = new JSONObject();
        loginObject.put("User", loginDetails);


        //Add employees to list
        JSONArray loginList = new JSONArray();
        loginList.add(loginObject);

        //Write JSON file
        try (FileWriter file = new FileWriter("login.json")) {

            file.write(loginList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package server;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.core.Mediator;

import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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

    public static void writeJSONtoFile(User user) {
        JSONParser jsonParser = new JSONParser();
        JSONArray userList = null;
        File loginFile = null;
        FileReader reader = null;
        FileWriter file = null;

        try {
            loginFile = new File("login.json");


            // If does not exist, create json file with []
            // because it won't make it by itself
            if (!loginFile.exists()) {
                loginFile.createNewFile();
                FileWriter tmpWriter = new FileWriter(loginFile);
                tmpWriter.write("[]");
                tmpWriter.close();
            }


            reader = new FileReader(loginFile);
            userList = (JSONArray) jsonParser.parse(reader);

            JSONObject loginDetails = new JSONObject();
            loginDetails.put("Name", user.getName());
            loginDetails.put("Birthday", user.getBirthdate() + "");
            loginDetails.put("Gender", user.getGender() + "");
            loginDetails.put("Interest", user.getInterest() + "");
            loginDetails.put("Password", user.getPassword());
            loginDetails.put("Phone", user.getPhone());


            JSONObject loginObject = new JSONObject();
            loginObject.put(user.getEmail(), loginDetails);


            //Add users to list

            userList.add(loginObject);


            //Write JSON file
            file = new FileWriter(loginFile);

            file.write(userList.toJSONString());
            file.flush();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert reader != null && file != null;
                reader.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static User loadUserFromJSON(String mail, String password) {
        JSONParser jsonParser = new JSONParser();
        User user;

        try (FileReader reader = new FileReader("login.json")) {
            //Read JSON file
            JSONArray userList = (JSONArray) jsonParser.parse(reader);

            //Iterate over user array
            for (Object usr : userList) {

                JSONObject usrObject = (JSONObject) ((JSONObject) usr).get(mail);

                if (usrObject == null)
                    continue;

                if (usrObject.get("Password").equals((Object) password)) {
                    user = new User(
                            usrObject.get("Name").toString(),
                            null,
                            User.genderFromString(usrObject.get("Gender").toString()),
                            User.interestFromString(usrObject.get("Interest").toString()),
                            mail,
                            password,
                            usrObject.get("Phone").toString()
                    );

                    System.out.println("Return user:\n" + user);
                    return user;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String appendMessageSender(String message) {

        return "<div class=\"row no-gutters\">\n" +
                "\t\t\t\t<div class=\"col-md-3 offset-md-9\">\n" +
                "\t\t\t\t<div class=\"chat-bubble chat-bubble--right\">\n" +
                "\t\t\t\t\t" + message + "\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</div>";

    }

    public static String appendMessageReceiver(String message) {

        return "<div class=\"row no-gutters\">\n" +
                "\t\t\t\t<div class=\"col-md-3\">\n" +
                "\t\t\t\t<div class=\"chat-bubble chat-bubble--left\">\n" +
                "\t\t\t\t\t" + message + "\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</div>";

    }


    public static boolean checkUserExistence(String mail) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("login.json")) {
            //Read JSON file
            JSONArray userList = (JSONArray) jsonParser.parse(reader);

            //Iterate over user array
            for (Object usr : userList) {

                JSONObject usrObject = (JSONObject) ((JSONObject) usr).get(mail);

                if (usrObject != null){
                    System.out.println("USR FOUND");
                    return true;
                }

            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.out.println("USR NOT FOUND");
        return false;
    }
}
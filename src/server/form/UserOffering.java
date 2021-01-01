package server.form;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserOffering {
    public static List<String> offerings(User u){

        Gender interest = u.getInterest();
        JSONParser jsonParser = new JSONParser();
        List<String> list = new ArrayList<>();
        try (FileReader reader = new FileReader("login.json")) {
            //Read JSON file
            JSONArray userList = (JSONArray) jsonParser.parse(reader);

            //Iterate over user array
            for (Object usr : userList) {
                JSONObject jo = (JSONObject)usr;

                for(Object var: jo.keySet()){

                    JSONObject usrObject = (JSONObject) ((JSONObject) usr).get(var);

                    if(usrObject.get("Gender").equals(interest.toString())){
                        list.add(usrObject.get("Name").toString());
                    }
                }
            }
            return list;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.err.println("Nothing found in JSON file");
        return null;
    }
}

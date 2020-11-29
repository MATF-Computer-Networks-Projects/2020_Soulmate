package server.form;

import java.util.Date;

enum Gender {
    MALE, FEMALE, OTHER
}

enum Interest {
    MALE, FEMALE, BOTH
}

public class User {

    private final String name;
    private final Date birthdate;
    private final Gender gender;
    private final Interest interest;
    private final String email;
    private final String password;  // TODO will be independent class
    private final String phone;
    // TODO image missing

    public User(String name
              , Date birthdate, Gender gender
              , Interest interest, String email
              , String password, String phone) {

        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.interest = interest;
        this.email = email;
        this.password = password;
        this.phone = phone;

    }
    public User(String contentData){

        String[] pairs = contentData.split("&");
        String[] userInfo = new String[7];

        int i = 0;
        for (String pair: pairs){
            int idx = pair.indexOf("=");
            //System.out.println(pair.substring(idx+1));
            userInfo[i] = pair.substring(idx+1);
            i++;
        }

        this.name = userInfo[0];
        //parsing email in format username%40domain
        this.email = userInfo[4].substring(0,userInfo[4].indexOf("%")) + "@" + userInfo[4].substring(userInfo[4].indexOf("%")+3);
        this.password = userInfo[5];
        this.phone = userInfo[6];
        String date = userInfo[1];
        String gender = userInfo[2];
        String interest = userInfo[3];

        switch (gender){
            case "Male": this.gender = Gender.MALE; break;
            case "Female": this.gender = Gender.FEMALE; break;
            case "Other": this.gender = Gender.OTHER; break;
            default: this.gender = null; break;
        }
        switch (interest){
            case "Male": this.interest = Interest.MALE; break;
            case "Female": this.interest = Interest.FEMALE; break;
            case "Other": this.interest = Interest.BOTH; break;
            default: this.interest = null; break;
        }

        this.birthdate = new Date(Integer.parseInt(date.substring(10))-1900, Integer.parseInt(date.substring(5,7))-1, Integer.parseInt(date.substring(0,2)));

    }

    @Override
    public String toString() {
         return "Name: " + this.name + "\n" +
                "Birthday: " + this.birthdate + "\n" +
                "Gender: " + this.gender + "\n" +
                "Interested in: " + this.interest + "\n" +
                "Email: " + this.email + "\n" +
                "Password: " + this.password + "\n" +
                "Phone: " + this.phone + "\n";

    }
}

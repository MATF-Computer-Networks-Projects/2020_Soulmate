package server.form;

import java.util.Date;

public class User {

    private final String name;
    private final Date birthdate;
    private final Gender gender;
    private final Gender interest;
    private final String email;
    private final String password;  // TODO will be independent class
    private final String phone;
    // TODO image missing

    public String getName() {
        return name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public Gender getInterest() {
        return interest;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public User(String name
              , Date birthdate, Gender gender
              , Gender interest, String email
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
            userInfo[i] = pair.substring(idx+1);
            i++;
        }

        this.name = userInfo[0];
        //parsing email in format username%40domain
        this.email = userInfo[4].substring(0,userInfo[4].indexOf("%")) + "@" + userInfo[4].substring(userInfo[4].indexOf("%")+3);
        this.password = userInfo[5];
        this.phone = userInfo[6];
        String date = userInfo[1];
        this.gender = genderFromString(userInfo[2]);
        this.interest = interestFromString(userInfo[3]);

        this.birthdate = new Date(Integer.parseInt(date.substring(10))-1900, Integer.parseInt(date.substring(5,7))-1, Integer.parseInt(date.substring(0,2)));

    }

    public static Gender genderFromString(String gender) {
        switch (gender){
            case "MALE":
            case "Male": return server.form.Gender.MALE;
            case "FEMALE":
            case "Female": return server.form.Gender.FEMALE;
            case "OTHER":
            case "Other": return server.form.Gender.OTHER;
            default: break;
        }

        return null;
    }

    public static Gender interestFromString(String interest) {
        switch (interest){
            case "MALE":
            case "Male":   return Gender.MALE;
            case "FEMALE":
            case "Female": return Gender.FEMALE;
            case "OTHER":
            case "Other":  return Gender.OTHER;
            default: break;
        }

        return null;
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

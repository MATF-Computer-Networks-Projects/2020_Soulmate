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

}

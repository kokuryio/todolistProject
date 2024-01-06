package models;

import java.io.Serializable;

public class LoginData implements Serializable {

    private static final long serialVersionUID = 2345678L;

    private String email;

    private String password;

    public LoginData(String email, String password){
        setEmail(email);
        setPassword(password);
    }

    public LoginData(){
        this(null, null);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }
}

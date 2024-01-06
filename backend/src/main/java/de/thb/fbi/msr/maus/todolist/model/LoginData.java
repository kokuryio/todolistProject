package de.thb.fbi.msr.maus.todolist.model;

import java.io.Serializable;

public class LoginData implements Serializable {

    private String email;

    private String password;

    private static final long serialVersionUID = 2345678L;

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

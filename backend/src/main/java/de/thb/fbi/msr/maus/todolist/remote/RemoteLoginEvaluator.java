package de.thb.fbi.msr.maus.todolist.remote;

import de.thb.fbi.msr.maus.todolist.model.LoginAccessor;
import de.thb.fbi.msr.maus.todolist.model.LoginData;



public class RemoteLoginEvaluator implements LoginAccessor {

    public boolean isLoginValid(LoginData login){

        String email = login.getEmail();
        String password = login.getPassword();


        if(email.equals("randomemail@th-brandenburg.de") && password.equals("123456")){
            return true;
        }else
            return false;
    }

}

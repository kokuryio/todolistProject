package de.thb.fbi.msr.maus.todolist.model;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/login")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface LoginAccessor {

    @POST
    public boolean isLoginValid(LoginData data);
}

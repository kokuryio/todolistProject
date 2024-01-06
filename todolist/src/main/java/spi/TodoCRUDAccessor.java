package spi;

import android.widget.ListAdapter;
import models.Todo;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/todos")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface TodoCRUDAccessor {

    @GET
    public List<Todo> readAllTodos();

    @POST
    public Todo createTodo(Todo todo);

    @DELETE
    @Path("/{itemId}")
    public boolean deleteTodo(@PathParam("itemId") long id);

    @PUT
    public Todo updateTodo(Todo todo);

}


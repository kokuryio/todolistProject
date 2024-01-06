package spi;

import activities.ToDoListActivity;
import models.Todo;
import remote.ResteasyTodoCRUDAccessor;

import java.util.List;

public class SwitchCRUDAccessor implements TodoCRUDAccessor{

    private RoomCRUDAccessor localAccessor;

    private ResteasyTodoCRUDAccessor remoteAccessor;

    private int accessorId;

    private TodoCRUDAccessor [] accessors;

    public SwitchCRUDAccessor(ToDoListActivity context, String baseUrl){

        localAccessor = new RoomCRUDAccessor(context);
        remoteAccessor = new ResteasyTodoCRUDAccessor(baseUrl);

        accessors = new TodoCRUDAccessor[]{localAccessor, remoteAccessor};

        if(remoteAccessor.testConnection())
            accessorId = 1;
        else
            accessorId = 0;

    }

    @Override
    public List<Todo> readAllTodos() {
        return accessors[accessorId].readAllTodos();
    }

    @Override
    public Todo createTodo(Todo todo) {

        Todo insert = todo;

        if(accessorId == 1){
            if(!(((RoomCRUDAccessor) accessors[0]).todoExists(todo.getId())))
                insert = accessors[0].createTodo(todo);
        }


        return accessors[accessorId].createTodo(insert);
    }

    @Override
    public boolean deleteTodo(long id) {

        if(accessorId == 1)
            accessors[0].deleteTodo(id);

        return accessors[accessorId].deleteTodo(id);
    }

    @Override
    public Todo updateTodo(Todo todo) {

        if(accessorId == 1)
            accessors[0].updateTodo(todo);

        return accessors[accessorId].updateTodo(todo);
    }

    public int getAccessorId(){
        return accessorId;
    }
}

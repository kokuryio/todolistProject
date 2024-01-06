package spi;

import activities.ToDoListActivity;
import models.Todo;
import room.AppDatabase;
import room.RoomTodoDAO;

import java.util.List;

public class RoomCRUDAccessor implements TodoCRUDAccessor {

    private AppDatabase mAppDB;

    private RoomTodoDAO mDAO;

    private ToDoListActivity context;

    public RoomCRUDAccessor(ToDoListActivity ToDoListActivity) {
        this.context = ToDoListActivity;
        init();
    }
    @Override
    public List<Todo> readAllTodos() {
        return mDAO.getAllTodos();
    }

    @Override
    public Todo createTodo(Todo todo) {
        mDAO.insert(todo);
        return todo;
    }

    @Override
    public boolean deleteTodo(long id) {
        int ret = mDAO.deleteTodoById(id);

        return (ret != 0);
    }

    @Override
    public Todo updateTodo(Todo todo) {
        mDAO.update(todo);
        return todo;
    }

    public boolean todoExists(long id){
        return mDAO.todoExists(id);
    }

    private void init() {
        mAppDB = AppDatabase.getInstance(context);
        mDAO = mAppDB.todoDao();
    }

}

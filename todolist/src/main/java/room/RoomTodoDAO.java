package room;



import androidx.room.*;
import models.Todo;

import java.util.List;
@Dao
public interface RoomTodoDAO {

    @Insert
    void insert(Todo aTodo);

    @Query("SELECT * FROM Todo")
    List<Todo> getAllTodos();

    @Update
    void update(Todo aTodo);


    @Query("SELECT EXISTS(SELECT 1 FROM Todo WHERE id = :todoId LIMIT 1)")
    boolean todoExists(long todoId);


    @Delete
    void deleteTodo(Todo todo);

    @Query("DELETE FROM Todo WHERE id = :todoId")
    int deleteTodoById(long todoId);

}

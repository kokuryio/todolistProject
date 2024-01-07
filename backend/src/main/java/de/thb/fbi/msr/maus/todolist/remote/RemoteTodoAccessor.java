package de.thb.fbi.msr.maus.todolist.remote;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import de.thb.fbi.msr.maus.todolist.model.Todo;
import de.thb.fbi.msr.maus.todolist.model.TodoCRUDAccessor;

public class RemoteTodoAccessor implements TodoCRUDAccessor {

	protected static Logger logger = Logger
			.getLogger(RemoteTodoAccessor.class);

	//This is where we put our Todo objects
	private static final List<Todo> sItemList = new ArrayList<Todo>();


	@Override
	public List<Todo> readAllTodos() {
		//Just log that we read our items, for debugging purposes
		logger.info("readAllTodos(): " + sItemList);

		return sItemList;
	}

	@Override
	public Todo createTodo(Todo todo) {
		sItemList.add(todo);
		return todo;
	}

	@Override
	public boolean deleteTodo(final long itemId) {
		logger.info("deleteTodo(): " + itemId);

		boolean removed = sItemList.remove(new Todo() {

			private static final long serialVersionUID = 71193783355593985L;

			@Override
			public long getId() {
				return itemId;
			}
		});

		return removed;
	}

	@Override
	public Todo updateTodo(Todo item) {
		logger.info("updateItem(): " + item);

		return sItemList.get(sItemList.indexOf(item)).updateFrom(item);
	}
}

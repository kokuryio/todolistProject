package remote;

import models.Todo;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import spi.TodoCRUDAccessor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class ResteasyTodoCRUDAccessor implements TodoCRUDAccessor {


   private TodoCRUDAccessor restClient;

    public ResteasyTodoCRUDAccessor(String baseUrl){

        this.restClient = ProxyFactory.create(TodoCRUDAccessor.class,
                baseUrl,
                new ApacheHttpClient4Executor());

    }

    @Override
    public List<Todo> readAllTodos(){
        List <Todo> todos = restClient.readAllTodos();
        return todos;
    }

    @Override
    public Todo createTodo(Todo todo) {
        todo = restClient.createTodo(todo);
        return todo;
    }

    @Override
    public boolean deleteTodo(long id) {
        boolean deleted = restClient.deleteTodo(id);
        return deleted;
    }

    @Override
    public Todo updateTodo(Todo todo) {
        Todo ret = restClient.updateTodo(todo);
        return ret;
    }

    public boolean testConnection() {
        AtomicBoolean isConnected = new AtomicBoolean(false);

        Thread thread = new Thread(() -> {
            try {
                // Make a test request to the server
                List<Todo> todos = restClient.readAllTodos();

                // If the request succeeds without any exception, the connection is successful
                isConnected.set(true);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                isConnected.set(false);
            }
        });

        thread.start();

        try {
            thread.join(1000); // Wait for the thread to complete or timeout after 1 second
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        if (thread.isAlive()) {
            // Thread is still running, consider it timed out
            thread.interrupt(); // Interrupt the thread
            isConnected.set(false); // Set the connection status to false
        }

        return isConnected.get();
    }
}

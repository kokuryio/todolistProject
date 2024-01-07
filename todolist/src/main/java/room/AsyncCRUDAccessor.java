package room;

import activities.ItemDetailsActivity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import models.Todo;
import remote.ResteasyTodoCRUDAccessor;
import spi.RoomCRUDAccessor;
import spi.SwitchCRUDAccessor;
import spi.TodoCRUDAccessor;
import de.thb.fbi.msr.maus.einkaufsliste.R;
import activities.ToDoListActivity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AsyncCRUDAccessor implements TodoCRUDAccessor {


    private SwitchCRUDAccessor switchCRUDAccessor;
    private ArrayAdapter<Todo> mAdapter;

    private ToDoListActivity context;

    public AsyncCRUDAccessor(ToDoListActivity ToDoListActivity) {
        //Change this to your backeend Url
        switchCRUDAccessor = new SwitchCRUDAccessor(ToDoListActivity, "http://your-url-here.com");
        context = ToDoListActivity;

        if(switchCRUDAccessor.getAccessorId() == 1){ //transfer data from local database to remote service if needed
            synchronize();
        }

    }


    //Access mostly copied from Prof. Schafföners code

    public ListAdapter getAdapter() {
        final List<Todo> items = new ArrayList<>();

            mAdapter = new ArrayAdapter<Todo>(context, R.layout.list_item_todo, R.id.todo_name) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    //get all UI elements
                    View view = super.getView(position, convertView, parent);
                    TextView textView = view.findViewById(R.id.todo_name);
                    ImageView greyStar = view.findViewById(R.id.imageView);
                    ImageView yellowStar = view.findViewById(R.id.imageView2);
                    ImageView greenTick = view.findViewById(R.id.imageView3);

                    //prepare the b&w filter for the green tick image view
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

                    Todo todo = getItem(position);
                    greyStar.setTag(todo);
                    yellowStar.setTag(todo);
                    greenTick.setTag(todo);
                    textView.setTag(todo);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                    LocalDateTime dueDate = todo.getDateTime();

                    if(dueDate != null)
                        textView.setText(todo.getName() + " " + dueDate.format(formatter));
                    else
                        textView.setText(todo.getName());

                    if(todo.getIsImportant() == true){
                        greyStar.setVisibility(View.INVISIBLE);
                        yellowStar.setVisibility(View.VISIBLE);
                    }else{
                        yellowStar.setVisibility(View.INVISIBLE);
                        greyStar.setVisibility(View.VISIBLE);
                    }

                    if (todo.getIsFinished()) {
                        greenTick.clearColorFilter(); // Remove color filter
                    } else {
                        greenTick.setColorFilter(filter);  // Change color from green to b&w
                    }


                    LocalDateTime dateTime = todo.getDateTime();
                    if (dateTime != null && dateTime.compareTo(LocalDateTime.now()) < 0 && !todo.getIsFinished()) {
                        textView.setTextColor(0xFFFF0000);
                    } else {
                        textView.setTextColor(0xFFFFFFFF);
                    }

                    getListener(yellowStar, false);

                    getListener(greyStar, true);

                    getOnClickListener(greenTick, filter);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ItemDetailsActivity.class);
                            intent.putExtra("1234", (Serializable) v.getTag());
                            context.startActivity(intent);
                        }
                    });
                    return view;
                }
            };

            sort();


        return mAdapter;
        }

    private void getListener(ImageView greyStar, boolean isImportant) {
        greyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo clickedTodo = (Todo) v.getTag();

                clickedTodo.setIsImportant(isImportant);
                updateTodo(clickedTodo);
            }
        });
    }

    private void getOnClickListener(ImageView greenTick, ColorMatrixColorFilter filter) {
        greenTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo clickedTodo = (Todo) v.getTag();

                if(clickedTodo.getIsFinished() == false){
                    clickedTodo.setIsFinished(true);
                    greenTick.clearColorFilter();
                }else{
                    clickedTodo.setIsFinished(false);
                    greenTick.setColorFilter(filter);

                }
                updateTodo(clickedTodo);
            }

        });
    }

    public void sort() {
        new AsyncTask<Void, Void, List<Todo>>() {
            @Override
            protected List<Todo> doInBackground(Void... voids) {
                return switchCRUDAccessor.readAllTodos();
            }

            Switch choice = context.findViewById(R.id.switch1);
            boolean importanceFirst = choice.isChecked();

            @Override
            protected void onPostExecute(List<Todo> todos) {
                Collections.sort(todos, new Comparator<Todo>() {
                    @Override
                    public int compare(Todo todo1, Todo todo2) {
                        boolean unfinished1 = !(todo1.getIsFinished());
                        boolean unfinished2 = !(todo2.getIsFinished());
                        boolean important1 = todo1.getIsImportant();
                        boolean important2 = todo2.getIsImportant();

                        if (importanceFirst == true) {
                            if (unfinished1 && !unfinished2) {
                                return -1; // unfinished to-do should come before finished to-do
                            } else if (!unfinished1 && unfinished2) {
                                return 1; // finished to-do should come after unfinished to-do
                            } else if (unfinished1 && unfinished2) {
                                // both to-dos are unfinished, compare importance first
                                if (important1 && !important2) {
                                    return -1; // important to-do should come before unimportant to-do
                                } else if (!important1 && important2) {
                                    return 1; // unimportant to-do should come after important to-do
                                } else {
                                    // both to-dos have the same importance, compare their dates
                                    LocalDateTime date1 = todo1.getDateTime();
                                    LocalDateTime date2 = todo2.getDateTime();

                                    if (date1 == null && date2 == null) {
                                        return 0; // both dates are null, consider them equal
                                    } else if (date1 == null) {
                                        return -1; // date1 is null, consider it smaller
                                    } else if (date2 == null) {
                                        return 1; // date2 is null, consider it smaller
                                    } else {
                                        return date1.compareTo(date2); // sort by date ascending
                                    }
                                }
                            } else {
                                // both to-dos are finished, compare importance first
                                if (important1 && !important2) {
                                    return -1; // important to-do should come before unimportant to-do
                                } else if (!important1 && important2) {
                                    return 1; // unimportant to-do should come after important to-do
                                } else {
                                    // both to-dos have the same importance, compare their positions
                                    return todos.indexOf(todo1) - todos.indexOf(todo2); // sort by original position
                                }

                            }
                        }else{
                            if (unfinished1 && !unfinished2) {
                                return -1; // unfinished to-do should come before finished to-do
                            } else if (!unfinished1 && unfinished2) {
                                return 1; // finished to-do should come after unfinished to-do
                            } else if (unfinished1 && unfinished2) {// both to-dos are unfinished, compare their dates first
                                LocalDateTime date1 = todo1.getDateTime();
                                LocalDateTime date2 = todo2.getDateTime();

                                if (date1 == null && date2 == null) {
                                    // both dates are null, consider them equal
                                    // compare importance
                                    if (important1 && !important2) {
                                        return -1; // important to-do should come before unimportant to-do
                                    } else if (!important1 && important2) {
                                        return 1; // unimportant to-do should come after important to-do
                                    } else {
                                        // both to-dos have the same importance, compare their positions
                                        return todos.indexOf(todo1) - todos.indexOf(todo2); // sort by original position
                                    }
                                } else if (date1 == null) {
                                    return -1; // date1 is null, consider it smaller
                                } else if (date2 == null) {
                                    return 1; // date2 is null, consider it smaller
                                } else {
                                    // compare dates
                                    int dateComparison = date1.compareTo(date2);
                                    if (dateComparison != 0) {
                                        return dateComparison; // sort by date ascending
                                    } else {
                                        // both to-dos have the same date, compare importance
                                        if (important1 && !important2) {
                                            return -1; // important to-do should come before unimportant to-do
                                        } else if (!important1 && important2) {
                                            return 1; // unimportant to-do should come after important to-do
                                        } else {
                                            // both to-dos have the same importance, compare their positions
                                            return todos.indexOf(todo1) - todos.indexOf(todo2); // sort by original position
                                        }
                                    }
                                }
                            } else {
                                // both to-dos are finished, compare importance first
                                if (important1 && !important2) {
                                    return -1; // important to-do should come before unimportant to-do
                                } else if (!important1 && important2) {
                                    return 1; // unimportant to-do should come after important to-do
                                } else {
                                    // both to-dos have the same importance, compare their positions
                                    return todos.indexOf(todo1) - todos.indexOf(todo2); // sort by original position
                                }
                            }
                        }
                    }
                });

                mAdapter.clear();
                mAdapter.addAll(todos);
                mAdapter.notifyDataSetChanged();
            }

        }.execute();
    }


    //copied from Prof. Schafföner

    @Override
    public boolean deleteTodo(long id) {
        final boolean[] deleted = new boolean[1];
        new AsyncTask<Void, Void, Void>()  {

            @Override
            protected Void doInBackground(Void... voids) {
                deleted[0] = switchCRUDAccessor.deleteTodo(id);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                sort();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
        return deleted[0];
    }

    @Override
    public List<Todo> readAllTodos() {
        return null;
        //method from this class isn't used but needs to be implemented anyway.
    }
    @Override
    public Todo createTodo(final Todo aTodo) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
               switchCRUDAccessor.createTodo(aTodo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.add(aTodo);
                sort();
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
        return aTodo;
    }
    @Override
    public Todo updateTodo(final Todo aTodo) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                switchCRUDAccessor.updateTodo(aTodo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                sort();
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
        return aTodo;
    }

    private void synchronize(){

        TodoCRUDAccessor localAccessor = new RoomCRUDAccessor(context);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void...voids){
                List <Todo> localTodos  = localAccessor.readAllTodos();
                List <Todo> remoteTodos = switchCRUDAccessor.readAllTodos();

                if(localTodos == null && !(remoteTodos == null)){
                    Iterator<Todo> it = remoteTodos.iterator();

                    while(it.hasNext()){
                        localAccessor.createTodo(it.next());
                    }
                }else if (!(localTodos.isEmpty()) && !(remoteTodos.isEmpty())){
                    Iterator<Todo> remote = remoteTodos.iterator();
                    Iterator <Todo> local = localTodos.iterator();

                    while(remote.hasNext()){
                        System.out.println(deleteTodo(remote.next().getId()));
                    }

                    while(local.hasNext()){
                        createTodo(local.next());
                    }
                }else if(!(localTodos == null) && remoteTodos == null){
                    Iterator <Todo> iterator = localTodos.iterator();

                    while(iterator.hasNext()){
                        createTodo(iterator.next());
                    }
                }
                return null;
            }protected void onPostExecute(Void aVoid){
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public int getAccessorId(){
        return switchCRUDAccessor.getAccessorId();
    }

}

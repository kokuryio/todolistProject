package activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import models.Todo;
import de.thb.fbi.msr.maus.einkaufsliste.R;
import room.AsyncCRUDAccessor;

import java.time.LocalDateTime;
import java.util.Calendar;

public class ToDoListActivity extends AppCompatActivity {

    private AsyncCRUDAccessor asyncCRUDAccessor;


    private ListView listView;
    private ListAdapter adapter;

    private static ToDoListActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);


        listView = findViewById(R.id.list);
        Switch choice = findViewById(R.id.switch1);
        Button addTodo = findViewById(R.id.button);
        asyncCRUDAccessor = new AsyncCRUDAccessor(this);

        if(asyncCRUDAccessor.getAccessorId() == 0)
            Toast.makeText(ToDoListActivity.this, "No server access!", Toast.LENGTH_SHORT).show();

        adapter = asyncCRUDAccessor.getAdapter();

        listView.setAdapter(adapter);

        listView.setScrollBarStyle(ListView.SCROLLBARS_INSIDE_OVERLAY);

        getOnCheckedChangeListener(choice);

        getOnClickListener(addTodo);

        instance = this;

        }

    private void getOnClickListener(Button addTodo) {
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ToDoListActivity.this);
                builder.setTitle("Create new todo");

                // Set dialog layout
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_todo, null);
                builder.setView(dialogView);

                // Find the views in the dialog
                EditText nameEditText = dialogView.findViewById(R.id.editTextName);
                EditText descriptionEditText = dialogView.findViewById(R.id.editTextDescription);
                CheckBox importanceCheckBox = dialogView.findViewById(R.id.checkBoxImportance);
                DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the values
                        String name = nameEditText.getText().toString();

                        if (!isNameValid(name)) {
                            Toast.makeText(ToDoListActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                            return; // return if name is empty
                        }

                        String description = descriptionEditText.getText().toString();
                        boolean isImportant = importanceCheckBox.isChecked();

                        // Creates the new Todo
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day, hour, minute);
                        LocalDateTime dateTime = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());

                        Todo newTodo = new Todo(name, description, false, isImportant, dateTime);

                        // Add new Todo to the Database
                        asyncCRUDAccessor.createTodo(newTodo);
                    }
                });

                builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void getOnCheckedChangeListener(Switch choice) {
        choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                asyncCRUDAccessor.sort();
            }
        });
    }

    private static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public AsyncCRUDAccessor getAsyncCRUDAccessor(){
        return asyncCRUDAccessor;
    }

    public static ToDoListActivity getInstance() {
        return instance;
    }
}
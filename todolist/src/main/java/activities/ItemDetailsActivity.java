package activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import de.thb.fbi.msr.maus.einkaufsliste.R;
import models.Todo;
import room.AsyncCRUDAccessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItemDetailsActivity extends AppCompatActivity {

    private AsyncCRUDAccessor accessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        //Get all the relevant UI elements
        TextView name = findViewById(R.id.textView3);
        TextView description = findViewById(R.id.textView4);
        TextView dueDate = findViewById(R.id.textView5);
        CheckBox importance = findViewById(R.id.checkBox);
        CheckBox finished = findViewById(R.id.checkBox2);
        Button edit = findViewById(R.id.button3);
        Button delete = findViewById(R.id.button4);

        Intent intent = getIntent();

        if(intent.hasExtra("1234")){

            //Get the Todo object which was selected by the User
            Todo todo = (Todo) intent.getSerializableExtra("1234");
            name.setText("Name: " +todo.getName());
            description.setText("Description: " + todo.getDescription());

            //Just to make sure that the due date is displayed correctly
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


            //Set due date, importance and isFinished label
            if(todo.getDateTime() != null)
                dueDate.setText("Due date: " + todo.getDateTime().format(formatter));

            if(todo.getIsImportant()){
                importance.setChecked(true);
            }else{
                importance.setChecked(false);
            }

            if(todo.getIsFinished()){
                finished.setChecked(true);
            }else{
                finished.setChecked(false);
            }

            ToDoListActivity previousActivity = ToDoListActivity.getInstance();
            accessor = previousActivity.getAsyncCRUDAccessor();


            //I extracted some methods here to not put everything in onCreate
            getOnCheckedChangeListener(importance, todo);

            getChangeListener(finished, todo);

            getListener(delete, todo);

            getEditOnClickListener(name, description, dueDate, edit, todo);

        }



    }

    private void getEditOnClickListener(TextView name, TextView description, TextView dueDate, Button edit, Todo todo) {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);
                builder.setTitle("Edit Todo");

                // Set dialog layout
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_todo, null);
                builder.setView(dialogView);

                // Find the views in the dialog
                EditText nameEditText = dialogView.findViewById(R.id.editTextName);
                EditText descriptionEditText = dialogView.findViewById(R.id.editTextDescription);
                DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);

                // Set initial values
                nameEditText.setText(todo.getName());
                descriptionEditText.setText(todo.getDescription());

                initDateTimePicker(datePicker, timePicker, todo);

                getPositiveButton(builder, nameEditText, descriptionEditText, datePicker, timePicker, todo, name, description, dueDate);

                getNegativeButton(builder);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private static void initDateTimePicker(DatePicker datePicker, TimePicker timePicker, Todo todo) {
        LocalDateTime dateTime = LocalDateTime.now();

        if(todo.getDateTime() != null)
            dateTime = todo.getDateTime();

        int year = dateTime.getYear();
        int month = dateTime.getMonthValue() - 1;  // Month in DatePicker starts from 0
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();

        // Set the date and time values in DatePicker and TimePicker
        datePicker.init(year, month, day, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    }

    private AlertDialog.Builder getPositiveButton(AlertDialog.Builder builder, EditText nameEditText, EditText descriptionEditText, DatePicker datePicker, TimePicker timePicker, Todo todo, TextView name, TextView description, TextView dueDate) {
        return builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Extract the updated values
                editTodoDetails(nameEditText, descriptionEditText, todo, datePicker, timePicker, name, description, dueDate);
            }
        });
    }


    private void editTodoDetails(EditText nameEditText, EditText descriptionEditText, Todo todo, DatePicker datePicker, TimePicker timePicker, TextView name, TextView description, TextView dueDate) {
        String nameText = nameEditText.getText().toString();
        if (TextUtils.isEmpty(nameText)) {
            Toast.makeText(ItemDetailsActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String descriptionText = descriptionEditText.getText().toString();

        // Update the Todo object with the new values
        todo.setName(nameText);
        todo.setDescription(descriptionText);

        // Update the date and time values
        int newYear = datePicker.getYear();
        int newMonth = datePicker.getMonth() + 1;  // Month in LocalDateTime starts from 1
        int newDay = datePicker.getDayOfMonth();
        int newHour, newMinute;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            newHour = timePicker.getHour();
            newMinute = timePicker.getMinute();
        } else {
            newHour = timePicker.getCurrentHour();
            newMinute = timePicker.getCurrentMinute();
        }
        LocalDateTime newDateTime = LocalDateTime.of(newYear, newMonth, newDay, newHour, newMinute);
        todo.setDateTime(newDateTime);

        // Update the Todo in the database
        accessor.updateTodo(todo);

        // Update the displayed information
        name.setText("Name: " + todo.getName());
        description.setText("Description: " + todo.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dueDate.setText("Due date: " + todo.getDateTime().format(formatter));
    }

    private static AlertDialog.Builder getNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void getListener(Button delete, Todo todo) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you really want to delete this Todo?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accessor.deleteTodo(todo.getId());
                        Toast.makeText(ToDoListActivity.getInstance(), "Todo deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void getChangeListener(CheckBox finished, Todo todo) {
        finished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    todo.setIsFinished(true);
                    accessor.updateTodo(todo);
                }else{
                    todo.setIsFinished(false);
                    accessor.updateTodo(todo);
                }
            }
        });
    }

    private void getOnCheckedChangeListener(CheckBox importance, Todo todo) {
        importance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    todo.setIsImportant(true);
                    accessor.updateTodo(todo);
                }else{
                    todo.setIsImportant(false);
                    accessor.updateTodo(todo);
                }
            }
        });
    }
}

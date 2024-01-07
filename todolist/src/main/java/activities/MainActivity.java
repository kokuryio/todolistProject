package activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import de.thb.fbi.msr.maus.einkaufsliste.R;
import remote.ResteasyTodoCRUDAccessor;

//This activty only checks if there is a connection to the backend and only then forwards to the login activity, otherwise
//straight forward to the Todo list
public class MainActivity extends AppCompatActivity {

    private ResteasyTodoCRUDAccessor restAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testConnection();


    }

    private void testConnection(){
        //Change this to your backend url
        restAccessor = new ResteasyTodoCRUDAccessor("http://your-url-here.com");

        if(restAccessor.testConnection()){
            startActivity(new Intent(MainActivity.this, LoginViewActivity.class));
        }else
            startActivity(new Intent(MainActivity.this, ToDoListActivity.class));
    }
}

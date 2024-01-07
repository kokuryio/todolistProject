package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import de.thb.fbi.msr.maus.einkaufsliste.R;
import models.LoginData;
import spi.LoginChecker;

//!!!Remeber: this activity only gets called when there is a connection to the remote server
public class LoginViewActivity extends AppCompatActivity {

    private LoginChecker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        //CHANGE this URL to the URL of YOUR backend
        checker = new LoginChecker("http://your-url-here.com");

        Button button = findViewById(R.id.button2);
        final EditText ticketMail1 = findViewById(R.id.editTextTextEmailAddress2);
        final TextView errorMessage = findViewById(R.id.textView);
        final TextView errorMessage2 = findViewById(R.id.textView2);
        final EditText passwordInput = findViewById(R.id.editTextTextPassword2);
        final TextView generalErrorMessage = findViewById(R.id.generalTextView);

        ProgressDialog progressDialog = new ProgressDialog(LoginViewActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        getTextChangedListener(button, ticketMail1, errorMessage2, passwordInput, generalErrorMessage);
        getTextChangedListener(button, passwordInput, errorMessage, ticketMail1, generalErrorMessage);
        getOnClickListener(button, ticketMail1, errorMessage, errorMessage2, generalErrorMessage, passwordInput, progressDialog);

    }

    private void getOnClickListener(Button button, EditText ticketMail1, TextView errorMessage, TextView errorMessage2, TextView generalErrorMessage, EditText passwordInput, ProgressDialog progressDialog) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        boolean correctPassword = checkLength(passwordInput);
                        boolean correctEmail = validateEmail(ticketMail1);
                        LoginData loginData = new LoginData(ticketMail1.getText().toString(), passwordInput.getText().toString());
                        boolean pass = checker.isLoginValid(loginData);

                        if (pass) {
                            startActivity(new Intent(LoginViewActivity.this, ToDoListActivity.class));
                        } else {
                            if (!correctPassword && !correctEmail) {
                                errorMessage.setVisibility(View.VISIBLE);
                                errorMessage2.setVisibility(View.VISIBLE);
                            } else if (!correctPassword)
                                errorMessage2.setVisibility(View.VISIBLE);
                            else if (!correctEmail)
                                errorMessage.setVisibility(View.VISIBLE);
                            else
                                generalErrorMessage.setVisibility(View.VISIBLE);

                            progressDialog.dismiss();
                            button.setEnabled(true);
                        }
                    }
                }, 3000);
            }
        });
    }


    private static void getTextChangedListener(Button button, EditText ticketMail1, TextView errorMessage2, EditText passwordInput, TextView generalErrorMessage) {
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                errorMessage2.setVisibility(View.INVISIBLE);
                generalErrorMessage.setVisibility(View.INVISIBLE);
                button.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ticketMail1.getText().toString().length() != 0 && passwordInput.getText().toString().length() != 0)
                    button.setEnabled(true);
            }
        });
    }

    private boolean checkLength(EditText password){
        String passwordInput = password.getText().toString();

        if(passwordInput.length() != 6)
            return false;
        else
            return true;
    }

    private boolean validateEmail(EditText email){
        String emailInput = email.getText().toString();

        if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){

            return true;
        }else{

            return false;
        }
    }
}

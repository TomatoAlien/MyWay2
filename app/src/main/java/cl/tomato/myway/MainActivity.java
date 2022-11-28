package cl.tomato.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailInput, passInput;
    Button startBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        startBtn = findViewById(R.id.startBtn);
        progressBar = findViewById(R.id.progressBar);

        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.startBtn:
                    userLogIn();
                    break;
            }
    }

    private void userLogIn() {
        String email = emailInput.getText().toString().trim();
        String pass = passInput.getText().toString().trim();

        if(email.isEmpty()){
            emailInput.setError("Ingrese email");
            emailInput.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            passInput.setError("Ingrese contraseña");
            passInput.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, pagina_principal.class));
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openSignUpPage(View view) {
        Intent i = new Intent(this, SignUpPage.class);
        startActivity(i);
    }};

package cl.tomato.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;



public class SignUpPage extends AppCompatActivity implements View.OnClickListener{
    Button registrar;
    ImageButton backBtn;
    EditText email, passwd, name;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.EmailAddress);
        passwd = findViewById(R.id.TextPassword);
        name = findViewById(R.id.user_name);

        backBtn = findViewById(R.id.btnBack);
        registrar = findViewById(R.id.button_registro);

        progressBar = findViewById(R.id.progressBar);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpPage.this, MainActivity.class));
            }
        });

        registrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_registro:
                registrarUsuario();
                break;
        }
    }

    private void registrarUsuario() {
        String correo = email.getText().toString().trim();
        String nombre = name.getText().toString().trim();
        String contraseña = passwd.getText().toString().trim();

        if (nombre.isEmpty()){
            name.setError("Ingrese nombre");
            name.requestFocus();
            return;
        }
        if(correo.isEmpty()){
            email.setError("Ingrese correo");
            email.requestFocus();
            return;
        }
        if(contraseña.isEmpty()){
            passwd.setError("Ingrese contraseña");
            passwd.requestFocus();
            return;
        }
        if(contraseña.length()<6){
            passwd.setError("Minimo 6 caracteres");
            passwd.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user usuario = new user(nombre, correo);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUpPage.this,"Registrado correctamente",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                        startActivity(new Intent(SignUpPage.this, pagina_principal.class));
                                    }else{
                                        Toast.makeText(SignUpPage.this, "Error al registrar", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
            }
        }
        });
    }
}
package cl.hugo.miprimeraaplicacion;
// Paquete donde se encuentra la clase MainActivity

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Definición de variables gráficas (campos y botones)
    private EditText nombreInput, emailInput, passwordInput;
    private Button registerButton, loginButton;

    // Variables de Firebase (autenticación y base de datos)
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Asigna el diseño (layout) de esta pantalla a activity_main.xml

        // Inicialización de Firebase
        mAuth = FirebaseAuth.getInstance(); // Maneja usuarios y autenticación
        databaseRef = FirebaseDatabase.getInstance().getReference("usuarios");
        // Referencia a la rama "usuarios" en Realtime Database

        // Enlazando elementos de la interfaz con el código
        nombreInput = findViewById(R.id.et_nombre);
        emailInput = findViewById(R.id.et_email);
        passwordInput = findViewById(R.id.et_password);
        registerButton = findViewById(R.id.btn_register);
        loginButton = findViewById(R.id.btn_login);

        // Botón de registro
        registerButton.setOnClickListener(v -> registerUser());

        // Botón de login
        loginButton.setOnClickListener(v -> loginUser());
    }

    // Método para registrar usuarios
    private void registerUser() {
        String nombre = nombreInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Validación de campos vacíos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registro en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

                // Guardar datos adicionales en Firebase Realtime Database
                String userId = mAuth.getCurrentUser().getUid();
                Map<String, Object> usuarioMap = new HashMap<>();
                usuarioMap.put("nombre", nombre);
                usuarioMap.put("email", email);
                usuarioMap.put("password", password); // Solo práctica, no recomendado en producción

                databaseRef.child(userId).setValue(usuarioMap);
            } else {
                Toast.makeText(MainActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para iniciar sesión
    private void loginUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingresa email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación contra Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                // Si el login es correcto, abre la segunda pantalla (MenuActivity)
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            } else {
                Toast.makeText(MainActivity.this, "Error de autenticación", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

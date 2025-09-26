package cl.hugo.miprimeraaplicacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {

    private TextView mensaje, tvResultado;
    private EditText etTotal, etDistancia;
    private Button btnCalcular;

    private FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;
    private DatabaseReference databaseRef;

    private static final int REQUEST_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Referencias al layout
        mensaje = findViewById(R.id.tv_ubicacion);   // ubicación GPS
        etTotal = findViewById(R.id.et_total);       // total de compra
        etDistancia = findViewById(R.id.et_distancia); // distancia
        btnCalcular = findViewById(R.id.btn_calcular); // botón calcular
        tvResultado = findViewById(R.id.tv_resultado); // resultado del cálculo

        // Inicializar Firebase Database
        databaseRef = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializar Fused Location Provider
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        // Verificar permisos antes de acceder a la ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos si aún no están otorgados
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }

        // Obtener la última ubicación conocida
        fusedClient.getLastLocation().addOnSuccessListener(this::handleLocation);

        // Configurar actualizaciones de ubicación
        LocationRequest req = LocationRequest.create()
                .setInterval(10000)          // cada 10 segundos
                .setFastestInterval(5000)    // mínimo cada 5 segundos
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Callback para manejar ubicaciones nuevas
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                Location loc = result.getLastLocation();
                handleLocation(loc);
            }
        };

        // Suscribirse a actualizaciones
        fusedClient.requestLocationUpdates(req, locationCallback, getMainLooper());

        // Acción del botón Calcular
        btnCalcular.setOnClickListener(v -> {
            try {
                double total = Double.parseDouble(etTotal.getText().toString());
                double distancia = Double.parseDouble(etDistancia.getText().toString());
                double costo = 0;

                if (total >= 50000 && distancia <= 20) {
                    // Despacho gratis
                    costo = 0;
                } else if (total >= 25000 && total <= 49999) {
                    // 150 pesos por km
                    costo = distancia * 150;
                } else if (total < 25000) {
                    // 300 pesos por km
                    costo = distancia * 300;
                }

                tvResultado.setText("Costo de despacho: $" + costo);

            } catch (Exception e) {
                tvResultado.setText("Error: ingrese valores válidos");
            }
        });
    }

    // Método para manejar y mostrar la ubicación
    private void handleLocation(Location loc) {
        if (loc != null) {
            String ubicacion = loc.getLatitude() + "," + loc.getLongitude();
            mensaje.setText("Tu ubicación: " + ubicacion);

            // Guardar ubicación en Firebase
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseRef.child(userId).child("ubicacion").setValue(ubicacion);
        } else {
            mensaje.setText("No se pudo obtener la ubicación.");
        }
    }

    // Manejar la respuesta del usuario al cuadro de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el usuario aceptó, reiniciar la actividad para aplicar los permisos
                recreate();
            } else {
                Toast.makeText(this, "Se requieren permisos de ubicación", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

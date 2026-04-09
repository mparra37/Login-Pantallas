package parra.mario.logintest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import parra.mario.logintest.ui.theme.LoginTestTheme

class RegistroActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRegistro(
                        auth, database,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        auth = Firebase.auth
        database = Firebase.database.reference
    }
}



@Composable
fun PantallaRegistro(auth: FirebaseAuth, database: DatabaseReference, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var correo by remember{ mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var edad by  remember { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "REGISTRO", fontSize = 30.sp)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(text = "Nombre") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text(text = "Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text(text = "Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = contra,
            onValueChange = { contra = it},
            label = { Text(text = "Contraseña")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button( onClick = {

            }

            ) { Text(text = "Cancelar")}



            Button( onClick = {

                if(correo.isNotEmpty() && contra.isNotEmpty()){
                    auth.createUserWithEmailAndPassword(correo,contra)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(context, "Se agregó el usuario", Toast.LENGTH_SHORT).show()
                                val usuario = Usuario(nombre, correo, edad.toInt())
                                val userID = auth.currentUser?.uid ?: ""
                                database.child("usuarios").child(userID).setValue(usuario)



                                Toast.makeText(context, "Se agregó el usuario", Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, PrincipalActivity::class.java)
                                intent.putExtra("nombre", nombre)
                                intent.putExtra("correo", correo)
                                context.startActivity(intent)

                            }else{
                                Toast.makeText(context, "No se puedo registrar", Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            }

            ) { Text(text = "Registrarse")}

        }


    }
}

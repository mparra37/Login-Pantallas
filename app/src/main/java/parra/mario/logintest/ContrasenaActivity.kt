package parra.mario.logintest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import parra.mario.logintest.ui.theme.LoginTestTheme

class ContrasenaActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        auth,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        auth = Firebase.auth
    }
}

@Composable
fun Greeting2(auth: FirebaseAuth, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var correo by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text(text = "Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            if(correo.isNotEmpty()){
                auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(context, "Se ha enviado un correo", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Error: correo no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(context, "Favor de ingresar correo", Toast.LENGTH_SHORT).show()
            }

        }) {Text(text = "Cambiar contraseña") }
    }

}


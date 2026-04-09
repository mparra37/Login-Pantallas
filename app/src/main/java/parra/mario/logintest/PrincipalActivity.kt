package parra.mario.logintest

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import parra.mario.logintest.ui.theme.LoginTestTheme

class PrincipalActivity : ComponentActivity() {
    lateinit var nombre: String
    lateinit var correo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        nombre = intent.getStringExtra("nombre") ?: ""
        correo = intent.getStringExtra("correo") ?: ""

        setContent {
            LoginTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        nombre,correo,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, correo: String, modifier: Modifier = Modifier) {



    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "BIENVENIDO",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$name",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$correo",
            fontSize = 40.sp
        )
    }

}


package parra.mario.logintest

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import parra.mario.logintest.ui.theme.LoginTestTheme

class PrincipalActivity : ComponentActivity() {
    lateinit var nombre: String
    lateinit var correo: String

    lateinit var userID: String

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

       // nombre = intent.getStringExtra("nombre") ?: ""
       // correo = intent.getStringExtra("correo") ?: ""

       // userID = intent.getStringExtra("userid") ?: ""

        //database = Firebase.database
        //myRef = database.getReference("usuarios").child(userID)

        //correo = mutableStateOf("ejemplo.com")
       // nombre = mutableStateOf("anónimo")

        val uid = Firebase.auth.currentUser?.uid ?: ""
        val myRef = Firebase.database.getReference("usuarios").child(uid)


        setContent {
            LoginTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(uid, myRef,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(uid: String, myRef: DatabaseReference, modifier: Modifier = Modifier) {

    var nombre by remember { mutableStateOf("Cargando...") }
    var correo by remember { mutableStateOf("Cargando...") }
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }


    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri
        }
    }

    val bitmap = remember(imageUri) {
        imageUri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            android.graphics.BitmapFactory.decodeStream(inputStream)
        }
    }


    LaunchedEffect(Unit) {
        android.util.Log.d("FIREBASE", "uid: $uid")
        myRef.get().addOnSuccessListener { snapshot ->
            android.util.Log.d("FIREBASE", "snapshot: ${snapshot.value}")
            nombre = snapshot.child("username").value.toString()
            correo = snapshot.child("email").value.toString()
        }
            .addOnFailureListener {
                android.util.Log.e("FIREBASE", "error: ${it.message}")
                nombre = "Error al cargar el nombre"
                correo = "Error al cargar el correo"
            }
    }



    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        IconButton(
            onClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.size(120.dp)
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Seleccionar foto",
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$nombre",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$correo",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            Firebase.auth.signOut()
            (context as? Activity)?.finish()
        }) {
            Text(text = "Cerrar Sesión")
        }
    }

}


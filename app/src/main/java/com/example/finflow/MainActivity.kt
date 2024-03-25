package com.example.finflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.finflow.ui.theme.FinFlowTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetContent(string = "Loading")
        }


        //Initializing database
        val db = Firebase.firestore

        val doc_ref = db.collection("Users").document("User1")

        doc_ref.get().addOnSuccessListener {
            document ->
            if(document != null){
                setContent{
                    SetContent(string = document.data?.get("born").toString())
                }
            }
        }

        //Creating a collection "Users"
        val users_collection = db.collection("Users")

        //Creating documents
        val user1 = hashMapOf(
            "name" to "Hemang",
            "lname" to "Mishra",
            "born" to 2005
        )
        val user2 = hashMapOf(
            "name" to "Devang",
            "lname" to "Mishra",
            "born" to 2000
        )

        //Adding documents to collections
        users_collection.document("User1").set(user1)
        users_collection.document("User2").set(user2)
    }
}

@Composable
fun SetContent(string: String) {
    FinFlowTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Greeting(string)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinFlowTheme {
        Greeting("Android")
    }
}
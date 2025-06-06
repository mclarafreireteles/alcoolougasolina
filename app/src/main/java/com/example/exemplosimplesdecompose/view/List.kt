package com.example.exemplosimplesdecompose.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.data.Coordenadas
import com.example.exemplosimplesdecompose.data.Posto
import com.example.exemplosimplesdecompose.data.PostoPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDePostos(navController: NavHostController, nomeDoPosto: String) {
    val context= LocalContext.current
    var postosSalvos by remember { mutableStateOf(listOf<Posto>()) }

    LaunchedEffect(Unit) {
        postosSalvos = PostoPreferences.recuperarPostos(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Postos") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(postosSalvos) { posto ->
                Card(
                    onClick = {
                        val coords = posto.coordenadas
                        if (coords != null && (coords.latitude != 0.0 || coords.longitude != 0.0)) {
                            val gmmIntentUri = Uri.parse("geo:${coords.latitude},${coords.longitude}?q=${Uri.encode(posto.nome)}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                Toast.makeText(context, "Google Maps não disponível", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Posto sem localização definida", Toast.LENGTH_SHORT).show()
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = posto.nome,
                                modifier = Modifier.padding(16.dp)
                            )

                            IconButton(onClick = {
                                postosSalvos = postosSalvos.toMutableList().also { it.remove(posto) }
                                PostoPreferences.salvarPostos(context, postosSalvos)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }

                            IconButton(onClick = {
                                navController.navigate("EditarPosto/${posto.nome}")
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                        }

                    }
                }
            }
        }
    }
}
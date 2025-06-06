package com.example.exemplosimplesdecompose.view

import android.content.ActivityNotFoundException
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
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.res.stringResource
import com.example.exemplosimplesdecompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDePostos(navController: NavHostController, nomeDoPosto: String) {
    val context= LocalContext.current

    val titulo = stringResource(R.string.lista_de_postos_titulo)
    val msgWazeNaoInstalado = stringResource(R.string.maps_nao_disponivel)
    val msgSemLocalizacao = stringResource(R.string.posto_sem_localizacao)
    val descAbrirNoMaps = stringResource(R.string.abrir_no_maps)
    val descExcluir = stringResource(R.string.excluir)
    val descEditar = stringResource(R.string.editar)

    var postosSalvos by remember { mutableStateOf(listOf<Posto>()) }

    LaunchedEffect(Unit) {
        postosSalvos = PostoPreferences.recuperarPostos(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { titulo }
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
                        navController.navigate("DetalhePosto/${Uri.encode(posto.nome)}")
//                        val coords = posto.coordenadas
//                        if (coords != null && (coords.latitude != 0.0 || coords.longitude != 0.0)) {
//                            val gmmIntentUri = Uri.parse("geo:${coords.latitude},${coords.longitude}?q=${Uri.encode(posto.nome)}")
//                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                            mapIntent.setPackage("com.google.android.apps.maps")
//                            if (mapIntent.resolveActivity(context.packageManager) != null) {
//                                context.startActivity(mapIntent)
//                            } else {
//                                Toast.makeText(context, "Google Maps não disponível", Toast.LENGTH_SHORT).show()
//                            }
//                        } else {
//                            Toast.makeText(context, "Posto sem localização definida", Toast.LENGTH_SHORT).show()
//                        }

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

                            // Botão abrir Google Maps
                            IconButton(onClick = {
                                val coords = posto.coordenadas
                                if (coords != null && (coords.latitude != 0.0 || coords.longitude != 0.0)) {
                                    try {
                                        val wazeUri = Uri.parse("https://waze.com/ul?ll=${coords.latitude},${coords.longitude}&navigate=yes")
                                        val intent = Intent(Intent.ACTION_VIEW, wazeUri)
                                        context.startActivity(intent)
                                    } catch (e: ActivityNotFoundException) {
                                        Toast.makeText(context, msgWazeNaoInstalado, Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, msgSemLocalizacao, Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Icon(Icons.Default.LocationOn, contentDescription = descAbrirNoMaps)
                            }

                            IconButton(onClick = {
                                postosSalvos = postosSalvos.toMutableList().also { it.remove(posto) }
                                PostoPreferences.salvarPostos(context, postosSalvos)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = descExcluir)
                            }

                            IconButton(onClick = {
                                navController.navigate("EditarPosto/${posto.nome}")
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = descEditar)
                            }
                        }

                    }
                }
            }
        }
    }
}
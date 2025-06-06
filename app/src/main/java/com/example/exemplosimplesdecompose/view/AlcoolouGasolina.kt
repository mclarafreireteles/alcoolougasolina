package com.example.exemplosimplesdecompose.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.data.Posto
import com.example.exemplosimplesdecompose.data.PostoPreferences
import com.google.android.gms.location.LocationServices
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.core.content.ContextCompat
import com.example.exemplosimplesdecompose.data.Coordenadas

val CoordenadasSaver: Saver<Coordenadas, List<Double>> = Saver(
    save = { listOf(it.latitude, it.longitude) },
    restore = { Coordenadas(it[0], it[1]) }
)

@Composable
fun AlcoolGasolinaPreco(navController: NavHostController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activity = context as ComponentActivity
            val permission = Manifest.permission.ACCESS_FINE_LOCATION

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), 0)
            }
        }
    }

    var alcool by rememberSaveable { mutableStateOf("") }
    var gasolina by rememberSaveable { mutableStateOf("") }
    var nomeDoPosto by rememberSaveable { mutableStateOf("") }
    var checkedState by rememberSaveable { mutableStateOf(true) }
    var resultado by rememberSaveable { mutableStateOf("") }
    var coordenadas = rememberSaveable(saver = CoordenadasSaver) {
        (Coordenadas(0.0, 0.0))
    }


    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nomeDoPosto,
                onValueChange = { nomeDoPosto = it },
                label = { Text("Nome do Posto (Opcional))") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            // Campo de texto para entrada do preço
            OutlinedTextField(
                value = alcool,
                onValueChange = { alcool = it }, // Atualiza o estado
                label = { Text("Preço do Álcool (R$)") },
                modifier = Modifier.fillMaxWidth(), // Preenche a largura disponível
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Configuração do teclado
            )
            // Campo de texto para preço da Gasolina
            OutlinedTextField(
                value = gasolina,
                onValueChange = { gasolina = it },
                label = { Text("Preço da Gasolina (R$)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Campo de texto para preço da Gasolina


            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.Start) {
                Text(
                    text = "75%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Switch(
                    modifier = Modifier.semantics { contentDescription = "Demo with icon" },
                    checked = checkedState,
                    onCheckedChange = { checkedState = it },
                    thumbContent = {
                        if (checkedState) {
                            // Icon isn't focusable, no need for content description
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }
                )
            }
            // Botão de cálculo
            Button(
                onClick = {
                    val precoAlcoolDouble = alcool.toDoubleOrNull() ?: 0.0
                    val precoGasolinaDouble = gasolina.toDoubleOrNull() ?: 0.0

                    val relacao = if (precoGasolinaDouble > 0) precoAlcoolDouble / precoGasolinaDouble else 1.0

                    val compensa = relacao < 0.75

                    checkedState = compensa

                    resultado = if (compensa) {
                        "Abasteça com Álcool (Álcool está abaixo de 75% do preço da Gasolina)"
                    } else {
                        "Abasteça com Gasolina (Álcool não compensa)"
                    }

                    obterLocalizacao(context) { latitude, longitude ->
                        coordenadas = Coordenadas(latitude, longitude)

                        val posto = Posto(
                            nome = if (nomeDoPosto.isNotBlank()) nomeDoPosto else "Posto sem nome",
                            precoAlcool = precoAlcoolDouble,
                            precoGasolina = precoGasolinaDouble,
                            coordenadas = Coordenadas(latitude, longitude)
                        )
                        PostoPreferences.salvarPosto(context, posto)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            // Texto do resultado
            Text(
                text = resultado,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.End) {
                FloatingActionButton(
                    onClick = { navController.navigate("ListaDePostos/$nomeDoPosto")},

                    ) {
                    Icon(Icons.Filled.Add, "Inserir Posto")
                }
            }
        }
    }
}

fun obterLocalizacao(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // Permissão não concedida – você pode pedir a permissão aqui se quiser
        return
    }

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                callback(location.latitude, location.longitude)
            }
        }
}

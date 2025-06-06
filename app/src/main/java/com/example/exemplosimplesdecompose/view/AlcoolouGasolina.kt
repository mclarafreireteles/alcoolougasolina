package com.example.exemplosimplesdecompose.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AlcoolGasolinaPreco(navController: NavHostController) {
    var alcool by rememberSaveable { mutableStateOf("") }
    var gasolina by rememberSaveable { mutableStateOf("") }
    var nomeDoPosto by rememberSaveable { mutableStateOf("") }
    var checkedState by rememberSaveable { mutableStateOf(true) }


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
            OutlinedTextField(
                value = nomeDoPosto,
                onValueChange = { nomeDoPosto = it },
                label = { Text("Nome do Posto (Opcional))") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

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

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            // Texto do resultado
            Text(
                text = "Vamos Calcular?",
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

package com.example.exemplosimplesdecompose.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.data.PostoPreferences
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DetalhePostoScreen(navController: NavHostController, nomeDoPosto: String) {
    val context = LocalContext.current
    val posto = remember {
        PostoPreferences.recuperarPostos(context).find { it.nome == nomeDoPosto }
    }

    if (posto == null) {
        Text(
            "Posto não encontrado",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        return
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = posto.nome,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        InfoItem(label = "Preço Álcool", value = "R$ ${posto.precoAlcool}")
        InfoItem(label = "Preço Gasolina", value = "R$ ${posto.precoGasolina}")
        InfoItem(label = "Data de Cadastro", value = posto.dataCadastro ?: "Não informado")

        val coord = posto.coordenadas
        if (coord != null) {
            InfoItem(label = "Coordenadas", value = "${coord.latitude}, ${coord.longitude}")
        } else {
            InfoItem(label = "Coordenadas", value = "Não disponíveis")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Voltar", fontSize = 18.sp)
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )
    }
}

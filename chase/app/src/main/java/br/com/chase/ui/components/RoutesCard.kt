package br.com.chase.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.chase.R
import br.com.chase.data.model.RouteResponse
import br.com.chase.ui.theme.Poppins
import br.com.chase.ui.theme.PrimaryRainbow
import br.com.chase.utils.formatAverageSpeed
import br.com.chase.utils.formatDistance
import br.com.chase.utils.formatTotalTime

@Composable
fun RoutesCard(
    route: RouteResponse,
    showDeleteButton: Boolean = false,
    onDelete: (String) -> Unit = {}
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Espaço vazio para balancear visualmente
                Spacer(modifier = Modifier.width(22.dp))

                // Localização centralizada
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.pin),
                        contentDescription = "Pin",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = route.startLocation,
                        fontFamily = Poppins,
                        fontSize = 14.sp
                    )
                }

                // Ícone da lixeira (somente no perfil)
                if (showDeleteButton) {
                    IconButton(onClick = { showConfirmDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Excluir rota",
                            tint = Color.Red,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(22.dp))
                }
            }

            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Excluir rota?") },
                    text = { Text("Tem certeza que deseja excluir esta rota?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showConfirmDialog = false
                            onDelete(route.rid)
                        }) {
                            Text("Excluir", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Distância",
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = formatDistance(route.distance),
                        fontFamily = Poppins,
                        fontSize = 19.sp
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tempo Recorde",
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = route.recordTime,
                        fontFamily = Poppins,
                        fontSize = 19.sp
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.competidores),
                            contentDescription = "",
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Competidores",
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = route.competitors.toString(),
                        fontFamily = Poppins,
                        fontSize = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(modifier = Modifier.width(320.dp), color = Color.LightGray)

            Spacer(modifier = Modifier.height(5.dp))

            route.top3.forEachIndexed { index, runner ->
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 12.dp, end = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}#",
                                style = if (index == 0) TextStyle(
                                    fontFamily = Poppins,
                                    fontWeight = Bold,
                                    fontSize = 14.sp,
                                    brush = Brush.linearGradient(colors = PrimaryRainbow)
                                ) else TextStyle(
                                    fontFamily = Poppins,
                                    fontSize = 14.sp
                                )
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Image(
                                painter = painterResource(R.drawable.user_sem_foto),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = runner.userName,
                                fontFamily = Poppins,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${formatTotalTime(runner.totalTime)} - ${formatAverageSpeed(runner.averageSpeed)}",
                            fontFamily = Poppins,
                            fontSize = 11.sp,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            modifier = Modifier.widthIn(min = 90.dp)
                        )
                    }
                }
            }
        }
    }
}

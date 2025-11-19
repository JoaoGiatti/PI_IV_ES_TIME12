package br.com.chase.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.ui.theme.PrimaryRainbow
import br.com.chase.R
import br.com.chase.ui.components.LoadingIndicator
import br.com.chase.ui.components.RoutesCard
import br.com.chase.ui.theme.Poppins
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.user) {
        viewModel.currentUser()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        when {
            state.isLoading -> {
                LoadingIndicator()
            }
            else -> {
                // Fundo arco-iris
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .align(Alignment.TopCenter),
                    shape = RectangleShape
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = PrimaryRainbow
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {}
                }

                // Parte das informacoes do usuario nome, bio e level
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(630.dp)
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(80.dp))
                        Text(
                            text = state.user?.displayName ?: "Usuário sem nome",
                            fontFamily = Poppins,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Nível do Usuário (Lvl 0)",
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(25.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.userData?.bio.isNullOrEmpty()) {
                                TextButton(
                                    onClick = { viewModel.onShowDialog() }
                                ) {
                                    Text(
                                        text = "+ Add Bio",
                                        fontFamily = Poppins,
                                        fontSize = 14.sp,
                                        color = Color.LightGray
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 55.dp, end = 55.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = state.userData?.bio ?: "",
                                        fontFamily = Poppins,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    IconButton(
                                        onClick = { viewModel.onShowDialog() }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.editar),
                                            contentDescription = "Editar",
                                            tint = Color.Unspecified
                                        )
                                    }
                                }
                            }
                        }

                        // Popup para digitar ou editar a bio
                        if (state.showDialog) {
                            AlertDialog(
                                onDismissRequest = { viewModel.onDismissDialog() },
                                title = {
                                    Text(
                                        text = "Editar Biografia",
                                        fontFamily = Poppins,
                                        fontSize = 18.sp
                                    )
                                },
                                text = {
                                    OutlinedTextField(
                                        value = state.editingBio,
                                        onValueChange = { viewModel.onBioChange(it) },
                                        label = {
                                            Text(
                                                text = "Digite sua biografia:",
                                                fontFamily = Poppins,
                                                fontSize = 15.sp
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                },
                                shape = RoundedCornerShape(18.dp),
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            viewModel.updateUserBio()
                                            viewModel.onDismissDialog()
                                        },
                                        contentPadding = PaddingValues(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    brush = Brush.horizontalGradient(
                                                        colors = PrimaryRainbow
                                                    ),
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                                .padding(vertical = 8.dp, horizontal = 35.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Salvar",
                                                color = Color.White,
                                                fontFamily = Poppins,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                },
                                dismissButton = {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.onDismissDialog()
                                        },
                                        border = BorderStroke(
                                            1.dp,
                                            Brush.horizontalGradient(
                                                colors = PrimaryRainbow
                                            )
                                        ),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = MaterialTheme.colorScheme.secondary
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "Cancelar",
                                            fontFamily = Poppins,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        // Parte das informacoes de quilometragem, tempo e gastos
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Quilometragem",
                                    fontFamily = Poppins,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "00.00 km",
                                    fontFamily = Poppins,
                                    fontSize = 19.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(18.dp))
                            Spacer(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(45.dp)
                                    .background(Color.LightGray)
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tempo Competido",
                                    fontFamily = Poppins,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "0:00:00 h",
                                    fontFamily = Poppins,
                                    fontSize = 19.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(18.dp))
                            Spacer(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(45.dp)
                                    .background(Color.LightGray)
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Gastos Totais",
                                    fontFamily = Poppins,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "0 Kcal",
                                    fontFamily = Poppins,
                                    fontSize = 19.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalDivider(
                            modifier = Modifier.width(350.dp),
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Parte da listagem de rotas que o usuario possui
                        if (state.routes.isEmpty()) {
                            Text(
                                text = "Não há rotas por enquanto",
                                fontFamily = Poppins,
                                fontSize = 15.sp,
                                color = Color.Gray
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                items(state.routes) { route ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    RoutesCard(route)
                                }

                                item { Spacer(modifier = Modifier.height(24.dp)) }
                            }
                        }
                    }
                }

                // Foto do usuario
                Box(
                    modifier = Modifier
                        .offset(y = (-235).dp)
                        .align(Alignment.Center)
                        .size(130.dp)
                        .shadow(5.dp, CircleShape)
                        .background(color = Color.White)
                ) {
                    Image(
                        painter = if (state.user?.photoUrl != null)
                            rememberAsyncImagePainter(state.user!!.photoUrl)
                        else
                            painterResource(R.drawable.user_sem_foto),
                        contentDescription = "Foto do Usuário",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}

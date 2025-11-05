package br.com.chase.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.R
import br.com.chase.ui.components.RoutesCard
import br.com.chase.data.local.model.RouteData
import br.com.chase.ui.theme.Poppins
import br.com.chase.ui.theme.PrimaryRainbow
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
    ){
        Card(modifier = Modifier
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
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Bio do Usuário",
                    fontFamily = Poppins,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(35.dp))

                // COLOCAR LÓGICA!!!!!!!
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
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
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
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
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
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
                Spacer(modifier = Modifier.height(15.dp))
                HorizontalDivider(
                    modifier = Modifier.width(350.dp),
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(20.dp))
                // 👇 SÓ PRA TER UMA IDEIA, TEM QUE PEGAR INFOS DAS ROTAS AINDA
                if (route == null){
                    Text(
                        text = "Não há rotas por enquanto",
                        fontFamily = Poppins,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                } else {
                    // PRA VER TEM QUE IR ATÉ O FINAL DO CÓDIGO
                    RoutesCard(sampleRoute)
                }

            }
        }
        Box(
            modifier = Modifier
                .offset(y = (-235).dp)
                .align(Alignment.Center)
                .size(130.dp)
                .shadow(5.dp, CircleShape)
                .background(color = Color.White)

        ) {
            // MUDAR PARA IF IMAGEM ADICIONADA ELSE USER SEM FOTO
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

// SÓ PRA VER SE O RoutesCard FUNCIONA 👍
val sampleRoute = RouteData.Route(
    location = "Rua Tal, Bairro Tal, Cidade Tal",
    distance = "5.0 km",
    recordTime = "0:25:30",
    competitors = 15,
    topRunners = listOf(
        RouteData.Runner("João", "0:25:30", "12 km/h"),
        RouteData.Runner("Maria", "0:26:10", "11.5 km/h"),
        RouteData.Runner("Pedro", "0:27:00", "11 km/h")
    )
)
val route = null // 👈 COLOCA QLQR COISA DIFERENTE DE NULL PRA VER O CARD
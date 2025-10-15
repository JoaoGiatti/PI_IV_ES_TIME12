package br.com.chase.ui.screens.profile

import android.hardware.lights.Light
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.chase.ui.theme.PrimaryRainbow
import br.com.chase.R
import br.com.chase.ui.theme.Poppins

@Composable
fun ProfileScreen(){
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
                    text = "Nome do Usuário",
                    fontFamily = Poppins,
                    fontSize = 26.sp
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
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(170.dp)
                            .height(45.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(
                                brush = Brush.linearGradient(colors = PrimaryRainbow),
                            )
                    ){
                        Button(
                            modifier = Modifier.fillMaxSize(),
                            onClick = { /* editar perfil */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Editar Perfil",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Button(
                        modifier = Modifier
                            .width(170.dp)
                            .height(45.dp),
                        onClick = { /* editar rotas */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        shape = RoundedCornerShape(7.dp)
                    ) {
                        Text(
                            text = "Editar Rotas",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))

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
                            fontSize = 12.sp,
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
                            fontSize = 12.sp,
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
                            fontSize = 12.sp,
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
                    modifier = Modifier.width(370.dp),
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(25.dp))
                Card(
                    modifier = Modifier
                        .width(370.dp)
                        .height(250.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ){
                    // COLOCAR LÓGICA
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Image(
                                painter = painterResource(R.drawable.pin),
                                contentDescription = "Pin",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Rua Tal, Bairro Tal, Cidade Tal",
                                fontFamily = Poppins,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Text(
                                    text = "Distância",
                                    fontFamily = Poppins,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "00.00 km",
                                    fontFamily = Poppins,
                                    fontSize = 19.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(18.dp))
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Text(
                                    text = "Tempo Recorde",
                                    fontFamily = Poppins,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "0:00:00",
                                    fontFamily = Poppins,
                                    fontSize = 19.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(18.dp))
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Row(verticalAlignment = Alignment.CenterVertically)
                                {
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
                                    text = "0",
                                    fontFamily = Poppins,
                                    fontSize = 19.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        HorizontalDivider(
                            modifier = Modifier.width(320.dp),
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        // COLOCAR LÓGICA
                        Row(verticalAlignment = Alignment.CenterVertically)
                        {
                            Text(
                                text = "1#",
                                style = TextStyle(
                                    fontFamily = Poppins,
                                    fontWeight = Bold,
                                    fontSize = 14.sp,
                                    brush = Brush.linearGradient(
                                        colors = PrimaryRainbow
                                    )
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            // COLOCAR LÓGICA!!!!!!!!!
                            Image(
                                painter = painterResource(R.drawable.user_sem_foto),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Usuário primeiro lugar",
                                fontFamily = Poppins,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = "0:00:00 - 0.0 km/h",
                                fontFamily = Poppins,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically)
                        {
                            Text(
                                text = "2#",
                                style = TextStyle(
                                    fontFamily = Poppins,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            // COLOCAR LÓGICA!!!!!!!!!
                            Image(
                                painter = painterResource(R.drawable.user_sem_foto),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Usuário segundo lugar",
                                fontFamily = Poppins,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = "0:00:00 - 0.0 km/h",
                                fontFamily = Poppins,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .offset(y = (-230).dp)
                .align(Alignment.Center)
                .size(135.dp)
                .shadow(5.dp, CircleShape)
                .background(color = Color.White)

        ) {
            // MUDAR PARA IF IMAGEM ADICIONADA ELSE USER SEM FOTO
            Image(
                painter = painterResource(R.drawable.user_sem_foto),
                contentDescription = "User sem foto",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(125.dp)
            )
        }
    }

    }

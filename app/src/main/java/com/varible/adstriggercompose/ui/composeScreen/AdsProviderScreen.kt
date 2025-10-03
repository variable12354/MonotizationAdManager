package com.varible.adstriggercompose.ui.composeScreen

import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import android.graphics.Color as C
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.makeopinion.cpxresearchlib.models.CPXCardConfiguration
import com.makeopinion.cpxresearchlib.models.CPXCardStyle
import com.varible.adstriggercompose.MyApp

@Composable
fun AdsProviderScreen(
    paddingValues: PaddingValues,
    onShowAdMobInterstitial: () -> Unit,
    onShowAdMobRewarded: () -> Unit,
    onShowApplovinInterstitial: () -> Unit,
    onShowApplovinRewarded: () -> Unit,
    onShowIronSourceInterstitial: () -> Unit,
    onShowIronSourceRewarded: () -> Unit,
    onOfferWall: () -> Unit,
    onShowSurvey: () -> Unit,
) {

    var showSurvey by remember { mutableStateOf(false) }


    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(56.dp)

    Box(modifier = Modifier.fillMaxSize().padding(vertical = 20.dp)) {
        if (showSurvey) {
            Column(modifier = Modifier.fillMaxSize()) {
                Button(onClick = {
                    showSurvey = false
                }, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }

                CPXResearchView(buildCardConfig())
            }

        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5))
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Ad Panel",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "AdMob Ads",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color(0xFF4285F4)
                        )

                        Button(
                            onClick = onShowAdMobInterstitial,
                            modifier = buttonModifier,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("Show AdMob Interstitial")
                        }

                        Button(
                            onClick = onShowAdMobRewarded,
                            modifier = buttonModifier,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("Show AdMob RewardAds")
                        }
                    }

                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Applovin Ads",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color(0xFF4285F4)
                        )

//                        Button(
//                            onClick = onShowApplovinInterstitial,
//                            modifier = buttonModifier,
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
//                        ) {
//                            Text("Show Applovin Interstitial")
//                        }

                        Button(
                            onClick = onShowApplovinRewarded,
                            modifier = buttonModifier,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("Show Applovin RewardAds")
                        }
                    }

                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "IronSource Ads",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color(0xFF4285F4)
                        )

                       /* Button(
                            onClick = onShowIronSourceInterstitial,
                            modifier = buttonModifier,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("Show IronSource Interstitial")
                        }*/

                        Button(
                            onClick = onShowIronSourceRewarded,
                            modifier = buttonModifier,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("Show IronSource RewardAds")
                        }
                    }

                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Tapjoy OfferWall",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color(0xFF4285F4)
                        )

                        Button(
                            onClick = onOfferWall,
                            modifier = buttonModifier,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("Show OfferWall")
                        }
                    }

                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Cpx Survey Reward",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color(0xFF4285F4)
                        )

                        Button(
                            onClick = { onShowSurvey(); showSurvey = true },
                            modifier = buttonModifier,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("Show Survey")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun CPXResearchView(cardConfig: CPXCardConfiguration) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    AndroidView(
        factory = { ctx ->
            FrameLayout(ctx).apply {
                id = View.generateViewId()
                (ctx.applicationContext as? MyApp)
                    ?.cpxResearch()
                    ?.insertCPXResearchCardsIntoContainer(
                        activity,
                        this,
                        cardConfig
                    )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

fun buildCardConfig(): CPXCardConfiguration {
    return CPXCardConfiguration
        .Builder()
        .accentColor(C.parseColor("#41d7e5"))
        .backgroundColor(C.WHITE)
        .starColor(C.parseColor("#ffaa00"))
        .inactiveStarColor(C.parseColor("#dfdfdf"))
        .textColor(C.DKGRAY)
        .dividerColor(C.parseColor("#5A7DFE"))
        .promotionAmountColor(C.RED)
        .cardsOnScreen(3)
        .cornerRadius(4f)
        .maximumSurveys(3)
        .paddingHorizontal(16f)
        .cpxCardStyle(CPXCardStyle.DEFAULT)
        .build()
}


@Preview(showBackground = true)
@Composable
fun AdsProviderScreenPreview() {
    AdsProviderScreen(
        PaddingValues(),
        onOfferWall = {},
        onShowApplovinRewarded = {},
        onShowIronSourceRewarded = {},
        onShowAdMobRewarded = {},
        onShowApplovinInterstitial = {},
        onShowAdMobInterstitial = {},
        onShowIronSourceInterstitial = {},
        onShowSurvey = {}
    )
}

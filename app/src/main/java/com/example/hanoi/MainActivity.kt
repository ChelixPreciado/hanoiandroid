package com.example.hanoi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hanoi.presenters.HanoiContract
import com.example.hanoi.presenters.HanoiPresenter
import com.example.hanoi.ui.theme.HanoiTheme
import com.example.hanoi.usecases.HanoiViewModel

class MainActivity : ComponentActivity(), HanoiContract.View {

    val torre1 = "A"
    val torre2 = "B"
    val torre3 = "C"

    private lateinit var presenter: HanoiContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = HanoiPresenter(this)
        enableEdgeToEdge()
        setContent {
            HanoiTheme {
                StartScreen()
            }
        }
    }

    @Composable
    fun StartScreen() {
        var discos by remember { mutableIntStateOf(4) }
        var currentView by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = "", onValueChange = {},
                placeholder = {
                    Text(text = "Towers of Hanoi")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 36.dp, end = 36.dp), onClick = { presenter.getDisksFromBE { discos = it }}) {
                Text(text = "Obtener numero de discos")
            }
            Spacer(modifier = Modifier.height(25.dp))
            Row {
                Text(
                    text = "Numero de discos: $discos",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 32.dp)
                )
                Button(
                    onClick = {
                        startOrder(discos)
                        currentView = 1
                    },
                    modifier = Modifier.padding(start = 48.dp)
                ) {
                    Text(text = "Comenzar")
                }
            }
            when (currentView) {
                1 -> StepsView() { currentView = 2 }
                2 -> AllStepsView() { currentView = 1 }
            }
        }
    }

    @Composable
    fun StepsView(toNextView: () -> Unit) {
        var stepIndex by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Numero de pasos para completar la torre ${presenter.getHanoiStepsSize()}",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Image(
                painter = painterResource(id = presenter.getHanoiStep(stepIndex).image),
                contentDescription = "Move ilustration",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp),
            )
            if (stepIndex < presenter.getHanoiStepsSize()) {
                Text(
                    text = "Paso ${stepIndex + 1} de ${presenter.getHanoiStepsSize()}",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
            Text(
                text = presenter.getHanoiStep(stepIndex).instruction,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
            )
            Box(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                Button(onClick = { stepIndex++ }) {
                    Text(
                        text = "Siguiente Paso",
                        fontSize = 12.sp
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = { toNextView.invoke() }) {
                    Text(
                        text = "Mostrar todos los pasos",
                        fontSize = 12.sp
                    )
                }
            }

        }
    }

    @Composable
    fun AllStepsView(showStepByStep: () -> Unit) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Numero de pasos para completar la torre ${presenter.getHanoiStepsSize()}"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = presenter.getAllInstructions()
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp),
                onClick = { showStepByStep.invoke() }) {
                Text(
                    text = "Mostrar paso a paso",
                    fontSize = 12.sp
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MyPreview() {
        StartScreen()
        //StepsView({})
    }

    override fun showResult(message: String) {
        Log.i("Chelix", message)
    }

    private fun startOrder(discos: Int) = presenter.hanoiOrder(discos, torre1, torre3, torre2)

}
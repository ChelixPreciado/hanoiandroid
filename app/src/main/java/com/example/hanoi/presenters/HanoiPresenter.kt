package com.example.hanoi.presenters

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.hanoi.R
import com.example.hanoi.models.HanoiStepVM
import com.example.hanoi.usecases.HanoiViewModel

class HanoiPresenter(private val view: HanoiContract.View) : HanoiContract.Presenter {

    private val instrucciones = mutableListOf<HanoiStepVM>()
    private val hanoiViewModel: HanoiViewModel by (view as ComponentActivity).viewModels()

    override fun getDisksFromBE(onDisksObtained: (Int) -> Unit) {
        hanoiViewModel.getDisks()
        hanoiViewModel.data.observe((view as ComponentActivity)) { response ->
            response?.let { onDisksObtained(it.discos) }
        }
    }

    override fun hanoiOrder(disks: Int, origin: String, destiny: String, aux: String) {
        if (disks == 1)
            instrucciones.add(instructionsMapper(disks, origin, destiny))
        else {
            hanoiOrder(disks - 1, origin, aux, destiny)
            instrucciones.add(instructionsMapper(disks, origin, destiny))
            hanoiOrder(disks - 1, aux, destiny, origin)
        }
    }

    override fun getHanoiStep(step: Int): HanoiStepVM =
        if (step < instrucciones.size) instrucciones[step] else HanoiStepVM(
            "Felicidades! Haz completado la torre!",
            R.drawable.hanoi_completa
        )

    override fun getHanoiStepsSize(): Int = instrucciones.size

    override fun getAllInstructions(): String {
        val stringBuilder = StringBuilder()
        for (ins in instrucciones) {
            stringBuilder.append(ins.instruction)
            stringBuilder.append('\n')
        }
        return stringBuilder.toString()
    }

    fun instructionsMapper(disk: Int, origin: String, destiny: String): HanoiStepVM {
        val instruction = "Mueve el disco $disk de la torre $origin a la torre $destiny"
        val image = when {
            origin == "A" && destiny == "B" -> R.drawable.hanoi_a_to_b
            origin == "A" && destiny == "C" -> R.drawable.hanoi_a_to_c
            origin == "B" && destiny == "A" -> R.drawable.hanoi_b_to_a
            origin == "B" && destiny == "C" -> R.drawable.hanoi_b_to_c
            origin == "C" && destiny == "A" -> R.drawable.hanoi_c_to_a
            origin == "C" && destiny == "B" -> R.drawable.hanoi_c_to_b
            else -> R.drawable.hanoi_a_to_b
        }
        return HanoiStepVM(instruction, image)
    }

}
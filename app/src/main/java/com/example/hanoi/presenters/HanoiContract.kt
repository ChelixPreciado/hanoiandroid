package com.example.hanoi.presenters

import com.example.hanoi.models.HanoiStepVM

interface HanoiContract {

    interface View {
        fun showResult(message: String)
    }

    interface Presenter {
        fun getDisksFromBE(onDisksObtained: (Int) -> Unit)
        fun hanoiOrder(disks: Int, origin: String, destiny: String, aux: String)
        fun getHanoiStep(step: Int): HanoiStepVM
        fun getHanoiStepsSize(): Int
        fun getAllInstructions(): String
    }

}
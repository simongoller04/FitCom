package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import java.io.Serializable

data class SingleCompleteExerciseData(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Float,
    val image: String
) : Serializable

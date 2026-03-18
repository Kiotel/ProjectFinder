package utils

import androidx.compose.runtime.compositionLocalOf
import dev.chrisbanes.haze.HazeState

val LocalHazeState = compositionLocalOf<HazeState> {
    error("No HazeState provided")
}
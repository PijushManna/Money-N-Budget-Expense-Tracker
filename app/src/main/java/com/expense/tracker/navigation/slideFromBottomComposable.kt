
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.slideFromBottomComposable(
    route: String,
    duration: Int = 700,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,

        enterTransition = {
            slideInVertically(
                initialOffsetY = { fullHeight ->
                    fullHeight // start fully off-screen
                },
                animationSpec = tween(
                    durationMillis = duration,
                    easing = LinearEasing
                )
            )
        },

        exitTransition = {
            slideOutVertically(
                targetOffsetY = { fullHeight ->
                    fullHeight
                },
                animationSpec = tween(
                    durationMillis = duration,
                    easing = LinearEasing
                )
            )
        },
        arguments = arguments
    ) { backStackEntry ->
        content(backStackEntry)
    }
}
package detailedProfile

import ProfileViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import modifiers.cheapGlassEffect
import theme.AppTheme


@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun DetailedProfileScreen(
    modifier: Modifier = Modifier, vm: DetailedProfileViewModel, svm: ProfileViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    ScreenLayout(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center).padding(innerPadding)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier.size(96.dp),
                        onClick = {},
                        shapes = IconButtonShapes(
                            shape = CircleShape,
                            pressedShape = MaterialTheme.shapes.medium
                        ),
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null
                        )
                    }
                    Column {
                        Text(
                            text = "Дизайн Дизайнер Дизайнерович",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleLargeEmphasized,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "19 лет",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleSmallEmphasized,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.verticalScroll(scrollState)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfilePart(title = "Образование") {
                    ProfilePartText(text = "Ростовская обл, г. Таганрог · ЮФУ")
                    HorizontalDivider(
                        color = Color.Transparent,
                        modifier = Modifier.cheapGlassEffect()
                    )
                    ProfilePartText(text = "Кафедра САПР")
                    HorizontalDivider(
                        color = Color.Transparent,
                        modifier = Modifier.cheapGlassEffect()
                    )
                    ProfilePartText(text = "Программа 09.03.02 · очная")
                }

                ProfilePart(title = "Личная информация") {
                    ProfilePartText(text = "Студент 2 курса, люблю капибар и компьютеры")
                    HorizontalDivider(
                        color = Color.Transparent,
                        modifier = Modifier.cheapGlassEffect()
                    )
                    ProfilePartText(text = "Открытость, порядочность, честность")
                    HorizontalDivider(
                        color = Color.Transparent,
                        modifier = Modifier.cheapGlassEffect()
                    )
                    ProfilePartText(text = "Photoshop, Figma, React JS")
                }

                ProfilePart(title = "Обратная связь") {
                    ProfilePartText(text = "Гибкий, 2–3 ч/день · Выходные: сб, вс")
                    HorizontalDivider(
                        color = Color.Transparent,
                        modifier = Modifier.cheapGlassEffect()
                    )
                    ProfilePartText(text = "rastegaev@sfedu.ru")
                    HorizontalDivider(
                        color = Color.Transparent,
                        modifier = Modifier.cheapGlassEffect()
                    )
                    ProfilePartText(text = "t.me @сарурс")
                }
            }
        }
    }
}

@Composable
private fun ProfilePart(
    modifier: Modifier = Modifier, title: String, content: @Composable ((ColumnScope) -> Unit)
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLargeEmphasized,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            HorizontalDivider(
                modifier = Modifier.cheapGlassEffect(
                    fillAlpha = 0.4f,
                    borderAlpha = 0.0f
                ),
                color = Color.Transparent
            )
        }
        Box(
            modifier.background(Color.Transparent).fillMaxWidth().cheapGlassEffect(
                shape = MaterialTheme.shapes.large, fillAlpha = 0.4f, borderAlpha = 0.6f
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                content(this)
            }
        }
    }
}

@Composable
private fun ProfilePartText(modifier: Modifier = Modifier, text: String) {
    Text(modifier = modifier.padding(vertical = 4.dp), text = text)
}

@Preview
@Composable
private fun PreviewProfilePart() {
    AppTheme {
        ProfilePart(title = "Образование") {
            ProfilePartText(text = "Ростовская обл, г. Таганрог · ЮФУ")
            ProfilePartText(text = "Кафедра САПР")
            ProfilePartText(text = "Программа 09.03.02 · очная")
        }
    }
}

@Preview
@Composable
private fun PreviewProfilePartText() {
    AppTheme {
        ProfilePartText(text = "Preview text")
    }
}
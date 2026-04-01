package description

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import components.ScreenLayout
import description.models.DescriptionState
import modifiers.cheapGlassEffect
import utils.SnackBarManager

@Composable
internal fun DescriptionScreen(
    modifier: Modifier = Modifier,
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit,
    snackBarManager: SnackBarManager,
) {
    val scrollState = rememberScrollState()
    ScreenLayout(
        modifier = modifier, snackBarManager = snackBarManager
    ) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(scrollState).padding(innerPadding)
                .padding(vertical = 12.dp, horizontal = 12.dp),
        ) {
            Header(
                step = 1,
                titleText = "Заполните форму",
                descriptionText = "Для правильной работы алгоритмов поиска заполните информацию честно — это поможет найти команду быстрее"
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 12.dp).imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DescriptionPart(title = "Учебное заведение") {
                    DescriptionPartTextField(
                        value = uiState.region,
                        onValueChange = { handleIntent(DescriptionIntent.SetRegion(it)) },
                        labelText = "РЕГИОН"
                    )
                    HorizontalDivider(
                        color = Transparent, modifier = Modifier.cheapGlassEffect()
                    )
                    DescriptionPartTextField(
                        value = uiState.university,
                        onValueChange = { handleIntent(DescriptionIntent.SetUniversity(it)) },
                        labelText = "ВУЗ"
                    )
                    HorizontalDivider(
                        color = Transparent, modifier = Modifier.cheapGlassEffect()
                    )
                    DescriptionPartTextField(
                        value = uiState.department,
                        onValueChange = { handleIntent(DescriptionIntent.SetDepartment(it)) },
                        labelText = "КАФЕДРА"
                    )
                    HorizontalDivider(
                        color = Transparent, modifier = Modifier.cheapGlassEffect()
                    )
                    DescriptionPartTextField(
                        value = uiState.programme,
                        onValueChange = { handleIntent(DescriptionIntent.SetProgramme(it)) },
                        labelText = "ПРОГРАММА"
                    )
                    HorizontalDivider(
                        color = Transparent, modifier = Modifier.cheapGlassEffect()
                    )
                    DescriptionPartTextField(
                        value = uiState.studyType,
                        onValueChange = { handleIntent(DescriptionIntent.SetStudyType(it)) },
                        labelText = "ФОРМА"
                    )
                }
                DescriptionPart(title = "Личная информация") {
                    DescriptionPartTextField(
                        value = uiState.about,
                        onValueChange = { handleIntent(DescriptionIntent.SetAbout(it)) },
                        labelText = "О СЕБЕ"
                    )
                    HorizontalDivider(
                        color = Transparent, modifier = Modifier.cheapGlassEffect()
                    )
                    DescriptionPartTextField(
                        value = uiState.qualities,
                        onValueChange = { handleIntent(DescriptionIntent.SetQualities(it)) },
                        labelText = "КАЧЕСТВА"
                    )
                    HorizontalDivider(
                        color = Transparent, modifier = Modifier.cheapGlassEffect()
                    )
                    DescriptionPartTextField(
                        value = uiState.skills,
                        onValueChange = { handleIntent(DescriptionIntent.SetSkills(it)) },
                        labelText = "НАВЫКИ"
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    step: Int,
    titleText: String,
    descriptionText: String
) {
    val titleTextList = remember { titleText.split(" ") }
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.cheapGlassEffect(
                tint = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
        ) {
            Text(
                text = "Шаг $step из 3",
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(modifier = Modifier.padding(top = 8.dp), text = buildAnnotatedString {
            titleTextList.forEachIndexed { index, str ->
                withStyle(
                    style = MaterialTheme.typography.headlineMediumEmphasized.toSpanStyle().copy(
                        color = if (index % 2 == 0) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    ),
                ) {
                    append("$str ")
                }
            }
        })
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = descriptionText,
            style = MaterialTheme.typography.bodyMediumEmphasized,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun DescriptionPart(
    modifier: Modifier = Modifier, title: String, content: @Composable ((ColumnScope) -> Unit)
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HorizontalDivider(
                thickness = 1.5.dp, modifier = Modifier.weight(1f).cheapGlassEffect(
                    fillAlpha = 0.4f, borderAlpha = 0.0f
                ), color = Transparent
            )
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = title,
                style = MaterialTheme.typography.titleLargeEmphasized,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            HorizontalDivider(
                thickness = 1.5.dp, modifier = Modifier.weight(1f).cheapGlassEffect(
                    fillAlpha = 0.4f, borderAlpha = 0.0f
                ), color = Transparent
            )
        }
        Box(
            modifier.padding(top = 8.dp).background(Transparent).fillMaxWidth().cheapGlassEffect(
                shape = MaterialTheme.shapes.large, fillAlpha = 0.4f, borderAlpha = 0.6f
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                content(this)
            }
        }
    }
}

@Composable
private fun DescriptionPartTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    value: String,
    onValueChange: (newValue: String) -> Unit
) {
    TextField(
        modifier = modifier.padding(vertical = 4.dp),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = labelText,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Transparent,
            focusedContainerColor = Transparent,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

package description

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import components.ScreenLayout
import description.models.DescriptionState
import modifiers.cheapGlassEffect
import utils.SnackBarManager

enum class Steps(val stepNumber: Int, val stepInfo: StepInfo) {
    STEP1(
        stepNumber = 1, stepInfo = StepInfo(
            title = "Кто вы?",
            description = "Укажите основные данные. Имя необходимо для того, чтобы приложение считало ваш профиль заполненным."
        )
    ),
    STEP2(
        stepNumber = 2, stepInfo = StepInfo(
            title = "Опыт и Навыки",
            description = "Расскажите о своих компетенциях и прикрепите ссылку на работы."
        )
    ),
    STEP3(
        stepNumber = 3, stepInfo = StepInfo(
            title = "Связь",
            description = "Как с вами связаться и когда вы готовы работать?"
        )
    )
}

data class StepInfo(
    val title: String,
    val description: String,
)

@Composable
internal fun DescriptionScreen(
    modifier: Modifier = Modifier,
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit,
    snackBarManager: SnackBarManager,
    onSubmit: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var currentStep by rememberSaveable { mutableStateOf(1) }
    
    ScreenLayout(
        modifier = modifier, snackBarManager = snackBarManager
    ) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(scrollState).padding(innerPadding)
                .padding(vertical = 12.dp, horizontal = 12.dp),
        ) {
            Header(
                step = Steps.entries.firstOrNull { it.stepNumber == currentStep } ?: Steps.STEP3,
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 12.dp).imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (currentStep) {
                    1 -> Step1(uiState, handleIntent)
                    2 -> Step2(uiState, handleIntent)
                    3 -> Step3(uiState, handleIntent)
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        enabled = currentStep > 1,
                        onClick = { currentStep -= 1 },
                    ) {
                        Text(text = "Назад")
                    }
                    
                    Button(
                        onClick = {
                            if (currentStep >= 3) {
                                onSubmit()
                            } else {
                                currentStep += 1
                            }
                        },
                        enabled = uiState.firstName.isNotBlank()
                    ) {
                        Text(text = if (currentStep >= 3) "Готово" else "Далее")
                    }
                }
            }
        }
    }
}

@Composable
private fun Step1(
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit
) {
    DescriptionPart(title = "Личные данные") {
        DescriptionPartTextField(
            value = uiState.firstName,
            onValueChange = { handleIntent(DescriptionIntent.SetFirstName(it)) },
            labelText = "ИМЯ (обязательно)*"
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.lastName,
            onValueChange = { handleIntent(DescriptionIntent.SetLastName(it)) },
            labelText = "ФАМИЛИЯ"
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        Row(modifier = Modifier.fillMaxWidth()) {
            DescriptionPartTextField(
                value = uiState.age,
                onValueChange = { if (it.all { c -> c.isDigit() }) handleIntent(DescriptionIntent.SetAge(it)) },
                labelText = "ВОЗРАСТ",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.width(8.dp))
            DescriptionPartTextField(
                value = uiState.city,
                onValueChange = { handleIntent(DescriptionIntent.SetCity(it)) },
                labelText = "ГОРОД",
                modifier = Modifier.weight(2f)
            )
        }
    }
    
    DescriptionPart(title = "Учеба") {
        DescriptionPartTextField(
            value = uiState.university,
            onValueChange = { handleIntent(DescriptionIntent.SetUniversity(it)) },
            labelText = "ВУЗ"
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.department,
            onValueChange = { handleIntent(DescriptionIntent.SetDepartment(it)) },
            labelText = "ФАКУЛЬТЕТ / КАФЕДРА"
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.programme,
            onValueChange = { handleIntent(DescriptionIntent.SetProgramme(it)) },
            labelText = "ПРОГРАММА (Напр. 09.03.04)"
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.studyType,
            onValueChange = { handleIntent(DescriptionIntent.SetStudyType(it)) },
            labelText = "ФОРМА (Очная/Заочная)"
        )
    }
}

@Composable
private fun Step2(
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit
) {
    DescriptionPart(title = "Опыт и Ссылки") {
        DescriptionPartTextField(
            value = uiState.about,
            onValueChange = { handleIntent(DescriptionIntent.SetAbout(it)) },
            labelText = "О СЕБЕ / ЦЕЛИ",
            minLines = 3
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.portfolioUrl,
            onValueChange = { handleIntent(DescriptionIntent.SetPortfolio(it)) },
            labelText = "ПОРТФОЛИО / GITHUB (URL)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
        )
    }

    DescriptionPart(title = "Навыки и Качества") {
        SkillsSelection(
            selectedSkills = uiState.selectedSkills,
            searchQuery = uiState.skillSearchQuery,
            availableSkills = uiState.availableSkills,
            onSearchQueryChange = { handleIntent(DescriptionIntent.SetSkillSearch(it)) },
            onSkillAdd = { handleIntent(DescriptionIntent.AddSkill(it)) },
            onSkillRemove = { handleIntent(DescriptionIntent.RemoveSkill(it)) }
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.qualities,
            onValueChange = { handleIntent(DescriptionIntent.SetQualities(it)) },
            labelText = "ЛИЧНЫЕ КАЧЕСТВА"
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.interests,
            onValueChange = { handleIntent(DescriptionIntent.SetInterests(it)) },
            labelText = "ИНТЕРЕСЫ"
        )
    }
}

@Composable
private fun Step3(
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit
) {
    DescriptionPart(title = "Доступность и Связь") {
        DescriptionPartTextField(
            value = uiState.workingHours,
            onValueChange = { handleIntent(DescriptionIntent.SetWorkingHours(it)) },
            labelText = "ГРАФИК (Напр. 20ч/нед, будни)"
        )
        HorizontalDivider(color = Transparent, modifier = Modifier.cheapGlassEffect())
        DescriptionPartTextField(
            value = uiState.waysToContact,
            onValueChange = { handleIntent(DescriptionIntent.SetWaysToContact(it)) },
            labelText = "КОНТАКТЫ (TG: @name, Email: ...)",
            minLines = 2
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsSelection(
    selectedSkills: List<String>,
    searchQuery: String,
    availableSkills: List<String>,
    onSearchQueryChange: (String) -> Unit,
    onSkillAdd: (String) -> Unit,
    onSkillRemove: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val filteredSkills = remember(searchQuery, availableSkills, selectedSkills) {
        availableSkills
            .filter { it !in selectedSkills }
            .filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "НАВЫКИ",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedSkills.forEach { skill ->
                SkillChip(name = skill, onRemove = { onSkillRemove(skill) })
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { 
                    onSearchQueryChange(it)
                    expanded = true
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Выберите навыки...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange(""); expanded = false }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Transparent,
                    focusedContainerColor = Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                ),
            )

            DropdownMenu(
                expanded = expanded && filteredSkills.isNotEmpty(),
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.8f).cheapGlassEffect(),
                properties = androidx.compose.ui.window.PopupProperties(focusable = false)
            ) {
                filteredSkills.forEach { skill ->
                    DropdownMenuItem(
                        text = { Text(skill) },
                        onClick = {
                            onSkillAdd(skill)
                            onSearchQueryChange("")
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillChip(name: String, onRemove: () -> Unit) {
    Surface(
        modifier = Modifier.clip(CircleShape),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.bodySmall)
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Удалить",
                modifier = Modifier.size(16.dp).clickable { onRemove() }
            )
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    step: Steps,
) {
    val titleTextList = remember(step) { step.stepInfo.title.split(" ") }
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.cheapGlassEffect(
                tint = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
        ) {
            Text(
                text = "Шаг ${step.stepNumber} из 3",
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
            text = step.stepInfo.description,
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
    onValueChange: (newValue: String) -> Unit,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        value = value,
        onValueChange = onValueChange,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
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

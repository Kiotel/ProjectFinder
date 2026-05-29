package description

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import components.ScreenLayout
import description.models.DescriptionState
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import modifiers.cheapGlassEffect
import utils.SnackBarManager

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun DescriptionScreen(
    modifier: Modifier = Modifier,
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit,
    snackBarManager: SnackBarManager,
    onSubmit: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var currentStep by rememberSaveable { mutableStateOf(1) }
    val hazeState = rememberHazeState()

    ScreenLayout(
        modifier = modifier, snackBarManager = snackBarManager
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Animated background
            MovingCirclesBackground(isDarkTheme = isSystemInDarkTheme())

            // Haze source
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState),
            )

            // Glass overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeEffect(
                        state = hazeState,
                        style = HazeStyle(
                            blurRadius = 20.dp,
                            noiseFactor = 0f,
                            tint = HazeTint(
                                color = if (isSystemInDarkTheme()) Color.Black.copy(alpha = 0.05f)
                                else Color.White.copy(alpha = 0.05f),
                            ),
                        ),
                    ),
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
            ) {
                Header(currentStep = currentStep)

                Spacer(Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth().imePadding(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (currentStep) {
                        1 -> Step1(uiState, handleIntent)
                        2 -> Step2(uiState, handleIntent)
                        3 -> Step3(uiState, handleIntent)
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            onClick = {
                                if (currentStep > 1) {
                                    currentStep -= 1
                                } else {
                                    onCancel()
                                }
                            },
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

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ─── Steps ─────────────────────────────────────────────────────────────────

@Composable
private fun Step1(
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit
) {
    FormSection(title = "Личные данные") {
        FormField(
            value = uiState.firstName,
            onValueChange = { handleIntent(DescriptionIntent.SetFirstName(it)) },
            label = "Имя *"
        )
        FormField(
            value = uiState.lastName,
            onValueChange = { handleIntent(DescriptionIntent.SetLastName(it)) },
            label = "Фамилия"
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            FormField(
                value = uiState.age,
                onValueChange = { if (it.all { c -> c.isDigit() }) handleIntent(DescriptionIntent.SetAge(it)) },
                label = "Возраст",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.width(8.dp))
            FormField(
                value = uiState.city,
                onValueChange = { handleIntent(DescriptionIntent.SetCity(it)) },
                label = "Город",
                modifier = Modifier.weight(2f)
            )
        }
    }

    FormSection(title = "Обучение") {
        FormField(
            value = uiState.university,
            onValueChange = { handleIntent(DescriptionIntent.SetUniversity(it)) },
            label = "Вуз"
        )
        FormField(
            value = uiState.department,
            onValueChange = { handleIntent(DescriptionIntent.SetDepartment(it)) },
            label = "Факультет / Кафедра"
        )
        FormField(
            value = uiState.programme,
            onValueChange = { handleIntent(DescriptionIntent.SetProgramme(it)) },
            label = "Направление подготовки (напр. 09.03.04)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        FormField(
            value = uiState.studyType,
            onValueChange = { handleIntent(DescriptionIntent.SetStudyType(it)) },
            label = "Форма обучения (очная / заочная / очно-заочная)"
        )
    }
}

@Composable
private fun Step2(
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit
) {
    FormSection(title = "О себе") {
        FormField(
            value = uiState.about,
            onValueChange = { handleIntent(DescriptionIntent.SetAbout(it)) },
            label = "Расскажите о своих целях и мотивации",
            minLines = 3
        )
        FormField(
            value = uiState.portfolioUrl,
            onValueChange = { handleIntent(DescriptionIntent.SetPortfolio(it)) },
            label = "Ссылка на портфолио или GitHub",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
        )
    }

    FormSection(title = "Навыки и качества") {
        SkillsSelection(
            selectedSkills = uiState.selectedSkills,
            searchQuery = uiState.skillSearchQuery,
            availableSkills = uiState.availableSkills,
            onSearchQueryChange = { handleIntent(DescriptionIntent.SetSkillSearch(it)) },
            onSkillAdd = { handleIntent(DescriptionIntent.AddSkill(it)) },
            onSkillRemove = { handleIntent(DescriptionIntent.RemoveSkill(it)) }
        )
        FormField(
            value = uiState.qualities,
            onValueChange = { handleIntent(DescriptionIntent.SetQualities(it)) },
            label = "Личные качества"
        )
        FormField(
            value = uiState.interests,
            onValueChange = { handleIntent(DescriptionIntent.SetInterests(it)) },
            label = "Интересы"
        )
    }
}

@Composable
private fun Step3(
    uiState: DescriptionState,
    handleIntent: (intent: DescriptionIntent) -> Unit
) {
    FormSection(title = "Доступность и контакты") {
        FormField(
            value = uiState.workingHours,
            onValueChange = { handleIntent(DescriptionIntent.SetWorkingHours(it)) },
            label = "Сколько часов в неделю готовы уделять проекту?",
        )
        FormField(
            value = uiState.waysToContact,
            onValueChange = { handleIntent(DescriptionIntent.SetWaysToContact(it)) },
            label = "Как с вами связаться? (TG: @username, Email: ...)",
            minLines = 2
        )
    }
}

// ─── Header ─────────────────────────────────────────────────────────────────

@Composable
private fun Header(
    currentStep: Int,
    modifier: Modifier = Modifier,
) {
    val steps = listOf(
        "Личные данные" to "Расскажите немного о себе",
        "Навыки" to "Покажите, что вы умеете",
        "Контакты" to "Договоримся о связи",
    )
    val (title, description) = steps.getOrElse(currentStep - 1) { steps[0] }

    Column(modifier = modifier.fillMaxWidth()) {
        // Step indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            (1..3).forEach { step ->
                val isActive = step == currentStep
                val isDone = step < currentStep
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .cheapGlassEffect(
                            shape = CircleShape,
                            fillAlpha = if (isActive || isDone) 0.95f else 0.1f,
                            borderAlpha = 0f,
                            tint = if (isActive || isDone) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.height(4.dp))

        // Description
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ─── Form components ────────────────────────────────────────────────────────

@Composable
private fun FormSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .cheapGlassEffect(
                    shape = RoundedCornerShape(16.dp),
                    fillAlpha = 0.12f,
                    borderAlpha = 0.15f,
                    borderWidth = 0.5.dp,
                ),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun FormField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (newValue: String) -> Unit,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        modifier = modifier.fillMaxWidth().padding(vertical = 3.dp),
        value = value,
        onValueChange = onValueChange,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Transparent,
            focusedContainerColor = Transparent,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        ),
    )
}

// ─── Skills ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsSelection(
    selectedSkills: List<String>,
    searchQuery: String,
    availableSkills: List<String>,
    onSearchQueryChange: (String) -> Unit,
    onSkillAdd: (String) -> Unit,
    onSkillRemove: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val filteredSkills = remember(searchQuery, availableSkills, selectedSkills) {
        availableSkills
            .filter { it !in selectedSkills }
            .filter { it.contains(searchQuery, ignoreCase = true) }
    }
    val displaySkills = if (searchQuery.isEmpty()) {
        availableSkills.filter { it !in selectedSkills }
    } else {
        filteredSkills
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Label with count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Выбранные навыки",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
            )
            if (selectedSkills.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .cheapGlassEffect(
                            shape = CircleShape,
                            fillAlpha = 0.3f,
                            borderAlpha = 0.2f,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = selectedSkills.size.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        // Selected skill chips
        if (selectedSkills.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                selectedSkills.forEach { skill ->
                    SkillChip(name = skill, onRemove = { onSkillRemove(skill) })
                }
            }
        }

        // Dropdown button
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = "Добавить навык",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .cheapGlassEffect(
                        fillAlpha = 0.25f,
                        borderAlpha = 0.2f,
                    ),
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    displaySkills.forEach { skill ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .cheapGlassEffect(
                                                shape = CircleShape,
                                                fillAlpha = 0.4f,
                                                borderAlpha = 0f,
                                                tint = MaterialTheme.colorScheme.primary,
                                            ),
                                    )
                                    Text(
                                        text = skill,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            },
                            onClick = {
                                onSkillAdd(skill)
                                expanded = false
                            },
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                        )
                    }
                    if (displaySkills.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Все навыки добавлены",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillChip(name: String, onRemove: () -> Unit) {
    var hovered by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .cheapGlassEffect(
                shape = RoundedCornerShape(24.dp),
                fillAlpha = if (hovered) 0.35f else 0.25f,
                borderAlpha = if (hovered) 0.25f else 0.15f,
                tint = MaterialTheme.colorScheme.primary,
            )
            .clickable(enabled = false) {},
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { onRemove() },
                modifier = Modifier
                    .size(28.dp)
                    .onFocusChanged { hovered = it.isFocused },
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

// ─── Background ─────────────────────────────────────────────────────────────

@Composable
private fun MovingCirclesBackground(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "onboarding_bg")

    val x1 by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(20000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c1_x",
    )
    val y1 by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(15000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c1_y",
    )

    val x2 by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(tween(25000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c2_x",
    )
    val y2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(18000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c2_y",
    )

    val color1 = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    val color2 = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
    val bgColor = MaterialTheme.colorScheme.background

    Canvas(modifier = modifier.fillMaxSize().background(bgColor)) {
        val radius1 = size.minDimension * 0.6f
        val radius2 = size.minDimension * 0.5f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color1, Color.Transparent),
                center = Offset(size.width * x1, size.height * y1),
                radius = radius1,
            ),
            center = Offset(size.width * x1, size.height * y1),
            radius = radius1,
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color2, Color.Transparent),
                center = Offset(size.width * x2, size.height * y2),
                radius = radius2,
            ),
            center = Offset(size.width * x2, size.height * y2),
            radius = radius2,
        )
    }
}

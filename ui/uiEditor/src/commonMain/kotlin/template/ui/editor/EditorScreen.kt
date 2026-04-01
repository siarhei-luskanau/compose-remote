package template.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.vectorResource
import template.ui.common.resources.Res
import template.ui.common.resources.ic_close
import template.ui.common.resources.ic_menu
import template.ui.common.theme.AppTheme

private val ELEMENT_TYPES = listOf("text", "button", "spacer", "hspacer", "divider", "card", "row")

@Composable
fun EditorScreen(viewModel: EditorViewModel) {
    EditorContent(
        viewStateFlow = viewModel.viewState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditorContent(
    viewStateFlow: StateFlow<EditorViewState>,
    onEvent: (EditorViewEvent) -> Unit,
) {
    val state = viewStateFlow.collectAsState().value

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editor") }) },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                ) {
                    Text("Build & Save")
                }
            }
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
        ) {
            item("settings") {
                SettingsSection(
                    config = state.config,
                    onUpdate = { onEvent(EditorViewEvent.UpdateLayout(it)) },
                )
            }
            item("palette") {
                PaletteSection(onAdd = { onEvent(EditorViewEvent.AddElement(it)) })
            }
            item("elements") {
                ElementsSection(
                    elements = state.config.elements,
                    selectedIndex = state.selectedIndex,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    config: LayoutConfig,
    onUpdate: (LayoutConfig) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("Layout Settings", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = config.backgroundColor,
            onValueChange = { onUpdate(config.copy(backgroundColor = it)) },
            label = { Text("Background color") },
            trailingIcon = {
                val parsed = parseColor(config.backgroundColor)
                if (parsed != null) {
                    Box(
                        modifier =
                            Modifier
                                .size(24.dp)
                                .background(parsed, RoundedCornerShape(4.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp)),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = config.padding?.toString() ?: "",
            onValueChange = { onUpdate(config.copy(padding = it.toIntOrNull())) },
            label = { Text("Padding") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = config.scrollable,
                onCheckedChange = { onUpdate(config.copy(scrollable = it)) },
            )
            Text("Scrollable")
        }

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun PaletteSection(onAdd: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("Add Element", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ELEMENT_TYPES.forEach { type ->
                SuggestionChip(
                    onClick = { onAdd(type) },
                    label = { Text(type) },
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun ElementsSection(
    elements: List<ElementConfig>,
    selectedIndex: Int?,
    onEvent: (EditorViewEvent) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("Elements (${elements.size})", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (elements.isEmpty()) {
            Text(
                "No elements yet. Add one from the palette above.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            DraggableElementList(
                elements = elements,
                selectedIndex = selectedIndex,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun DraggableElementList(
    elements: List<ElementConfig>,
    selectedIndex: Int?,
    onEvent: (EditorViewEvent) -> Unit,
) {
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var itemHeightPx by remember { mutableFloatStateOf(56f) }

    Column {
        elements.forEachIndexed { index, element ->
            val isDragging = draggingIndex == index
            val visualShift =
                when {
                    draggingIndex == null -> {
                        0f
                    }

                    isDragging -> {
                        dragOffsetY
                    }

                    else -> {
                        val src = draggingIndex!!
                        val offsetInItems = dragOffset(dragOffsetY, itemHeightPx)
                        val target = (src + offsetInItems).coerceIn(0, elements.lastIndex)
                        when {
                            src < target && index in (src + 1)..target -> -itemHeightPx
                            src > target && index in target until src -> itemHeightPx
                            else -> 0f
                        }
                    }
                }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onSizeChanged { itemHeightPx = it.height.toFloat() }
                        .zIndex(if (isDragging) 1f else 0f)
                        .graphicsLayer { translationY = visualShift },
            ) {
                ElementRow(
                    element = element,
                    isSelected = selectedIndex == index,
                    onClick = {
                        val next = if (selectedIndex == index) null else index
                        onEvent(EditorViewEvent.SelectElement(next))
                    },
                    onRemove = { onEvent(EditorViewEvent.RemoveElement(index)) },
                    dragHandleModifier =
                        Modifier.pointerInput(index) {
                            detectDragGestures(
                                onDragStart = {
                                    draggingIndex = index
                                    dragOffsetY = 0f
                                },
                                onDragEnd = {
                                    draggingIndex?.let { src ->
                                        val target =
                                            (src + dragOffset(dragOffsetY, itemHeightPx))
                                                .coerceIn(0, elements.lastIndex)
                                        if (target != src) onEvent(EditorViewEvent.MoveElement(src, target))
                                    }
                                    draggingIndex = null
                                    dragOffsetY = 0f
                                },
                                onDragCancel = {
                                    draggingIndex = null
                                    dragOffsetY = 0f
                                },
                                onDrag = { change, amount ->
                                    change.consume()
                                    dragOffsetY += amount.y
                                },
                            )
                        },
                )
            }
        }
    }
}

private fun dragOffset(
    offsetY: Float,
    itemHeight: Float,
): Int = if (itemHeight > 0f) (offsetY / itemHeight).toInt() else 0

@Composable
private fun ElementRow(
    element: ElementConfig,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    dragHandleModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                ).clickable { onClick() }
                .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_menu),
            contentDescription = "Drag",
            modifier =
                dragHandleModifier
                    .padding(horizontal = 8.dp)
                    .size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = elementLabel(element),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
        )
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_close),
                contentDescription = "Remove",
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

private fun elementLabel(element: ElementConfig): String {
    val detail =
        when (element.type) {
            "text", "button" -> element.text?.let { " - \"$it\"" } ?: ""
            "spacer" -> element.height?.let { " - ${it}dp" } ?: ""
            "hspacer" -> element.width?.let { " - ${it}dp" } ?: ""
            "card", "row" -> " (${element.children?.size ?: 0} children)"
            else -> ""
        }
    return "${element.type}$detail"
}

private fun parseColor(hex: String): Color? {
    val clean = hex.trimStart('#')
    return when (clean.length) {
        6 -> clean.toLongOrNull(16)?.let { Color(0xFF000000 or it) }
        8 -> clean.toLongOrNull(16)?.let { Color(it) }
        else -> null
    }
}

@Preview
@Composable
internal fun EditorScreenEmptyPreview() =
    AppTheme {
        EditorContent(
            viewStateFlow = MutableStateFlow(EditorViewState()),
            onEvent = {},
        )
    }

@Preview
@Composable
internal fun EditorScreenWithElementsPreview() =
    AppTheme {
        EditorContent(
            viewStateFlow =
                MutableStateFlow(
                    EditorViewState(
                        config =
                            LayoutConfig(
                                elements =
                                    listOf(
                                        ElementConfig(type = "text", text = "Hello"),
                                        ElementConfig(type = "button", text = "Click me"),
                                        ElementConfig(type = "divider"),
                                    ),
                            ),
                        selectedIndex = 0,
                    ),
                ),
            onEvent = {},
        )
    }

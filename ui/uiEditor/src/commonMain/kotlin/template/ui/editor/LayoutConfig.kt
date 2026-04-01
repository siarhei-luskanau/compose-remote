package template.ui.editor

import kotlinx.serialization.Serializable

@Serializable
data class LayoutConfig(
    val backgroundColor: String = "#F3E5F5",
    val scrollable: Boolean = false,
    val padding: Int? = null,
    val elements: List<ElementConfig> = emptyList(),
)

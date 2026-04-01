package template.ui.editor

sealed interface EditorViewEvent {
    data class AddElement(
        val type: String,
    ) : EditorViewEvent

    data class RemoveElement(
        val index: Int,
    ) : EditorViewEvent

    data class MoveElement(
        val from: Int,
        val to: Int,
    ) : EditorViewEvent

    data class UpdateElement(
        val index: Int,
        val updated: ElementConfig,
    ) : EditorViewEvent

    data class UpdateLayout(
        val config: LayoutConfig,
    ) : EditorViewEvent

    data class SelectElement(
        val index: Int?,
    ) : EditorViewEvent
}

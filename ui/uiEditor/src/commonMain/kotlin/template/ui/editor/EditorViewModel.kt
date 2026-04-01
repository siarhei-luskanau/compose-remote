package template.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import template.core.pref.PrefService

@KoinViewModel
class EditorViewModel(
    @Provided private val documentBuilder: DocumentBuilder,
    @Provided private val prefService: PrefService,
) : ViewModel() {
    val viewState: StateFlow<EditorViewState>
        field = MutableStateFlow(EditorViewState())

    fun onEvent(event: EditorViewEvent) {
        when (event) {
            is EditorViewEvent.AddElement -> addElement(event.type)
            is EditorViewEvent.RemoveElement -> removeElement(event.index)
            is EditorViewEvent.MoveElement -> moveElement(event.from, event.to)
            is EditorViewEvent.UpdateElement -> updateElement(event.index, event.updated)
            is EditorViewEvent.UpdateLayout -> viewState.value = viewState.value.copy(config = event.config)
            is EditorViewEvent.SelectElement -> viewState.value = viewState.value.copy(selectedIndex = event.index)
            is EditorViewEvent.BuildAndSave -> buildAndSave()
        }
    }

    private fun buildAndSave() {
        viewModelScope.launch {
            val bytes = documentBuilder.build(viewState.value.config)
            prefService.setDocumentBytes(bytes)
        }
    }

    private fun addElement(type: String) {
        val element = defaultElement(type)
        val updated = viewState.value.config.copy(elements = viewState.value.config.elements + element)
        viewState.value = viewState.value.copy(config = updated)
    }

    private fun removeElement(index: Int) {
        val newList =
            viewState.value.config.elements
                .toMutableList()
                .also { it.removeAt(index) }
        viewState.value =
            viewState.value.copy(
                config = viewState.value.config.copy(elements = newList),
                selectedIndex = null,
            )
    }

    private fun moveElement(
        from: Int,
        to: Int,
    ) {
        val list =
            viewState.value.config.elements
                .toMutableList()
        val element = list.removeAt(from)
        list.add(to, element)
        viewState.value = viewState.value.copy(config = viewState.value.config.copy(elements = list))
    }

    private fun updateElement(
        index: Int,
        updated: ElementConfig,
    ) {
        val newList =
            viewState.value.config.elements
                .toMutableList()
                .also { it[index] = updated }
        viewState.value = viewState.value.copy(config = viewState.value.config.copy(elements = newList))
    }

    private fun defaultElement(type: String): ElementConfig =
        when (type) {
            "text" -> ElementConfig(type = "text", text = "Text", color = "#000000", fontSize = 16)
            "button" -> ElementConfig(type = "button", text = "Button", color = "#6200EA", textColor = "#FFFFFF", cornerRadius = 24)
            "spacer" -> ElementConfig(type = "spacer", height = 16)
            "hspacer" -> ElementConfig(type = "hspacer", width = 16)
            "divider" -> ElementConfig(type = "divider", color = "#CCCCCC", height = 1)
            "card" -> ElementConfig(type = "card", color = "#FFFFFF", cornerRadius = 16, children = emptyList())
            "row" -> ElementConfig(type = "row", children = emptyList())
            else -> ElementConfig(type = type)
        }
}

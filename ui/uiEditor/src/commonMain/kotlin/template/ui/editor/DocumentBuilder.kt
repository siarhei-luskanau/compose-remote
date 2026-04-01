package template.ui.editor

interface DocumentBuilder {
    fun build(config: LayoutConfig): ByteArray
}

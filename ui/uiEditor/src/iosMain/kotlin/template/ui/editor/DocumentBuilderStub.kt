package template.ui.editor

import org.koin.core.annotation.Single

@Single
internal class DocumentBuilderStub : DocumentBuilder {
    override fun build(config: LayoutConfig): ByteArray = ByteArray(0)
}

package template.core.pref

import androidx.datastore.core.Storage
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.WebStorage
import androidx.datastore.core.okio.WebStorageType
import org.koin.core.annotation.Single

@Single
internal class TestStorageProvider : StorageProvider {
    override fun <T> getStorage(serializer: OkioSerializer<T>): Storage<T> =
        WebStorage(
            serializer = serializer,
            name = "test.app.pref.json",
            storageType = WebStorageType.LOCAL,
        )
}

actual fun cleanUpTestStorage() = Unit

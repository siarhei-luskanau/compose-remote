package template.di

import androidx.datastore.core.Storage
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.WebStorage
import androidx.datastore.core.okio.WebStorageType
import org.koin.core.annotation.Single
import template.core.pref.StorageProvider

@Single
internal class AppStorageProviderWeb : StorageProvider {
    override fun <T> getStorage(serializer: OkioSerializer<T>): Storage<T> =
        WebStorage(
            serializer = serializer,
            name = "app.pref.json",
            storageType = WebStorageType.LOCAL,
        )
}

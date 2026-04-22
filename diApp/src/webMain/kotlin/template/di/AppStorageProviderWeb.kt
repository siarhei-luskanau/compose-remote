package template.di

import androidx.datastore.core.Storage
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.WebOpfsStorage
import org.koin.core.annotation.Single
import template.core.pref.StorageProvider

@Single
internal class AppStorageProviderWeb : StorageProvider {
    override fun <T> getStorage(serializer: OkioSerializer<T>): Storage<T> =
        WebOpfsStorage(
            serializer = serializer,
            name = "app.pref.json",
        )
}

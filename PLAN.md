# Compose Remote - Project Plan

## Goal

Build a Kotlin Multiplatform / Compose Multiplatform (KMP/CMP) application with two screens:
- **Player** - renders a Remote Compose document (`.rc` file) stored in DataStore
- **Editor** - form-based drag-and-drop editor that assembles a layout and saves it as a `.rc` file to DataStore

**Reference template**: https://github.com/siarhei-luskanau/compose-multiplatform-template
**compose-remote version**: `1.0.0-alpha07`
**Docs**: https://developer.android.com/jetpack/androidx/releases/compose-remote

---

## Target Platforms

| Platform | Player | Editor (writer) |
|----------|--------|-----------------|
| Android  | Real (`RemoteDocumentPlayer`) | Real (`JvmRcPlatformServices`) |
| iOS      | Stub | Stub |
| Desktop (JVM) | Stub | Real (`JvmRcPlatformServices`) |
| Web (WASM/JS) | Stub | Stub |

Stub = renders a placeholder composable with a "Not supported on this platform" message.

---

## Architecture

### Module structure

The project follows the existing multi-module template layout. Two new UI modules are added:

```
:app
  :androidApp          - Android entry point (AppActivity)
  :desktopApp          - Desktop entry point (Compose Window)
  :webApp              - Web entry point (ComposeViewport)

:core
  :coreCommon          - DispatcherSet, PlatformService (interfaces + platform @Single impls)
  :corePref            - PrefService, PrefServiceDataStore, PrefData, StorageProvider
                         EXTEND: add documentBytes: String? (base64) to PrefData

:diApp                 - Koin wiring, platform StorageProvider @Single impls, DiKoinApplication
                         ADD: wire new uiPlayer and uiEditor modules

:navigation            - AppRoutes (sealed NavKey), AppNavigation, NavApp, NavigationCommonModule
                         ADD: AppRoutes.Player, AppRoutes.Editor routes and screen registrations

:ui
  :uiCommon            - AppTheme, Color, string/drawable resources
  :uiMain              - existing Main screen (keep as-is)
  :uiSplash            - existing Splash screen (keep as-is)
  :uiPlayer            - NEW: Player screen + PlayerViewModel
  :uiEditor            - NEW: Editor screen + EditorViewModel, LayoutConfig, ElementConfig
```

### Source sets (per KMP module)

| Source set   | Platform          | RemoteDocumentView     | buildDocument                  |
|--------------|-------------------|------------------------|-------------------------------|
| `commonMain` | all               | interface (Koin)       | interface (Koin)              |
| `androidMain`| Android           | real implementation    | real (`JvmRcPlatformServices`)|
| `jvmMain`    | Desktop           | stub                   | real (`JvmRcPlatformServices`)|
| `iosMain`    | iOS               | stub                   | stub                          |
| `webMain`    | Web (JS/WASM)     | stub                   | stub                          |

### Platform abstraction pattern

The project uses **Koin DI** (not Kotlin `expect/actual`) for platform-specific logic - matching the existing `DispatcherSet` / `StorageProvider` pattern:

```
// commonMain - interface
interface RemoteDocumentRenderer {
    @Composable fun Render(bytes: ByteArray, modifier: Modifier)
}

interface DocumentBuilder {
    fun build(config: LayoutConfig): ByteArray
}

// androidMain - @Single class RemoteDocumentRendererAndroid : RemoteDocumentRenderer
// jvmMain     - @Single class RemoteDocumentRendererJvm     : RemoteDocumentRenderer (stub)
// iosMain     - @Single class RemoteDocumentRendererIos     : RemoteDocumentRenderer (stub)
// webMain     - @Single class RemoteDocumentRendererWeb     : RemoteDocumentRenderer (stub)

// androidMain + jvmMain - @Single class DocumentBuilderJvm : DocumentBuilder (real)
// iosMain + webMain     - @Single class DocumentBuilderStub: DocumentBuilder (stub)
```

Both interfaces are registered in the respective platform Koin modules and injected into ViewModels.

### Navigation

Uses existing **JetBrains Navigation3** (`NavDisplay` + `koinEntryProvider`).

Extend `AppRoutes` (sealed interface in `:navigation`):
```kotlin
@Serializable object Player : AppRoutes
@Serializable object Editor : AppRoutes
```

`AppNavigation` (@Single, in `:navigation`) adds:
```kotlin
fun goPlayerScreen()
fun goEditorScreen()
```

`MainScreen` (existing) becomes a tab host or shows two navigation items (Player / Editor).

### DataStore

Reuse the existing `:core:corePref` DataStore stack - no new library needed.

Extend `PrefData` with one new field:
```kotlin
@Serializable
data class PrefData(
    val key: String? = null,
    val documentBytes: String? = null,   // base64-encoded .rc ByteArray
)
```

Extend `PrefService` interface with:
```kotlin
fun getDocumentBytes(): Flow<ByteArray?>
suspend fun setDocumentBytes(bytes: ByteArray)
```

Implemented in `PrefServiceDataStore` using `Base64` encoding/decoding (available in `kotlin.io.encoding` since Kotlin 1.8).

---

## Data Model (`commonMain`)

```kotlin
@Serializable
data class LayoutConfig(
    val backgroundColor: String = "#F3E5F5",
    val scrollable: Boolean = false,
    val padding: Int? = null,
    val elements: List<ElementConfig> = emptyList(),
)

@Serializable
data class ElementConfig(
    val type: String,           // "text"|"button"|"spacer"|"hspacer"|"divider"|"card"|"row"
    val id: String = "",
    val text: String? = null,
    val color: String? = null,
    val textColor: String? = null,
    val fontSize: Int? = null,
    val height: Int? = null,
    val width: Int? = null,
    val cornerRadius: Int? = null,
    val borderColor: String? = null,
    val borderWidth: Int? = null,
    val paddingH: Int? = null,
    val paddingV: Int? = null,
    val actionName: String? = null,
    val align: String? = null,  // "start"|"center"|"end"
    val children: List<ElementConfig>? = null,
)
```

---

## Player Screen

Module: `:ui:uiPlayer` - follows the same structure as `:ui:uiMain`.

### commonMain
- `PlayerScreen` composable: observes `PrefService.getDocumentBytes()` via `PlayerViewModel`
- If `null` → show "No document saved yet"
- Otherwise → calls `RemoteDocumentRenderer.Render(bytes, modifier)` injected via Koin

### androidMain - `RemoteDocumentRendererAndroid`
```kotlin
@Single
class RemoteDocumentRendererAndroid : RemoteDocumentRenderer {
    @Composable
    override fun Render(bytes: ByteArray, modifier: Modifier) {
        val document = remember(bytes.contentHashCode()) { RemoteDocument(bytes) }
        RemoteDocumentPlayer(
            document = document.document,
            documentWidth = document.width,
            documentHeight = document.height,
            modifier = modifier,
            onAction = { _, _ -> },
        )
    }
}
```

### jvmMain / iosMain / webMain - stub
```kotlin
@Single
class RemoteDocumentRendererStub : RemoteDocumentRenderer {
    @Composable
    override fun Render(bytes: ByteArray, modifier: Modifier) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text("Remote Compose Player is not supported on this platform")
        }
    }
}
```

---

## Editor Screen

### Layout

```
┌──────────────────────────────────────┐
│  Layout Settings (bg color, scroll,  │
│  padding)                            │
├──────────────────────────────────────┤
│  Element palette                     │
│  [Text] [Button] [Spacer] [HSpacer]  │
│  [Divider] [Card] [Row]              │
├──────────────────────────────────────┤
│  Element list (drag-and-drop)        │
│  ┌────────────────────────────────┐  │
│  │ ≡  Text - "Hello"          [x] │  │
│  │ ≡  Button - "Click me"     [x] │  │
│  │ ≡  Divider                 [x] │  │
│  └────────────────────────────────┘  │
│                                      │
│  Selected element property panel     │
│  ┌────────────────────────────────┐  │
│  │ text:       [Hello         ]   │  │
│  │ color:      [#000000       ]   │  │
│  │ fontSize:   [16            ]   │  │
│  └────────────────────────────────┘  │
├──────────────────────────────────────┤
│              [Build & Save]          │
└──────────────────────────────────────┘
```

### ViewModel (`EditorViewModel`, `commonMain`)

State:
```kotlin
data class EditorState(
    val config: LayoutConfig = LayoutConfig(),
    val selectedIndex: Int? = null,
)
```

Actions:
- `addElement(type: String)` - appends a default `ElementConfig` for the type
- `removeElement(index: Int)`
- `moveElement(from: Int, to: Int)` - drag-and-drop reorder
- `updateElement(index: Int, updated: ElementConfig)`
- `updateLayout(config: LayoutConfig)`
- `buildAndSave()` - calls `buildDocument(config)` then `DocumentRepository.save(bytes)`

### Drag-and-drop

Use a simple manual drag implementation with `Modifier.pointerInput` + `LazyColumn` with animated item placement (no third-party library required for basic reorder). Each list item has a drag handle icon (≡) on the left.

### Element palette

A horizontal scrolling row of chips/buttons. Tapping one calls `addElement(type)` with sensible defaults:

| Type | Defaults |
|------|----------|
| text | text="Text", color="#000000", fontSize=16 |
| button | text="Button", color="#6200EA", textColor="#FFFFFF", cornerRadius=24 |
| spacer | height=16 |
| hspacer | width=16 |
| divider | color="#CCCCCC", height=1 |
| card | color="#FFFFFF", cornerRadius=16, children=[] |
| row | children=[] |

### Property panel

Shows fields relevant to the selected element's type. Only fields applicable to the type are shown (no irrelevant fields).

| Field | Input type |
|-------|-----------|
| text | TextField |
| color / textColor / borderColor / backgroundColor | TextField (hex) with a colored swatch preview |
| fontSize / height / width / cornerRadius / borderWidth / paddingH / paddingV | NumberField |
| actionName / id | TextField |
| align | DropdownMenu (start / center / end) |
| scrollable | Checkbox |

For `card` and `row` elements, children are shown as a nested sub-list with their own add/remove/reorder controls.

### Document builder (Koin DI)

Interface in `commonMain` (`:ui:uiEditor`):
```kotlin
interface DocumentBuilder {
    fun build(config: LayoutConfig): ByteArray
}
```

**androidMain + jvmMain** - `DocumentBuilderJvm` (`@Single`): full `RemoteComposeWriter` implementation using `JvmRcPlatformServices`, supporting all element types: `text`, `button`, `spacer`, `hspacer`, `divider`, `card`, `row`.

**iosMain + webMain** - `DocumentBuilderStub` (`@Single`): returns `ByteArray(0)` and shows a snackbar "Building documents is not supported on this platform".

---

## Dependencies

Already present in `libs.versions.toml`:
- `androidx-remote = "1.0.0-alpha07"` - version alias exists; artifact aliases need adding
- `androidx-datastore = "1.3.0-alpha07"` - DataStore via `androidx-datastore-core-okio`
- `kotlinx-serialization = "1.10.0"` - via `kotlinx-serialization-json`
- `koin-bom = "4.2.0"`, `kotlin = "2.3.20"` - all Koin and Kotlin tooling in place

Artifact aliases already in `libs.versions.toml`:
- `androidx-remote-core` → `androidx.compose.remote:remote-core`
- `androidx-remote-player-core` → `androidx.compose.remote:remote-player-core`
- `androidx-remote-player-view` → `androidx.compose.remote:remote-player-view`

Alias added in Phase 1:
```toml
androidx-remote-creation = { module = "androidx.compose.remote:remote-creation", version.ref = "androidx-remote" }
```

Module-level dependency wiring:
- `:ui:uiPlayer` `androidMain` → `androidx-remote-player-compose`, `androidx-remote-player-core`
- `:ui:uiEditor` `androidMain` + `jvmMain` → `androidx-remote-creation`, `androidx-remote-core`
- `:ui:uiPlayer`, `:ui:uiEditor` `commonMain` → `projects.core.corePref`, `projects.ui.uiCommon`
- `:navigation` `commonMain` → add `projects.ui.uiPlayer`, `projects.ui.uiEditor`
- `:diApp` `commonMain` → add `projects.ui.uiPlayer`, `projects.ui.uiEditor`

---

## Implementation Phases

### Phase 1 - Dependencies & DataStore extension
- Add 4 `androidx-remote-*` artifact aliases to `libs.versions.toml`
- Extend `PrefData` with `documentBytes: String?` field
- Add `getDocumentBytes()` / `setDocumentBytes()` to `PrefService` and `PrefServiceDataStore`

### Phase 2 - New modules scaffold
- Create `:ui:uiPlayer` module (copy structure of `:ui:uiMain`)
- Create `:ui:uiEditor` module (copy structure of `:ui:uiMain`)
- Wire both into `:diApp` and `:navigation` dependency graphs
- Add `AppRoutes.Player` and `AppRoutes.Editor` to `:navigation`
- Register routes in `NavigationCommonModule`

### Phase 3 - Player screen
- `RemoteDocumentRenderer` interface in `:ui:uiPlayer` `commonMain`
- `RemoteDocumentRendererAndroid` (`androidMain`) - real `RemoteDocumentPlayer`
- `RemoteDocumentRendererStub` (`jvmMain`, `iosMain`, `webMain`)
- `PlayerViewModel` + `PlayerScreen` composable (empty-state + render delegate)

### Phase 4 - Editor screen (core)
- `LayoutConfig` / `ElementConfig` data classes in `:ui:uiEditor` `commonMain`
- `EditorViewModel` with full state: `config`, `selectedIndex`
- `EditorScreen` layout: settings panel, element palette, drag-and-drop list

### Phase 5 - Editor property panel
- Per-type property forms (TextField, NumberField, hex color swatch, DropdownMenu, Checkbox)
- Nested children sub-editor for `card` and `row`

### Phase 6 - Document builder
- `DocumentBuilder` interface in `:ui:uiEditor` `commonMain`
- `DocumentBuilderJvm` (`androidMain` + `jvmMain`) - full `RemoteComposeWriter` impl
- `DocumentBuilderStub` (`iosMain` + `webMain`) - returns empty bytes
- Wire "Build & Save" in `EditorViewModel.buildAndSave()`

### Phase 7 - Navigation integration & polish
- Update `MainScreen` or `AppNavigation` to expose Player / Editor tab navigation
- Error handling: invalid hex color guard, build failure snackbar
- Platform-specific stub banners where applicable

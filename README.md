# kotlinx-htmx

[![](https://jitpack.io/v/MartinWie/kotlinx-htmx.svg)](https://jitpack.io/#MartinWie/kotlinx-htmx)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.20-blue.svg?logo=kotlin)](http://kotlinlang.org)

Type-safe [HTMX](https://htmx.org/) attributes and utilities for [kotlinx-html](https://github.com/Kotlin/kotlinx.html).

Write HTMX-powered HTML in Kotlin with full IDE support, documentation, and compile-time safety.

## Features

- **All HTMX attributes** as Kotlin extension functions with KDoc documentation
- **Type-safe enums** for swap options, sync modifiers, extensions, and headers
- **SVG embedding** with caching for inline SVG icons
- **CSP nonce support** for Content Security Policy compliance
- **Custom utilities** like `hxResetFormAfterSuccess` and `hxApplyDuringRequest`

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.MartinWie:kotlinx-htmx:0.1.0")
}
```

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.MartinWie:kotlinx-htmx:0.1.0'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.MartinWie</groupId>
    <artifactId>kotlinx-htmx</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

### Basic HTMX Attributes

```kotlin
import io.github.martinwie.htmx.*
import kotlinx.html.*

fun example() = buildHTMLString {
    button {
        hxPost("/api/submit")
        hxSwap(HxSwapOption.OUTER_HTML)
        hxTarget("#result")
        +"Submit"
    }
    
    div {
        id = "result"
        hxGet("/api/data")
        hxTrigger("load")
    }
}
```

### All HTTP Methods

```kotlin
button {
    hxGet("/api/items")      // GET request
    hxPost("/api/items")     // POST request
    hxPut("/api/items/1")    // PUT request
    hxPatch("/api/items/1")  // PATCH request
    hxDelete("/api/items/1") // DELETE request
}
```

### Swap Options

```kotlin
div {
    hxGet("/content")
    hxSwap(HxSwapOption.INNER_HTML)   // Replace inner HTML (default)
    hxSwap(HxSwapOption.OUTER_HTML)   // Replace entire element
    hxSwap(HxSwapOption.BEFORE_BEGIN) // Insert before element
    hxSwap(HxSwapOption.AFTER_BEGIN)  // Insert at start of element
    hxSwap(HxSwapOption.BEFORE_END)   // Insert at end of element
    hxSwap(HxSwapOption.AFTER_END)    // Insert after element
    hxSwap(HxSwapOption.DELETE)       // Delete target element
    hxSwap(HxSwapOption.NONE)         // No swap (OOB only)
}
```

### Out-of-Band Swaps

```kotlin
// Server response with OOB swap
div {
    id = "notification"
    hxSwapOob(true)  // Swap by matching ID
    +"Updated!"
}

// With specific swap strategy
div {
    id = "counter"
    hxSwapOob(HxSwapOption.INNER_HTML)
    +"42"
}
```

### Request Synchronization

```kotlin
input {
    hxPost("/search")
    hxTrigger("keyup changed delay:500ms")
    hxSync("closest form", SyncModifier.ABORT)  // Abort previous requests
}

// Available sync modifiers:
// SyncModifier.DROP        - Ignore if request in flight (default)
// SyncModifier.ABORT       - Abort in-flight, cancel if new request comes
// SyncModifier.REPLACE     - Abort current and replace with new
// SyncModifier.QUEUE_FIRST - Queue first request
// SyncModifier.QUEUE_LAST  - Queue last request
// SyncModifier.QUEUE_ALL   - Queue all requests
```

### Event Handling

```kotlin
button {
    hxPost("/api/action")
    hxOn("click", "console.log('clicked!')")
    hxOn("htmx:beforeRequest", "showSpinner()")
    hxOn("htmx:afterRequest", "hideSpinner()")
}
```

### Loading States & Indicators

```kotlin
button {
    hxPost("/api/slow-action")
    hxIndicator("#spinner")        // Show element during request
    hxDisabled()                   // Disable during request
    hxApplyDuringRequest("loading") // Add CSS class during request
    +"Submit"
}

span {
    id = "spinner"
    classes = setOf("htmx-indicator")
    +"Loading..."
}
```

### Form Utilities

```kotlin
form {
    hxPost("/api/submit")
    hxResetFormAfterSuccess()  // Reset form only on success
    // or
    hxResetFormAfterSubmit()   // Reset form after any request
    
    input { type = InputType.text; name = "email" }
    button { +"Subscribe" }
}
```

### URL and History Management

```kotlin
a {
    hxGet("/page/2")
    hxPushUrl(true)           // Push URL to browser history
    hxPushUrl("/custom-url")  // Push custom URL
    +"Next Page"
}

div {
    hxGet("/content")
    hxReplaceUrl(true)        // Replace current URL (no history entry)
}
```

### HTMX Extensions

```kotlin
body {
    hxExt(HtmxExtension.JSON_ENC)        // Use JSON encoding for requests
    hxExt(HtmxExtension.LOADING_STATES)  // Enhanced loading states
    hxExt(HtmxExtension.PRELOAD)         // Preload on hover/focus
}
```

### SVG Embedding

```kotlin
// Embed SVG directly from resources (cached)
button {
    embedSvg("/static/svg/icon.svg")
    +"Click me"
}

// Manual SVG loading
val svgContent = SvgLoader.loadSvg("/static/svg/logo.svg")
```

### JavaScript Integration

```kotlin
div {
    // Inline JavaScript with IDE support
    addJs("""
        document.addEventListener('DOMContentLoaded', function() {
            console.log('Page loaded!');
        });
    """)
}

// Event handlers
button {
    onEvent(JsEvent.ON_CLICK, "handleClick()")
    onEvent(JsEvent.ON_MOUSE_ENTER, "showTooltip()")
}
```

### Content Security Policy (CSP) Support

```kotlin
// In your request handler
val nonce = generateSecureNonce()
PageSecurityContext.scriptNonce = nonce

try {
    response.header("Content-Security-Policy", "script-src 'nonce-$nonce'")
    val html = buildHTMLString {
        // Scripts will automatically include the nonce
        addJs("console.log('secure!')")
    }
    respondHtml(html)
} finally {
    PageSecurityContext.scriptNonce = null
}
```

## HTMX Headers

Use the `HtmxHeaders` enum for type-safe header access:

```kotlin
// Request headers (sent by HTMX)
val isHtmxRequest = request.headers[HtmxHeaders.REQUEST_HX_REQUEST.value] == "true"
val currentUrl = request.headers[HtmxHeaders.REQUEST_HX_CURRENT_URL.value]
val targetId = request.headers[HtmxHeaders.REQUEST_HX_TARGET.value]

// Response headers (control HTMX behavior)
response.header(HtmxHeaders.RESPONSE_HX_REDIRECT.value, "/login")
response.header(HtmxHeaders.RESPONSE_HX_REFRESH.value, "true")
response.header(HtmxHeaders.RESPONSE_HX_TRIGGER.value, "showMessage")
```

## Framework Integration

### Ktor

```kotlin
fun Application.module() {
    routing {
        get("/") {
            call.respondText(
                buildHTMLString {
                    html {
                        body {
                            button {
                                hxGet("/api/greeting")
                                hxSwap(HxSwapOption.OUTER_HTML)
                                +"Load Greeting"
                            }
                        }
                    }
                },
                ContentType.Text.Html
            )
        }
    }
}
```

### Spring Boot

```kotlin
@Controller
class MyController {
    @GetMapping("/", produces = ["text/html"])
    @ResponseBody
    fun index(): String = buildHTMLString {
        html {
            body {
                div {
                    hxGet("/api/content")
                    hxTrigger("load")
                }
            }
        }
    }
}
```

## API Reference

### Attributes

| Function | HTMX Attribute | Description |
|----------|---------------|-------------|
| `hxGet(url)` | `hx-get` | Issue GET request |
| `hxPost(url)` | `hx-post` | Issue POST request |
| `hxPut(url)` | `hx-put` | Issue PUT request |
| `hxPatch(url)` | `hx-patch` | Issue PATCH request |
| `hxDelete(url)` | `hx-delete` | Issue DELETE request |
| `hxSwap(option)` | `hx-swap` | Control swap behavior |
| `hxSwapOob(...)` | `hx-swap-oob` | Out-of-band swap |
| `hxTarget(selector)` | `hx-target` | Target element for swap |
| `hxTrigger(trigger)` | `hx-trigger` | Event that triggers request |
| `hxSelect(selector)` | `hx-select` | Select content from response |
| `hxSelectOob(selector)` | `hx-select-oob` | Select OOB content |
| `hxVals(json)` | `hx-vals` | Add values to request |
| `hxHeaders(json)` | `hx-headers` | Add headers to request |
| `hxInclude(selector)` | `hx-include` | Include element values |
| `hxParams(value)` | `hx-params` | Filter parameters |
| `hxSync(selector, modifier)` | `hx-sync` | Synchronize requests |
| `hxPushUrl(...)` | `hx-push-url` | Push URL to history |
| `hxReplaceUrl(...)` | `hx-replace-url` | Replace URL in history |
| `hxOn(event, code)` | `hx-on:*` | Event handler |
| `hxIndicator(selector)` | `hx-indicator` | Loading indicator |
| `hxDisabled(selector)` | `hx-disabled-elt` | Disable during request |
| `hxConfirm(text)` | `hx-confirm` | Confirmation dialog |
| `hxPrompt(text)` | `hx-prompt` | Prompt dialog |
| `hxBoost(enabled)` | `hx-boost` | Boost links/forms |
| `hxExt(extension)` | `hx-ext` | Enable extension |
| `hxPreserve()` | `hx-preserve` | Preserve during swap |
| `hxValidate()` | `hx-validate` | HTML5 validation |

### Custom Utilities

| Function | Description |
|----------|-------------|
| `hxResetFormAfterSuccess()` | Reset form on successful request |
| `hxResetFormAfterSubmit()` | Reset form after any request |
| `hxApplyDuringRequest(class)` | Add CSS class during request |
| `buildHTMLString { }` | Build HTML string with kotlinx-html |
| `embedSvg(path)` | Embed SVG from resources |
| `addJs(code)` | Add inline JavaScript |
| `onEvent(event, code)` | Add JS event handler |

## Requirements

- Kotlin 1.9+
- kotlinx-html 0.12.0+

## License

MIT License - see [LICENSE](LICENSE) for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Links

- [HTMX Documentation](https://htmx.org/docs/)
- [HTMX Attributes Reference](https://htmx.org/reference/)
- [kotlinx-html](https://github.com/Kotlin/kotlinx.html)

@file:Suppress("unused")

package io.github.martinwie.htmx

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.intellij.lang.annotations.Language

/**
 * Builds an HTML string using the provided builder action.
 *
 * @param builderAction The action to build the HTML content
 * @return The generated HTML string
 *
 * Example usage:
 * ```kotlin
 * val html = buildHTMLString {
 *     div {
 *         h1 { +"Hello, World!" }
 *     }
 * }
 * ```
 */
fun buildHTMLString(builderAction: TagConsumer<StringBuilder>.() -> Unit): String =
    buildString {
        appendHTML().builderAction()
    }

/**
 * Object for loading SVG content from resources.
 *
 * SVGs are cached after first load to improve performance.
 */
object SvgLoader {
    private val cache = mutableMapOf<String, String>()

    /**
     * Loads SVG content from a file in the resources directory.
     *
     * @param path The path to the SVG file in the resources directory
     * @return The SVG content as a string
     * @throws IllegalArgumentException if the SVG file is not found
     *
     * Example usage:
     * ```kotlin
     * val svgContent = SvgLoader.loadSvg("/static/svg/icon.svg")
     * ```
     */
    fun loadSvg(path: String): String =
        cache.getOrPut(path) {
            val resource =
                SvgLoader::class.java.getResourceAsStream(path)
                    ?: throw IllegalArgumentException("SVG file not found: $path")
            resource.bufferedReader().use { it.readText() }
        }

    /**
     * Clears the SVG cache. Useful for testing or if SVG files might change at runtime.
     */
    fun clearCache() {
        cache.clear()
    }
}

/**
 * Embeds an SVG from a resource path directly into the HTML.
 *
 * @param svgPath The path to the SVG file in the resources directory
 *
 * Example usage:
 * ```kotlin
 * div {
 *     embedSvg("/static/svg/user.svg")
 * }
 * ```
 */
fun HTMLTag.embedSvg(svgPath: String) {
    unsafe { +SvgLoader.loadSvg(svgPath) }
}

/**
 * Enum representing JavaScript event types that can be used as HTML attributes.
 *
 * @property attributeName The corresponding HTML attribute name for the event
 *
 * Example usage:
 * ```kotlin
 * button {
 *     onEvent(JsEvent.ON_CLICK, "handleClick()")
 * }
 * ```
 */
enum class JsEvent(
    val attributeName: String,
) {
    ON_CLICK("onclick"),
    ON_CHANGE("onchange"),
    ON_INPUT("oninput"),
    ON_SUBMIT("onsubmit"),
    ON_BLUR("onblur"),
    ON_FOCUS("onfocus"),
    ON_LOAD("onload"),
    ON_MOUSE_ENTER("onmouseenter"),
    ON_MOUSE_LEAVE("onmouseleave"),
    ON_KEY_DOWN("onkeydown"),
    ON_KEY_UP("onkeyup"),
    ON_KEY_PRESS("onkeypress"),
}

/**
 * Adds a JavaScript event handler to an HTML tag.
 *
 * @param eventType The JavaScript event type from the JsEvent enum
 * @param jsCode The JavaScript code to execute when the event is triggered
 *
 * Example usage:
 * ```kotlin
 * button {
 *     onEvent(JsEvent.ON_CLICK, "alert('Button clicked!')")
 * }
 * ```
 */
fun HTMLTag.onEvent(
    eventType: JsEvent,
    @Language("JavaScript")
    jsCode: String,
) {
    attributes += eventType.attributeName to jsCode
}

/**
 * Request-scoped storage for page security context (CSP nonce).
 *
 * The nonce set here is automatically applied to `<script>` tags generated
 * by [addJs] and [addDeferredJs].
 *
 * ### Recommended usage — scoped helper
 *
 * The safest way to use the nonce is via the [withNonce] helper, which
 * guarantees cleanup even when an exception is thrown:
 *
 * ```kotlin
 * val html = PageSecurityContext.withNonce(myNonce) {
 *     buildHTMLString {
 *         addJs("console.log('secure!')")
 *     }
 * }
 * ```
 *
 * ### Coroutine safety
 *
 * Internally the nonce is stored in a [ThreadLocal]. This is safe as long as
 * the [withNonce] block (or the manual set/get) does **not** suspend across
 * dispatcher boundaries.
 *
 * If you need to access the nonce inside a suspending block that may change
 * threads, propagate the ThreadLocal with
 * `kotlinx-coroutines`' `ThreadLocal.asContextElement()`:
 *
 * ```kotlin
 * import kotlinx.coroutines.asContextElement
 * import kotlinx.coroutines.withContext
 *
 * withContext(PageSecurityContext.nonceThreadLocal.asContextElement(nonce)) {
 *     // scriptNonce is now available on any dispatcher thread
 * }
 * ```
 *
 * The [nonceThreadLocal] is intentionally exposed as public for this purpose.
 */
object PageSecurityContext {
    /**
     * The underlying [ThreadLocal] that holds the nonce value.
     *
     * Exposed so that coroutine-based frameworks can propagate it via
     * `ThreadLocal.asContextElement()` from `kotlinx-coroutines`.
     */
    val nonceThreadLocal = ThreadLocal<String?>()

    /**
     * The CSP nonce to be applied to script tags.
     * Prefer using [withNonce] over setting this property directly.
     */
    var scriptNonce: String?
        get() = nonceThreadLocal.get()
        set(value) = nonceThreadLocal.set(value)

    /**
     * Executes [block] with [scriptNonce] set to [nonce], then clears it.
     *
     * The nonce is guaranteed to be cleared in a `finally` block,
     * even if [block] throws an exception.
     *
     * @param nonce The CSP nonce value to set for the duration of [block].
     * @param block The code to execute with the nonce active. Must be non-suspending
     *              to remain thread-safe (see class-level docs for coroutine usage).
     * @return The result of [block].
     */
    fun <T> withNonce(nonce: String, block: () -> T): T {
        scriptNonce = nonce
        try {
            return block()
        } finally {
            scriptNonce = null
        }
    }
}

/**
 * Adds inline JavaScript code to an HTML element inside a script tag.
 *
 * If [PageSecurityContext.scriptNonce] is set, the nonce will be automatically
 * added to the script tag for CSP compliance.
 *
 * @param jsCode The JavaScript code to be embedded
 *
 * Example usage:
 * ```kotlin
 * div {
 *     addJs("""
 *         document.addEventListener('DOMContentLoaded', function() {
 *             console.log('Page loaded!');
 *         });
 *     """)
 * }
 * ```
 */
fun FlowOrMetaDataOrPhrasingContent.addJs(
    @Language("JavaScript")
    jsCode: String,
) {
    script(ScriptType.textJavaScript) {
        PageSecurityContext.scriptNonce?.let { attributes["nonce"] = it }
        unsafe {
            raw(jsCode)
        }
    }
}

/**
 * Adds inline JavaScript code with deferred execution.
 * The script will execute after the document has been parsed.
 *
 * @param jsCode The JavaScript code to be embedded
 */
fun FlowOrMetaDataOrPhrasingContent.addDeferredJs(
    @Language("JavaScript")
    jsCode: String,
) {
    script(ScriptType.textJavaScript) {
        attributes["defer"] = "true"
        PageSecurityContext.scriptNonce?.let { attributes["nonce"] = it }
        unsafe {
            raw(jsCode)
        }
    }
}

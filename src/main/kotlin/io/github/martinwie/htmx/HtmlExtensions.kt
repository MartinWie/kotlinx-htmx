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
 * Thread-local storage for page security context.
 *
 * This allows setting a CSP nonce for script tags that will be automatically
 * applied when using [addJs].
 *
 * Example usage:
 * ```kotlin
 * // In your request handler
 * PageSecurityContext.scriptNonce = generateNonce()
 * try {
 *     // Render HTML - scripts will automatically include the nonce
 * } finally {
 *     PageSecurityContext.scriptNonce = null
 * }
 * ```
 */
object PageSecurityContext {
    private val nonceThreadLocal = ThreadLocal<String?>()

    /**
     * The CSP nonce to be applied to script tags.
     * Set this before rendering HTML if you're using Content Security Policy.
     */
    var scriptNonce: String?
        get() = nonceThreadLocal.get()
        set(value) = nonceThreadLocal.set(value)
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

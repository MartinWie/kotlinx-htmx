package io.github.martinwie.htmx

/**
 * Enum representing all official HTMX extensions.
 *
 * Extensions provide additional functionality for HTMX and can be enabled
 * using the `hx-ext` attribute on any element.
 *
 * @property value The extension identifier used in the hx-ext attribute
 * @see [HTMX Extensions Documentation](https://htmx.org/extensions/)
 */
@Suppress("unused")
enum class HtmxExtension(
    val value: String,
) {
    /**
     * Includes the commonly-used X-Requested-With header that identifies
     * ajax requests in many backend frameworks.
     */
    AJAX_HEADER("ajax-header"),

    /**
     * An extension for using the Alpine.js morph plugin as the swapping
     * mechanism in htmx.
     */
    ALPINE_MORPH("alpine-morph"),

    /**
     * An extension for manipulating timed addition and removal of classes
     * on HTML elements.
     */
    CLASS_TOOLS("class-tools"),

    /**
     * Support for client side template processing of JSON/XML responses.
     */
    CLIENT_SIDE_TEMPLATES("client-side-templates"),

    /**
     * An extension for debugging of a particular element using htmx.
     */
    DEBUG("debug"),

    /**
     * Includes a JSON serialized version of the triggering event, if any.
     */
    EVENT_HEADER("event-header"),

    /**
     * Support for merging the head tag from responses into the existing
     * document's head.
     */
    HEAD_SUPPORT("head-support"),

    /**
     * Allows you to include additional values in a request.
     */
    INCLUDE_VALS("include-vals"),

    /**
     * Use JSON encoding in the body of requests, rather than the default
     * x-www-form-urlencoded.
     */
    JSON_ENC("json-enc"),

    /**
     * An extension for using the idiomorph morphing algorithm as a swapping
     * mechanism.
     */
    IDIOMORPH("idiomorph"),

    /**
     * Allows you to disable inputs, add and remove CSS classes to any element
     * while a request is in-flight.
     */
    LOADING_STATES("loading-states"),

    /**
     * Use the X-HTTP-Method-Override header for non-GET and POST requests.
     */
    METHOD_OVERRIDE("method-override"),

    /**
     * An extension for using the morphdom library as the swapping mechanism
     * in htmx.
     */
    MORPHDOM_SWAP("morphdom-swap"),

    /**
     * Allows to swap multiple elements with different swap methods.
     */
    MULTI_SWAP("multi-swap"),

    /**
     * An extension for expressing path-based dependencies similar to
     * intercooler.js.
     */
    PATH_DEPS("path-deps"),

    /**
     * Preloads selected href and hx-get targets based on rules you control.
     */
    PRELOAD("preload"),

    /**
     * Allows you to remove an element after a given amount of time.
     */
    REMOVE_ME("remove-me"),

    /**
     * Allows to specify different target elements to be swapped when different
     * HTTP response codes are received.
     */
    RESPONSE_TARGETS("response-targets"),

    /**
     * Allows you to trigger events when the back button has been pressed.
     */
    RESTORED("restored"),

    /**
     * Uni-directional server push messaging via EventSource.
     */
    SERVER_SENT_EVENTS("server-sent-events"),

    /**
     * Bi-directional connection to WebSocket servers.
     */
    WEB_SOCKETS("web-sockets"),

    /**
     * Allows to use parameters for path variables instead of sending them
     * in query or body.
     */
    PATH_PARAMS("path-params"),
    ;

    override fun toString(): String = value
}

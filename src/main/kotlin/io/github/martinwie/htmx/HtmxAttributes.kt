@file:Suppress("unused")

package io.github.martinwie.htmx

import kotlinx.html.HTMLTag
import org.intellij.lang.annotations.Language

// ==================== HTTP Method Attributes ====================

/**
 * The hx-get attribute will cause an element to issue a GET to the specified URL
 * and swap the HTML into the DOM using a swap strategy.
 *
 * @param path The URL to issue the GET request to
 * @see [hx-get Documentation](https://htmx.org/attributes/hx-get/)
 */
fun HTMLTag.hxGet(path: String) {
    attributes += "hx-get" to path
}

/**
 * The hx-post attribute will cause an element to issue a POST to the specified URL
 * and swap the HTML into the DOM using a swap strategy (HxSwapOption).
 *
 * @param path The URL to issue the POST request to
 * @see [hx-post Documentation](https://htmx.org/attributes/hx-post/)
 */
fun HTMLTag.hxPost(path: String) {
    attributes += "hx-post" to path
}

/**
 * The hx-put attribute will cause an element to issue a PUT to the specified URL
 * and swap the HTML into the DOM using a swap strategy (HxSwapOption).
 *
 * @param path The URL to issue the PUT request to
 * @see [hx-put Documentation](https://htmx.org/attributes/hx-put/)
 */
fun HTMLTag.hxPut(path: String) {
    attributes += "hx-put" to path
}

/**
 * The hx-delete attribute will cause an element to issue a DELETE to the specified URL
 * and swap the HTML into the DOM using a swap strategy (HxSwapOption).
 *
 * @param path The URL to issue the DELETE request to
 * @see [hx-delete Documentation](https://htmx.org/attributes/hx-delete/)
 */
fun HTMLTag.hxDelete(path: String) {
    attributes += "hx-delete" to path
}

/**
 * The hx-patch attribute will cause an element to issue a PATCH to the specified URL
 * and swap the HTML into the DOM using a swap strategy (HxSwapOption).
 *
 * @param path The URL to issue the PATCH request to
 * @see [hx-patch Documentation](https://htmx.org/attributes/hx-patch/)
 */
fun HTMLTag.hxPatch(path: String) {
    attributes += "hx-patch" to path
}

// ==================== Swap Attributes ====================

/**
 * Enum representing the available swap strategies for hx-swap.
 *
 * @property value The string value used in the hx-swap attribute
 */
enum class HxSwapOption(
    val value: String,
) {
    /** Replace the inner HTML of the target element */
    INNER_HTML("innerHTML"),

    /** Replace the entire target element with the response */
    OUTER_HTML("outerHTML"),

    /** Insert the response before the target element */
    BEFORE_BEGIN("beforebegin"),

    /** Insert the response before the first child of the target element */
    AFTER_BEGIN("afterbegin"),

    /** Insert the response after the last child of the target element */
    BEFORE_END("beforeend"),

    /** Insert the response after the target element */
    AFTER_END("afterend"),

    /** Delete the target element regardless of the response */
    DELETE("delete"),

    /** Does not append content from response (out of band items will still be processed) */
    NONE("none"),
}

/**
 * The hx-swap attribute allows you to specify how the response will be swapped
 * in relative to the target of an AJAX request. If you do not specify the option,
 * the default is htmx.config.defaultSwapStyle (innerHTML).
 *
 * @param swapOption The swap strategy to use
 * @see [hx-swap Documentation](https://htmx.org/attributes/hx-swap/)
 */
fun HTMLTag.hxSwap(swapOption: HxSwapOption) {
    attributes += "hx-swap" to swapOption.value
}

/**
 * The hx-swap-oob attribute allows you to specify that some content in a response
 * should be swapped into the DOM somewhere other than the target, that is "Out of Band".
 * This allows you to piggy back updates to other element updates on a response.
 *
 * If hx-swap-oob is set to a boolean value in a server response, it can trigger an
 * update of a specific element on the page that shares the same ID as the response element.
 *
 * @param enabled Whether out-of-band swapping is enabled
 * @see [hx-swap-oob Documentation](https://htmx.org/attributes/hx-swap-oob/)
 */
fun HTMLTag.hxSwapOob(enabled: Boolean = true) {
    attributes += "hx-swap-oob" to "$enabled"
}

/**
 * The hx-swap-oob attribute allows you to specify that some content in a response
 * should be swapped into the DOM somewhere other than the target, that is "Out of Band".
 *
 * If set, a server response containing the corresponding selector can update elements
 * marked with hx-swap-oob, allowing for simultaneous, targeted updates on the page.
 *
 * @param selector The CSS selector for the target element
 * @see [hx-swap-oob Documentation](https://htmx.org/attributes/hx-swap-oob/)
 */
fun HTMLTag.hxSwapOob(selector: String) {
    attributes += "hx-swap-oob" to selector
}

/**
 * The hx-swap-oob attribute with a specific swap option and optional selector.
 *
 * @param swapOption The swap strategy to use
 * @param selector Optional CSS selector for the target element
 * @see [hx-swap-oob Documentation](https://htmx.org/attributes/hx-swap-oob/)
 */
fun HTMLTag.hxSwapOob(
    swapOption: HxSwapOption,
    selector: String? = null,
) {
    attributes += "hx-swap-oob" to swapOption.value + selector?.let { ":$it" }
}

// ==================== Target and Selection Attributes ====================

/**
 * Defines the `hx-target` attribute used to specify the target element for content
 * swapping in AJAX responses. This attribute can accept various values:
 *
 * - A CSS query selector specifying the exact element to target.
 * - `this` to indicate that the element with the `hx-target` attribute is the target.
 * - `closest <CSS selector>` to find the nearest ancestor (or the element itself) matching the specified selector.
 * - `find <CSS selector>` to locate the first child descendant that matches the specified selector.
 * - `next` which targets the element immediately following the current one in the DOM.
 * - `next <CSS selector>` to find the next element in the DOM that matches the specified selector.
 * - `previous` which targets the element immediately preceding the current one in the DOM.
 * - `previous <CSS selector>` to find the previous element in the DOM that matches the specified selector.
 *
 * @param selector The CSS selector or special value for targeting
 * @see [hx-target Documentation](https://htmx.org/attributes/hx-target/)
 */
fun HTMLTag.hxTarget(selector: String) {
    attributes += "hx-target" to selector
}

/**
 * The hx-select attribute allows you to select the content you want swapped from a response.
 * The value of this attribute is a CSS query selector of the element or elements to select
 * from the response.
 *
 * @param selector The CSS selector to select content from the response
 * @see [hx-select Documentation](https://htmx.org/attributes/hx-select/)
 */
fun HTMLTag.hxSelect(selector: String) {
    attributes += "hx-select" to selector
}

/**
 * The hx-select-oob attribute allows you to select content from a response to be swapped
 * in via an out-of-band swap. The value of this attribute is comma separated list of
 * elements to be swapped out of band. This attribute is almost always paired with hx-select.
 *
 * @param selector The CSS selector for out-of-band content selection
 * @see [hx-select-oob Documentation](https://htmx.org/attributes/hx-select-oob/)
 */
fun HTMLTag.hxSelectOob(selector: String) {
    attributes += "hx-select-oob" to selector
}

// ==================== Trigger Attributes ====================

/**
 * The hx-trigger attribute allows you to specify what triggers an AJAX request.
 * A trigger value can be one of the following:
 *
 * - An event name (e.g. "click" or "my-custom-event") followed by an event filter and a set of event modifiers
 * - A polling definition of the form "every <timing declaration>"
 * - A comma-separated list of such events
 *
 * @param trigger The trigger specification
 * @see [hx-trigger Documentation](https://htmx.org/attributes/hx-trigger/)
 */
fun HTMLTag.hxTrigger(trigger: String) {
    attributes += "hx-trigger" to trigger
}

/**
 * The hx-on* attributes allow you to embed scripts inline to respond to events directly
 * on an element; Similar to the "onevent" properties found in HTML, such as onClick.
 *
 * @param event The event name to respond to
 * @param jsCode The JavaScript code to execute when the event fires
 * @see [hx-on Documentation](https://htmx.org/attributes/hx-on/)
 */
fun HTMLTag.hxOn(
    event: String,
    @Language("JavaScript")
    jsCode: String,
) {
    attributes += "hx-on:$event" to jsCode
}

// ==================== URL and History Attributes ====================

/**
 * The hx-push-url attribute allows you to push a URL into the browser location history.
 * This creates a new history entry, allowing navigation with the browser's back and forward buttons.
 * htmx snapshots the current DOM and saves it into its history cache, and restores from this cache
 * on navigation.
 *
 * @param enabled Whether to push the fetched URL to browser history
 * @see [hx-push-url Documentation](https://htmx.org/attributes/hx-push-url/)
 */
fun HTMLTag.hxPushUrl(enabled: Boolean) {
    attributes += "hx-push-url" to "$enabled"
}

/**
 * The hx-push-url attribute allows you to push a URL into the browser location history.
 *
 * @param path The URL to push into the browser history
 * @see [hx-push-url Documentation](https://htmx.org/attributes/hx-push-url/)
 */
fun HTMLTag.hxPushUrl(path: String) {
    attributes += "hx-push-url" to path
}

/**
 * The hx-replace-url attribute allows you to replace the current url of the browser
 * location history.
 *
 * @param useFetchedUrl If true, replaces the fetched URL in the browser navigation bar
 * @see [hx-replace-url Documentation](https://htmx.org/attributes/hx-replace-url/)
 */
fun HTMLTag.hxReplaceUrl(useFetchedUrl: Boolean) {
    attributes += "hx-replace-url" to useFetchedUrl.toString()
}

/**
 * The hx-replace-url attribute allows you to replace the current url of the browser
 * location history with a specific URL.
 *
 * @param url A URL to be replaced into the location bar (may be relative or absolute)
 * @see [hx-replace-url Documentation](https://htmx.org/attributes/hx-replace-url/)
 */
fun HTMLTag.hxReplaceUrl(url: String) {
    attributes += "hx-replace-url" to url
}

/**
 * Set the hx-history attribute to false on any element in the current document,
 * or any html fragment loaded into the current document by htmx, to prevent sensitive
 * data being saved to the localStorage cache when htmx takes a snapshot of the page state.
 *
 * @see [hx-history Documentation](https://htmx.org/attributes/hx-history/)
 */
fun HTMLTag.hxHistory() {
    attributes += "hx-history" to "false"
}

/**
 * The hx-history-elt attribute allows you to specify the element that will be used to
 * snapshot and restore page state during navigation. By default, the body tag is used.
 *
 * @see [hx-history-elt Documentation](https://htmx.org/attributes/hx-history/)
 */
fun HTMLTag.hxHistoryElt() {
    attributes += "hx-history-elt" to "true"
}

// ==================== Request Modification Attributes ====================

/**
 * The hx-vals attribute allows you to add to the parameters that will be submitted
 * with an AJAX request.
 *
 * By default, the value of this attribute is a list of name-expression values in JSON format.
 * If you wish for hx-vals to evaluate the values given, you can prefix the values with
 * "javascript:" or "js:".
 *
 * **Security Considerations**: By default, the value of hx-vals must be valid JSON.
 * It is not dynamically computed. If you use the "javascript:" prefix, be aware that you
 * are introducing security considerations, especially when dealing with user input.
 *
 * @param json The JSON object containing additional values
 * @see [hx-vals Documentation](https://htmx.org/attributes/hx-vals/)
 */
fun HTMLTag.hxVals(json: String) {
    attributes += "hx-vals" to json
}

/**
 * The hx-headers attribute allows you to add to the headers that will be submitted
 * with an AJAX request.
 *
 * By default, the value of this attribute is a list of name-expression values in JSON format.
 * If you wish for hx-headers to evaluate the values given, you can prefix the values with
 * "javascript:" or "js:".
 *
 * @param headers The JSON object containing additional headers
 * @see [hx-headers Documentation](https://htmx.org/attributes/hx-headers/)
 */
fun HTMLTag.hxHeaders(headers: String) {
    attributes += "hx-headers" to headers
}

/**
 * The hx-include attribute allows you to include additional element values in an AJAX request.
 * The value of this attribute can be:
 *
 * - A CSS query selector of the elements to include
 * - "this" which will include the descendants of the element
 * - "closest <CSS selector>" which will find the closest ancestor element or itself
 * - "find <CSS selector>" which will find the first child descendant element
 * - "next <CSS selector>" which will scan the DOM forward
 * - "previous <CSS selector>" which will scan the DOM backwards
 *
 * @param selector The selector specifying elements to include
 * @see [hx-include Documentation](https://htmx.org/attributes/hx-include/)
 */
fun HTMLTag.hxInclude(selector: String = "this") {
    attributes += "hx-include" to selector
}

/**
 * The hx-params attribute allows you to filter the parameters that will be submitted
 * with an AJAX request.
 *
 * Possible values:
 * - "*" - Include all parameters (default)
 * - "none" - Include no parameters
 * - "not <param-list>" - Include all except the comma separated list of parameter names
 * - "<param-list>" - Include all the comma separated list of parameter names
 *
 * @param value The parameter filter specification
 * @see [hx-params Documentation](https://htmx.org/attributes/hx-params/)
 */
fun HTMLTag.hxParams(value: String = "*") {
    attributes += "hx-params" to value
}

/**
 * Exclude version of hxParams function to handle excluding certain parameters.
 * "not <param-list>" - Include all except the comma-separated list of parameter names.
 *
 * @param params The parameter names to exclude
 */
fun HTMLTag.hxParamsExclude(vararg params: String) {
    attributes +=
        if (params.isNotEmpty()) {
            "hx-params" to params.joinToString(",")
        } else {
            "hx-params" to "*"
        }
}

/**
 * Include version of hxParams function to handle including only specific parameters.
 * "<param-list>" - Include only the comma-separated list of parameter names.
 *
 * @param params The parameter names to include
 */
fun HTMLTag.hxParamsInclude(vararg params: String) {
    attributes +=
        if (params.isNotEmpty()) {
            "hx-params" to params.joinToString(",")
        } else {
            "hx-params" to "none"
        }
}

/**
 * The hx-request attribute allows you to configure various aspects of the request:
 *
 * - "timeout" - the timeout for the request, in milliseconds
 * - "credentials" - if the request will send credentials
 * - "noHeaders" - strips all headers from the request
 *
 * It is possible to concat multiple with a ",".
 * You may make the values dynamically evaluated by adding the "javascript:" or "js:" prefix.
 *
 * @param requestAttributes The request configuration string
 * @see [hx-request Documentation](https://htmx.org/attributes/hx-request/)
 */
fun HTMLTag.hxRequest(requestAttributes: String) {
    attributes += "hx-request" to requestAttributes
}

/**
 * The hx-encoding attribute allows you to switch the request encoding
 * from the usual "application/x-www-form-urlencoded" encoding to "multipart/form-data",
 * usually to support file uploads in an ajax request.
 *
 * @see [hx-encoding Documentation](https://htmx.org/attributes/hx-encoding/)
 */
fun HTMLTag.hxEncoding() {
    attributes += "hx-encoding" to "multipart/form-data"
}

// ==================== Synchronization Attributes ====================

/**
 * Enum representing synchronization strategies for hx-sync.
 *
 * @property value The string value used in the hx-sync attribute
 */
enum class SyncModifier(
    val value: String,
) {
    /** Drop (ignore) this request if an existing request is in flight (the default) */
    DROP("drop"),

    /** Drop this request if an existing request is in flight, and abort this request if another occurs */
    ABORT("abort"),

    /** Abort the current request, if any, and replace it with this request */
    REPLACE("replace"),

    /** Queue the first request to show up while a request is in flight */
    QUEUE_FIRST("queue first"),

    /** Queue the last request to show up while a request is in flight */
    QUEUE_LAST("queue last"),

    /** Queue all requests that show up while a request is in flight */
    QUEUE_ALL("queue all"),
}

/**
 * The hx-sync attribute allows you to synchronize AJAX requests between multiple elements.
 *
 * @param selector The CSS selector to synchronize with
 * @param modifier The synchronization strategy
 * @see [hx-sync Documentation](https://htmx.org/attributes/hx-sync/)
 */
fun HTMLTag.hxSync(
    selector: String,
    modifier: SyncModifier = SyncModifier.DROP,
) {
    attributes += "hx-sync" to "$selector :${modifier.value}"
}

// ==================== UI Feedback Attributes ====================

/**
 * The hx-indicator attribute allows you to specify the element that will have the
 * htmx-request class added to it for the duration of the request.
 * This can be used to show spinners or progress indicators while the request is in flight.
 *
 * @param selector The CSS selector of the indicator element
 * @see [hx-indicator Documentation](https://htmx.org/attributes/hx-indicator/)
 */
fun HTMLTag.hxIndicator(selector: String) {
    attributes += "hx-indicator" to selector
}

/**
 * The hx-disabled-elt attribute allows you to specify elements that will have the
 * disabled attribute added to them for the duration of the request.
 *
 * @param selector The CSS selector of elements to disable, or "this"
 * @see [hx-disabled-elt Documentation](https://htmx.org/attributes/hx-disabled-elt/)
 */
fun HTMLTag.hxDisabled(selector: String = "this") {
    attributes += "hx-disabled-elt" to selector
}

// ==================== Behavior Modification Attributes ====================

/**
 * The hx-boost attribute allows you to "boost" normal anchors and form tags to use
 * AJAX instead. This has the nice fallback that, if the user does not have javascript
 * enabled, the site will continue to work.
 *
 * @param value Whether boosting is enabled
 * @see [hx-boost Documentation](https://htmx.org/attributes/hx-boost/)
 */
fun HTMLTag.hxBoost(value: Boolean = true) {
    attributes += "hx-boost" to "$value"
}

/**
 * The hx-disable attribute will disable htmx processing for a given element and all
 * its children. This can be useful as a backup for HTML escaping, when you include
 * user generated content in your site.
 *
 * @see [hx-disable Documentation](https://htmx.org/attributes/hx-disable/)
 */
fun HTMLTag.hxDisable() {
    attributes += "hx-disable" to "true"
}

/**
 * The hx-disinherit attribute allows you to control automatic attribute inheritance.
 *
 * @param htmxTags List of htmx attributes to disinherit, defaults to "*" (all)
 * @see [hx-disinherit Documentation](https://htmx.org/attributes/hx-disinherit/)
 */
fun HTMLTag.hxDisinherit(htmxTags: List<String> = listOf("*")) {
    attributes += "hx-disinherit" to htmxTags.joinToString(" ")
}

/**
 * The hx-preserve attribute allows you to keep an element unchanged during HTML replacement.
 * Elements with hx-preserve set are preserved by id when htmx updates any ancestor element.
 * You must set an unchanging id on elements for hx-preserve to work.
 *
 * @see [hx-preserve Documentation](https://htmx.org/attributes/hx-preserve/)
 */
fun HTMLTag.hxPreserve() {
    attributes += "hx-preserve" to "true"
}

// ==================== Confirmation Attributes ====================

/**
 * The hx-confirm attribute allows you to confirm an action before issuing a request.
 * This can be useful in cases where the action is destructive and you want to ensure
 * that the user really wants to do it.
 *
 * @param text The confirmation message to display
 * @see [hx-confirm Documentation](https://htmx.org/attributes/hx-confirm/)
 */
fun HTMLTag.hxConfirm(text: String) {
    attributes += "hx-confirm" to text
}

/**
 * The hx-prompt attribute allows you to show a prompt before issuing a request.
 * The value of the prompt will be included in the request in the "HX-Prompt" header.
 *
 * @param text The prompt message to display
 * @see [hx-prompt Documentation](https://htmx.org/attributes/hx-prompt/)
 */
fun HTMLTag.hxPrompt(text: String) {
    attributes += "hx-prompt" to text
}

// ==================== Validation Attributes ====================

/**
 * The hx-validate attribute will cause an element to validate itself by way of the
 * HTML5 Validation API before it submits a request.
 *
 * Only <form> elements validate data by default, but other elements do not.
 * Adding hx-validate="true" to <input>, <textarea> or <select> enables validation
 * before sending requests.
 *
 * @see [hx-validate Documentation](https://htmx.org/attributes/hx-validate/)
 */
fun HTMLTag.hxValidate() {
    attributes += "hx-validate" to "true"
}

// ==================== Extension Attributes ====================

/**
 * The hx-ext attribute enables a htmx extension for an element and all its children.
 *
 * The value can be a single extension name or a comma separated list of extensions to apply.
 * The hx-ext tag may be placed on parent elements if you want a plugin to apply to an
 * entire swath of the DOM, and on the body tag for it to apply to all htmx requests.
 *
 * @param extension The HTMX extension to enable
 * @see [hx-ext Documentation](https://htmx.org/attributes/hx-ext/)
 */
fun HTMLTag.hxExt(extension: HtmxExtension) {
    attributes += "hx-ext" to extension.value
}

/**
 * The hx-ext attribute with a custom extension name string.
 *
 * @param extensionName The extension name string
 * @see [hx-ext Documentation](https://htmx.org/attributes/hx-ext/)
 */
fun HTMLTag.hxExt(extensionName: String) {
    attributes += "hx-ext" to extensionName
}

// ==================== Custom Utility Extensions ====================

/**
 * Resets a form after a successful HTMX request.
 *
 * This is a convenience function that adds an hx-on::after-request handler
 * to reset the form only when the request was successful.
 */
fun HTMLTag.hxResetFormAfterSuccess() {
    attributes += "hx-on::after-request" to "if(event.detail.successful) this.reset()"
}

/**
 * Resets a form after any HTMX request (regardless of success/failure).
 *
 * This is a convenience function that adds an hx-on::after-request handler
 * to reset the form after every request.
 */
fun HTMLTag.hxResetFormAfterSubmit() {
    attributes += "hx-on::after-request" to "this.reset()"
}

/**
 * Automatically adds a CSS class to an element during an HTMX request and removes
 * it when the request completes.
 *
 * This creates a convenient way to apply visual feedback (like loading spinners or
 * style changes) during AJAX requests without having to manually add multiple event handlers.
 *
 * @param styleClass The CSS class to apply during the request
 */
fun HTMLTag.hxApplyDuringRequest(styleClass: String) {
    attributes += "hx-on:htmx:before-request" to "this.classList.add('$styleClass')"
    attributes += "hx-on:htmx:after-request" to "this.classList.remove('$styleClass')"
}

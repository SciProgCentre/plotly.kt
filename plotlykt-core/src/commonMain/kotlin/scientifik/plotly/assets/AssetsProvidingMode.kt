package scientifik.plotly.assets

/**
 * Available modes:
 *   - [Online][AssetsProvidingMode.Online].
 *   - [Bundled][AssetsProvidingMode.Bundled].
 */
enum class AssetsProvidingMode {
    /** Assets will be referenced as online resource via url. */
    Online,

    /**
     * Assets will be downloaded and included in html file.
     * This will increase file size (to 5-10 Mb) but can be sent as a single file
     * and opened on devices without Internet connection.
     */
    Bundled

    /** Assets will be downloaded and referenced as a local file. */
    //Offline, // TODO implement
}

package scientifik.plotly.models

enum class ZsmoothType {
    fast,
    best,
    `false`
}

interface `2DTableInterface` {
    var xgap: Int

    var ygap: Int

    var zsmooth: ZsmoothType
}
package scientifik.plotly.models

enum class ZsmoothType {
    fast,
    best,
    `false`
}

interface Table2D {
    var xgap: Number

    var ygap: Number

    var zsmooth: ZsmoothType
}
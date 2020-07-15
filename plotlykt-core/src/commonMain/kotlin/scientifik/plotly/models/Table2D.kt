package scientifik.plotly.models

enum class ZsmoothType {
    fast,
    best,
    `false`
}

interface Table2D {
    var xgap: Int

    var ygap: Int

    var zsmooth: ZsmoothType
}
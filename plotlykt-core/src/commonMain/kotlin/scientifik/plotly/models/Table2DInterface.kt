package scientifik.plotly.models

enum class ZsmoothType {
    fast,
    best,
    `false`
}

interface Table2DInterface {
    var xgap: Int

    var ygap: Int

    var zsmooth: ZsmoothType
}
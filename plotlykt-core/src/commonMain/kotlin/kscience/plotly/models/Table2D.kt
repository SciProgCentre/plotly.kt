package kscience.plotly.models

public enum class ZsmoothType {
    fast,
    best,
    `false`
}

public interface Table2D {
    public var xgap: Number

    public var ygap: Number

    public var zsmooth: ZsmoothType
}
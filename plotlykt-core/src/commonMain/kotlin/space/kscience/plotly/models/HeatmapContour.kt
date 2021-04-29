package space.kscience.plotly.models

public enum class DataType {
    array,
    scaled
}

public interface HeatmapContour {
    public var xtype: DataType

    public var ytype: DataType
}
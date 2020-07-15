package scientifik.plotly.models

enum class DataType {
    array,
    scaled
}

interface HeatmapContour {
    var xtype: DataType

    var ytype: DataType
}
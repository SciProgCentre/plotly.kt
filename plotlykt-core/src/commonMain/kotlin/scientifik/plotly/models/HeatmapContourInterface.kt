package scientifik.plotly.models

enum class DataType {
    array,
    scaled
}

interface HeatmapContourInterface {
    var xtype: DataType

    var ytype: DataType
}
package kscience.plotly.models

interface Line {
    val color: Color?

    var width: Number

    var widthList: List<Number>
}
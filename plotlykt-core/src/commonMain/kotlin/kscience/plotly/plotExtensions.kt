package kscience.plotly

import kscience.plotly.models.*


public inline fun Plot.histogram(block: Histogram.() -> Unit): Histogram {
    val trace = Histogram(block)
    traces(trace)
    return trace
}

public inline fun Plot.histogram2d(block: Histogram2D.() -> Unit): Histogram2D {
    val trace = Histogram2D(block)
    traces(trace)
    return trace
}

public inline fun Plot.histogram2dcontour(block: Histogram2DContour.() -> Unit): Histogram2DContour {
    val trace = Histogram2DContour(block)
    traces(trace)
    return trace
}

public inline fun Plot.pie(block: Pie.() -> Unit): Pie {
    val trace = Pie(block)
    traces(trace)
    return trace
}

public inline fun Plot.contour(block: Contour.() -> Unit): Contour {
    val trace = Contour(block)
    traces(trace)
    return trace
}

public inline fun Plot.scatter(block: Scatter.() -> Unit): Scatter {
    val trace = Scatter(block)
    traces(trace)
    return trace
}

public inline fun Plot.heatmap(block: Heatmap.() -> Unit): Heatmap {
    val trace = Heatmap(block)
    traces(trace)
    return trace
}

public inline fun Plot.box(block: Box.() -> Unit): Box {
    val trace = Box(block)
    traces(trace)
    return trace
}

public inline fun Plot.violin(block: Violin.() -> Unit): Violin {
    val trace = Violin(block)
    traces(trace)
    return trace
}

public inline fun Plot.bar(block: Bar.() -> Unit): Bar {
    val trace = Bar(block)
    traces(trace)
    return trace
}

public fun Plot.text(block: Text.() -> Unit) {
    layout.annotation(block)
}

public fun Plot.shape(block: Shape.() -> Unit) {
    layout.figure(block)
}

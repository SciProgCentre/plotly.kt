### How To Draw A Sinus?
In data visualization tasks, quite often there is a need to depict mathematical 
functions - for example, to compare the convergence rate of a model
with ideal one or to find an approximation for a given time series. Such charts 
have a number of features that are not inherent in most other charts: for example,
visualizing the perpendicular axes (OX, OY) from (0, 0) and a large number of
additional information. This can be the expected value of the function, its extremum
points, deviation at various points, etc.

This article will tell you how to depict an ordinary sinus using the library `Plotly.kt`
so that in the end you will get a visual and informative plot.

1. Let's start with drawing perpendicular OX and OY axes. This requires to do a few things:
   hide default axes lines, hide zero lines (it's impossible to draw an arrow on the end of it),
   add perpendicular arrows using annotations (`Text`) with visible arrows on the end, but without
   text. After that, let's sign the axes themselves. It is also important to specify the required 
   chart sizes (`width`,` height`) so that after saving the graph will look the same as in the browser.
        
    ```
    Plotly.page {                                // making new html page with plot
        layout {
            width = 900                          // width of the plot (in px.)
            height = 500                         // height of the plot (in px.)

            text {                               // vertical axis
                x = Value.of(0)                  // position of the top end of the arrow = (0, 1+eps)
                y = Value.of(1 + eps)
                ax = Value.of(0)                 // ax, ay means the offset of the bottom edge relative to the top
                ay = Value.of(430)               // positive (negative) value is the arrow length
                                                 // upwards (top down) and from right to left (from left to right)
            }

            text {                               // horizontal axis
                y = Value.of(0)                  // arrow left position = (2PI + eps, 0)
                x = Value.of(2 * PI + eps)
                ax = Value.of(-800)              // position of the left end of the arrow
                ay = Value.of(0)
            }

            xaxis {                              // OX parameters
                showline = false                 // hide OX line
                zeroline = false                 // hide zero line
            }
            yaxis {                              // OY parameters
                showline = false                 // hide OY line
                zeroline = false                 // hide zero line
            }
        }
    }.makeFile()                                 // making temporary file and visualing plot in the browser
    ```
         
2. Now we will draw the sinus function itself - for its visualization let's choose a bright blue color,
   contrasting with both the white background and the black ticks on the axes. After that using 
   the variation of the `mode` parameter (` ScatterMode: lines, markers`) the intersection points with the OY axis
   will be marked with a slightly darker shade of the same color.
   
    ```
    x1 = (-410..410).map{ it / 200 * PI }        // function domain (-2PI - eps, 2PI + eps)
    y1 = sin(x1)                                 // function values

    Plotly.page {
        scatter {                                // making Scatter plot (sinus)
            x.set(x1)                            // assigning OX values
            y.set(y1)                            // assigning OY values
            line { color(XKCD.CERULEAN) }        // color of the line is cerulean
        }

        scatter {                                // making Scatter plot (OX axis dots)
            mode = ScatterMode.markers           // visualizing only dots
            x(-2* PI, -PI, PI, 2* PI)            // points of intersection of sinus with OX (values ​​along the X axis)
            y(0, 0, 0, 0)                        // points of intersection of sinus with OX (values ​​along the Y axis)
            line { color(XKCD.CERULEAN_BLUE) }   // color of the dots is darker cerulean
            marker { size = 8 }                  // dot's diameter is 8 px.
        }

        layout { ... }
    }.makeFile()
    ```
3. Let's draw horizontal dashed lines on the chart, corresponding to values ​​equal to -1, 1 (extremum lines) and 1/2.
   This requires changing `line.dash` parameter to `Dash.dash` value. The specified values ​​will be plotted after that 
   on the OY axis using the `tickvals` and` ticklabels` parameters.

    ```
    ...
    Plotly.page {
        ...
        layout {
            shape {                               // adding new figure (line y = 1)
                x0 = Value.of(-2*PI)              // (-2PI, 1) and (2PI, 1) coordinates  
                x1 = Value.of(2*PI)               // will be connected by a line
                y0 = Value.of(1)
                y1 = Value.of(1)
                line { dash = Dash.dash }         // dashed type of the line
            }

            shape {                               // adding new figure (line y = 1/2)
                x0 = Value.of(-2*PI)              // coordinates (-2PI, 1/2) and (2PI, 1/2) 
                x1 = Value.of(2*PI)               // will be connected by a line
                y0 = Value.of(0.5)
                y1 = Value.of(0.5)
                line {
                    color("red")                  // red color of the line
                    dash = Dash.dash              // dashed type of the line
                }
            }

            shape {                               // adding new figure (line y = -1)
                x0 = Value.of(-2*PI)              // coordinates (-2PI, -1) and (2PI, -1) 
                x1 = Value.of(2*PI)               // will be connected by a line
                y0 = Value.of(-1)
                y1 = Value.of(-1)
                line { dash = Dash.dash }         // dashed type of the line
            }
            ...
        }
    }.makeFile()
    ```

4. After that, it is worth adding vertical lines connecting points on OX, the values in
   which equals 1/2, and the value itself in LaTeX format. Shapes are used again for this - an array
   values ​​of type `Shape.line`. To use LaTeX format you need to include the `MathJax` header.

    ```
    ...
    val sub = PI / 6
    val xElems = listOf(-2PI + sub, -PI - sub, 0 + sub, PI - sub)
    // points, where sinus equals 1/2

    val shapesList = mutableListOf<Shape>()        // making list of lines
    for (x: xElems) {
        val shape = Shape {
            x0 = Value.of(x)                       // (x, 0) and (x, 0.5) will be connected
            y0 = Value.of(0)
            x1 = Value.of(x)
            y0 = Value.of(1/2)
            line { color("red") }                  // red color of the line
        }
        shapesList.add(shape)                      // adding new figure to the list
    }

    Plotly.page(mathJaxHeader, cdnPlotlyHeader) {
        scatter {                                  // add string "1/2"
            mode = ScatterMode.text                // visualing only text
            x(-0.35)                               // text position on the OX axis
            y(0.56)                                // text position on the OX axis
            text = listOf("$\Large{1/2}$")         // increasing font size with ТеХ
            textfont { color("red") }              // red color of the font
            showlegend = false                     // do not show this trace in the legend
            hoverinfo = "none"                     // do not show anything on hover
        }
        ...
        layout {                                   // adding figure list to the plot
            shapes = shapesList                      
            ...
        }
    }.makeFile()
    ``` 

5. It remains to add labels on OX corresponding to the intersections of the sinus with the axis. It
   is done in a similar way, using axis labels (`tickvals`,` ticktext`) and writing text in LaTeX format.
                                                                                                 
    ```
    ...
    Plolty.page(mathJaxHeader, cdnPlotlyHeader) {
        ...
        layout {
            xaxis {                                // OX axis parameters
                ...
                anchor = "free"                    // axis position is set manually
                position = 0.43                    // assigning axis position
                tickvals(listOf(-2 * PI-0.05, -PI - 0.15, PI - 0.05, 2 * PI + 0.1))
                ticktext(listOf("\$\\huge{-2\\pi}\$", "\$\\huge{-\\pi}\$", "\$\\huge{\\pi}\$", "\$\\huge{2\\pi}\$"))
                // ticks positions and text
            }
            yaxis {                                // OY axis parameters
                ...
                anchor = "free"                    // axis position is set manually
                position = 0.485                   // assigning axis position
                tickvals(listOf(-0.91, 0.09, 1.09))
                ticktext(listOf("\$\\Large{-1}\$", "\$\\Large{0}\$", "\$\\Large{1}\$"))
                // ticks positions and text
            }
            ...
        }
    }.makeFile()
    ```

6. As a final improvement, let's add a legend to the plot, which will be placed in upper right corner of the graph.
   First you need to set the name of the corresponding line, after which with using the parameters `xanchor = right` 
   and` yanchor = top` set the position of the legend so that the coordinates specified by the parameters 
   `x = 1`,` y = 1` correspond to the position in the upper right corner of the image.

    ```
    ...
    Plotly.page(mathJaxHeader, cdnPlotlyHeader) {
        scatter {                                   // sinus Scatter trace
            ...
            name = "\$\\Large{y = \\mathrm{sin}\\,x}\$"
            // the name of the plot displayed in the legend
        }

        layout {
            legend {                                // legend parameters
                x = 0.97                            // horizontal position of the legend
                y = 1                               // vertical position of the legend
                borderwidth = 1                     // width of the legend border
                font { size = 32 }                  // size of the legend font
                xanchor = XAnchor.right             // "anchoring" the position of the legend to the right corner
                yanchor = YAnchor.top               // "anchoring" the position of the legend to the bottom of the graph
            }
            ...
        }
    }.makeFile()
    ```

7. Final picture:

![Картинка](https://i.ibb.co/wKTCp4H/newplot-17.png)

Source code is available at: https://mipt-npm.jetbrains.space/p/plt/code/plotly.kt/files/dev-katsam-3/examples/src/main/kotlin/tutorials/SinusPicture.kt
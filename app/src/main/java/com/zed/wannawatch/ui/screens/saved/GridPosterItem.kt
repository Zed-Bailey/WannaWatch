package com.zed.wannawatch.ui.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathParser
import com.zed.wannawatch.R
import com.zed.wannawatch.ui.screens.search.AnimatedImageLoader
import java.util.regex.Pattern

@Composable
fun GridPosterItem(watched: Boolean, posterUrl: String, onclick: () -> Unit) {

    if(watched) {
        Box(modifier = Modifier
            .width(128.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(128.dp)
                    .height(170.dp)
                    .clip(CutShape())
                    .clickable {
                        onclick()
                    }
            ) {
                AnimatedImageLoader(url = posterUrl, width = 128.dp, height = 170.dp)
            }

            Icon(
                painter = painterResource(id = R.drawable.watched_tick),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd),
                tint = Color.Unspecified
            )
        }

    } else {
        Box(
            modifier = Modifier
                .width(128.dp)
                .height(170.dp)
                .clip(RoundedCornerShape(15.dp))
                .clickable {
                    onclick()
                }
        ) {

            AnimatedImageLoader(url = posterUrl, width = 128.dp, height = 170.dp)
        }
    }


}

@Preview
@Composable
fun PreviewClippedShape() {

    Box(
        modifier = Modifier
            .width(128.dp)
            .height(192.dp)
            .clip(CutShape())

    ) {
        Box(modifier = Modifier.width(128.dp).height(192.dp).background(Color.Red))
    }

}

class CutShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
//        https://blog.devgenius.io/custom-shapes-in-jetpack-compose-deep-dive-b987a52c743c
        val svgPath = "M 115.5 30 C 120.396728515625 30 124.82369995117188 27.988832473754883 128 24.747455596923828 L 128 179 C 128 186.17970275878906 122.1796875 192 115 192 L 13 192 C 5.8203125 192 0 186.17970275878906 0 179 L 0 12.999996185302734 C 0 5.820293426513672 5.8203125 0 13 0 L 103.25253295898438 0 C 100.01116943359375 3.1762924194335938 98 7.603279113769531 98 12.5 C 98 22.164981842041016 105.83502197265625 30 115.5 30 Z "
        val scaleX = size.width/128f
        val scaleY = size.height/192f
        return Outline.Generic(PathParser.createPathFromPathData(resize(svgPath,scaleX, scaleY)).asComposePath())
    }
    private fun resize(pathData: String,scaleX:Float,scaleY:Float): String {
        val matcher = Pattern.compile("[0-9]+[.]?([0-9]+)?").matcher(pathData) // match the numbers in the path
        val stringBuffer = StringBuffer()
        var count = 0
        while(matcher.find()){
            val number = matcher.group().toFloat()
            matcher.appendReplacement(stringBuffer,(if(count % 2 == 0) number * scaleX else number * scaleY).toString()) // replace numbers with scaled numbers
            ++count
        }
        return stringBuffer.toString() // return the scaled path
    }
}
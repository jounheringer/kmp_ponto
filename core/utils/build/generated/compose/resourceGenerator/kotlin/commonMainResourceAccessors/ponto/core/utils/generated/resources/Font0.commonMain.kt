@file:OptIn(InternalResourceApi::class)

package ponto.core.utils.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/ponto.core.utils.generated.resources/"

internal val Res.font.indie_flower_regular: FontResource by lazy {
      FontResource("font:indie_flower_regular", setOf(
        ResourceItem(setOf(), "${MD}font/indie_flower_regular.ttf", -1, -1),
      ))
    }

internal val Res.font.permanent_marker_regular: FontResource by lazy {
      FontResource("font:permanent_marker_regular", setOf(
        ResourceItem(setOf(), "${MD}font/permanent_marker_regular.ttf", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("indie_flower_regular", Res.font.indie_flower_regular)
  map.put("permanent_marker_regular", Res.font.permanent_marker_regular)
}

package com.tatsuki.droidkitapp.ui.compose.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.droidkitapp.R

@Composable
fun SliderRow(
  modifier: Modifier,
  @StringRes valueTypeResource: Int,
  value: Float,
  onValueChange: (Float) -> Unit,
  valueRange: ClosedFloatingPointRange<Float>,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(text = stringResource(id = valueTypeResource, "%.1f".format(value)))
    Spacer(modifier = Modifier.size(24.dp))
    Slider(
      value = value,
      onValueChange = onValueChange,
      valueRange = valueRange,
    )
  }
}

@Preview
@Composable
private fun PreviewSpeedSliderRow() {
  SliderRow(
    modifier = Modifier.fillMaxWidth(),
    valueTypeResource = R.string.speed_label,
    value = 0f,
    onValueChange = {},
    valueRange = 0f..1f
  )
}

@Preview
@Composable
private fun PreviewDegreeSliderRow() {
  SliderRow(
    modifier = Modifier.fillMaxWidth(),
    valueTypeResource = R.string.degree_label,
    value = 0f,
    onValueChange = {},
    valueRange = 0f..180f
  )
}

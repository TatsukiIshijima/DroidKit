package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tatsuki.droidkitapp.R

@Composable
fun ColorSection(
  modifier: Modifier,
  colorPickerController: ColorPickerController,
  onColorChanged: (ColorEnvelope) -> Unit,
  onClickSet: () -> Unit,
  onClickReset: () -> Unit,
) {
  SectionFrame(
    modifier = modifier,
    titleResource = R.string.color_section_title,
    body = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        HsvColorPicker(
          modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
          controller = colorPickerController,
          onColorChanged = onColorChanged,
        )
        Spacer(modifier = Modifier.size(16.dp))
        ActionButtonsRow(
          onClickPositive = onClickSet,
          onClickNegative = onClickReset
        )
      }
    }
  )
}

@Preview
@Composable
private fun PreviewColorSection() {
  ColorSection(
    modifier = Modifier.fillMaxWidth(),
    colorPickerController = rememberColorPickerController(),
    onColorChanged = {},
    onClickSet = {},
    onClickReset = {},
  )
}

package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.droidkitapp.R

@Composable
fun RotationSection(
  modifier: Modifier,
  value: Float,
  onValueChange: (Float) -> Unit,
  onClickTurn: () -> Unit,
  onClickRest: () -> Unit,
) {
  SectionFrame(
    modifier = modifier,
    titleResource = R.string.rotation_section_title,
    body = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        SliderRow(
          modifier = Modifier.fillMaxWidth(),
          valueTypeResource = R.string.degree_label,
          value = value,
          onValueChange = onValueChange,
          valueRange = 0f..180f
        )
        ActionButtonsRow(
          positiveButtonLabelId = R.string.turn_button_label,
          onClickPositive = onClickTurn,
          onClickNegative = onClickRest
        )
      }
    }
  )
}

@Preview
@Composable
private fun PreviewRotationSection() {
  RotationSection(
    modifier = Modifier.fillMaxWidth(),
    value = 0f,
    onValueChange = {},
    onClickTurn = {},
    onClickRest = {},
  )
}

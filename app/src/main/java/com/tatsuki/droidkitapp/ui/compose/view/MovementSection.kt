package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.droidkitapp.R

@Composable
fun MovementSection(
  modifier: Modifier,
  value: Float,
  onValueChange: (Float) -> Unit,
  onClickGo: () -> Unit,
  onClickBack: () -> Unit,
) {
  SectionFrame(
    modifier = modifier,
    titleResource = R.string.movement_section_title,
    body = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        SliderRow(
          modifier = Modifier.fillMaxWidth(),
          valueTypeResource = R.string.speed_label,
          value = value,
          onValueChange = onValueChange,
          valueRange = 0f..1f,
        )
        ActionButtonsRow(
          positiveButtonLabelId = R.string.go_button_label,
          negativeButtonLabelId = R.string.back_button_label,
          onClickPositive = onClickGo,
          onClickNegative = onClickBack,
        )
      }
    }
  )
}

@Preview
@Composable
private fun PreviewMovementSection() {
  MovementSection(
    modifier = Modifier.fillMaxWidth(),
    onClickGo = {},
    onClickBack = {},
    value = 0f,
    onValueChange = {}
  )
}

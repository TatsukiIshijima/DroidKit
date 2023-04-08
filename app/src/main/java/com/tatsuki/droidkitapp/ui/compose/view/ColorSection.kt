package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.droidkitapp.R

@Composable
fun ColorSection(
  modifier: Modifier,
  onClickSet: () -> Unit,
  onClickReset: () -> Unit,
) {
  SectionFrame(
    modifier = modifier,
    titleResource = R.string.color_section_title,
    body = {
      ActionButtonsRow(
        onClickPositive = onClickSet,
        onClickNegative = onClickReset
      )
    }
  )
}

@Preview
@Composable
private fun PreviewColorSection() {
  ColorSection(
    modifier = Modifier.fillMaxWidth(),
    onClickSet = {},
    onClickReset = {}
  )
}

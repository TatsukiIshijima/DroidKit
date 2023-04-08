package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.droidkitapp.R

@Composable
fun ConnectionSection(
  modifier: Modifier,
  onClickConnect: () -> Unit,
  onClickDisconnect: () -> Unit,
) {
  SectionFrame(
    modifier = modifier,
    titleResource = R.string.connection_section_title,
    body = {
      ActionButtonsRow(
        positiveButtonLabelId = R.string.connect_button_label,
        negativeButtonLabelId = R.string.disconnect_button_label,
        onClickPositive = onClickConnect,
        onClickNegative = onClickDisconnect,
      )
    }
  )
}

@Preview
@Composable
private fun PreviewConnectionSection() {
  ConnectionSection(
    modifier = Modifier.fillMaxWidth(),
    onClickConnect = {},
    onClickDisconnect = {},
  )
}

package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.droidkitapp.R

@Composable
fun ConnectionSection(
  modifier: Modifier,
  onClickConnect: () -> Unit,
  onClickDisconnect: () -> Unit,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    SectionTitle(
      modifier = Modifier.fillMaxWidth(),
      textResource = R.string.connection_section_title,
    )
    Spacer(modifier = Modifier.size(8.dp))
    ActionButtonsRow(
      positiveButtonLabelId = R.string.connect_button_label,
      negativeButtonLabelId = R.string.disconnect_button_label,
      onClickPositive = onClickConnect,
      onClickNegative = onClickDisconnect,
    )
  }
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

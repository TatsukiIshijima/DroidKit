package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.droidkitapp.R

@Composable
fun MovementButtonsRow(
  onClickGo: () -> Unit,
  onClickStop: () -> Unit,
  onClickBack: () -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
  ) {
    Button(
      onClick = onClickGo,
      content = {
        Text(text = stringResource(id = R.string.go_button_label))
      }
    )
    Spacer(modifier = Modifier.size(16.dp))
    Button(
      onClick = onClickStop,
      content = {
        Text(text = stringResource(id = R.string.stop_button_label))
      }
    )
    Spacer(modifier = Modifier.size(16.dp))
    Button(
      onClick = onClickBack,
      content = {
        Text(text = stringResource(id = R.string.back_button_label))
      }
    )
  }
}

@Preview
@Composable
private fun PreviewMovementButtonsRow() {
  MovementButtonsRow(
    onClickGo = {},
    onClickStop = {},
    onClickBack = {}
  )
}
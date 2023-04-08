package com.tatsuki.droidkitapp.ui.compose.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionFrame(
  modifier: Modifier,
  @StringRes titleResource: Int,
  body: @Composable () -> Unit,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    SectionTitle(
      modifier = Modifier.fillMaxWidth(),
      textResource = titleResource,
    )
    Spacer(modifier = Modifier.size(8.dp))
    body()
  }
}

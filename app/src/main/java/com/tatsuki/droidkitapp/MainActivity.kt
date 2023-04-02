package com.tatsuki.droidkitapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkitapp.ui.theme.DroidKitAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

  private val mainViewModel by viewModels<MainViewModel>()

  // TODO:ViewModelへ移行
  private val mutableIsGrantedFineLocationPermissionStateFlow = MutableStateFlow(false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // TODO:ViewModelへ移行
    val requestPermissionLauncher =
      registerForActivityResult(RequestPermission()) { isGranted ->
        mutableIsGrantedFineLocationPermissionStateFlow.value = isGranted
      }
    requestPermissionLauncher.launch(
      Manifest.permission.ACCESS_FINE_LOCATION
    )

    setContent {
      DroidKitAppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          Body(
            connectSection = {
              ConnectionSection(
                onClickConnect = {
                  mainViewModel.connect()
                },
                onClickDisconnect = {
                  mainViewModel.disconnect()
                },
              )
            },
            colorSection = {
              ColorSection()
            },
            soundSection = {
              SoundSection(
                onClickButton = {
                  mainViewModel.playSound(DroidCommand.PlaySound.S1)
                }
              )
            },
          )
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()

    mainViewModel.disconnect()
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  DroidKitAppTheme {
    Body(
      connectSection = {
        ConnectionSection(
          onClickConnect = {},
          onClickDisconnect = {},
        )
      },
      colorSection = { ColorSection() },
      soundSection = { SoundSection() },
    )
  }
}

@Composable
private fun Body(
  modifier: Modifier = Modifier.fillMaxWidth(),
  connectSection: @Composable () -> Unit = {},
  colorSection: @Composable () -> Unit = {},
  soundSection: @Composable () -> Unit = {},
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    connectSection()
    Spacer(modifier = Modifier.size(24.dp))
    colorSection()
    Spacer(modifier = Modifier.size(24.dp))
    soundSection()
    Spacer(modifier = Modifier.size(24.dp))
  }
}

@Preview
@Composable
private fun ConnectionSection(
  modifier: Modifier = Modifier.fillMaxWidth(),
  onClickConnect: () -> Unit = {},
  onClickDisconnect: () -> Unit = {},
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = "Connection")
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
    ) {
      ActionButtonsRow(
        positiveButtonLabel = "Connect",
        negativeButtonLabel = "Disconnect",
        onClickPositive = { onClickConnect() },
        onClickNegative = { onClickDisconnect() },
      )
    }
  }
}

@Preview
@Composable
private fun ColorSection(
  modifier: Modifier = Modifier.fillMaxWidth()
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = "Color")
    ActionButtonsRow(
      positiveButtonLabel = "Set",
      negativeButtonLabel = "Reset",
      onClickPositive = {},
      onClickNegative = {},
    )
  }
}

@Preview
@Composable
private fun SoundSection(
  modifier: Modifier = Modifier.fillMaxWidth(),
  onClickButton: () -> Unit = {}
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = "Sound")
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Type No.")
      Spacer(modifier = Modifier.size(16.dp))
      Button(
        onClick = {
          onClickButton()
        },
        content = {
          Text(text = "Set")
        },
      )
    }
  }
}

@Preview
@Composable
private fun ActionButtonsRow(
  positiveButtonLabel: String = "OK",
  negativeButtonLabel: String = "Cancel",
  onClickPositive: () -> Unit = {},
  onClickNegative: () -> Unit = {},
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    Button(
      onClick = { onClickPositive() },
      content = {
        Text(text = positiveButtonLabel)
      },
    )
    Spacer(modifier = Modifier.size(16.dp))
    Button(
      onClick = { onClickNegative() },
      content = {
        Text(text = negativeButtonLabel)
      },
    )
  }
}
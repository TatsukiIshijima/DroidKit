package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import com.tatsuki.droidkit.common.DroidBLE.CONNECT_FAILED
import com.tatsuki.droidkit.common.DroidBLE.CONNECT_FAILED_TIMEOUT_ERROR
import com.tatsuki.droidkit.event.BluetoothGattEvent
import com.tatsuki.droidkit.event.ScanEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class DroidClientImpl(
  private val context: Context,
  private val coroutineScope: CoroutineScope,
) : DroidClient {

  private val bluetoothAdapter =
    (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

  private val droidScanner = DroidScannerImpl(context, bluetoothAdapter)
  private var droidConnector: DroidConnector? = null
  private var droidOperator: DroidOperator? = null

  private val mutableScanStateFlow = MutableStateFlow<ScanEvent?>(null)
  private val mutableBluetoothGattStateFlow = MutableStateFlow<BluetoothGattEvent?>(null)

  private var bluetoothDevice: BluetoothDevice? = null
  private var bluetoothGatt: BluetoothGatt? = null

  @SuppressLint("MissingPermission")
  override suspend fun connect(
    onSuccess: () -> Unit,
    onFailure: (errorCode: Int) -> Unit,
  ) {
    scan()
    val scannedResult = awaitFoundedDroid()
    if (scannedResult is ScanEvent.OnScanFailed) {
      mutableScanStateFlow.value = null
      onFailure(scannedResult.errorCode)
      return
    }

    bluetoothDevice = (scannedResult as? ScanEvent.OnScanResult)?.device ?: return
    connectTo(bluetoothDevice)
    when (awaitConnectedDroid()) {
      is BluetoothGattEvent.OnConnected -> {
        onSuccess()
      }
      is BluetoothGattEvent.OnConnectingTimeout -> {
        onFailure(CONNECT_FAILED_TIMEOUT_ERROR)
      }
      else -> {
        onFailure(CONNECT_FAILED)
      }
    }
    mutableScanStateFlow.value = null
    mutableBluetoothGattStateFlow.value = null
  }

  private fun scan() {
    droidScanner.startScan()
      .onEach { scanEvent ->
        mutableScanStateFlow.value = scanEvent
      }
      .launchIn(coroutineScope)
  }

  private suspend fun awaitFoundedDroid(): ScanEvent {
    return mutableScanStateFlow.filterNotNull().first()
  }

  private fun connectTo(device: BluetoothDevice?) {
    if (device == null) return
    droidConnector = DroidConnectorImpl(context, device)
    droidConnector?.connect()
      ?.onEach { gattEvent ->
        bluetoothGatt = gattEvent.gatt
        mutableBluetoothGattStateFlow.value = gattEvent
      }
      ?.launchIn(coroutineScope)
  }

  private suspend fun awaitConnectedDroid(): BluetoothGattEvent {
    return mutableBluetoothGattStateFlow.filterNotNull().first {
      it is BluetoothGattEvent.OnConnected || it is BluetoothGattEvent.OnConnectingTimeout
    }
  }

  fun inConnected(): Boolean {
    if (bluetoothGatt == null || bluetoothDevice == null) return false
    return bluetoothGatt?.getConnectionState(bluetoothDevice) == BluetoothProfile.STATE_CONNECTED
  }

  override fun disconnect() {
    droidConnector?.disconnect(bluetoothGatt)
    droidConnector = null
    droidOperator = null
    bluetoothGatt = null
    bluetoothDevice = null
  }
}
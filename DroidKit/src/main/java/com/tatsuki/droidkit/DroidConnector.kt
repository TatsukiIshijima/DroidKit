package com.tatsuki.droidkit

import android.bluetooth.BluetoothGatt
import com.tatsuki.droidkit.event.BluetoothGattEvent
import kotlinx.coroutines.flow.Flow

interface DroidConnector {

  fun connect(timeout: Long = CONNECT_TIMEOUT_MILL): Flow<BluetoothGattEvent>

  fun disconnect(gatt: BluetoothGatt?)

  companion object {
    private const val CONNECT_TIMEOUT_MILL = 5000L
  }
}
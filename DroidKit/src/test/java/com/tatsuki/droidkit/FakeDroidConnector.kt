package com.tatsuki.droidkit

import android.bluetooth.BluetoothGattCharacteristic

class FakeDroidConnector : DroidConnector {
    override suspend fun connect() {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override suspend fun discoverServices() {
        TODO("Not yet implemented")
    }

    override suspend fun discoverCharacteristics() {
        TODO("Not yet implemented")
    }

    override suspend fun setNotifyValue(characteristic: BluetoothGattCharacteristic) {
        TODO("Not yet implemented")
    }

    override suspend fun setNotifyValues() {
        TODO("Not yet implemented")
    }

    override suspend fun writeValue() {
        TODO("Not yet implemented")
    }
}
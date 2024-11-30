package com.example.calculator6_

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EquipmentInputsShape(equipment: Equipment, onUpdate: (Equipment) -> Unit) {

    OutlinedTextField(
        value = equipment.name,
        onValueChange = { onUpdate(equipment.copy(name = it)) },
        label = { Text("Найменування ЕП") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = equipment.efficiency,
        onValueChange = { onUpdate(equipment.copy(efficiency = it)) },
        label = { Text("Номінальне значення ККД (ηн)") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = equipment.powerFactor,
        onValueChange = { onUpdate(equipment.copy(powerFactor = it)) },
        label = { Text("Коефіцієнт потужності навантаження (cos φ)") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = equipment.voltage,
        onValueChange = { onUpdate(equipment.copy(voltage = it)) },
        label = { Text("Напруга навантаження (Uн, кВ)") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = equipment.quantity,
        onValueChange = { onUpdate(equipment.copy(quantity = it)) },
        label = { Text("Кількість ЕП (n, шт)") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = equipment.nominalPower,
        onValueChange = { onUpdate(equipment.copy(nominalPower = it)) },
        label = { Text("Номінальна потужність ЕП (Рн, кВт)") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = equipment.usageCoefficient,
        onValueChange = { onUpdate(equipment.copy(usageCoefficient = it)) },
        label = { Text("Коефіцієнт використання (КВ)") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = equipment.reactivePowerFactor,
        onValueChange = { onUpdate(equipment.copy(reactivePowerFactor = it)) },
        label = { Text("Коефіцієнт реактивної потужності (tg φ)") },
        modifier = Modifier.fillMaxWidth()
    )
}
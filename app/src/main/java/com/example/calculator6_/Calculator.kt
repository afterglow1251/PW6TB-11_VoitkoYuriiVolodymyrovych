package com.example.calculator6_

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Stable
data class Equipment(
    var name: String = "",
    var efficiency: String = "",
    var powerFactor: String = "",
    var voltage: String = "",
    var quantity: String = "",
    var nominalPower: String = "",
    var usageCoefficient: String = "",
    var reactivePowerFactor: String = "",
    var totalNominalPower: String = "",
    var current: String = "",
)

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {

    var equipmentList by remember {
        mutableStateOf(
            listOf(
                Equipment("Шліфувальний верстат", "0.92", "0.9", "0.38", "4", "20", "0.15", "1.33"),
                Equipment("Свердлильний верстат", "0.92", "0.9", "0.38", "2", "14", "0.12", "1"),
                Equipment("Фугувальний верстат", "0.92", "0.9", "0.38", "4", "42", "0.15", "1.33"),
                Equipment("Циркулярна пила", "0.92", "0.9", "0.38", "1", "36", "0.3", "1.52"),
                Equipment("Прес", "0.92", "0.9", "0.38", "1", "20", "0.5", "0.75"),
                Equipment("Полірувальний верстат", "0.92", "0.9", "0.38", "1", "40", "0.2", "1"),
                Equipment("Фрезерний верстат", "0.92", "0.9", "0.38", "2", "32", "0.2", "1"),
                Equipment("Вентилятор", "0.92", "0.9", "0.38", "1", "20", "0.65", "0.75"),
            )
        )
    }

    var totalNominalPowerWithCoefficient by remember { mutableDoubleStateOf(0.0) }

    var loadCoefficient1 by remember { mutableStateOf("0.7") }
    var loadCoefficient2 by remember { mutableStateOf("1.25") }

    var groupUtilizationCoefficient by remember { mutableStateOf("") }
    var effectiveEquipmentCount by remember { mutableStateOf("") }

    var totalDeptUtilizationCoef by remember { mutableStateOf("") }
    var effectiveEquipmentDeptAmount by remember { mutableStateOf("") }

    var totalActivePowerDept by remember { mutableStateOf("") }
    var totalReactivePowerDept by remember { mutableStateOf("") }
    var totalApparentPowerDept by remember { mutableStateOf("") }
    var totalCurrentDept by remember { mutableStateOf("") }

    var totalActivePowerDept1 by remember { mutableStateOf("") }
    var totalReactivePowerDept1 by remember { mutableStateOf("") }
    var totalApparentPowerDept1 by remember { mutableStateOf("") }
    var totalCurrentDept1 by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 24.dp, end=16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Button(
            onClick = { equipmentList = equipmentList + Equipment() },
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
        ) {
            Text("Додати ЕП")
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            equipmentList.forEachIndexed { index, equipment ->
                EquipmentInputsShape(
                    equipment = equipment,
                    onUpdate = { updatedEquipment ->
                        equipmentList = equipmentList.toMutableList().apply {
                            this[index] = updatedEquipment
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Button(
            onClick = {
                var totalNominalPowerCoeffProduct = 0.0
                var totalNominalPowerProduct = 0.0
                var totalNominalPowerSquared = 0.0

                equipmentList.forEach { equipment ->
                    val quantity = equipment.quantity.toDouble()
                    val nominalPower = equipment.nominalPower.toDouble()
                    equipment.totalNominalPower = "${quantity * nominalPower}"
                    val currentPower = equipment.totalNominalPower.toDouble() / (
                            sqrt(3.0) *
                                    equipment.voltage.toDouble() *
                                    equipment.powerFactor.toDouble() *
                                    equipment.efficiency.toDouble()
                            )
                    equipment.current = currentPower.toString()

                    totalNominalPowerCoeffProduct += equipment.totalNominalPower.toDouble() * equipment.usageCoefficient.toDouble()
                    totalNominalPowerProduct += equipment.totalNominalPower.toDouble()
                    totalNominalPowerSquared += quantity * nominalPower.pow(2)
                }

                totalNominalPowerWithCoefficient = totalNominalPowerCoeffProduct

                val groupUtilization = totalNominalPowerCoeffProduct / totalNominalPowerProduct
                groupUtilizationCoefficient = groupUtilization.toString()

                val effectiveEquipment = ceil(totalNominalPowerProduct * totalNominalPowerProduct / totalNominalPowerSquared)
                effectiveEquipmentCount = effectiveEquipment.toString()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Обчислити")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = """
        Груповий коефіцієнт використання: $groupUtilizationCoefficient
        Ефективна кількість ЕП: $effectiveEquipmentCount
    """.trimIndent(),
        )

        OutlinedTextField(
            value = loadCoefficient2,
            onValueChange = { loadCoefficient2 = it },
            label = { Text("Розрахунковий коеф. активної потужності (Kr)") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = {
                val utilizationCoef = groupUtilizationCoefficient.toDoubleOrNull() ?: 0.0
                val loadCoef = loadCoefficient2.toDoubleOrNull() ?: 0.0

                val voltageLevel = 0.38

                val referencePower = 26.0 // за 7 варіантом
                val tanPhi = 1.62 // за 7 варіантом

                val activePower = loadCoef * totalNominalPowerWithCoefficient
                val reactivePower = utilizationCoef * referencePower * tanPhi
                val apparentPower = sqrt(activePower.pow(2) + reactivePower.pow(2))
                val groupCurrent = activePower / voltageLevel

                totalActivePowerDept = activePower.toString()
                totalReactivePowerDept = reactivePower.toString()
                totalApparentPowerDept = apparentPower.toString()
                totalCurrentDept = groupCurrent.toString()

                totalDeptUtilizationCoef = (752.0 / 2330.0).toString()
                effectiveEquipmentDeptAmount = (2330.0 * 2330.0 / 96399.0).toString()
            },
            modifier = Modifier.fillMaxWidth().padding(top=16.dp)
        ) {
            Text("Обчислити потужність та струм")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = """
        Розрахункове активне навантаження: $totalActivePowerDept
        Розрахункове реактивне навантаження: $totalReactivePowerDept
        Повна потужність: $totalApparentPowerDept
        Розрахунковий груповий струм ШР1: $totalCurrentDept
        Коефіцієнт використання цеху в цілому: $totalDeptUtilizationCoef
        Ефективна кількість ЕП цеху в цілому: $effectiveEquipmentDeptAmount
    """.trimIndent(),
        )

        OutlinedTextField(
            value = loadCoefficient1,
            onValueChange = { loadCoefficient1 = it },
            label = { Text("Розрахунковий коеф. активної потужності (Kr2)") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = {
                val utilizationCoef = loadCoefficient1.toDoubleOrNull() ?: 0.0
                val activePowerBus = utilizationCoef * 752.0
                val reactivePowerBus = utilizationCoef * 657.0
                val apparentPowerBus = sqrt(activePowerBus.pow(2) + reactivePowerBus.pow(2))
                val busCurrent = activePowerBus / 0.38

                totalActivePowerDept1 = activePowerBus.toString()
                totalReactivePowerDept1 = reactivePowerBus.toString()
                totalApparentPowerDept1 = apparentPowerBus.toString()
                totalCurrentDept1 = busCurrent.toString()
            },
            modifier = Modifier.fillMaxWidth().padding(top=16.dp)
        ) {
            Text("Обчислити (шини 0,38 кВ ТП)")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = """
        Розрахункове активне навантаження на шинах: $totalActivePowerDept1
        Розрахункове реактивне навантаження на шинах: $totalReactivePowerDept1
        Повна потужність на шинах: $totalApparentPowerDept1
        Розрахунковий груповий струм на шинах: $totalCurrentDept1
    """.trimIndent(),
        )
    }
}

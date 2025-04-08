package com.liamaulida0026.assessmentmobpro1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.liamaulida0026.assessmentmobpro1.R
import com.liamaulida0026.assessmentmobpro1.model.Logo
import com.liamaulida0026.assessmentmobpro1.navigation.Screen
import com.liamaulida0026.assessmentmobpro1.ui.theme.AssessmentMobpro1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.menu))
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.bantuan)) },
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.Help.route)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.tentang_aplikasi)) },
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.About.route)
                            }
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var inputAngka by rememberSaveable { mutableStateOf("") }
    var inputAngkaError by rememberSaveable { mutableStateOf(false) }

    var selectedFromUnit by rememberSaveable { mutableStateOf("Pilih satuan awal") }
    var selectedToUnit by rememberSaveable { mutableStateOf("Pilih satuan tujuan") }

    var hasil by rememberSaveable { mutableStateOf(false) }
    var resultValue by rememberSaveable { mutableFloatStateOf(0f) }

    val context = LocalContext.current

    val data = Logo(
        nama = "Logo Aplikasi",
        imageResId = R.drawable.logo
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = data.imageResId),
            contentDescription = stringResource(R.string.gambar_logo),
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = stringResource(id = R.string.intro_aplikasi),
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = inputAngka,
            onValueChange = { inputAngka = it },
            label = { Text(stringResource(id = R.string.input_label)) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { IconPicker(inputAngkaError, "") },
            supportingText = { ErrorHint(inputAngkaError) },
            isError = inputAngkaError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )

        DropdownSelector(stringResource(id = R.string.dari_satuan), selectedFromUnit) { selectedFromUnit = it }
        DropdownSelector(stringResource(id = R.string.ke_satuan), selectedToUnit) { selectedToUnit = it }

        Button(
            onClick = {
                inputAngkaError = (inputAngka.isEmpty() || inputAngka == "0")

                // Jika error input, nantinya akan langsung keluar
                if (inputAngkaError) {
                    hasil = false
                    return@Button
                }

                // Jika satuan belum dipilih, juga tidak tampilkan hasilnya
                if (
                    selectedFromUnit == "Pilih satuan awal" ||
                    selectedToUnit == "Pilih satuan tujuan"
                ) {
                    hasil = false
                    return@Button
                }

                // Jika semua valid, baru akan di hitung
                resultValue = convertWeight(inputAngka, selectedFromUnit, selectedToUnit)
                hasil = true
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.hasil))
        }

        if (hasil) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = stringResource(R.string.hasil_x, resultValue),
                style = MaterialTheme.typography.headlineLarge
            )
            Button(
                onClick = {
                    shareData(
                        context = context,
                        message = context.getString(R.string.bagikan_template, inputAngka, selectedFromUnit, selectedToUnit, resultValue)
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.bagikan))
            }
        }

    }
}

@Composable
fun DropdownSelector(label: String, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Gram", "Kilogram", "Ons", "Pound")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError)
        Text(
            text = stringResource(id = R.string.invalid_input),
            color = MaterialTheme.colorScheme.error
        )
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    } else {
        Text(text = unit)
    }
}

fun convertWeight(value: String, fromUnit: String, toUnit: String): Float {
    if (value.isEmpty() || value == "0" || value.toFloatOrNull() == null) {
        return 0f
    }

    val inputValue = value.toFloat()

    return when (fromUnit) {
        "Gram" -> when (toUnit) {
            "Gram" -> inputValue
            "Kilogram" -> inputValue / 1000
            "Ons" -> inputValue / 28.3495f
            "Pound" -> inputValue * 0.00220462f
            else -> 0f
        }
        "Kilogram" -> when (toUnit) {
            "Gram" -> inputValue * 1000
            "Kilogram" -> inputValue
            "Ons" -> inputValue * 35.274f
            "Pound" -> inputValue * 2.20462f
            else -> 0f
        }
        "Ons" -> when (toUnit) {
            "Gram" -> inputValue * 28.3495f
            "Kilogram" -> inputValue / 35.274f
            "Ons" -> inputValue
            "Pound" -> inputValue * 0.220462f
            else -> 0f
        }
        "Pound" -> when (toUnit) {
            "Gram" -> inputValue * 453.592f
            "Kilogram" -> inputValue * 0.453592f
            "Ons" -> inputValue * 16f
            "Pound" -> inputValue
            else -> 0f
        }
        else -> 0f
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }

    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    AssessmentMobpro1Theme {
        MainScreen(rememberNavController())
    }
}

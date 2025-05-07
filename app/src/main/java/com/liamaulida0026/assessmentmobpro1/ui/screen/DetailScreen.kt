package com.liamaulida0026.assessmentmobpro1.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.liamaulida0026.assessmentmobpro1.R
import com.liamaulida0026.assessmentmobpro1.ui.theme.AssessmentMobpro1Theme
import com.liamaulida0026.assessmentmobpro1.util.ViewModelFactory

const val KEY_ID_CATATAN = "idCatatan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var berat by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("") }

    val satuanList = listOf("Kilogram", "Gram", "Ons")
    val radioOptions = listOf(
        stringResource(id = R.string.sayur),
        stringResource(id = R.string.buah),
        stringResource(id = R.string.daging),
        stringResource(id = R.string.lainnya)
    )
    var expanded by remember { mutableStateOf(false) }

    var kategori by remember { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(Unit) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getCatatan(id) ?: return@LaunchedEffect
        judul = data.judul
        berat = data.berat.toString()
        satuan = data.satuan
        kategori = data.kategori
    }

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_catatan))
                    else
                        Text(text = stringResource(id = R.string.edit_catatan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        val beratValue = berat.toDoubleOrNull()
                        if (judul == "" || beratValue == null || satuan == "") {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(judul, beratValue, satuan, kategori)
                        }
                        navController.popBackStack()
                    }){
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        FormCatatan(
            title = judul,
            onTitleChange = { judul = it },
            berat = berat,
            onBeratChange = { berat = it },
            satuan = satuan,
            onSatuanChange = { satuan = it },
            kategori = kategori,
            onKategoriChange = { kategori = it },
            satuanList = satuanList,
            kategoriList = radioOptions,
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun CategoryOption(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCatatan(
    title: String, onTitleChange: (String) -> Unit,
    berat: String, onBeratChange: (String) -> Unit,
    satuan: String, onSatuanChange: (String) -> Unit,
    kategori: String, onKategoriChange: (String) -> Unit,
    satuanList: List<String>,
    kategoriList: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Membuat TextField untuk Judul
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.judul)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Membuat Berat dan Satuan dalam satu baris
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Membuat TextField untuk Berat
            OutlinedTextField(
                value = berat,
                onValueChange = onBeratChange,
                label = { Text(stringResource(id = R.string.berat)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
            // Membuat Dropdown untuk Satuan
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = satuan,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.satuan)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) }
                ) {
                    satuanList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onSatuanChange(item)
                                onExpandedChange(false)
                            }
                        )
                    }
                }
            }
        }

        // Radio Button untuk Kategori
        Text(text = stringResource(id = R.string.kategori), style = MaterialTheme.typography.labelLarge)
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            kategoriList.forEach { text ->
                CategoryOption (
                    label = text,
                    isSelected = kategori == text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onClick = { onKategoriChange(text) }
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    AssessmentMobpro1Theme {
        DetailScreen(rememberNavController())
    }
}
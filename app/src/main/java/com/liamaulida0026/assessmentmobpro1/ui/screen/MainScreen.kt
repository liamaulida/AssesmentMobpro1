package com.liamaulida0026.assessmentmobpro1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.liamaulida0026.assessmentmobpro1.R
import com.liamaulida0026.assessmentmobpro1.model.Catatan
import com.liamaulida0026.assessmentmobpro1.navigation.Screen
import com.liamaulida0026.assessmentmobpro1.ui.theme.AssessmentMobpro1Theme
import com.liamaulida0026.assessmentmobpro1.util.SettingsDataStore
import com.liamaulida0026.assessmentmobpro1.util.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = remember { SettingsDataStore(context) }
    val layoutFlow by dataStore.layoutFlow.collectAsState(initial = true)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
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
                    IconButton(onClick = {
                        scope.launch { dataStore.saveLayout(!layoutFlow) }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (layoutFlow) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (layoutFlow) R.string.grid else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(Screen.Sampah.route)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_restore_from_trash_24),
                            contentDescription = stringResource(R.string.tempat_sampah),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_catatan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        ScreenContent(
            showList = layoutFlow,
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun ScreenContent(
    showList: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = ViewModelFactory(context))
    val detailViewModel: DetailViewModel = viewModel(factory = ViewModelFactory(context))
    val data by viewModel.data.collectAsState()
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val selectedCatatan = remember { mutableStateOf<Catatan?>(null) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(stringResource(R.string.pesan_hapus)) },
            text = { Text(stringResource(R.string.hapus_list)) },
            confirmButton = {
                TextButton(onClick = {
                    selectedCatatan.value?.let { catatan ->
                        detailViewModel.recentlyDeletedList = catatan
                        detailViewModel.delete(catatan.id)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = context.getString(R.string.catatan_dihapus),
                                actionLabel = context.getString(R.string.undo),
                                duration = SnackbarDuration.Short,
                                )
                            if (result == SnackbarResult.ActionPerformed) {
                                detailViewModel.restoreDeletedCatatan()
                            }
                        }
                    }

                    openDialog.value = false
                }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }

    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) { catatan ->
                    ListItem(
                        catatan = catatan,
                        onClick = { navController.navigate(Screen.FormUbah.withId(catatan.id)) },
                        onDelete = {
                            selectedCatatan.value = it
                            openDialog.value = true
                        }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) { catatan ->
                    GridItem(
                        catatan = catatan,
                        onClick = { navController.navigate(Screen.FormUbah.withId(catatan.id)) },
                        onDelete = {
                            selectedCatatan.value = it
                            openDialog.value = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ListItem(catatan: Catatan, onClick: () -> Unit, onDelete: (Catatan) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = catatan.judul,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = catatan.berat.toString() + " " + catatan.satuan,
            modifier = Modifier.padding(horizontal = 4.dp),
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = catatan.kategori,
            modifier = Modifier.padding(horizontal = 4.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { onDelete(catatan) },
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 4.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
        thickness = 1.dp
    )
}

@Composable
fun GridItem(catatan: Catatan, onClick: () -> Unit,  onDelete: (Catatan) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(1.dp, DividerDefaults.color)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = catatan.judul,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 4.dp),
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = catatan.berat.toString() + " " + catatan.satuan,
                modifier = Modifier.padding(horizontal = 4.dp),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = catatan.kategori,
                modifier = Modifier.padding(horizontal = 4.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onDelete(catatan) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus")
                }
            }
        }
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
package com.liamaulida0026.assessmentmobpro1.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.liamaulida0026.assessmentmobpro1.R

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_hapus)) },
        text = { Text(stringResource(R.string.pesan_dialog)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.hapus))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.batal))
            }
        }
    )
}
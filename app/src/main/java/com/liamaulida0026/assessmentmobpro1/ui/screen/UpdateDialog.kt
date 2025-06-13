package com.liamaulida0026.assessmentmobpro1.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.liamaulida0026.assessmentmobpro1.R
import com.liamaulida0026.assessmentmobpro1.model.Buku
import com.liamaulida0026.assessmentmobpro1.network.BukuApi
import com.liamaulida0026.assessmentmobpro1.ui.theme.AssessmentMobpro1Theme

@Composable
fun UpdateDialog(
    buku: Buku,
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onUpdateConfirmed: (id: String, judul: String, penulis: String, review: String, bitmap: Bitmap?) -> Unit,
    onEditImage: () -> Unit
) {

    var judul by remember { mutableStateOf(buku.judul_buku) }
    var penulis by remember { mutableStateOf(buku.penulis_buku) }
    var review by remember { mutableStateOf(buku.review_buku) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.edit),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp, 180.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = stringResource(R.string.gambar_baru),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(BukuApi.getBukuUrl(buku.imageId))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.buku),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.broken_img),
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        IconButton(
                            onClick = onEditImage,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit),
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    TextButton(
                        onClick = onEditImage,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.edit),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text(stringResource(R.string.judul_buku)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = penulis,
                    onValueChange = { penulis = it },
                    label = { Text(stringResource(R.string.penulis_buku)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = review,
                    onValueChange = { review = it },
                    label = { Text(stringResource(R.string.review_buku)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (judul.isNotBlank() && penulis.isNotBlank() && review.isNotBlank()) {
                        onUpdateConfirmed(buku.id, judul, penulis, review, bitmap)
                    }
                },
                enabled = judul.isNotBlank() && penulis.isNotBlank() && review.isNotBlank()
            ) {
                Text(stringResource( R.string.simpan))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.batal))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun UpdateDialogPreview() {
    AssessmentMobpro1Theme {
        UpdateDialog(
            buku = Buku(
                id = "1",
                judul_buku = "Buku Contoh",
                penulis_buku = "Penulis Contoh",
                review_buku = "Review Buku Contoh",
                imageId = "imageId_example"
            ),
            bitmap = null,
            onDismissRequest = {},
            onUpdateConfirmed = { _, _, _, _, _ -> },
            onEditImage = {}
        )
    }
}

package io.github.akiomik.seiun.ui.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.akiomik.seiun.ui.feed.NewPostFormModal

@Composable
fun NewPostFab() {
    var showPostForm by remember { mutableStateOf(false) }
    if (showPostForm) {
        NewPostFormModal { showPostForm = false }
    } else {
            FloatingActionButton(
                //modifier = Modifier
                    //.padding(0.dp),
                    //.align(alignment = Alignment.BottomEnd),
                onClick = {
                    showPostForm = true
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create new post")
            }
    }
}

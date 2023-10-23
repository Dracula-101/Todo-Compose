package io.github.dracula101.todo.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import io.github.dracula101.todo.R
import io.github.dracula101.todo.data.models.Category
import io.github.dracula101.todo.data.models.listOfCategory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    categories: List<Category> = listOfCategory,
    onCategorySaved: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedCategory by remember {
        mutableStateOf(0)
    }
    AlertDialog(
        modifier = Modifier
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            ),
        properties = DialogProperties(
            securePolicy = SecureFlagPolicy.SecureOn,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = {}

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Select Category",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                columns = GridCells.Fixed(3)
            ) {
                items(categories) { index ->
                    CategoryCard(
                        category = index,
                        onCategorySelected = {
                            selectedCategory = it
                        },
                        selected = selectedCategory == index.id
                    )
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ){
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Cancel")
                }
                TextButton(
                    onClick = {
                        onCategorySaved(selectedCategory)
                        onDismiss()
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: Category,
    onCategorySelected: (Int) -> Unit,
    selected: Boolean
){
    Column(
        modifier = Modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = 60.dp,
                    height = 65.dp
                )
                .border(
                    width = 1.dp,
                    color = if(selected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    shape = MaterialTheme.shapes.small
                )
                .padding(4.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    onCategorySelected(category.id)
                }
                .background(
                    color = category.color,
                    shape = MaterialTheme.shapes.small
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = category.icon),
                contentDescription = "Category Icon",
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
fun CategoryCardPreview(){
    CategoryCard(
        category = listOfCategory.first(),
        onCategorySelected = {},
        selected = true
    )
}
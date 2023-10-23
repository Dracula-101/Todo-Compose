package io.github.dracula101.todo.data.models

import androidx.compose.ui.graphics.Color
import io.github.dracula101.todo.R

data class Category(
    val id : Int ,
    val name: String,
    val color: Color,
    val icon: Int,
)

val listOfCategory = listOf<Category>(
    Category(0,"Grocery", Color(0xFFCCFF80), R.drawable.grocery),
    Category(1,"Work", Color(0xFFFF9680) , R.drawable.work),
    Category(2,"Sport", Color(0xFF80FFFF), R.drawable.sport),
    Category(3,"Design", Color(0xFF80FFD9), R.drawable.design),
    Category(4,"University", Color(0xFF809CFF), R.drawable.university),
    Category(5, "Social", Color(0xFF809CFF), R.drawable.social),
    Category(6,"Music", Color(0xFFFC80FF), R.drawable.music),
    Category(7,"Health", Color(0xFF80FFA3), R.drawable.health),
    Category(8,"Movie", Color(0xFF80D1FF), R.drawable.movie),
    Category(9,"Home", Color(0xFFFFCC80), R.drawable.home),
)

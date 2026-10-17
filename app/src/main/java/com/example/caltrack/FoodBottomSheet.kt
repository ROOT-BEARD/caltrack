package com.example.caltrack

import android.R
import androidx.annotation.experimental.Experimental
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


private var grams by mutableStateOf(0.0f);

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodBottomSheet(
    onDismissRequest: () -> Unit,
    currentFood: FoodItem,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
){
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle()
        }
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ){
            FoodCard(currentFood)
        }
    }
}

@Composable
fun FoodCard(currentFood: FoodItem){

    Column (modifier = Modifier.fillMaxWidth(),) {
        Row(modifier = Modifier.padding( vertical = 30.dp, horizontal = 30.dp)) {
            Column() {
                Text(currentFood.name.toString(), fontWeight = FontWeight.Bold);
                Text(currentFood.brand.toString());
            }
        }
        var text by remember { mutableStateOf("") }
        Row(modifier = Modifier.padding( horizontal = 30.dp)) {
            BasicTextField(
                value = text.toString(),
                onValueChange = { newVal ->
                                if(newVal.all { it.isDigit() } && newVal.length < 9){
                                    text = newVal;
                                    if(newVal.length > 0){
                                        grams = newVal.toFloat();
                                    }
                                }},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(72.dp)
                    .height(38.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(8.dp)
                    ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                }
            )
            Text("grams", modifier = Modifier.absolutePadding(left=10.dp));
        }
        Row(modifier = Modifier.padding( vertical = 30.dp, horizontal = 30.dp)) {
            Column() {
                Text("CALORIES: " + (currentFood.cals?.toFloat()?.times(grams)));
                Text("CARBS: " + (currentFood.carbs?.toFloat()?.times(grams)));
                Text("PROTEIN: " + (currentFood.pro?.toFloat()?.times(grams)));
                Text("FAT: " + (currentFood.fats?.toFloat()?.times(grams)));
            }
        }
        Row(modifier = Modifier.padding(bottom = 5000.dp)) { }
    }
}

package com.example.citylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import com.example.citylist.ui.theme.CityListTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            CityListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)

                    )
                }
            }
        }
    }
}

@Composable
fun CityListScreen(
    //Shows the scrolling list

    // The list of city names this screen receives
    cities: List<String>,

    onDeleteCity: (String) -> Unit,
    // The add city function
    onAddCity: (String) -> Unit,

    // Allows layout information to be passed into this screen
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }

    // the selected city the user has tapped, for deletion feature
    var selectedCity by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Button( // ADD BUTTON
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }

            ) {
                Text("Add City")
            }
            Button(
                onClick = {
                    selectedCity?.let { city ->
                        onDeleteCity(city)
                        selectedCity = null
                    }
                },
                enabled = selectedCity != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("Delete City")
            }
        }


        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    isSelected = city == selectedCity, // if the name matches the name of the selected city, then its selected
                    onClick = {
                        selectedCity =
                            if (selectedCity == city) null else city // de-select on click if selected, otherwise select
                    }
                )
            }
        }

    }
}

@Composable
fun CityRow(
    city: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = modifier
            .fillMaxWidth()
            .background(if (isSelected) Color.Gray else Color.LightGray)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}


class CityRepository {

    // the registry's data should be stored in a private backing property
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Hamilton",
        "New York", "Osaka", "Tokyo",
        "London", "Calgary", "Paris", "Oslo"
    )

    // Read only
    val cities: List<String>
        get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}


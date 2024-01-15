package br.pedroso.citieslist.ui.components

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.pedroso.citieslist.R
import br.pedroso.citieslist.domain.entities.City
import br.pedroso.citieslist.domain.entities.Coordinates
import br.pedroso.citieslist.ui.theme.CitiesListTheme
import br.pedroso.citieslist.utils.getCountryFlagEmoji

@Composable
fun CityItem(city: City, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Row(modifier = modifier.padding(16.dp), horizontalArrangement = spacedBy(8.dp)) {
        Text(
            text = getCountryFlagEmoji(city.countryCode),
            style = MaterialTheme.typography.titleMedium
        )

        Column(verticalArrangement = spacedBy(4.dp)) {
            Text(text = city.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(id = R.string.country_code_label, city.countryCode),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(
                    id = R.string.coordinates_label,
                    city.coordinates.latitude,
                    city.coordinates.longitude
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityItemPreview() {
    CitiesListTheme {
        CityItem(
            city = City(
                name = "Test",
                countryCode = "BR",
                coordinates = Coordinates(0.0, 0.0),
                id = 1
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
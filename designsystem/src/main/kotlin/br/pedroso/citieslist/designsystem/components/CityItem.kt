package br.pedroso.citieslist.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.pedroso.citieslist.designsystem.R
import br.pedroso.citieslist.designsystem.theme.CitiesListTheme
import br.pedroso.citieslist.designsystem.utils.getCountryFlagEmoji
import br.pedroso.citieslist.domain.City
import br.pedroso.citieslist.domain.Coordinates

@Composable
fun CityItem(
    city: City,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    showStarredIndicator: Boolean = true,
) {
    Surface(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = spacedBy(16.dp),
        ) {
            Text(
                text = getCountryFlagEmoji(city.countryCode),
                style = MaterialTheme.typography.titleLarge,
            )

            Column(modifier = Modifier.weight(1f), verticalArrangement = spacedBy(4.dp)) {
                Text(text = city.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = stringResource(id = R.string.country_code_label, city.countryCode),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (showStarredIndicator && city.isStarred) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_star_filled),
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CityItemPreview() {
    CitiesListTheme {
        CityItem(
            city =
                City(
                    name = "Test",
                    countryCode = "BR",
                    coordinates = Coordinates(0.0, 0.0),
                    id = 1,
                    isStarred = true,
                ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

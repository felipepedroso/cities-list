package br.pedroso.citieslist.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.pedroso.citieslist.designsystem.theme.CitiesListTheme

@Composable
fun ErrorState(
    message: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
) {
    BaseScreen(modifier) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Absolute.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )

            Button(onClick = onButtonClick) {
                Text(text = buttonText)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ErrorStatePreview() {
    CitiesListTheme {
        ErrorState(
            message = "Something went wrong",
            buttonText = "Retry",
            onButtonClick = {},
        )
    }
}

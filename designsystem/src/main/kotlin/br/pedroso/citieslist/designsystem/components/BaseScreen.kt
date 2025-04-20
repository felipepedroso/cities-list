package br.pedroso.citieslist.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.pedroso.citieslist.designsystem.theme.CitiesListTheme

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(modifier = modifier.fillMaxSize(), content = content)
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BaseScreenComponentPreview() {
    CitiesListTheme {
        BaseScreen {
            Box(contentAlignment = Alignment.Center) {
                Text("This is a screen!")
            }
        }
    }
}

package br.pedroso.citieslist.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import java.util.Date

@Composable
fun QueryHistoryItem(
    query: String,
    date: Date,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onRemoveClicked: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(8.dp),
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_history), contentDescription = null)

        Column(modifier = Modifier.weight(1f)) {
            Text(text = query)

            Text(
                text =
                    DateUtils.getRelativeTimeSpanString(
                        date.time,
                        Date().time,
                        DateUtils.DAY_IN_MILLIS,
                    ).toString(),
                style = MaterialTheme.typography.bodySmall,
            )
        }

        IconButton(onClick = onRemoveClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = stringResource(R.string.remove_from_history),
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QueryHistoryItemPreview() {
    CitiesListTheme {
        QueryHistoryItem(query = "Bristol", date = Date())
    }
}

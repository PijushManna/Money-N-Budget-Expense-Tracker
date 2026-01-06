package com.expense.tracker.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.expense.tracker.navigation.NavGraph
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme

@Composable
fun MainContainer() {
//    var nav by remember { mutableStateOf("home") }
//    val headerConfig by rememberHeaderConfig(HeaderConfig(title = stringResource(R.string.app_name), navigationIcon = null, onNavigationClick = {}))

//    Scaffold(
//        modifier = modifier,
//        topBar = {
//            Header(config = headerConfig)
//        },
//        bottomBar = {
//            Footer(currentRoute = nav){
//                nav = it
//            }
//        }
//    ) {
//        Surface(Modifier.padding(it)) {
//            when(nav){
//                "home" -> HomeScreenPreview()
//                "charts" -> ChartsScreenPreview()
//                "reports" -> HomeScreenPreview()
//                "settings" -> ProfileScreenPreview()
//                "add" -> AddNewTransactionScreenPreview()
//            }
//        }
//    }
    NavGraph()
}

@Preview
@Composable
private fun MainContainerPreview() {
    MoneyBudgetExpenseTrackerTheme {
        MainContainer()
    }
}
package com.akdhs323.dailyhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DailyHelperApp() }
    }
}

data class Todo(val text: String, val done: Boolean = false)

@Composable
fun DailyHelperApp() {
    var todos by remember { mutableStateOf(listOf(Todo("오늘 할 일 정리하기"), Todo("물 한 잔 마시기"))) }
    var input by remember { mutableStateOf("") }
    var spending by remember { mutableStateOf(0) }
    var showAddExpense by remember { mutableStateOf(false) }
    var expenseInput by remember { mutableStateOf("") }

    MaterialTheme(colorScheme = lightColorScheme()) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("하루도우미", fontWeight = FontWeight.Bold) }) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text("오늘도 깔끔하게 시작해요 👋", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(4.dp))
                    Text("할 일과 지출을 한곳에서 관리하세요.")
                }
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            Text("오늘의 요약", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(10.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("완료 ${todos.count { it.done }} / ${todos.size}")
                                Text("지출 ${"%,d".format(spending)}원")
                            }
                        }
                    }
                }
                item {
                    Text("✅ 할 일", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(input, { input = it }, Modifier.weight(1f), placeholder = { Text("할 일을 입력하세요") }, singleLine = true)
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { if (input.isNotBlank()) { todos = todos + Todo(input.trim()); input = "" } }) { Text("추가") }
                    }
                }
                items(todos) { todo ->
                    val index = todos.indexOf(todo)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = todo.done, onCheckedChange = { checked ->
                            todos = todos.toMutableList().also { it[index] = todo.copy(done = checked) }
                        })
                        Text(todo.text, Modifier.weight(1f))
                        TextButton(onClick = { todos = todos.filterIndexed { i, _ -> i != index } }) { Text("삭제") }
                    }
                }
                item {
                    Text("💰 오늘의 지출", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Button(onClick = { showAddExpense = true }) { Text("지출 추가") }
                }
            }
        }
        if (showAddExpense) {
            AlertDialog(
                onDismissRequest = { showAddExpense = false },
                title = { Text("지출 추가") },
                text = { OutlinedTextField(expenseInput, { expenseInput = it.filter(Char::isDigit) }, placeholder = { Text("금액 (원)") }, singleLine = true) },
                confirmButton = { TextButton(onClick = { spending += expenseInput.toIntOrNull() ?: 0; expenseInput = ""; showAddExpense = false }) { Text("추가") } },
                dismissButton = { TextButton(onClick = { showAddExpense = false }) { Text("취소") } }
            )
        }
    }
}

package com.akdhs323.dailyhelper

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DailyHelperApp(getSharedPreferences("dailyhelper", Context.MODE_PRIVATE)) }
    }
}

data class Todo(val text: String, val done: Boolean = false)

@Composable
fun DailyHelperApp(prefs: android.content.SharedPreferences) {
    val defaults = listOf(Todo("오늘 할 일 정리하기"), Todo("물 한 잔 마시기"))
    var todos by remember { mutableStateOf(loadTodos(prefs).ifEmpty { defaults }) }
    var spending by remember { mutableIntStateOf(prefs.getInt("spending", 0)) }
    var input by remember { mutableStateOf("") }
    var expenseInput by remember { mutableStateOf("") }
    var showExpense by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableIntStateOf(prefs.getInt("timer", 25 * 60)) }
    var timerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(timerRunning) {
        while (timerRunning && timerSeconds > 0) {
            kotlinx.coroutines.delay(1000)
            timerSeconds--
            prefs.edit().putInt("timer", timerSeconds).apply()
        }
        if (timerSeconds == 0) timerRunning = false
    }

    fun save() {
        saveTodos(prefs, todos)
        prefs.edit().putInt("spending", spending).putInt("timer", timerSeconds).apply()
    }

    MaterialTheme(colorScheme = lightColorScheme()) {
        Scaffold(topBar = { TopAppBar(title = { Text("하루도우미", fontWeight = FontWeight.Bold) }) }) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text("오늘도 깔끔하게 시작해요 👋", style = MaterialTheme.typography.headlineSmall)
                    Text("Android 전체 기능판", color = MaterialTheme.colorScheme.primary)
                }
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            Text("오늘의 요약", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(10.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("완료 ${todos.count { it.done }} / ${todos.size}")
                                Text("지출 ${String.format(Locale.KOREA, "%,d", spending)}원")
                            }
                        }
                    }
                }
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⏱️ 집중 타이머", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(formatTime(timerSeconds), style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { timerRunning = !timerRunning }) { Text(if (timerRunning) "일시정지" else "시작") }
                                OutlinedButton(onClick = { timerRunning = false; timerSeconds = 25 * 60; save() }) { Text("25분 리셋") }
                            }
                        }
                    }
                }
                item {
                    Text("✅ 할 일", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(input, { input = it }, Modifier.weight(1f), placeholder = { Text("할 일을 입력하세요") }, singleLine = true)
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { if (input.isNotBlank()) { todos = todos + Todo(input.trim()); input = ""; save() } }) { Text("추가") }
                    }
                }
                itemsIndexed(todos) { index, todo ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = todo.done, onCheckedChange = { checked ->
                            todos = todos.toMutableList().also { it[index] = todo.copy(done = checked) }
                            save()
                        })
                        Text(todo.text, Modifier.weight(1f))
                        TextButton(onClick = { todos = todos.filterIndexed { i, _ -> i != index }; save() }) { Text("삭제") }
                    }
                }
                item {
                    Text("💰 오늘의 지출", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Button(onClick = { showExpense = true }) { Text("지출 추가") }
                }
            }
        }
        if (showExpense) {
            AlertDialog(
                onDismissRequest = { showExpense = false },
                title = { Text("지출 추가") },
                text = { OutlinedTextField(expenseInput, { expenseInput = it.filter(Char::isDigit) }, placeholder = { Text("금액 (원)") }, singleLine = true) },
                confirmButton = { TextButton(onClick = { spending += expenseInput.toIntOrNull() ?: 0; expenseInput = ""; showExpense = false; save() }) { Text("추가") } },
                dismissButton = { TextButton(onClick = { showExpense = false }) { Text("취소") } }
            )
        }
    }
}

private fun formatTime(total: Int): String = "%02d:%02d".format(total / 60, total % 60)

private fun loadTodos(prefs: android.content.SharedPreferences): List<Todo> {
    val raw = prefs.getString("todos", "") ?: ""
    if (raw.isBlank()) return emptyList()
    return raw.split("\\n").filter { it.isNotBlank() }.map {
        val parts = it.split("|", limit = 2)
        Todo(parts.getOrElse(1) { "" }, parts.getOrElse(0) { "0" } == "1")
    }
}

private fun saveTodos(prefs: android.content.SharedPreferences, todos: List<Todo>) {
    val raw = todos.joinToString("\\n") { "${if (it.done) 1 else 0}|${it.text.replace("|", "︱").replace("\n", " ")}" }
    prefs.edit().putString("todos", raw).apply()
}

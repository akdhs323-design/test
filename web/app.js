const KEY='dailyhelper-web-v1';
let state=JSON.parse(localStorage.getItem(KEY)||'{"todos":[],"total":0}');
const save=()=>{localStorage.setItem(KEY,JSON.stringify(state));render()};
function render(){
 document.getElementById('todoCount').textContent=`${state.todos.length} / 3`;
 document.getElementById('total').textContent=state.total.toLocaleString('ko-KR')+'원';
 document.getElementById('todoList').innerHTML=state.todos.map((t,i)=>`<li><input type="checkbox" ${t.done?'checked':''} onchange="toggleTodo(${i})"><span class="${t.done?'done':''}">${esc(t.text)}</span><button onclick="removeTodo(${i})">삭제</button></li>`).join('');
}
function esc(s){return s.replace(/[&<>\"]/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));}
function addTodo(){const input=document.getElementById('todoInput');const text=input.value.trim();if(!text)return;if(state.todos.length>=3){alert('웹 체험판은 할 일을 최대 3개까지 등록할 수 있어요. Android 앱에서 제한 없이 사용하세요.');return;}state.todos.push({text,done:false});input.value='';save()}
function toggleTodo(i){state.todos[i].done=!state.todos[i].done;save()}
function removeTodo(i){state.todos.splice(i,1);save()}
function addExpense(){const input=document.getElementById('expenseInput');const amount=Number(input.value.replace(/[^0-9]/g,''));if(!amount||amount<1){alert('금액을 입력해주세요.');return;}state.total+=amount;input.value='';save()}
function showAppNotice(){alert('집중 타이머와 알림은 Android 앱에서 제공됩니다.');}
document.getElementById('todoInput').addEventListener('keydown',e=>{if(e.key==='Enter')addTodo()});
document.getElementById('expenseInput').addEventListener('keydown',e=>{if(e.key==='Enter')addExpense()});
render();

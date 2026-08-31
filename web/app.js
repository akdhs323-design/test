const KEY='dailyhelper-web-v1';

function loadState(){
  try{
    const parsed=JSON.parse(localStorage.getItem(KEY)||'null');
    if(!parsed || !Array.isArray(parsed.todos) || typeof parsed.total!=='number') return {todos:[],total:0};
    return {todos:parsed.todos.slice(0,3).filter(t=>t&&typeof t.text==='string').map(t=>({text:t.text.slice(0,40),done:Boolean(t.done)})),total:Math.max(0,Math.floor(parsed.total))};
  }catch(_){return {todos:[],total:0};}
}

let state=loadState();
const save=()=>{localStorage.setItem(KEY,JSON.stringify(state));render()};
const esc=s=>String(s).replace(/[&<>\"]/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));

function render(){
  document.getElementById('todoCount').textContent=`${state.todos.length} / 3`;
  document.getElementById('total').textContent=state.total.toLocaleString('ko-KR')+'원';
  document.getElementById('todoList').innerHTML=state.todos.map((t,i)=>`<li><input type="checkbox" ${t.done?'checked':''} onchange="toggleTodo(${i})"><span class="${t.done?'done':''}">${esc(t.text)}</span><button onclick="removeTodo(${i})">삭제</button></li>`).join('');
}

function addTodo(){
  const input=document.getElementById('todoInput');
  const text=input.value.trim();
  if(!text)return;
  if(state.todos.length>=3){alert('웹 체험판은 할 일을 최대 3개까지 등록할 수 있어요. Android 앱에서 제한 없이 사용하세요.');return;}
  state.todos.push({text:text.slice(0,40),done:false});
  input.value='';save();
}
function toggleTodo(i){if(!state.todos[i])return;state.todos[i].done=!state.todos[i].done;save()}
function removeTodo(i){if(i<0||i>=state.todos.length)return;state.todos.splice(i,1);save()}
function addExpense(){
  const input=document.getElementById('expenseInput');
  const amount=Number(input.value.replace(/[^0-9]/g,''));
  if(!Number.isSafeInteger(amount)||amount<1){alert('1원 이상의 금액을 입력해주세요.');return;}
  if(state.total>Number.MAX_SAFE_INTEGER-amount){alert('총액이 너무 큽니다.');return;}
  state.total+=amount;input.value='';save();
}
function showAppNotice(){alert('집중 타이머, 상세 지출 내역, 메모 등은 Android 앱에서 제공됩니다.');}
document.getElementById('todoInput').addEventListener('keydown',e=>{if(e.key==='Enter')addTodo()});
document.getElementById('expenseInput').addEventListener('keydown',e=>{if(e.key==='Enter')addExpense()});
render();

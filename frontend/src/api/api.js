// src/api.js
const API_BASE = process.env.REACT_APP_API_BASE_URL;

// 에러 처리 헬퍼
async function handleError(res) {
  // JSON 응답이면 message 추출, 아니면 텍스트
  const contentType = res.headers.get('Content-Type') || '';
  let msg;
  if (contentType.includes('application/json')) {
    const json = await res.json();
    msg = json.message || JSON.stringify(json);
  } else {
    msg = await res.text();
  }
  throw new Error(msg || res.statusText);
}

// 고정 확장자 전체 조회
export async function fetchFixedExtensions() {
  const res = await fetch(`${API_BASE}/file-extensions/fixed`);
  if (!res.ok) await handleError(res);
  return res.json(); // { data: [ { id, name, checked } ] }
}

// 고정 확장자 체크 상태 업데이트
export async function toggleFixedExtension(id, checked) {
  const res = await fetch(`${API_BASE}/file-extensions/${id}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ isChecked: checked }),
  });
  if (!res.ok) await handleError(res);
  return res;
}

// 커스텀 확장자 조회 (페이징)
export async function fetchCustomExtensions(page = 1, size = 20) {
  const res = await fetch(
    `${API_BASE}/file-extensions/custom?page=${page}&size=${size}`
  );
  if (!res.ok) await handleError(res);
  return res.json(); // { currentCount, maxCount, data: [ { id, name } ] }
}

// 커스텀 확장자 추가
export async function addCustomExtension(name) {
  const res = await fetch(`${API_BASE}/file-extensions`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name }),
  });
  if (!res.ok) await handleError(res);
  return res.json(); // { id, name }
}

// 커스텀 확장자 삭제
export async function removeCustomExtension(id) {
  const res = await fetch(`${API_BASE}/file-extensions/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) await handleError(res);
  return res;
}

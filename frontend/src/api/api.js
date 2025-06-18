const API_BASE = process.env.REACT_APP_API_BASE_URL;

// 고정 확장자 전체 조회
export async function fetchFixedExtensions() {
  const res = await fetch(`${API_BASE}/file-extensions/fixed`);
  return res.json();
}

// 고정 확장자 체크 상태 업데이트
export async function toggleFixedExtension(id, checked) {
  return fetch(`${API_BASE}/file-extensions/${id}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ checked }),
  });
}

// 커스텀 확장자 조회 (페이징 지원)
export async function fetchCustomExtensions(page = 1, size = 20) {
  const res = await fetch(`${API_BASE}/file-extensions/custom?page=${page}&size=${size}`);
  return res.json();
}

// 커스텀 확장자 추가
export async function addCustomExtension(name) {
  const res = await fetch(`${API_BASE}/file-extensions`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name }),
  });
  return res.json();
}

// 커스텀 확장자 삭제
export async function removeCustomExtension(id) {
  return fetch(`${API_BASE}/file-extensions/${id}`, { method: 'DELETE' });
}

// src/CustomExtensions.js
import React, { useState, useEffect } from 'react';
import {
  fetchCustomExtensions,
  addCustomExtension,
  removeCustomExtension
} from '../api/api';

const size = 20;

export default function CustomExtensions() {

  const [items, setItems] = useState([]);           // 화면에 뿌릴 데이터
  const [totalCount, setTotalCount] = useState(0);
  const [maxCount, setMaxCount] = useState(0);
  const [name, setName] = useState('');             // 추가할 이름
  const [page, setPage] = useState(1);              // 현재 페이지

  // 1) 서버에서 해당 페이지 데이터 불러오기
  const loadPage = async (p) => {
    try {
      const { data, totalCount, maxCount } =
        await fetchCustomExtensions(p, size);

      // 서버가 준 그대로 상태에 세팅
      setItems(data);
      setTotalCount(totalCount);
      setMaxCount(maxCount);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  // 2) 컴포넌트 마운트 혹은 page 변경 시마다 서버 호출
  useEffect(() => {
    loadPage(page);
  }, [page]);

  const totalPages = Math.ceil(maxCount / size);

  // 3) 추가 → 서버 POST → 1페이지 다시 로드
  const handleAdd = async () => {
    const trimmed = name.trim();
    if (!trimmed) return;
    // 입력창 비우기
    setName('');
    try {
      const newExt = await addCustomExtension(trimmed);
      setItems(prev => [newExt, ...prev]);
      setTotalCount(c => c + 1);

    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  // 4) 삭제 → 서버 DELETE → 현재 페이지 다시 로드
  const handleRemove = async (id) => {
    try {
      await removeCustomExtension(id);
      await loadPage(page);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <section>
      <h2>커스텀 확장자</h2>
      <div>
        <input
          placeholder="확장자 입력"
          value={name}
          onChange={e => setName(e.target.value)}
        />
        <button type="button" onClick={handleAdd}>추가</button>
      </div>

      <div>
        {totalCount} / {maxCount}
      </div>

      <ul>
        {items.map(({ id, name }) => (
          <li key={id}>
            {name}
            <button onClick={() => handleRemove(id)}>X</button>
          </li>
        ))}
      </ul>

      <div style={{ marginTop: 10 }}>
        <button
          onClick={() => setPage(p => Math.max(1, p - 1))}
          disabled={page === 1}
        >
          이전
        </button>
        <span style={{ margin: '0 8px' }}>
          {page} / {totalPages}
        </span>
        <button
          onClick={() => setPage(p => Math.min(totalPages, p + 1))}
          disabled={page === totalPages}
        >
          다음
        </button>
      </div>
    </section>
  );
}

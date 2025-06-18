// src/CustomExtensions.js
import React, { useState, useEffect } from 'react';
import {
  fetchCustomExtensions,
  addCustomExtension,
  removeCustomExtension
} from '../api/api';

export default function CustomExtensions() {
  const [items, setItems] = useState([]);
  const [currentCount, setCurrentCount] = useState(0);
  const [maxCount, setMaxCount] = useState(0);
  const [name, setName] = useState('');
  const [page, setPage] = useState(1);
  const size = 20;

  const load = async (pageToLoad = page) => {
    try {
      const { data, currentCount, maxCount } = await fetchCustomExtensions(pageToLoad, size);
      setItems(data);
      setCurrentCount(currentCount);
      setMaxCount(maxCount);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    load(page);
  }, [page]);

  const handleAdd = async () => {
    const trimmed = name.trim();
    if (!trimmed) return;
    setName('');
    try {
      const newExt = await addCustomExtension(trimmed);
      setItems(prev => [newExt, ...prev]);
      setCurrentCount(c => c + 1);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  const handleRemove = async (id) => {
    try {
      await removeCustomExtension(id);
      load(page);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  const totalPages = Math.ceil(maxCount / size);

  return (
    <section>
      <h2>커스텀 확장자</h2>

      <div>
        <input
          placeholder="확장자 입력"
          value={name}
          onChange={e => setName(e.target.value)}
        />
        <button onClick={handleAdd}>추가</button>
      </div>

      <div>
        {currentCount} / {maxCount}
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
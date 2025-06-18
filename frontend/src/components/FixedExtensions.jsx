// src/FixedExtensions.js
import React, { useState, useEffect, useRef } from 'react';
import { fetchFixedExtensions, toggleFixedExtension } from '../api/api';

import debounce from 'lodash/debounce';

const DEBOUNCE_DELAY_MS = 500;

export default function FixedExtensions() {
  const [items, setItems] = useState([]);
  // id별로 debounced 함수를 저장할 맵
  const debouncedMap = useRef(new Map());

  // 초기 데이터 로드
  useEffect(() => {
    (async () => {
      try {
        const { data } = await fetchFixedExtensions();
        setItems(data);
      } catch (err) {
        console.error(err);
        alert(err.message);
      }
    })();
  }, []);

  const handleToggle = (id) => {
    const idx = items.findIndex(x => x.id === id);
    if (idx < 0) return;

    const old = items[idx];
    const newChecked = !old.checked;

    // UI 즉시 업데이트
    setItems(prev =>
      prev.map(item =>
        item.id === id ? { ...item, checked: newChecked } : item
      )
    );

    // 해당 id의 debounced 함수 가져오기 또는 생성
    let debouncedFn = debouncedMap.current.get(id);
    if (!debouncedFn) {
      debouncedFn = debounce(async (itemId, checked, original) => {
        // original과 같으면 서버 요청 생략
        if (checked === original) return;
        try {
          await toggleFixedExtension(itemId, checked);
        } catch (err) {
          console.error(err);
          alert(err.message);
          // 실패 시 UI 롤백
          setItems(prev =>
            prev.map(item =>
              item.id === itemId ? { ...item, checked: original } : item
            )
          );
        }
      }, DEBOUNCE_DELAY_MS);
      debouncedMap.current.set(id, debouncedFn);
    }

    // debounce 호출: 마지막 클릭으로부터 1초 후 실행
    debouncedFn(id, newChecked, old.checked);
  };

  return (
    <section>
      <h2>고정 확장자</h2>
      <ul>
        {items.map(({ id, name, checked }) => (
          <li key={id}>
            <label>
              <input
                type="checkbox"
                checked={checked}
                onChange={() => handleToggle(id)}
              />
              {name}
            </label>
          </li>
        ))}
      </ul>
    </section>
  );
}

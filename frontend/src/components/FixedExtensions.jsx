// src/FixedExtensions.js
import React, { useState, useEffect } from 'react';
import { fetchFixedExtensions, toggleFixedExtension } from '../api/api';

export default function FixedExtensions() {
  const [items, setItems] = useState([]);

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

  const handleToggle = async (id) => {
    const idx = items.findIndex(x => x.id === id);
    if (idx < 0) return;

    const old = items[idx];
    const updated = { ...old, checked: !old.checked };
    setItems([
      ...items.slice(0, idx),
      updated,
      ...items.slice(idx + 1)
    ]);

    try {
      await toggleFixedExtension(id, updated.checked);
    } catch (err) {
      console.error(err);
      alert(err.message);
      setItems([
        ...items.slice(0, idx),
        old,
        ...items.slice(idx + 1)
      ]);
    }
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
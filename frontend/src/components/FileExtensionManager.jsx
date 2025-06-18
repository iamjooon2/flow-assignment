import React, { useState, useEffect } from 'react';
import {
  fetchFixedExtensions,
  fetchCustomExtensions,
  addCustomExtension,
  removeCustomExtension,
  toggleFixedExtension,
} from '../api/api';

const FileExtensionManager = () => {
  const [fixedList, setFixedList] = useState([]);
  const [customList, setCustomList] = useState([]);
  const [newExt, setNewExt] = useState('');

  useEffect(() => {
    (async () => {
      const fixed = await fetchFixedExtensions();
      const custom = await fetchCustomExtensions();
      setFixedList(fixed.data || fixed);
      setCustomList(custom.data || custom);
    })();
  }, []);

  const onToggle = async (id, checked) => {
    setFixedList(prev => prev.map(e => (e.id === id ? { ...e, checked } : e)));
    await toggleFixedExtension(id, checked);
  };

  const handleAdd = async () => {
    const name = newExt.trim();
    if (!name || name.length > 20 || customList.length >= 200) return;
    const saved = await addCustomExtension(name);
    setCustomList(prev => [...prev, saved]);
    setNewExt('');
  };

  const handleRemove = async id => {
    await removeCustomExtension(id);
    setCustomList(prev => prev.filter(e => e.id !== id));
  };

  return (
    <div className="container">
      <div className="fixed-section">
        <h3 className="font-semibold text-lg">고정 확장자</h3>
        <div className="fixed-list">
          {fixedList.map(e => (
            <label key={e.id} className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={e.checked}
                onChange={ev => onToggle(e.id, ev.target.checked)}
              />
              <span>{e.name}</span>
            </label>
          ))}
        </div>
      </div>

      <div>
        <h3 className="font-semibold text-lg mb-2">커스텀 확장자</h3>
        <div className="custom-input-group">
          <input
            className="flex-grow px-3 py-1 border rounded"
            value={newExt}
            onChange={e => setNewExt(e.target.value)}
            placeholder="확장자 입력"
          />
          <button
            onClick={handleAdd}
            disabled={!newExt.trim() || customList.length >= 200}
            className="bg-blue-600 text-white px-4 py-1.5 rounded disabled:opacity-50"
          >
            추가
          </button>
        </div>
      </div>

      <div>
        <p className="text-sm text-gray-500 mb-2">{customList.length}/200</p>
        <div className="border rounded p-3 min-h-[80px]">
          <div className="custom-tag-list">
            {customList.map(e => (
              <span key={e.id} className="bg-gray-100 rounded-full px-3 py-1 flex items-center gap-2 text-sm">
                {e.name}
                <button onClick={() => handleRemove(e.id)} className="text-red-600 font-bold">×</button>
              </span>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default FileExtensionManager;

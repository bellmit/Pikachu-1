package com.pikachu.common.collection;

import com.pikachu.common.util.PikachuArrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Desc
 * @Date 2019-11-08 12:06
 * @Author AD
 */
public class DataSet {
    
    private Map<String, Object> datas = new HashMap<>();
    
    public DataSet() {
    }
    
    public DataSet(List<NameValue> nameValues) {
        if (!PikachuArrays.isEmpty(nameValues)) {
            nameValues.stream().forEach(nv -> datas.put(nv.getName().toUpperCase(), nv.getValue()));
        }
        
    }
    
    public void add(Object name, Object value) {
        if (name != null) {
            this.datas.put(name.toString().toUpperCase(), value);
        }
    }
    
    public void addAll(DataSet dataSet) {
        if (!isEmpty(dataSet)) {
            this.datas.putAll(dataSet.datas);
        }
    }
    
    public void addAll(Map<String, Object> map) {
        if (map != null) {
            map.entrySet().stream().forEach(next -> datas.put(next.getKey().toUpperCase(), next.getValue()));
        }
    }
    
    public Map<String, Object> getMap() {
        return this.datas;
    }
    
    public int size() {
        return this.datas.size();
    }
    
    public void clear() {
        this.datas.clear();
    }
    
    public Object remove(Object key) {
        return key == null ? null : datas.remove(key.toString().toUpperCase());
    }
    
    public boolean containsKey(Object key) {
        return key != null && datas.containsKey(key.toString().toUpperCase());
    }
    
    public <T> T get(Object key) {
        return key == null ? null : (T) datas.get(key.toString().toUpperCase());
    }
    
    public String getString(Object key) {
        return getString(key, (String) null);
    }
    
    public String getString(Object key, String defaultValue) {
        Object value = datas.get(key.toString().toUpperCase());
        return Optional.ofNullable(value).orElseGet(() -> defaultValue == null ? "" : defaultValue).toString();
    }
    
    public short getShort(Object key) {
        return this.getShort(key, (short) 0);
    }
    
    public short getShort(Object key, short defaultValue) {
        Object value = datas.get(key.toString().toUpperCase());
        value = Optional.ofNullable(value).orElse(defaultValue);
        if (!value.getClass().equals(Short.TYPE)) {
            try {
                return Short.valueOf(value.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return (short) value;
        }
    }
    
    public int getInt(Object key) {
        return this.getInt(key, 0);
    }
    
    public int getInt(Object key, int defaultValue) {
        Object value = datas.get(key.toString().toUpperCase());
        value = Optional.ofNullable(value).orElse(defaultValue);
        if (!value.getClass().equals(Integer.TYPE)) {
            try {
                return Integer.parseInt(value.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return (int) value;
        }
    }
    
    public long getLong(Object key) {
        return this.getLong(key, 0L);
    }
    
    public long getLong(Object key, long defaultValue) {
        Object value = datas.get(key.toString().toUpperCase());
        value = Optional.ofNullable(value).orElse(defaultValue);
        if (!value.getClass().equals(Long.TYPE)) {
            try {
                return Long.parseLong(value.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return (long) value;
        }
    }
    
    public double getDouble(Object key) {
        return this.getDouble(key, 0.0D);
    }
    
    public double getDouble(Object key, double defaultValue) {
        Object value = datas.get(key.toString().toUpperCase());
        value = Optional.ofNullable(value).orElse(defaultValue);
        if (!value.getClass().equals(Double.TYPE)) {
            try {
                return Double.parseDouble(value.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return (double) value;
        }
    }
    
    public float getFloat(Object key) {
        return this.getFloat(key, 0.0F);
    }
    
    public float getFloat(Object key, float defaultValue) {
        Object value = datas.get(key.toString().toUpperCase());
        value = Optional.ofNullable(value).orElse(defaultValue);
        if (!value.getClass().equals(Float.TYPE)) {
            try {
                return Float.parseFloat(value.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return (float) value;
        }
    }
    
    public boolean getBoolean(Object key) {
        return this.getBoolean(key, false);
    }
    
    public boolean getBoolean(Object key, boolean defaultValue) {
        Object value = datas.get(key.toString().toUpperCase());
        value = Optional.ofNullable(value).orElse(defaultValue);
        if (!value.getClass().equals(Boolean.TYPE)) {
            String t = key.toString().toUpperCase();
            return "TRUE".equals(t) || "Y".equals(t) || "1".equals(t);
        } else {
            return (boolean) value;
        }
    }
    
    @Override
    public String toString() {
        return datas.toString();
    }
    
    // ---------------------- 辅助方法 ----------------------
    
    private boolean isEmpty(DataSet dataSet) {
        return dataSet == null || dataSet.datas.size() == 0;
    }
    
}

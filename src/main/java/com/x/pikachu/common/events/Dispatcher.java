package com.x.pikachu.common.events;

import com.x.pikachu.common.util.PikachuStrings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/13 13:11
 */
public class Dispatcher {
    
    private final Object lock = new Object();
    
    private final Map<String, ListenerContainer> map = new ConcurrentHashMap<>();
    
    public void addListener(String type, IListener listener) {
        if (PikachuStrings.isNotNull(type) && listener != null) {
            this.addListener(type, new ListenerData(listener));
        }
    }
    
    public void addListener(String type, IListener listener, int priority, Object... params) {
        if (PikachuStrings.isNotNull(type) && listener != null) {
            this.addListener(type, new ListenerData(priority, listener, params));
        }
    }
    
    public void addListener(String type, Class<?> listenerClass) {
        if (PikachuStrings.isNotNull(type) && listenerClass != null) {
            this.addListener(type, new ListenerData(listenerClass));
        }
    }
    
    public void addListener(String type, Class<?> listenerClass, int priority, Object... params) {
        if (PikachuStrings.isNotNull(type) && listenerClass != null) {
            this.addListener(type, new ListenerData(priority, listenerClass, params));
        }
    }
    
    public boolean hasListener(String type) {
        return map.containsKey(type);
    }
    
    public boolean hasListener(String type, IListener listener) {
        ListenerContainer container = map.get(type);
        return container != null && container.contain(listener);
    }
    
    public boolean hasListener(String type, Class<? extends IListener> listenerClass) {
        ListenerContainer container = map.get(type);
        return container != null && container.contain(listenerClass);
    }
    
    public void removeListener(String type) {
        synchronized (lock) {
            map.remove(type);
        }
    }
    
    public void removeListener(String type, IListener listener) {
        ListenerContainer container = map.get(type);
        if (container != null) {
        synchronized (lock) {
                if (container.remove(listener) == 0) {
                    map.remove(type);
                }
            }
        }
    }
    
    public void removeListener(String type, Class<? extends IListener> listenerClass) {
        ListenerContainer container = map.get(type);
        if (container != null) {
            synchronized (lock) {
                if (container.remove(listenerClass) == 0) {
                    map.remove(type);
                }
            }
        }
    }
    
    public void removeAllListeners() {
        synchronized (this.lock) {
            map.clear();
        }
    }
    
    public int dispatch(Event event) {
        ListenerContainer container;
        if (event == null || (container = map.get(event.getType())) == null) {
            return 0;
        } else {
            ListenerData[] datas = container.getListeners();
            int count = 0;
            for (ListenerData data : datas) {
                if (event.isStopped()) {
                    break;
                }
                event.setParams(data.getParams());
                try {
                    data.getListener().onEvent(event);
                    ++count;
                } catch (Exception e) {
                    e.printStackTrace();
                    event.setException(e);
                }
            }
            return count;
        }
    }
    
    private void addListener(String type, ListenerData info) {
        if (!PikachuStrings.isNull(type)) {
            ListenerContainer container = map.get(type);
            if (container != null) {
                container.add(info);
            } else {
                synchronized (lock) {
                    container = map.get(type);
                    if (container == null) {
                        container = new ListenerContainer();
                        map.put(type, container);
                    }
                }
                container.add(info);
            }
        }
    }
    
}

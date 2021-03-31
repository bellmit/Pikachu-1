package com.x.pikachu.common.events;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：linstener容器
 * @Author：AD
 * @Date：2020/1/13 11:26
 */
class ListenerContainer {

    private static final Comparator<ListenerData> COMPARATOR = new Comparator<ListenerData>() {
        @Override
        public int compare(ListenerData first, ListenerData second) {
            int firstPriority = first.getPriority();
            int secondPriority = second.getPriority();
            if (firstPriority == secondPriority) {
                return 0;
            } else {
                return firstPriority < secondPriority ? -1 : 1;
            }
        }
    };

    private Map<Class<?>, ListenerData> classMap = new ConcurrentHashMap<>();

    private Map<IListener, ListenerData> infoMap = new ConcurrentHashMap<>();

    private volatile boolean changed = false;

    private final Object lock = new Object();

    private ListenerData[] listeners;


    ListenerContainer() {}

    boolean add(ListenerData info) {
        if (info == null) {return false;}
        IListener listener = info.getListener();
        if (listener == null) {
            Class<?> listenerClass = info.getListenerClass();
            if (listenerClass == null) {
                return false;
            }
            try {
                listener = (IListener)listenerClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            synchronized (lock) {
                if (infoMap.containsKey(listener)) {
                    return true;
                }
                info.setListener(listener);
                classMap.put(listenerClass, info);
                infoMap.put(listener, info);
                if (!changed) {
                    changed = true;
                }
            }
            return true;
        } else {
            synchronized (lock) {
                if (infoMap.containsKey(listener)) {
                    return true;
                }
                infoMap.put(listener, info);
                if (!changed) {
                    changed = true;
                }
            }
            return true;
        }
    }


    int remove(IListener listener) {
        synchronized (lock) {
            ListenerData remove = infoMap.remove(listener);
            if (remove != null && !changed) {
                changed = true;
            }
        }
        return infoMap.size();
    }

    int remove(Class<? extends IListener> listenerClass) {
        synchronized (lock) {
            ListenerData remove = classMap.remove(listenerClass);
            if (remove != null) {
                infoMap.remove(remove.getListener());
                if (!changed) {
                    changed = true;
                }
            }
        }
        return infoMap.size();
    }

    boolean contain(IListener listener) {
        return infoMap.containsKey(listener);
    }

    boolean contain(Class<? extends IListener> listenerClass) {
        return classMap.containsKey(listenerClass);
    }

    ListenerData[] getListeners() {
        if (changed) {
            synchronized (lock) {
                if (changed) {
                    changed = false;
                    this.listeners = infoMap.values().toArray(new ListenerData[infoMap.size()]);
                    Arrays.sort(this.listeners, COMPARATOR);
                }
            }
        }
        return this.listeners;
    }
}

package ru.vizzi.Utils.config;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class Flex {

    @Nonnull
    public <T, V> List<V> aggregateValues(@Nonnull Collection<T> collection, @Nonnull Predicate<T> predicate, @Nonnull Function<T, V> function) {
        List<V> list = new ArrayList<>();
        collection.forEach(t -> {
            if(predicate.test(t)) {
                list.add(function.apply(t));
            }
        });
        return list;
    }

    @SafeVarargs
    public <T> boolean containsAny(@Nonnull Collection<T> collection, @Nonnull T... elements) {
        for (T element : elements) {
            if(collection.contains(element)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public <T> boolean containsAll(@Nonnull Collection<T> collection, @Nonnull T... elements) {
        if(collection.isEmpty()) return false;
        for (T element : elements) {
            boolean contains = collection.contains(element);
            if(!contains) {
                return false;
            }
        }
        return true;
    }

    /**
     * Позволяет найти уникальный элемент в коллекции.
     * @param predicate сравнивает последний наиболее подходящий элемент с перебираемым. Первым аргументом будет самый
     *                  подходящий атрибут на данный момент. Вторым - текущий. При true результате, запомнит
     *                  перебираемый атрибут как наиболее подходящий.
     * @param function функция получения уникального атрибута объекта для сравнения.
     * */
    @Nullable
    public <T, V> T getUniqueElement(@Nonnull Collection<T> collection, @Nonnull Function<T, V> function, @Nonnull BiPredicate<V, V> predicate) {
        T last = null;
        V o = null;
        for (T t : collection) {
            V apply = function.apply(t);
            if (o == null || predicate.test(o, apply)) {
                last = t;
                o = apply;
            }
        }
        return last;
    }

    /**
     * Позволяет ремапить массивы без использования кривых стримов.
     * @param output массив должен иметь размер равный размеру целевого массива input.
     * */
    public <T, R> void remapArray(@Nonnull T[] input, @Nonnull R[] output, @Nonnull Function<T, R> function) {
        if(input.length != output.length) throw new IllegalArgumentException("Length of T and R arrays must be the same!");
        for (int i = 0; i < output.length; i++) {
            output[i] = function.apply(input[i]);
        }
    }

    /**
     * Пропалывает очередь с задачами, избегая флекс-аномалий с null-элементами.
     * В случае, если аномалия возникнет, метод очистит очередь.
     * */
    public <T> void pollQueue(@Nonnull Queue<FutureTask<T>> queue) {
        while(!queue.isEmpty()) {
            FutureTask<T> poll = queue.poll();
            if(poll != null) {
                poll.run();
            } else {
                if (!queue.remove(null)) {
                    queue.clear();
                }
            }
        }
    }

    /**
     * Реализовывает прерываемый перебор карты. Остановка перебора производится возвратом false в функции.
     * @return false, если перебор был прерван.
     * */
    public <K, V> boolean forEach(@Nonnull Map<K, V> map, @Nonnull Function<Map.Entry<K, V>, Boolean> function) {
        for (Map.Entry<K, V> kvEntry : map.entrySet()) {
            if(!function.apply(kvEntry)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Реализовывает прерываемый перебор коллекции. Остановка перебора производится возвратом false в функции.
     * @return false, если перебор был прерван.
     * */
    public <T> boolean forEach(@Nonnull Collection<T> collection, @Nonnull Function<T, Boolean> function) {
        for (T t : collection) {
            if (!function.apply(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Реализовывает прерываемый перебор массива. Остановка перебора производится возвратом false в функции.
     * @return false, если перебор был прерван.
     * */
    public <T> boolean forEach(@Nonnull T[] array, @Nonnull Function<T, Boolean> function) {
        for (T t : array) {
            if (!function.apply(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Реализовывает прерываемый перебор коллекции. Остановка перебора производится возвратом false в функции.
     * Использует {@link ThrFunction}, которая позволяет не обрабатывать исключения в лямбде.
     * @return false, если перебор был прерван.
     * */
    public <T> boolean forEachThr(@Nonnull Collection<T> collection, @Nonnull ThrFunction<T, Boolean> function) throws IOException {
        for (T t : collection) {
            if (!function.apply(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Использует {@link ThrConsumer}, который позволяет не обрабатывать исключения в лямбде.
     * */
    public <T> void forEachThr(@Nonnull Collection<T> collection, @Nonnull ThrConsumer<T> function) throws IOException {
        for (T t : collection) {
            function.accept(t);
        }
    }

    /**
     * @param predicate условие удаления элемента из карты.
     * @param consumer вызывается перед удалением элемента.
     * @return true если по крайней мере один элемент был удален из карты.
     * */
    public <T, V> boolean removeIf(@Nonnull Map<T, V> map, @Nonnull Predicate<Map.Entry<T, V>> predicate, @Nonnull Consumer<Map.Entry<T, V>> consumer) {
        boolean flag = false;
        Iterator<Map.Entry<T, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<T, V> next = iterator.next();
            if(predicate.test(next)) {
                consumer.accept(next);
                iterator.remove();
                flag = true;
            }
        }
        return flag;
    }

    /**
     * @param consumer выполнится перед удалением элемента из карты.
     * */
    public <T, V> void removeAll(@Nonnull Map<T, V> map, @Nonnull Consumer<Map.Entry<T, V>> consumer) {
        Iterator<Map.Entry<T, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<T, V> next = iterator.next();
            consumer.accept(next);
            iterator.remove();
        }
    }

    /**
     * @param predicate условие удаления элемента из коллекции.
     * @param consumer вызывается перед удалением элемента.
     * @return true если по крайней мере один элемент был удален из коллекции.
     * */
    public <T> boolean removeIf(@Nonnull Collection<T> collection, @Nonnull Predicate<T> predicate, @Nonnull Consumer<T> consumer) {
        boolean flag = false;
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if(predicate.test(next)) {
                consumer.accept(next);
                iterator.remove();
                flag = true;
            }
        }
        return flag;
    }

    /**
     * @param predicate условие удаления элемента из карты.
     * @param consumer вызывается перед удалением элемента.
     * @return количество удаленных элементов.
     * */
    public <T, V> int removeAllIf(@Nonnull Map<T, V> map, @Nonnull Predicate<Map.Entry<T, V>> predicate, @Nonnull Consumer<Map.Entry<T, V>> consumer) {
        int i = 0;
        Iterator<Map.Entry<T, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<T, V> next = iterator.next();
            if(predicate.test(next)) {
                consumer.accept(next);
                iterator.remove();
                i++;
            }
        }
        return i;
    }

    /**
     * @param predicate условие удаления элемента из коллекции.
     * @param consumer вызывается перед удалением элемента.
     * @return количество удаленных элементов.
     * */
    public <T> int removeAllIf(@Nonnull Collection<T> collection, @Nonnull Predicate<T> predicate, @Nonnull Consumer<T> consumer) {
        int i = 0;
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if(predicate.test(next)) {
                consumer.accept(next);
                iterator.remove();
                i++;
            }
        }
        return i;
    }

    /**
     * @return первый найденный объект, соответствующий входному предикату.
     * */
    @Nullable
    public <T> T getArrayElement(@Nonnull T[] array, @Nonnull Predicate<T> predicate) {
        for (T t : array) {
            if(predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    @Nullable
    public <K, V> K getMapEntryKey(@Nonnull Map<K, V> map, @Nonnull Predicate<Map.Entry<K, V>> predicate) {
        Map.Entry<K, V> mapEntry = getMapEntry(map, predicate);
        return mapEntry != null ? mapEntry.getKey() : null;
    }

    @Nullable
    public <K, V> V getMapEntryValue(@Nonnull Map<K, V> map, @Nonnull Predicate<Map.Entry<K, V>> predicate) {
        Map.Entry<K, V> mapEntry = getMapEntry(map, predicate);
        return mapEntry != null ? mapEntry.getValue() : null;
    }

    @Nullable
    public <K, V> Map.Entry<K, V> getMapEntry(@Nonnull Map<K, V> map, @Nonnull Predicate<Map.Entry<K, V>> predicate) {
        for (Map.Entry<K, V> tvEntry : map.entrySet()) {
            if(predicate.test(tvEntry)) {
                return tvEntry;
            }
        }
        return null;
    }

    @Nullable
    public <T> T getCollectionElement(@Nonnull Collection<T> collection, @Nonnull Predicate<T> predicate) {
        for (T t : collection) {
            if(predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    @SneakyThrows
    public boolean inEnumRange(@Nonnull Class<? extends Enum<?>> aClass, int ordinal) {
        if(ordinal < 0) return false;
        Method values = aClass.getMethod("values");
        return ((Object[]) values.invoke(aClass)).length > ordinal;
    }
}

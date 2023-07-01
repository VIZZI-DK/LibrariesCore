package ru.vizzi.librariescore.databases;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BiHolder<F, S> {

    private final F first;
    private final S second;
}

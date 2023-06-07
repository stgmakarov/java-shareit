package ru.practicum.shareit.utilites;

public class ParamNotFoundException extends RuntimeException {
    public ParamNotFoundException(String s) {
        super(s);
    }
}

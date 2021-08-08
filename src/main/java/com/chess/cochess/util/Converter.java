package com.chess.cochess.util;

public interface Converter<From, To> {
    To convert(From source);
}

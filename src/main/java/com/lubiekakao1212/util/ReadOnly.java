package com.lubiekakao1212.util;

/**
 * Used to indicate that a value should be treated as readOnly <p>
 * Does nothing to enforce it, It's just a nice reminder
 */
public record ReadOnly<T>(T value) { }

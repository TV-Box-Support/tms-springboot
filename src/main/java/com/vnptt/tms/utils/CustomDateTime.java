package com.vnptt.tms.utils;

import java.time.LocalDateTime;

public class CustomDateTime{
    public static LocalDateTime nowPlus7Hours() {
        return LocalDateTime.now().plusHours(7);
    }
}
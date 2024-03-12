package com.bcn.authService.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    public static Date convertBirthday(String birthdayString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(birthdayString, formatter);
        return Date.valueOf(localDate);
    }
}

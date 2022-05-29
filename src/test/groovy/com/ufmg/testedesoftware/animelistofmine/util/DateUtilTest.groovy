package com.ufmg.testedesoftware.animelistofmine.util

import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DateUtilTest extends Specification {
    DateUtil dateUtil

    def setup() {
        dateUtil = new DateUtil()
    }

    def "It should convert LocalDateTime to data base style"() {
        given:
        LocalDate localDate = LocalDate.of(2018, 6, 1)
        LocalTime localTime = LocalTime.of(17, 00)

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime)

        when:
        String formattedLocalDateTime = dateUtil.formatLocalDateTimeToDatabaseStyle(localDateTime)

        then:
        assert formattedLocalDateTime == "2018-06-01 17:00:00"
    }
/*
    def "FLAKY TEST - It should convert LocalDateTime to data base style"() {
        given:
        LocalDateTime localDateTime = LocalDateTime.now()

        when:
        String formattedDate = dateUtil.formatLocalDateTimeToDatabaseStyle(localDateTime).split()[0]

        then:
        // this test will only work in May 21th, 2022
        assert formattedDate == "2022-05-21"
    }
 */
}

package dev.jaym21.trackin.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UtilitiesTest {

    @Test
    fun `Get the formatted time for timer`() {
        val formattedTime = Utilities.timeToTimerFormat(5766, true)
        assertThat(formattedTime).isNotNull()
    }

    @Test
    fun `Get the formatted time for Overall Stats`() {
        val formattedTime = Utilities.timeToOverallStatsFormat(5766)
        assertThat(formattedTime).isNotNull()
    }

    @Test
    fun `Convert timestamp to date`() {
        val date = Utilities.convertDateFormat(1654498274676)
        assertThat(date).isNotNull()
    }

    @Test
    fun `Convert timestamp to get month full name`() {
        val month = Utilities.getMonthFullName(1654498274676)
        assertThat(month).isNotNull()
    }

    @Test
    fun `Convert timestamp to get date`() {
        val date = Utilities.getDateFromTimestamp(1654498274676)
        assertThat(date).isNotNull()
    }
}
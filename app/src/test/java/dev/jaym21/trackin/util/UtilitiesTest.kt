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
}
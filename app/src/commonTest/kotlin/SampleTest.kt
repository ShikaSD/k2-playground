import internal.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SampleTest {
    @Test
    fun `2 + 2 = 4`() {
        assertEquals(4, 2 + 2)
    }
}

@Ignore
class IgnoredTest {
    @Test
    fun `2 + 2 = 5`() {
        assertEquals(4, 2 + 2)
    }
}
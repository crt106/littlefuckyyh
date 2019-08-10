package command

import org.junit.Test

/**
 * @author crt106 on 2019/8/11.
 */
class TpCommandTest {

    @Test
    fun toHexString() {
        val test1 = TpCommand("hahah", 547, 977, 2432)
        val sample1: String = "130003102f747020353437203937372032343332"
        val test2 = TpCommand("hahah", 543, 77, 89)
        val sample2: String = "1000030d2f747020353433203737203839"
        assert(sample1.equals(test1.toHexString(), true))
        assert(sample2.equals(test2.toHexString(), true))
    }
}
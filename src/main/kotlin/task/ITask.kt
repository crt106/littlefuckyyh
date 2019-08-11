package task

import model.FuckTarget
import java.io.InputStream
import java.io.OutputStream

/**
 * @author chaoruitao
 * @date 2019-08-11
 */
interface ITask : Runnable {

    val fuckTarget: FuckTarget

    /**
     * Socket输入流(S->C)
     */
    var socketIn: InputStream

    /**
     * Socket输出流(C->S)
     */
    var socketOut: OutputStream

    fun doTask()

    override fun run() {
        doTask()
    }

}
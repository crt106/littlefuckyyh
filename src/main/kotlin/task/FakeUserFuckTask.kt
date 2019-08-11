package task

import model.FuckTarget
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

/**
 * @author chaoruitao
 * @date 2019-08-11
 */
class FakeUserFuckTask(override val fuckTarget: FuckTarget) : ITask {

    override lateinit var socketIn: InputStream
    override lateinit var socketOut: OutputStream

    init {
        val socket = Socket(fuckTarget.ip, fuckTarget.port)
        socketIn = socket.getInputStream()
        socketOut = socket.getOutputStream()
    }

    override fun doTask() {

    }
}
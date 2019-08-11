package command

import util.HexUtil
import java.io.OutputStream

/**
 * @author crt106 on 2019/8/11.
 */
interface ICommand {

    /**
     * 输出的命令可读文字
     */
    var commandStr: String?


    /**
     * 将所执行的命令转换为16进制字符串输出
     */
    fun toHexString(): String

    fun sendCommand(outputStream: OutputStream) {
        outputStream.write(HexUtil.hexStrToBinaryStr(toHexString()))
        outputStream.flush()
    }

}
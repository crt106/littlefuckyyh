package command

import util.HexUtil

/**
 * TP指令格式
 * [len+3][00 03][len][command]
 * 示例:
 * 13 0003 10 2f747020353437203937372032343332 ->/tp 547 977 2432
 * 10 0003 0d 2f747020353433203737203839 ->/tp 543 77 89
 *
 * @author crt106 on 2019/8/11.
 */
class TpCommand(var x: Int, var y: Int, var z: Int) : ICommand {

    override var commandStr: String? = ""

    init {
        commandStr = "/tp $x $y $z"
    }

    companion object {

        private const val XZMIN = -9999
        private const val XZMAX = 9999
        private const val YMIN = 0
        private const val YMAX = 256

        /**
         * 生成随机Tp指令
         */
        fun randomTp(): TpCommand {
            val x = (XZMIN..XZMAX).random()
            val y = (YMIN..YMAX).random()
            val z = (-XZMIN..XZMAX).random()
            return TpCommand(x, y, z)
        }
    }

    /**
     * 将所执行的命令转换为16进制字符串输出
     */
    override fun toHexString(): String {
        val len = commandStr?.length
        if (len != null) {
            return HexUtil.int2HexStr(len + 3) + "0003" + HexUtil.int2HexStr(len) + HexUtil.str2HexStr(commandStr);
        }
        return ""
    }
}
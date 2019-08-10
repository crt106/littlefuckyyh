package command

/**
 * @author crt106 on 2019/8/11.
 */
abstract class BaseCommand(var name: String) {

    /**
     * 输出的命令可读文字
     */
    var commandStr: String? = ""


    /**
     * 将所执行的命令转换为16进制字符串输出
     */
    abstract fun toHexString(): String

}
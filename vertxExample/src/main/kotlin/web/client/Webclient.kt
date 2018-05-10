package web.client

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/5/10 下午3:46
 */
class Webclient(val age:Int, val name: String){

}

fun main(args: Array<String>) {
    var map: HashMap<Int, Int> = hashMapOf(1 to 11, 2 to 12, 3 to 13)

    var aa = hashMapOf(1 to 11, 2 to 12, 3 to 13)

    map = aa


    listOf<Int>().last { true }



}

fun getInt():Int?{
    return null
}
/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 上午11:22
 */
class Admin {
    def userId = UUID.randomUUID().toString()
    def ping = -1

    Map toMap() {
        return [
                'userId': userId
        ]
    }
}

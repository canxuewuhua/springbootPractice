package cn.yongqiang.base;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 *  redis 事务操作  使用watch实现乐观锁
 */
public class TestTx {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.94.155.21", 6379);

        jedis.flushDB();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");
        jsonObject.put("name", "kuangshen");

        // 开启事务
        Transaction multi = jedis.multi();
        String res = jsonObject.toJSONString();

        jedis.watch(res);

        try{
            multi.set("user1", res);
            multi.set("user2", res);

            int i = 1/0;// =====代码抛出异常事务 执行失败！====

            multi.exec();  // 执行事务
        }catch (Exception e){
            multi.discard(); // 放弃事务
            e.printStackTrace();
            jedis.close(); // 关闭连接
        }



    }
}

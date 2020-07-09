package com.bevis.logger.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bevis.logger.Logger
import org.json.JSONObject
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {
    class Test1 {
        val param1: String = "param_1"
        val param2: String = "param_2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val message1 = Logger().e("bevisLog").addExtra("array",
            Logger()
                .e("bevisLog").addExtra("array", Logger().e("bevisLog").addExtra("array", "1").close()).close()).close()



//
//        val message3 = Logger().e("bevisLog").addExtra("array",
//            mapOf("key1" to JSONObject().apply {
//                put("k1", "v1")
//                put("k2", "v2")
//
//            }.toString())).close()
//
//        val message2 = Logger().e("bevisLog").addExtra("array", message3).close()


        Logger().e("bevisLog").addExtra("array",
            message1).print()

//        this.javaClass.typeParameters.forEach { typeVariable ->
//            typeVariable.bounds.forEach { bound ->
//                Log.i("bevisLog","type ==> ${bound}")
//            }
//        }



        Logger()
            .e("bevisLog")
            .setMaxLevel(1)
            .setHead("%s", "this is head")
            .addBody("%s", "this is body")
            .addBody("this is body2")
            .addBody(JSONObject().put("k1", "v1")
                .put("k2", "v2")
                .put("k3",
                    JSONObject().put("k1", "v1")
                        .put("k2", "v2")
                        .put("k3", "v3")).toString())
            .addExtra("key1", "%s", "value1")
            .addExtra("key2", JSONObject().put("k1", "v1")
                .put("k2", "v2")
                .put("k3",
                    JSONObject().put("k1", "v1")
                        .put("k2", "v2")
                        .put("k3", "v3")))
            .addExtra("key3", JSONObject().put("k1", "v1")
                .put("k2", "v2")
                .put("k3",
                    JSONObject().put("k1", "v1")
                        .put("k2", "v2")
                        .put("k3", "v3")).toString())
            .addExtra("list1", arrayListOf(1,2,3,4,5))
            .addExtra("map1", mapOf(1 to "one", 2 to "tow", 3 to "three", 4 to JSONObject().put("k1", "v1")
                .put("k2", "v2")
                .put("k3",
                    JSONObject().put("k1", "v1")
                        .put("k2", "v2")
                        .put("k3", "v3")).toString(), 5 to  JSONObject().put("k1", "v1")
                .put("k2", "v2")
                .put("k3", "v3")))
            .addExtra("bundle1", Bundle().apply {
                putInt("integer1", 2)
                putString("json1", JSONObject().put("k1", "v1")
                    .put("k2", "v2")
                    .put("k3", "v3").toString())
            })
            .addExtra("param1", Test1())
            .addExtra("array1", arrayOf(JSONObject().put("k1", "v1")
                .put("k2", "v2")
                .put("k3", "v3"), JSONObject().put("k1", "v1")
                .put("k2", "v2")
                .put("k3", "v3")))
            .setTail("%s", "this is tail")
            .setThrowable(IllegalStateException("this is a error"))
            .print()
//
//
//        Logger.tag("bevisLog")
//        Logger.warn("test debug %s", "param1")
//        Logger.error(RuntimeException("test error1"), "test error1 %s", "param1")
//        Logger.error().setThrowable(RuntimeException("test error2")).print()

    }
}

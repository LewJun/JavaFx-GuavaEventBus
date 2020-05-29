package org.example.lewjun

import com.google.gson.reflect.TypeToken
import org.example.lewjun.model.Ab01
import org.example.lewjun.model.Ac01
import org.example.lewjun.util.GsonUtil

import java.time.LocalDate
import java.time.LocalDateTime

class GsonUtilTest extends GroovyTestCase {

    void testMapListToJson() {
        def emptyMap = [:]
        println GsonUtil.objToJsonString(emptyMap)

        def numDict = [1: "One", 2: "Two", 3: "Three"]
        println GsonUtil.objToJsonString(numDict)

        def nums = [2, 5, 8]
        println GsonUtil.objToJsonString(nums)
    }

    void testAc01ToJson() {
        def ac01 = new Ac01(
                aac001: 7839,
                aac002: "KING",
                aac003: "PRESIDENT",
                aac005: LocalDate.now(),
                aac006: 1,
                aac007: 66.6f,
                aac008: 1.72f,
                aac009: Arrays.asList("Reading", "Coding", "Play"),
                ab01: new Ab01(aab001: 10, aab002: "ACCOUNTING", aab003: "NEW YORK"),
                aac100: LocalDateTime.now(),
                aac101: new Date()
        )

        println GsonUtil.objToJsonString(ac01)

        def ac01Json = '''
{"aac001":7839,"aac002":"KING","aac003":"PRESIDENT","aac005":1590681600000,"aac006":1,"aac007":66.6,"aac008":1.72,"aac009":["Reading","Coding","Play"],"ab01":{"aab001":10,"aab002":"ACCOUNTING","aab003":"NEW YORK"},"aac100":1590736151968,"aac101":1590736151973}
'''
        def ac01Obj = GsonUtil.jsonStringToObj(ac01Json, new TypeToken<Ac01>() {}.getType()) as Ac01
        println ac01Obj
    }


    void testAc01ListToJson() {
        def ac01List = Arrays.asList(
                new Ac01(
                        aac001: 7839,
                        aac002: "KING",
                        aac003: "PRESIDENT",
                        aac005: LocalDate.now(),
                        aac006: 1,
                        aac007: 66.6f,
                        aac008: 1.72f,
                        aac009: Arrays.asList("Reading", "Coding", "Play"),
                        ab01: new Ab01(aab001: 10, aab002: "ACCOUNTING", aab003: "NEW YORK"),
                        aac100: LocalDateTime.now(),
                        aac101: new Date()
                ),
                new Ac01(
                        aac001: 7566,
                        aac002: "JONES",
                        aac003: "MANAGER",
                        aac004: 7839,
                        aac005: LocalDate.now(),
                        aac006: 0,
                        aac007: 62.3f,
                        aac008: 1.62f,
                        aac009: Arrays.asList("Play Games", "Chat", "Photoing"),
                        ab01: new Ab01(aab001: 20, aab002: "RESEARCH", aab003: "DALLAS"),
                        aac100: LocalDateTime.now(),
                        aac101: new Date()
                )
        )

        println GsonUtil.objToJsonString(ac01List)

        def ac01ListJson = '''
[{"aac001":7839,"aac002":"KING","aac003":"PRESIDENT","aac005":1590681600000,"aac006":1,"aac007":66.6,"aac008":1.72,"aac009":["Reading","Coding","Play"],"ab01":{"aab001":10,"aab002":"ACCOUNTING","aab003":"NEW YORK"},"aac100":1590736632723,"aac101":1590736632730},{"aac001":7566,"aac002":"JONES","aac003":"MANAGER","aac004":7839,"aac005":1590681600000,"aac006":0,"aac007":62.3,"aac008":1.62,"aac009":["Play Games","Chat","Photoing"],"ab01":{"aab001":20,"aab002":"RESEARCH","aab003":"DALLAS"},"aac100":1590736632747,"aac101":1590736632747}]
'''
        def ac01ListObj = GsonUtil.jsonStringToObj(ac01ListJson, new TypeToken<List<Ac01>>() {}.getType()) as List<Ac01>
        println ac01ListObj

    }

    void testAb01ToJson() {
        def ab01 = new Ab01(aab001: 10, aab002: "ACCOUNTING", aab003: "NEW YORK")
        println GsonUtil.objToJsonString(ab01)

        def ab01Json = '''
{"aab001":10,"aab002":"ACCOUNTING","aab003":"NEW YORK"}
'''

        def ab01Obj = GsonUtil.jsonStringToObj(ab01Json, new TypeToken<Ab01>() {}.getType()) as Ab01
        println ab01Obj
    }

    void testAb01ListToJson() {
        println GsonUtil.objToJsonString(
                Arrays.asList(
                        new Ab01(aab001: 10, aab002: "ACCOUNTING", aab003: "NEW YORK"),
                        new Ab01(aab001: 20, aab002: "RESEARCH", aab003: "DALLAS"),
                        new Ab01(aab001: 30, aab002: "SALES", aab003: "CHICAGO"),
                        new Ab01(aab001: 40, aab002: "OPERATIONS", aab003: "BOSTON")
                )
        )

        def ab01ListJson = '''
[
  {
    "aab001": 10,
    "aab002": "ACCOUNTING",
    "aab003": "NEW YORK"
  },
  {
    "aab001": 20,
    "aab002": "RESEARCH",
    "aab003": "DALLAS"
  },
  {
    "aab001": 30,
    "aab002": "SALES",
    "aab003": "CHICAGO"
  },
  {
    "aab001": 40,
    "aab002": "OPERATIONS",
    "aab003": "BOSTON"
  }
]
'''

        def ab01List = GsonUtil.jsonStringToObj(ab01ListJson, new TypeToken<List<Ab01>>() {}.getType()) as List<Ab01>
        println ab01List
    }
}

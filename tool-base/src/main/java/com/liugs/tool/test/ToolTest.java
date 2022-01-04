package com.liugs.tool.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.base.Console;
import com.liugs.tool.constant.ToolConstants;
import com.liugs.tool.constant.ToolException;
import com.liugs.tool.encode.EncodeExecuter;
import com.liugs.tool.encode.RsaEncodeTool;
import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import com.tydic.payment.pay.rsa.util.EncodeUtil;
import com.tydic.payment.pay.sdk.PayCenterSdkException;
import org.joda.time.DateTime;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ToolTest
 * @Description 工具测试类
 * @Author liugs
 * @Date 2020/7/9 9:38:22
 */
public class ToolTest {

    public static void main(String[] args) throws IOException {
      /*  try {
            enCode();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*dealNum();*/

//        dealListSort();

//        ToolExceptionTest();

//        sort();

        test();

//        listToMap();

//        listPage();

//        delete();

//        testDecode();

//        testFile();

//        testMap();
//        testDownload();
//        testXmlToXls();
    }

    private static void testDownload() {
        Integer[] functionIds = {1776};
        JSONObject requestParam = new JSONObject();
        requestParam.put("amount", "192526.53");
        requestParam.put("applyNo", "KPSQ20210728000002");
        requestParam.put("outStore", "中核供应链仓库");
        requestParam.put("outstockDate", "2021-08-11 18:01:01");
        requestParam.put("outstockNo", "XCKD20210811000482");
        requestParam.put("outstockNoArr", "XCKD20210811000482");
        requestParam.put("purchaseName", "中核控制系统工程有限公司");
        requestParam.put("taxAmt", "22149.83");
        requestParam.put("untaxAmt", "170376.7");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("functionIds", functionIds);
        jsonObject.put("businessCenterId", "FSC");
        jsonObject.put("requestParam", requestParam.toJSONString());
        jsonObject.put("userId", "11111");

        Console.show(HttpUtil.post("http://127.0.0.1:8089/api/task/commit", jsonObject.toJSONString()));
    }

    public static void testXmlToXls() {
        //加载xml文件
        Workbook wb = new Workbook();
        wb.loadFromXml("D:\\resources\\Work\\tmp_files\\FSC\\complex\\8580.xml");

        //转为2013版xlsx格式的Excel
        wb.saveToFile("D:\\resources\\Work\\tmp_files\\FSC\\complex\\xmltoExcel.xlsx", FileFormat.Version2013);

        //转为03版xls格式的Excel
        wb.saveToFile("D:\\resources\\Work\\tmp_files\\FSC\\complex\\xmltoExcel.xls", FileFormat.Version97to2003);
    }

    private static void testMap() {
        List<String> list = new ArrayList<>();
        list.add("1,1,0");
        list.add("1,1,1");
        list.add("1");
        list.add("1,2,2");
        list.add("2,1,2");
        list.add("2,2,2");
      /*  Map<String, Object> firstMap = new HashMap<>();
        for (String item : list) {
            LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(item.split(",")));
            getTreeMap(firstMap, linkedList);
            Console.show(list.size());
        }
        Console.show(JSON.toJSONString(firstMap));*/
        List<Long> tests = list.stream().map(item -> Long.valueOf(item.substring(item.lastIndexOf(",") + 1))).collect(Collectors.toList());
        Console.show(JSON.toJSON(tests));
    }

    private static void getTreeMap(Map<String, Object> treeMap, LinkedList<String> categotyTreeList) {
        if (!categotyTreeList.isEmpty()) {
            String categoryId = categotyTreeList.removeFirst();
            if (categotyTreeList.isEmpty()) {
                treeMap.put(categoryId, categoryId);
                return;
            }
            Map<String, Object> childMap;
            try {
                childMap = (Map<String, Object>) treeMap.get(categoryId);
            } catch (Exception e) {
                childMap = new HashMap<>(1);
            }

            if (null == childMap) {
                childMap = new HashMap<>(1);
            }

            treeMap.put(categoryId, childMap);

            getTreeMap(childMap, categotyTreeList);
        }
    }

    private static void testFile() throws IOException {
        InputStream in = FileUtil.getInputStream("E:\\Liunuer\\Desktop\\111.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\Liunuer\\Desktop\\1112.txt");

        ByteArrayOutputStream outputStream = convertToOut(in);
        outputStream.write(("nihao" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));

        fileOutputStream.write(outputStream.toByteArray());

        Console.show(outputStream.toString());

        outputStream.close();
        fileOutputStream.close();
        in.close();
    }

    private static ByteArrayOutputStream convertToOut(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        for (int readSize; (readSize = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, readSize);
            out.flush();
        }

        return out;
    }

    private static InputStream convertToIn(ByteArrayOutputStream  out) {
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void testDecode() {
        /**--------------------------这段代码是为了得到用来演示的加密报文-------------------*/
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIZVkkrlPKLhn5mpLw81avOiDe+HwENMQy0X/gdtdj6pbvBW4RVEXHEBXu5H6r176J6IuF0mQeEUP22bOS9C53j0Ftj06dPMf9Z7lwUGJgGbaaDS+rP4UZgxDdtFk/SMIBgP0tsFhEbPhpWd0TOFIswNt4zTyy7Bqsx1M3mIXt41AgMBAAECgYAl+DHtcXX+I//Ukvl2NwcP7hI6TgiN/RRPvqRSvSHa/FEbJbNhK31lg5mtiC5VeJx7kvFpgtLEJ9D1zhYPwb1jCPYUKRMI0xniQOWTFEUuSyJlvexaYqw1T4hpVuN9RpW7JxItBG6hlzhYU+jEp9zH1QcWq0XBFv2MOv+3h1zRAQJBAPvA6qrcDNugd0uhFz1+1Qo9aJ5Ru4168YBoEd5/jqdji73SLKzTk5nLjeKXKxxyRU0SDAdv/rAY7J7pmZm/xwUCQQCImaID3BwY6bC93alSdikwz48LqzuKT+Et8VeJSUxhhDG0trc13nGRPzXGmWHl4MorL24UxET0PDOiKR0//QFxAkEA8ZPlm58dF4Ob9g7W5kPW2sSip4l2mATpyXYT75Ynpah4Z+ZOyGkese4KcOzuiZV9ur8em+R0WTcRmExBALBuoQJANQZuPdFTltggI5PIBpqXorrvbDgsBKS9ZHgq4r/xRmlqYhwLQn3218sRtOYVeoan89uVf7owih5UbL5I/G3aAQJANBsbYr+ghZMEScxeeaKYjZvWiXBeCJEMfQ7KSuZU5zHiI3VnVfK9HjUcKQFYLFGyDFeRCiIPqtXJlSgvQb4bNQ==";
        String signkey = "lDZ1V4s20zOjiwpCIuB4qkPEm7gU5csr";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("realFee", "1.00");
        jsonObject.put("orderAttrValue2", "");
        jsonObject.put("orderAttrValue3", "");
        jsonObject.put("resultCode", "SUCCESS");
        jsonObject.put("transactionsId", "1500940");
        jsonObject.put("merchantId", "1000000001");
        jsonObject.put("resultMsg", "");
        jsonObject.put("tradeTime", "20181228162210");
        jsonObject.put("outOrderId", "1545985309289");
        jsonObject.put("createOperId", "LTHQY211");
        jsonObject.put("payNotifyTransId", "2018122822001481070598194");

        String data = jsonObject.toJSONString();
        String content = "";
        try {
            content = EncodeUtil.privateEncode(data, privateKey, signkey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Console.show(content);
        /**-------------------------------------结束------------------------------------------*/

        //实际使用时，直接传入content和publicKey
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGVZJK5Tyi4Z+ZqS8PNWrzog3vh8BDTEMtF/4HbXY+qW7wVuEVRFxxAV7uR+q9e+ieiLhdJkHhFD9tmzkvQud49BbY9OnTzH/We5cFBiYBm2mg0vqz+FGYMQ3bRZP0jCAYD9LbBYRGz4aVndEzhSLMDbeM08suwarMdTN5iF7eNQIDAQAB";
        //调用解密方法
        String retStr;
        try {
            retStr = EncodeUtil.publicDecode(content, publicKey);
        } catch (Exception e) {
            throw new PayCenterSdkException("数据解密异常", e);
        }
        System.out.println("使用工具类解密后的数据：" + retStr);
    }

    private static void delete() {
        File file = new File("E:\\Liunuer\\Desktop\\斗破苍穹.txt");
        File newfile = new File("E:\\Liunuer\\Desktop\\斗破苍穹1.txt");
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(newfile));
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            String str;
            while ((str = bufferedReader.readLine()) != null) {
                if ("".equals(str)) {
                    continue;
                }
                bufferedWriter.write(str + System.lineSeparator());
            }
            //关闭数据流
            bufferedReader.close();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void test() {
        /*String url = "http://172.16.100.36:18000/upload/cms/column/10001_banner/index.html";
        String result = HttpUtil.get(url);
        System.out.println("result:" + result);
        JSONObject jsonObject = JSON.parseObject(result);

        JSONArray rows = (JSONArray) jsonObject.get("rows");
        System.out.println(rows);*/

//        Set<String> keySet = ToolPropertiesKey.getKeySet();
//        System.out.println(JSON.toJSONString(keySet));

        /*String testStr = "key-value&name-liugs&age-24&";
        String enCodeStr = null;
        try {
            enCodeStr = URLEncoder.encode(testStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String deCodeStr = null;
        try {
            deCodeStr = URLDecoder.decode(enCodeStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String[] strs = deCodeStr.split("&");
        Map<String, String> map = new HashMap<>(16);
        for (String s : strs) {
            String[] str = s.split("-");
            map.put(str[0], str[1]);
        }

        Console.show(JSON.toJSONString(map));*/

//        String dateStr = "2020-09-15 16:17:22";
//        DateTime tradeTime = DateTime.parse(dateStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
//        Console.show(tradeTime.toString("yyyyHHdd"));
//        Console.show(tradeTime.toString("HHmmss"));

        /*Map<String, String> valueMap = new HashMap<>(16);
        valueMap.put("Version", "20150922");
        valueMap.put("MerId", "000092009169004");
        valueMap.put("MerOrderNo", "dfsrwr23423423");
        valueMap.put("TranDate", "20200919");
        valueMap.put("TranTime", "175823");
        valueMap.put("OrderAmt", "1");
        valueMap.put("BusiType", "0001");
        valueMap.put("MerBgUrl", "www.baidu.com");
        valueMap.put("Signature", "sdfsdfsdfsdf");

        String formHead = "<form name=\"message_form\" method=\"post\" action=\"URL\">";
        String inputItem = "<input type=\"hidden\" name=\"CODE\" value=\"VALUE\">";
        String submit = "<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >";
        String script = "<script>document.forms[0].submit();</script>";

        StringBuilder htmlBuilder =new StringBuilder();
        htmlBuilder.append(formHead.replace("URL", ""));
        for (String key : valueMap.keySet()) {
            String input = inputItem.replace("CODE", key).replace("VALUE", valueMap.get(key));
            htmlBuilder.append(input);
        }
        htmlBuilder.append(submit);
        htmlBuilder.append("</form>");
        htmlBuilder.append(script);

        System.out.println(htmlBuilder);*/

        /*Console.show("时间戳：" + DateTime.now().getMillis());

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();
        Console.show("时间戳：" + timestamp);*/

//        String resultStr = HttpUtil.get("https://www.cnncmall.com/upload/cms/column/12323_banner/index.html");
        /*JSONObject columnJson = new JSONObject();
        columnJson.put("ceshi", "fdsfas");
//        String result = HttpUtil.post("http://192.168.3.53:9000/ms-mcms/cms/service/addColumnService.do", columnJson.toJSONString());
        String result = null;
        JSONObject resultJson = JSON.parseObject(result);
        Console.show(resultJson.toString());*/

        /*String str = "{\n" +
                "    \"goodsSubId\": \"323\",\n" +
                "    \"goodsName\": \"商品名称\",\n" +
                "    \"payeeCompanyName\": \"发射点发\",\n" +
                "    \"goodsNumber\": \"234234\",\n" +
                "    \"goodsUnit\": \"地方撒地方\",\n" +
                "    \"goodsAmt\": \"sdfasdf\"\n" +
                "}";
        GoodInfo goodInfo = JSONObject.parseObject(str, GoodInfo.class);
        Console.show(JSON.toJSONString(goodInfo));*/

        /*String str = "http://liugs.utools.club/pay-dev/pay/rest/receiveIcbcNotify";
        Console.show(str.substring(str.indexOf("/pay/")));
        String SLASH = "/";
        Matcher slashMatcher = Pattern.compile(SLASH).matcher(str);
        int index = 0;
        while(slashMatcher.find()) {
            index++;
            if(index == 3){
                break;
            }
        }
        Console.show(slashMatcher.start());
        Console.show(str.substring(slashMatcher.start()));*/

        /*JSONObject json = new JSONObject();
        json.put("billDate", "20201224163121");
        Map<String, String> paramMap = new HashMap<>(16);
        paramMap.put("MerId", "23423423");
        json.put("paraMap", paramMap);
        Console.show(JSON.toJSONString(json));*/

        /*String input = "elastic:1qaz2wsx";
        String str = Base64Encoder.encode(input.getBytes(StandardCharsets.UTF_8));
        Console.show(str);

        JSONObject object = new JSONObject();
        object.put("","");*/


        /*List<GoodInfo> goodInfos = new ArrayList<>();
        GoodInfo goodInfo1 = new GoodInfo();
        goodInfo1.setGoodsSubId("10001");

        GoodInfo goodInfo2 = new GoodInfo();
        goodInfo2.setGoodsSubId("10002");

        GoodInfo goodInfo3 = new GoodInfo();
        goodInfo3.setGoodsSubId("10002");

        GoodInfo goodInfo4 = new GoodInfo();
        goodInfo4.setGoodsSubId("10001");

        GoodInfo goodInfo5 = new GoodInfo();
        goodInfo5.setGoodsSubId("10004");

        goodInfos.add(goodInfo1);
        goodInfos.add(goodInfo2);
        goodInfos.add(goodInfo3);
        goodInfos.add(goodInfo4);
        goodInfos.add(goodInfo5);

        List<String> goodsSubIds = goodInfos.stream().map(GoodInfo::getGoodsSubId).distinct().collect(Collectors.toList());
        Console.show(JSON.toJSONString(goodsSubIds));

        goodInfos = goodInfos.stream().filter(a -> !goodsSubIds.contains(a.getGoodsSubId())).collect(Collectors.toList());
        Console.show(JSON.toJSONString(goodInfos));*/

        /*Map<Long, GoodInfo> goodMap = new HashMap<>(1);
        goodMap.put(1L, goodInfo1);
        goodMap.put(2L, goodInfo3);
        goodMap.put(3L, goodInfo4);
        Console.show(goodMap.get(4L));

        Long i = 4L;
        Console.show(goodMap.get(1L) == null || goodMap.get(i ++) != null);
        Console.show(i);

        Set<Long> testSet = new HashSet<>();
        testSet.add(1L);
        testSet.add(3L);
        GoodInfo testInfo = new GoodInfo();
        testInfo.setTestList(new ArrayList<>(testSet));
        testSet.clear();
        Console.show(testInfo.getTestList());*/

//        String timeStr = "20210408162016";
//        DateTime tradeTime = DateTime.parse(timeStr, DateTimeFormat.forPattern("yyyyMMddHHmmss"));
//        Console.show(tradeTime.toString("yyyyMMdd"));
//        Console.show(tradeTime.toString("HHmmss"));

//        String str = "20200305|000000000000014|999999999999999||20200306|2347586|700000000000003|CNY|0002CNY|0002|0000|20200305|220000|100||2|98|01||20200305|220000|232324|";
//        String[] splits = str.split(Pattern.quote("|"), -1);
//        Console.show(splits.length);
//        Console.show(JSON.toJSONString(Arrays.asList(splits)));
//        Map<Integer, String> map = new HashMap<>();
//        for (int i=0; i < splits.length; i ++) {
//            map.put(i, splits[i]);
//        }
//        Console.show(JSON.toJSONString(map));

//        String str = "576852725162389504";
//        String[] strs = str.split("-");
//        for (int i = 0; i < strs.length; i ++) {
//            Console.show(strs[i]);
//        }
//        Long[] demo = {1L, 2L, 3L, 4L, 5L, 6L};
//        List<Long> list = Arrays.asList(demo);
//
//        Long[] demo2 = {1L, 7L, 3L, 9L, 10L, 6L};
//        List<Long> list2 = Arrays.asList(demo2);
//
//        list2 = list2.stream().filter(item -> !list.contains(item)).collect(Collectors.toList());
//        Console.show(JSON.toJSONString(list2));
        /*Console.show(getRandom());

        Console.show(RandomStringUtils.randomNumeric(18));

        String fileName = "急急急就.xml";
        // 文件名（不带文件扩展名）
        String mainName = fileName.substring(0, fileName.lastIndexOf("."));
        String newMainName = mainName + "_" + cn.hutool.core.date.DateTime.now().toString("yyyyMMddHHmmss");
        fileName = fileName.replace(mainName, newMainName);
        Console.show("fileName：" + fileName);

        StringBuffer stringBuffer = new StringBuffer(fileName);
        Console.show(stringBuffer.insert(stringBuffer.lastIndexOf("."), cn.hutool.core.date.DateTime.now().toString("yyyyMMddHHmmss")));*/

        getThree();

        getThreeMethod2();
    }

    private static String getRandom() {
        StringBuffer randomStr = new StringBuffer();
        long num;
        for (int i = 1; i < 10; i ++) {
            double a = Math.random();
            num = Math.round(a*25+65);
            randomStr.append((char) num);
        }
        return randomStr.toString();
    }

    public static void getThree() {
        int initValue;
        int sun = 0;
        // 先从200 开始加，得到第一个能被3整除的数
        for (int i = 200; ;i ++) {
            // 对3取余，余数为零就是被整除了，赋值到初始值，结束循环
            if (i % 3 == 0) {
                initValue = i;
                break;
            }
        }

        // 从初始值开始，每次加3，及都可被整除的。
        for (int j = initValue; j <= 400; j += 3) {
            sun += j;
            System.out.println("sun = " + sun);
        }

        System.out.println("Sun(200-400)=" + sun);
    }

    public static void getThreeMethod2() {
        int sun = 0;
        // 直接从200开始，每次对三取余，为零的即是被整除的，累加即可。但这种循环次数会更多。
        for (int i = 200; i <= 400 ;i ++) {
            if (i % 3 == 0) {
                sun += i;
                System.out.println("2sun = " + sun);
            }
        }

        System.out.println("Sun(200-400)=" + sun);
    }


    public static class GoodInfo {
        /**商品信息子序号*/
        private String goodsSubId;
        /** 商品名称 */
        private String goodsName;
        /** 收款人户名 */
        private String payeeCompanyName;
        /** 商品数量 */
        private String goodsNumber;
        /** 商品单位 */
        private String goodsUnit;
        /** 商品金额（单位：分） */
        private String goodsAmt;

        private String parameterCode;
        private String parameterValue;

        private List<Long> testList;

        public List<Long> getTestList() {
            return testList;
        }

        public void setTestList(List<Long> testList) {
            this.testList = testList;
        }

        public String getGoodsSubId() {
            return goodsSubId;
        }

        public void setGoodsSubId(String goodsSubId) {
            this.goodsSubId = goodsSubId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getPayeeCompanyName() {
            return payeeCompanyName;
        }

        public void setPayeeCompanyName(String payeeCompanyName) {
            this.payeeCompanyName = payeeCompanyName;
        }

        public String getGoodsNumber() {
            return goodsNumber;
        }

        public void setGoodsNumber(String goodsNumber) {
            this.goodsNumber = goodsNumber;
        }

        public String getGoodsUnit() {
            return goodsUnit;
        }

        public void setGoodsUnit(String goodsUnit) {
            this.goodsUnit = goodsUnit;
        }

        public String getGoodsAmt() {
            return goodsAmt;
        }

        public void setGoodsAmt(String goodsAmt) {
            this.goodsAmt = goodsAmt;
        }
    }

    /**
     * 描述 list排序
     * @author liugs
     * @date 2020/7/29 10:51:04
     */
    private static void dealListSort() {
        DateTime date = new DateTime();
        Console.show(date);

        ListDemo demo1 = new ListDemo();
        demo1.setDateTime(date.toDate());
        demo1.setIndex(1);
        ListDemo demo2 = new ListDemo();
        demo2.setDateTime(date.plusDays(1).toDate());
        demo2.setIndex(2);
        ListDemo demo3 = new ListDemo();
        demo3.setDateTime(date.plusHours(2).toDate());
        demo3.setIndex(3);

        List<ListDemo> listDemos = new ArrayList<>();
        listDemos.add(demo1);
        listDemos.add(demo2);
        listDemos.add(demo3);

        Console.show("==========before sort=========");
        for (ListDemo demo : listDemos) {
            Console.show("index：" + demo.getIndex() + ", dateTime：" + new DateTime(demo.getDateTime()).toString("yyyy年MM月dd日HH时mm分ss秒"));
        }

        /*Collections.sort(listDemos, new Comparator<ListDemo>() {
            @Override
            public int compare(ListDemo o1, ListDemo o2) {
                if (o1.getDateTime().before(o2.getDateTime())) {
                    return 1;
                } else if (o1.getDateTime().equals(o2.getDateTime())) {
                    return 0;
                }
                return -1;
            }
        });*/

        Collections.sort(listDemos, (o1, o2) -> {
            if (o1.getDateTime().before(o2.getDateTime())) {
                return 1;
            } else if (o1.getDateTime().equals(o2.getDateTime())) {
                return 0;
            }
            return -1;
        });

        Console.show("==========after sort=========");
       for (ListDemo demo : listDemos) {
           Console.show("index：" + demo.getIndex() + ", dateTime：" + new DateTime(demo.getDateTime()).toString("yyyy年MM月dd日HH时mm分ss秒"));
       }
    }

    /**
     * list 排序demo
     */
    private static class ListDemo {
        public Date dateTime;
        public Integer index;

        public Date getDateTime() {
            return dateTime;
        }

        public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }
    }

    /**
     * 描述 测试异常类
     * @author liugs
     * @date 2020/7/16 11:02:49
     */
    private static void ToolExceptionTest() {
        throw new ToolException("1000001", "错误");
    }

    /**
     * 描述 测试RSA加密
     * @author liugs
     * @date 2020/7/9 9:39:26
     */
    private static void enCode() throws Exception {
        Map<String, String> keyMap = RsaEncodeTool.generateRSAKeys();

        String publicKey = keyMap.get(ToolConstants.EncodeConstants.PUBLIC_KEY);
        String privateKey = keyMap.get(ToolConstants.EncodeConstants.PRIVATE_KEY);
        console("publicKey：" + publicKey);
        console("privateKey：" + privateKey);
        //获取一个签名Key
        String signKey = RsaEncodeTool.getRandomStringByLength(32);
        //待加密对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "tester");
        jsonObject.put("age", "100");
        jsonObject.put("sex", "man");
        String resourceStr = jsonObject.toJSONString();

//        String QQStr = "1138208740";

        console("==================公钥加密，私钥解密==================");
        //公钥加密串
        String encodeStr = EncodeExecuter.publicEncode(publicKey, resourceStr, signKey);
        console("encodeStr：" + encodeStr);
        //私钥解密
        String decodeStr = EncodeExecuter.privateDecode(privateKey, encodeStr);
        console("decodeStr：" + decodeStr);

        console("==================私钥加密，公钥解密==================");
        //私钥加密
        String encodeStrByPriKey = EncodeExecuter.privateEncode(privateKey, resourceStr, signKey);
        console("encodeStrByPriKey：" + encodeStrByPriKey);
        //公钥解密
        String decodeStrByPubKey = EncodeExecuter.publicDeCode(publicKey, encodeStrByPriKey);
        console("decodeStrByPubKey：" + decodeStrByPubKey);

        //校验签名
        console(EncodeExecuter.validateSign(decodeStr, signKey));
    }

    /**
     * 描述 金额单位转换，及保留位数
     * @return void
     * @author liugs
     * @date 2020/7/10 9:36:14
     */
    private static void dealNum() {
        BigDecimal yuan = new BigDecimal(1);
        BigDecimal wan = yuan.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_DOWN);
        BigDecimal fen = yuan.multiply(new BigDecimal(100)).divide(BigDecimal.ONE, 2, BigDecimal.ROUND_DOWN);
        console("元：" + yuan);
        console("万：" + wan);
        console("分：" +fen);
    }

    /**
     * 描述 打印
     * @param obj
     * @return void
     * @author liugs
     * @date 2020/7/9 10:31:10
     */
    private static void console(Object obj) {
        System.out.println(obj);
    }


    public static void sort() {
     /*   List<String> strs = new ArrayList<>();
        char[] chars = {'a', 'b', 'c', 'd'};
        char[] target = new char[4];
        for (int i = 0; i < 4; i ++) {
            char first = chars[i];
            for (int j = 0; j < 4; j ++) {
                char second = chars[j];
                if (first == second) {
                    continue;
                }
                for (int k = 0; k < 4; k ++) {
                    char third = chars[k];
                    if (first == third || second == third) {
                        continue;
                    }
                    for (int l = 0; l < 4; l ++) {
                        char fouth = chars[l];
                        if (first == fouth || second == fouth || third == fouth) {
                            continue;
                        }
                        target[0]=first;
                        target[1]=second;
                        target[2]=third;
                        target[3]=fouth;
                        Console.show(new String(target));
                    }
                }
            }
        }*/
    }

    private static void listToMap() {
        //将列表转为Map<key=name, value = Demo>
        List<Demo> list = new ArrayList<>();
        Demo demo = new Demo();
        demo.setName("ceshi");
        list.add(demo);

        Demo demo2 = new Demo();
        demo2.setName("ceshi2");
        list.add(demo2);

        //list 转 map
        Map<String, Demo> relMap = list.stream().collect(Collectors.toMap(Demo::getName, retRelPo -> retRelPo));

        //遍历map
        Iterator iterator = relMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Console.show(JSON.toJSONString(iterator.next()));
        }
        for (String demo1 : relMap.keySet()) {
            Console.show(relMap.get(demo1).getName());
        }
    }

    private static void listPage() {
        int pageNo = 2;
        int pageSize = 1;

        List<Demo> demos = new ArrayList<>();
        Demo demo1 = new Demo();
        demo1.setName("demo1");
        demos.add(demo1);

        Demo demo2 = new Demo();
        demo2.setName("demo2");
        demos.add(demo2);

        List<Demo> retList = new ArrayList<>();

        //获取指针 (如果第一页，就从0开始，如果是其他页码，就用 当前码*页面大小)
        int index = ( pageNo > 1 ? (pageNo - 1) * pageSize : 0);

        for (int i = 0; i < pageSize && i < demos.size() - index; i++) {
            Demo dataBo = demos.get(index + i);
            retList.add(dataBo);
        }

        int total = demos.size() % pageSize == 0 ? demos.size()/pageSize : demos.size()/pageSize +1;
        int recodeTotal = demos.size();

        Console.show("total:" + total);
        Console.show("recodeTotal:" + recodeTotal);
        Console.show("pageNo:" + pageNo);
        Console.show("pageSize:" + pageSize);
        Console.show("retList:" + JSON.toJSONString(retList));


    }

    private static class Demo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

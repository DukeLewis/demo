import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/6/21
 * @Copyright： https://github.com/DukeLewis
 */
public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        String jsonStr = getJsonStr();
        // 假设这是需要提取的属性名列表
        String[] requiredFields = {"id", "mappedMeasures.mappedMeasures.A5CAA685-637A-EC11-9C21-4CCC6ACF6129.name"};
        // 解析 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        // 获取 JSON 根节点
        JsonNode jsonNode = objectMapper.readTree(jsonStr);
        // 存放需要翻译的文本
        List<String> contents = new ArrayList<>();
        // 取出文本
        for (String requiredField : requiredFields) {
            getContents(0, jsonNode, contents, requiredField.split("\\."));
        }
        // 模拟翻译
        for (int i = 0; i < contents.size(); i++) {
            contents.set(i, "333");
        }
        System.out.println("=============================================================");
        // 将翻译结果放入 Deque 方便进行翻译值回放到 JSON
        Deque<String> deque = new ArrayDeque<>(contents);
        // 打印 翻译结果
//        deque.forEach(System.out::println);
        // 设置翻译后的文本
        for (String requiredField : requiredFields) {
            // 设置文本
            setContents(0, jsonNode, deque, requiredField.split("\\."));
        }
        System.out.println("=============================================================");
        // 打印验证
        for (String requiredField : requiredFields) {
            getContents(0, jsonNode, contents, requiredField.split("\\."));
        }
        // 将处理完成的结果转换成 JSON 字符串
//        System.out.println(objectMapper.writeValueAsString(jsonNode));

    }

    private static void getContents(int idx, JsonNode cur, List<String> contents, String[] keys) {
        // 校验
        if (keys == null || idx > keys.length || keys.length == 0 || contents == null || cur == null) {
            // 抛出异常
            return;
        }
        // 当遍历到需要处理的属性节点
        if (idx == keys.length) {
//            System.out.println(cur.asText());
            // 将文本内容存放到 List
            contents.add(cur.asText());
            return;
        }
        // 如果当前节点是数组节点
        if (cur.isArray()) {
            for (JsonNode node : cur) {
                // 向下一层遍历
                getContents(idx + 1, node.get(keys[idx]), contents, keys);
            }
        } else { // 非数组节点
            getContents(idx + 1, cur.get(keys[idx]), contents, keys);
        }
    }

    private static void setContents(int idx, JsonNode cur, Deque<String> contents, String[] keys) {
        // 校验
        if (keys == null || idx > keys.length || keys.length == 0 || contents == null || contents.isEmpty() || cur == null) {
            // 抛出异常
            return;
        }
        // 如果当前节点是所要设置节点的父节点
        if (idx == keys.length - 1) {
            try {
                if (cur.isArray()) {
                    // 遍历数组节点
                    for (JsonNode node : cur) {
                        // 获取子节点
                        JsonNode childNode = node.get(keys[idx]);
                        // 如果父节点是 Object 类型并且子节点是文本类型
                        if (node.isObject() && childNode.isTextual()) {
                            ((ObjectNode) node).put(keys[idx], contents.pollFirst());
                        }
                    }
                } else if (cur.isObject()) {
                    // 获取子节点
                    JsonNode node = cur.get(keys[idx]);
                    // 判断是否文本节点
                    if (node.isTextual()) {
                        // 修改子节点内容为翻译后的值
                        ((ObjectNode) cur).put(keys[idx], contents.pollFirst());
                    }
                }
            } catch (Exception e) {
                // 抛出异常
                throw new RuntimeException(e);
            }
            return;
        }
        // 如果是数组节点
        if (cur.isArray()) {
            for (JsonNode node : cur) {
                setContents(idx + 1, node.get(keys[idx]), contents, keys);
            }
        } else {
            setContents(idx + 1, cur.get(keys[idx]), contents, keys);
        }
    }

    private static String getJsonStr() {

        return "{\n" +

                "    \"id\": \"16152CCA-8133-4965-9398-08CC8DB11F8B\",\n" +

                "    \"name\": \"Boggle\",\n" +

                "    \"createUserId\": \"FDE19F36-6B4D-4413-BAF9-1A79C1C0344E\",\n" +

                "    \"authorName\": \"Learning Genie\",\n" +

                "    \"updateTime\": 1665628564753,\n" +

                "    \"ages\": [\n" +

                "        \"PS/PK (3-4)\"\n" +

                "    ],\n" +

                "    \"ageValues\": [\n" +

                "        \"3\"\n" +

                "    ],\n" +

                "    \"themes\": [\n" +

                "        {\n" +

                "            \"id\": \"0A94DF23-D926-9DA9-1A2A-74A3C2F0EA42\",\n" +

                "            \"name\": \"Indoor Activities\",\n" +

                "            \"custom\": false\n" +

                "        }\n" +

                "    ],\n" +

                "    \"prepareTime\": 5,\n" +

                "    \"activityTime\": 10,\n" +

                "    \"framework\": {\n" +

                "        \"id\": \"A5845474-BDCE-E411-AF66-02C72B94B99B\",\n" +

                "        \"name\": \"DRDP2015-PRESCHOOL Comprehensive view\",\n" +

                "        \"type\": \"CHILD_PORTFOLIO\",\n" +

                "        \"evaluationType\": \"PS\",\n" +

                "        \"linkUrl\": \"https://www.desiredresults.us/sites/default/files/docs/forms/DRDP2015_PSC_Comprehensive_View_Combined-20200219_ADA.pdf\",\n" +

                "        \"spanishLinkUrl\": \"https://s3.amazonaws.com/com.learning-genie.cdn/pdf/DRDP2015_Final_PS_Sp012516_2.pdf\",\n" +

                "        \"scoreTemplateId\": \"A5845474-BDCE-E411-AF66-02C72B94B99B\",\n" +

                "        \"categoryId\": \"E8506154-3B50-E411-8312-02DBFC8648CE\",\n" +

                "        \"multiType\": false,\n" +

                "        \"typeDisplay\": null,\n" +

                "        \"sortIndex\": 0,\n" +

                "        \"createAtUtc\": 1625488660970,\n" +

                "        \"updateAtUtc\": 1625488660970,\n" +

                "        \"deleted\": false,\n" +

                "        \"country\": null,\n" +

                "        \"state\": null,\n" +

                "        \"grades\": null\n" +

                "    },\n" +

                "    \"domains\": [\n" +

                "        {\n" +

                "            \"id\": \"63D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"name\": \"Language and Literacy Development\",\n" +

                "            \"abbreviation\": \"LLD\",\n" +

                "            \"core\": false,\n" +

                "            \"iconPath\": null,\n" +

                "            \"description\": \"The LLD domain assesses the progress of all children in developing foundational language and literacy skills. These skills can be demonstrated in any language and in any mode of communication. Language and literacy skills in a child’s first language form the foundation for learning English. Therefore, dual language learners may demonstrate knowledge and skills in their home language, in English, or in both languages. LLD measures should be completed for all infants, toddlers, and preschool-age children, including those who are dual language learners.\",\n" +

                "            \"isNode\": \"1\",\n" +

                "            \"sortIndex\": 15,\n" +

                "            \"applicableFramework\": \"IT|PS|K\",\n" +

                "            \"parentId\": null,\n" +

                "            \"createAtUtc\": 1627903398717,\n" +

                "            \"updateAtUtc\": 1627903398717,\n" +

                "            \"deleted\": false,\n" +

                "            \"hasCoreMeasure\": false,\n" +

                "            \"mappingAbbr\": null,\n" +

                "            \"useCondition\": null\n" +

                "        },\n" +

                "        {\n" +

                "            \"id\": \"75D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"name\": \"English Language Development\",\n" +

                "            \"abbreviation\": \"ELD\",\n" +

                "            \"core\": false,\n" +

                "            \"iconPath\": null,\n" +

                "            \"description\": \"The ELD domain assesses the progress of children who are dual language learners in learning to communicate in English. The developmental progression described in the four ELD measures is related to the child’s experiences with English, not the child’s age. Keep in mind that children acquire English in different ways and at different rates. Factors that affect English acquisition include degree of exposure to English, level of support provided in their home/first language, and individual differences such as age of exposure to English or the structure of the child’s home/first language. The ELD measures should be completed only for preschool-age children whose home language is other than English.\",\n" +

                "            \"isNode\": \"1\",\n" +

                "            \"sortIndex\": 26,\n" +

                "            \"applicableFramework\": \"PS|K\",\n" +

                "            \"parentId\": null,\n" +

                "            \"createAtUtc\": 1627903398717,\n" +

                "            \"updateAtUtc\": 1627903398717,\n" +

                "            \"deleted\": false,\n" +

                "            \"hasCoreMeasure\": false,\n" +

                "            \"mappingAbbr\": null,\n" +

                "            \"useCondition\": null\n" +

                "        }\n" +

                "    ],\n" +

                "    \"measures\": [\n" +

                "        {\n" +

                "            \"id\": \"64D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"name\": \"Understanding of Language (Receptive)\",\n" +

                "            \"abbreviation\": \"LLD1\",\n" +

                "            \"core\": false,\n" +

                "            \"iconPath\": null,\n" +

                "            \"description\": \"Child understands increasingly complex communication and language\",\n" +

                "            \"isNode\": \"0\",\n" +

                "            \"sortIndex\": 16,\n" +

                "            \"applicableFramework\": \"IT|PS|K\",\n" +

                "            \"parentId\": \"63D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"createAtUtc\": 1627903398717,\n" +

                "            \"updateAtUtc\": 1627903398717,\n" +

                "            \"deleted\": false,\n" +

                "            \"hasCoreMeasure\": false,\n" +

                "            \"mappingAbbr\": null,\n" +

                "            \"useCondition\": null\n" +

                "        },\n" +

                "        {\n" +

                "            \"id\": \"6CD0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"name\": \"Letter and Word Knowledge\",\n" +

                "            \"abbreviation\": \"LLD9\",\n" +

                "            \"core\": false,\n" +

                "            \"iconPath\": null,\n" +

                "            \"description\": \"Child shows increasing awareness of letters in the environment and their relationship to sound, including understanding that letters make up words\",\n" +

                "            \"isNode\": \"0\",\n" +

                "            \"sortIndex\": 24,\n" +

                "            \"applicableFramework\": \"PS|K\",\n" +

                "            \"parentId\": \"63D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"createAtUtc\": 1627903398717,\n" +

                "            \"updateAtUtc\": 1627903398717,\n" +

                "            \"deleted\": false,\n" +

                "            \"hasCoreMeasure\": false,\n" +

                "            \"mappingAbbr\": null,\n" +

                "            \"useCondition\": null\n" +

                "        },\n" +

                "        {\n" +

                "            \"id\": \"76D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"name\": \"Comprehension of English (Receptive English)\",\n" +

                "            \"abbreviation\": \"ELD1\",\n" +

                "            \"core\": false,\n" +

                "            \"iconPath\": null,\n" +

                "            \"description\": \"Child shows increasing progress toward fluency in understanding English\",\n" +

                "            \"isNode\": \"0\",\n" +

                "            \"sortIndex\": 27,\n" +

                "            \"applicableFramework\": \"PS|K\",\n" +

                "            \"parentId\": \"75D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"createAtUtc\": 1627903398717,\n" +

                "            \"updateAtUtc\": 1627903398717,\n" +

                "            \"deleted\": false,\n" +

                "            \"hasCoreMeasure\": false,\n" +

                "            \"mappingAbbr\": null,\n" +

                "            \"useCondition\": null\n" +

                "        },\n" +

                "        {\n" +

                "            \"id\": \"79D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"name\": \"Symbol, Letter, and Print Knowledge in English\",\n" +

                "            \"abbreviation\": \"ELD4\",\n" +

                "            \"core\": false,\n" +

                "            \"iconPath\": null,\n" +

                "            \"description\": \"Child shows an increasing understanding that print in English carries meaning\",\n" +

                "            \"isNode\": \"0\",\n" +

                "            \"sortIndex\": 30,\n" +

                "            \"applicableFramework\": \"PS|K\",\n" +

                "            \"parentId\": \"75D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"createAtUtc\": 1627903398717,\n" +

                "            \"updateAtUtc\": 1627903398717,\n" +

                "            \"deleted\": false,\n" +

                "            \"hasCoreMeasure\": false,\n" +

                "            \"mappingAbbr\": null,\n" +

                "            \"useCondition\": null\n" +

                "        }\n" +

                "    ],\n" +

                "    \"mappedMeasures\": [\n" +

                "        {\n" +

                "            \"measureId\": \"64D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"mappedMeasures\": {\n" +

                "                \"A5CAA685-637A-EC11-9C21-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"396BDACA-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"SL.K.2\",\n" +

                "                        \"abbreviation\": \"SL.K.2\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Confirm understanding of a text read aloud or information presented orally or through other media by asking and answering questions about key details and requesting clarification if something is not understood.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 52,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"5B589FC6-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642073250560,\n" +

                "                        \"updateAtUtc\": 1642073250560,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ],\n" +

                "                \"6A9D1DAC-8F93-EC11-9C22-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"3B423304-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Vocabulary 2.1\",\n" +

                "                        \"abbreviation\": \"Vocabulary 2.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"At around 48 months of age\\r\\nUnderstand and use accepted words for objects, actions, and attributes encountered frequently in both real and symbolic contexts.\\r\\nAt around 60 months of age\\r\\nUnderstand and use an increasing variety and specificity of accepted words for objects, actions, and attributes encountered in both real and symbolic contexts.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 23,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"BDC1BDC2-B593-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517051543,\n" +

                "                        \"updateAtUtc\": 1645517051543,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"3C423304-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Grammar 3.1\",\n" +

                "                        \"abbreviation\": \"Grammar 3.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"At around 48 months of age\\r\\nUnderstand and use increasingly complex and longer sentences, including sentences that combine two phrases or two to three concepts to communicate ideas.\\r\\nAt around 60 months of age\\r\\nUnderstand and use increasingly complex and longer sentences, including sentences that combine two to three phrases or three to four concepts to communicate ideas.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 24,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"BDC1BDC2-B593-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517051543,\n" +

                "                        \"updateAtUtc\": 1645517051543,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"3D423304-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Grammar 3.2\",\n" +

                "                        \"abbreviation\": \"Grammar 3.2\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"At around 48 months of age\\r\\nUnderstand and typically use age-appropriate grammar, including accepted word forms, such as subject-verb agreement, progressive tense, regular past tense, regular plurals, pronouns, and possessives.\\r\\nAt around 60 months of age\\r\\nUnderstand and typically use age-appropriate grammar, including accepted word forms, such as subject-verb agreement, progressive tense, regular and irregular past tense, regular and irregular plurals, pronouns, and possessives.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 25,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"BDC1BDC2-B593-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517051543,\n" +

                "                        \"updateAtUtc\": 1645517051543,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ]\n" +

                "            }\n" +

                "        },\n" +

                "        {\n" +

                "            \"measureId\": \"6CD0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"mappedMeasures\": {\n" +

                "                \"A5CAA685-637A-EC11-9C21-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1\",\n" +

                "                        \"abbreviation\": \"RF.K.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Demonstrate understanding of the organization and basic features of print.\",\n" +

                "                        \"isNode\": \"1\",\n" +

                "                        \"sortIndex\": 23,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"53A10685-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642069704943,\n" +

                "                        \"updateAtUtc\": 1642069704943,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"444E4770-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1a\",\n" +

                "                        \"abbreviation\": \"RF.K.1a\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Follow words from left to right, top to bottom, and page by page.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 24,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070092117,\n" +

                "                        \"updateAtUtc\": 1642070092117,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"8982D7A3-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1b\",\n" +

                "                        \"abbreviation\": \"RF.K.1b\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Recognize that spoken words are represented in written language by specific sequences of letters.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 25,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070178610,\n" +

                "                        \"updateAtUtc\": 1642070178610,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"F79F32CB-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1c\",\n" +

                "                        \"abbreviation\": \"RF.K.1c\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Understand that words are separated by spaces in print.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 26,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070244653,\n" +

                "                        \"updateAtUtc\": 1642070244653,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"4FC882DF-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1d\",\n" +

                "                        \"abbreviation\": \"RF.K.1d\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Recognize and name all upper- and lowercase letters of the alphabet.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 27,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070280483,\n" +

                "                        \"updateAtUtc\": 1642070280483,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"2D73BBB4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.2\",\n" +

                "                        \"abbreviation\": \"RF.K.2\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Demonstrate understanding of spoken words, syllables, and sounds (phonemes).\",\n" +

                "                        \"isNode\": \"1\",\n" +

                "                        \"sortIndex\": 28,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"53A10685-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642069777463,\n" +

                "                        \"updateAtUtc\": 1642069777463,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3\",\n" +

                "                        \"abbreviation\": \"RF.K.3\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Know and apply grade-level phonics and word analysis skills in decoding words.\",\n" +

                "                        \"isNode\": \"1\",\n" +

                "                        \"sortIndex\": 34,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"53A10685-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642069857820,\n" +

                "                        \"updateAtUtc\": 1642069857820,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"0D81F0B6-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3a\",\n" +

                "                        \"abbreviation\": \"RF.K.3a\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Demonstrate basic knowledge of one-to-one letter-sound correspondences by producing the primary sound or many of the most frequent sounds for each consonant.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 35,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070640163,\n" +

                "                        \"updateAtUtc\": 1642070640163,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"FDD651EA-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3b\",\n" +

                "                        \"abbreviation\": \"RF.K.3b\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Associate the long and short sounds with common spellings (graphemes) for the five major vowels.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 36,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070726363,\n" +

                "                        \"updateAtUtc\": 1642070726363,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"D7DB21FA-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3c\",\n" +

                "                        \"abbreviation\": \"RF.K.3c\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Read common high-frequency words by sight (e.g., the, of, to, you, she, my, is, are, do, does).\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 37,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070755307,\n" +

                "                        \"updateAtUtc\": 1642070755307,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"91CF1E06-5E74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3d\",\n" +

                "                        \"abbreviation\": \"RF.K.3d\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Distinguish between similarly spelled words by identifying the sounds of the letters that differ.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 38,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070773007,\n" +

                "                        \"updateAtUtc\": 1642070773007,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ],\n" +

                "                \"6A9D1DAC-8F93-EC11-9C22-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"53D2C3A1-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Alphabetics and Word/Print Recognition 3.1\",\n" +

                "                        \"abbreviation\": \"Alphabetics and Word/Print Recognition 3.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"At around 48 months of age\\r\\nRecognize the first letter of own name.\\r\\nAt around 60 months of age\\r\\nRecognize own name or other common words in print.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 31,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"FF6C8115-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517315893,\n" +

                "                        \"updateAtUtc\": 1645517315893,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"54D2C3A1-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Alphabetics and Word/Print Recognition 3.2\",\n" +

                "                        \"abbreviation\": \"Alphabetics and Word/Print Recognition 3.2\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"At around 48 months of age\\r\\nMatch some letter names to their printed form.\\r\\nAt around 60 months of age\\r\\nMatch more than half of uppercase letter names and more than half of lowercase letter names to their printed form.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 32,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"FF6C8115-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517315893,\n" +

                "                        \"updateAtUtc\": 1645517315893,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"55D2C3A1-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Alphabetics and Word/Print Recognition 3.3\",\n" +

                "                        \"abbreviation\": \"Alphabetics and Word/Print Recognition 3.3\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"At around 60 months of age\\r\\nBegin to recognize that letters have sounds.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 33,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"FF6C8115-B693-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517315893,\n" +

                "                        \"updateAtUtc\": 1645517315893,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ]\n" +

                "            }\n" +

                "        },\n" +

                "        {\n" +

                "            \"measureId\": \"76D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"mappedMeasures\": {\n" +

                "                \"A5CAA685-637A-EC11-9C21-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"4E8F4259-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"SL.K.1\",\n" +

                "                        \"abbreviation\": \"SL.K.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Participate in collaborative conversations with diverse partners about kindergarten topics and texts with peers and adults in small and larger groups.\",\n" +

                "                        \"isNode\": \"1\",\n" +

                "                        \"sortIndex\": 49,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"5B589FC6-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642073059973,\n" +

                "                        \"updateAtUtc\": 1642073059973,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"887B806F-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"SL.K.1a\",\n" +

                "                        \"abbreviation\": \"SL.K.1a\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Follow agreed-upon rules for discussions (e.g., listening to others and taking turns speaking about the topics and texts under discussion).\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 50,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"4E8F4259-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642073104807,\n" +

                "                        \"updateAtUtc\": 1642073104807,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"57074092-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"SL.K.1b\",\n" +

                "                        \"abbreviation\": \"SL.K.1b\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Continue a conversation through multiple exchanges.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 51,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"4E8F4259-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642073155587,\n" +

                "                        \"updateAtUtc\": 1642073155587,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"396BDACA-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"SL.K.2\",\n" +

                "                        \"abbreviation\": \"SL.K.2\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Confirm understanding of a text read aloud or information presented orally or through other media by asking and answering questions about key details and requesting clarification if something is not understood.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 52,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"5B589FC6-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642073250560,\n" +

                "                        \"updateAtUtc\": 1642073250560,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"97E3BCD8-6374-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"SL.K.3\",\n" +

                "                        \"abbreviation\": \"SL.K.3\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Ask and answer questions in order to seek help, get information, or clarify something that is not understood.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 53,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"5B589FC6-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642073282323,\n" +

                "                        \"updateAtUtc\": 1642073282323,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ],\n" +

                "                \"6A9D1DAC-8F93-EC11-9C22-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"D9A9F88F-B793-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Children listen with understanding 1.1\",\n" +

                "                        \"abbreviation\": \"Children listen with understanding 1.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Beginning\\r\\nAttend to English oral language in both real and pretend activity, relying on intonation, facial expressions, or the gestures of the speaker.\\r\\n Middle\\r\\nDemonstrate understanding of words in English for objects and actions as well as phrases encountered frequently in both real and pretend activity.\\r\\nLater\\r\\nBegin to demonstrate an understanding of a larger set of words in English (for objects and actions, personal pronouns, and possessives) in both real and pretend activity.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 43,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"2D87A94B-B793-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517715537,\n" +

                "                        \"updateAtUtc\": 1645517715537,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"DAA9F88F-B793-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Children listen with understanding 1.3\",\n" +

                "                        \"abbreviation\": \"Children listen with understanding 1.3\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Beginning\\r\\nDemonstrate an understanding of words related to basic and advanced concepts in the home language that are appropriate for the age (as reported by parents, teachers, assistants, or others, with the assistance of an interpreter if necessary).\\r\\nMiddle\\r\\nBegin to demonstrate an understanding of words in English related to basic concepts.\\r\\nLater\\r\\nDemonstrate an understanding of words in English related to more advanced concepts.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 44,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"2D87A94B-B793-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645517715537,\n" +

                "                        \"updateAtUtc\": 1645517715537,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ]\n" +

                "            }\n" +

                "        },\n" +

                "        {\n" +

                "            \"measureId\": \"79D0392C-55D3-EB11-9C19-4CCC6ACF6129\",\n" +

                "            \"mappedMeasures\": {\n" +

                "                \"A5CAA685-637A-EC11-9C21-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1\",\n" +

                "                        \"abbreviation\": \"RF.K.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Demonstrate understanding of the organization and basic features of print.\",\n" +

                "                        \"isNode\": \"1\",\n" +

                "                        \"sortIndex\": 23,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"53A10685-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642069704943,\n" +

                "                        \"updateAtUtc\": 1642069704943,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"444E4770-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1a\",\n" +

                "                        \"abbreviation\": \"RF.K.1a\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Follow words from left to right, top to bottom, and page by page.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 24,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070092117,\n" +

                "                        \"updateAtUtc\": 1642070092117,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"8982D7A3-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1b\",\n" +

                "                        \"abbreviation\": \"RF.K.1b\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Recognize that spoken words are represented in written language by specific sequences of letters.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 25,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070178610,\n" +

                "                        \"updateAtUtc\": 1642070178610,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"F79F32CB-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1c\",\n" +


                "                        \"abbreviation\": \"RF.K.1c\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Understand that words are separated by spaces in print.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 26,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070244653,\n" +

                "                        \"updateAtUtc\": 1642070244653,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"4FC882DF-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.1d\",\n" +

                "                        \"abbreviation\": \"RF.K.1d\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Recognize and name all upper- and lowercase letters of the alphabet.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 27,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"63658189-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070280483,\n" +

                "                        \"updateAtUtc\": 1642070280483,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"2D73BBB4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.2\",\n" +

                "                        \"abbreviation\": \"RF.K.2\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Demonstrate understanding of spoken words, syllables, and sounds (phonemes).\",\n" +

                "                        \"isNode\": \"1\",\n" +

                "                        \"sortIndex\": 28,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"53A10685-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642069777463,\n" +

                "                        \"updateAtUtc\": 1642069777463,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"52B42807-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.2a\",\n" +

                "                        \"abbreviation\": \"RF.K.2a\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Recognize and produce rhyming words.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 29,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"2D73BBB4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070345253,\n" +

                "                        \"updateAtUtc\": 1642070345253,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"623F8E24-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.2b\",\n" +

                "                        \"abbreviation\": \"RF.K.2b\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Count, pronounce, blend, and segment syllables in spoken words.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 30,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"2D73BBB4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070394583,\n" +

                "                        \"updateAtUtc\": 1642070394583,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"0D185947-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.2c\",\n" +

                "                        \"abbreviation\": \"RF.K.2c\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Blend and segment onsets and rimes of single-syllable spoken words.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 31,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"2D73BBB4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070452950,\n" +

                "                        \"updateAtUtc\": 1642070452950,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"0ADAE682-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.2d\",\n" +

                "                        \"abbreviation\": \"RF.K.2d\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"\\r\\n\\r\\nIsolate and pronounce the initial, medial vowel, and final sounds (phonemes) in three-phoneme (consonant-vowel-consonant, or CVC) words. (This does not include CVCs ending with /l/, /r/, or /x/.)\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 32,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"2D73BBB4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070552860,\n" +

                "                        \"updateAtUtc\": 1642070552860,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"23904994-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.2e\",\n" +

                "                        \"abbreviation\": \"RF.K.2e\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Add or substitute individual sounds (phonemes) in simple, one-syllable words to make new words.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 33,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"2D73BBB4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070583293,\n" +

                "                        \"updateAtUtc\": 1642070583293,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3\",\n" +

                "                        \"abbreviation\": \"RF.K.3\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Know and apply grade-level phonics and word analysis skills in decoding words.\",\n" +

                "                        \"isNode\": \"1\",\n" +

                "                        \"sortIndex\": 34,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"53A10685-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642069857820,\n" +

                "                        \"updateAtUtc\": 1642069857820,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"0D81F0B6-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3a\",\n" +

                "                        \"abbreviation\": \"RF.K.3a\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Demonstrate basic knowledge of one-to-one letter-sound correspondences by producing the primary sound or many of the most frequent sounds for each consonant.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 35,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070640163,\n" +

                "                        \"updateAtUtc\": 1642070640163,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"FDD651EA-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3b\",\n" +

                "                        \"abbreviation\": \"RF.K.3b\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Associate the long and short sounds with common spellings (graphemes) for the five major vowels.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 36,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070726363,\n" +

                "                        \"updateAtUtc\": 1642070726363,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"D7DB21FA-5D74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3c\",\n" +

                "                        \"abbreviation\": \"RF.K.3c\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Read common high-frequency words by sight (e.g., the, of, to, you, she, my, is, are, do, does).\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 37,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070755307,\n" +

                "                        \"updateAtUtc\": 1642070755307,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"91CF1E06-5E74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.3d\",\n" +

                "                        \"abbreviation\": \"RF.K.3d\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Distinguish between similarly spelled words by identifying the sounds of the letters that differ.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 38,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"9526A0E4-5B74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642070773007,\n" +

                "                        \"updateAtUtc\": 1642070773007,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"DE045B13-5C74-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"name\": \"RF.K.4\",\n" +

                "                        \"abbreviation\": \"RF.K.4\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Read emergent-reader texts with purpose and understanding.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 39,\n" +

                "                        \"applicableFramework\": \"CCSS\",\n" +

                "                        \"parentId\": \"53A10685-5674-EC11-9C21-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1642069936220,\n" +

                "                        \"updateAtUtc\": 1642069936220,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ],\n" +

                "                \"6A9D1DAC-8F93-EC11-9C22-4CCC6ACF6129\": [\n" +

                "                    {\n" +

                "                        \"id\": \"571E938A-B893-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Children demonstrate awareness that print carries meaning 4.1\",\n" +

                "                        \"abbreviation\": \"Children demonstrate awareness that print carries meaning 4.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Beginning\\r\\nBegin to recognize that symbols in the environment (classroom, community, or home) carry a consistent meaning in the home language or in English.\\r\\nMiddle\\r\\nRecognize in the environment (classroom, community, or home) some familiar symbols, words, and print labels in the home language or in English.\\r\\nLater\\r\\nRecognize in the environment (classroom, community, or home) an increasing number of familiar symbols, words, and print labels in English.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 58,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"7C3DC42E-B893-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645518135980,\n" +

                "                        \"updateAtUtc\": 1645518135980,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    },\n" +

                "                    {\n" +

                "                        \"id\": \"581E938A-B893-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"name\": \"Children demonstrate progress in their knowledge of the alphabet in English 5.1\",\n" +

                "                        \"abbreviation\": \"Children demonstrate progress in their knowledge of the alphabet in English 5.1\",\n" +

                "                        \"core\": false,\n" +

                "                        \"iconPath\": null,\n" +

                "                        \"description\": \"Beginning\\r\\nInteract with material representing the letters of the English alphabet.\\r\\nMiddle\\r\\nBegin to talk about the letters of the English alphabet while playing and interacting with them; may code-switch (use the home language and English).\\r\\nLater\\r\\nBegin to demonstrate understanding that the letters of the English alphabet are symbols used to make words.\",\n" +

                "                        \"isNode\": \"0\",\n" +

                "                        \"sortIndex\": 59,\n" +

                "                        \"applicableFramework\": \"CA-PLF\",\n" +

                "                        \"parentId\": \"7C3DC42E-B893-EC11-9C22-4CCC6ACF6129\",\n" +

                "                        \"createAtUtc\": 1645518135980,\n" +

                "                        \"updateAtUtc\": 1645518135980,\n" +

                "                        \"deleted\": false,\n" +

                "                        \"hasCoreMeasure\": false,\n" +

                "                        \"mappingAbbr\": null,\n" +

                "                        \"useCondition\": null\n" +

                "                    }\n" +

                "                ]\n" +

                "            }\n" +

                "        }\n" +

                "    ],\n" +

                "    \"objectives\": [\n" +

                "        \"1-Select letters that correspond to the card they have in slot to match them.\\n2-Identifies some letters by name as they match the 3D letter cubes to the paper letter in English and possibly in their own language.\\n3-Sorts out the letters to the corresponding letter card after an adult says in English to match the corresponding card\\n4-Names in English the name of the letters/objects (objects that start wit a certain letter)  they are matching.\"\n" +

                "    ],\n" +

                "    \"materials\": {\n" +

                "        \"descriptions\": [\n" +

                "            \"First \\nBoggle game with\\n3D cube letters\\n3-4 letter words with picture cards\\n\\nSecond, If a child is not able to recognize the letter to the card picture, have the child place the cube letter to the card so they can visually see the letter and match to the corresponding 3D letter.\\nFor children who might be beyond encourage them to say the object they see in the card and try to place the letter that makes the begging sound of the card object.\"\n" +

                "        ],\n" +

                "        \"mediaId\": null,\n" +

                "        \"media\": null,\n" +

                "        \"externalMediaUrlId\": null,\n" +

                "        \"externalMediaUrl\": null,\n" +

                "        \"externalMediaCoverUrl\": null,\n" +

                "        \"externalMediaModel\": null,\n" +

                "        \"attachmentMediaIds\": [],\n" +

                "        \"attachmentMedias\": []\n" +

                "    },\n" +

                "    \"steps\": [\n" +

                "        {\n" +

                "            \"ageGroupName\": \"PS/PK (3-4)\",\n" +

                "            \"ageGroupValue\": \"3\",\n" +

                "            \"content\": \"<p><strong>1.How will you introduce </strong></p><p>Introduce to the children the cube letters and have them feel them.</p><p>Introduced the word cards with the 3-letter word on one side and 4-letter word on the other side</p><p>Model matching one of the letters to one of the cards for visual cue.</p><p><br></p><p><strong>2.How will you assist</strong></p><p>The adult will be present to help repeat or hear the letter the child picks up and help them recognize and place it below the corresponding letter.</p><p>The adult will be present to help repeat or hear the sound of the picture card the child selects and help them recognize the letters if they need it.</p><p><br></p><p><strong>3.How will you end</strong></p><p>Leave the game as an open activity for anyone who wants to try</p><p>Ask reflective questions to end activity such a</p><p>What's the first letter of your name?</p><p>What us the first letter of _____?</p><p>What sound does it make?</p><p>Where else could you hear that letter sound?</p>\",\n" +

                "            \"groupId\": null,\n" +

                "            \"universalDesignForLearning\": null,\n" +

                "            \"universalDesignForLearningGroup\": null,\n" +

                "            \"universalDesignForLearningGroupModels\": null,\n" +

                "            \"iepModels\": null,\n" +

                "            \"eldModels\": null,\n" +

                "            \"udlGroups\": [],\n" +

                "            \"teacherAdaptationGroups\": [],\n" +

                "            \"culturallyResponsiveInstruction\": null,\n" +

                "            \"culturallyResponsiveInstructionGroup\": null,\n" +

                "            \"culturallyResponsiveInstructionGroupModels\": null,\n" +

                "            \"homeActivity\": null,\n" +

                "            \"lessonStepGuides\": [],\n" +

                "            \"mediaId\": null,\n" +

                "            \"media\": null,\n" +

                "            \"lessonSource\": null,\n" +

                "            \"lessonClrAndSources\": null,\n" +

                "            \"showAtHome\": false,\n" +

                "            \"externalMediaUrlId\": null,\n" +

                "            \"externalMediaUrl\": null,\n" +

                "            \"externalMediaModel\": null\n" +

                "        }\n" +

                "    ],\n" +

                "    \"coverMedias\": [\n" +

                "        {\n" +

                "            \"id\": \"ADC48F51-5C2E-4DE8-A662-DF400BAE47B8\",\n" +

                "            \"url\": \"https://s3.amazonaws.com//com.learning-genie.prod.us/294452cc-042d-4e00-b07d-f0320b72c305.png\",\n" +

                "            \"sourceFileName\": \"Screen Shot 2022-04-25 at 4.06.27 PM.png\",\n" +

                "            \"size\": 487739,\n" +

                "            \"fileType\": \"png\",\n" +

                "            \"coverUrl\": null\n" +

                "        }\n" +

                "    ],\n" +

                "    \"books\": [],\n" +

                "    \"videoBooks\": [],\n" +

                "    \"attachmentMedias\": [],\n" +

                "    \"readCount\": 1046,\n" +

                "    \"likeCount\": 12,\n" +

                "    \"liked\": false,\n" +

                "    \"favoriteCount\": 8,\n" +

                "    \"favorite\": false,\n" +

                "    \"type\": \"PERSONAL\",\n" +

                "    \"status\": \"PUBLISHED\",\n" +

                "    \"dlls\": [],\n" +

                "    \"coverExternalMediaUrl\": null,\n" +

                "    \"coverExternalMediaUrlId\": null,\n" +

                "    \"keyVocabularyWords\": null,\n" +

                "    \"generateStatus\": null,\n" +

                "    \"universalDesignForLearning\": null,\n" +

                "    \"culturallyResponsiveInstruction\": null,\n" +

                "    \"homeActivities\": null,\n" +

                "    \"externalMediaModel\": null,\n" +

                "    \"showCoreMeasureOpen\": false,\n" +

                "    \"showDLLOpen\": false,\n" +

                "    \"isAdaptedLesson\": false\n" +

                "}".replaceAll("\n", "");
    }
}

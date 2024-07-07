import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/7/6
 * @Copyright： https://github.com/DukeLewis
 */
public class TestJsoup {
    public static void main(String[] args) {
        Document document = Jsoup.parse("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>\n" +
                "  <p><a href=\"https://www.baidu.com\">321</a></p>\n" +
                "</div>\n" +
                "<span>hello</span>\n" +
                "<div>\n" +
                "  <div>\n" +
                "    <div>\n" +
                "      <div>\n" +
                "        <p><a href=\"https://www.baidu.com\">123</a></p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");
//        System.out.println(document.text());
        Element body = document.body();
        List<Node> nodeList = body.childNodes();
        System.out.println(nodeList.get(1).nodeName());
        System.out.println(nodeList.get(1));
//        System.out.println(body.tagName());
    }
}

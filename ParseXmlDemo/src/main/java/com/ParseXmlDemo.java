package com;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

/**
 * 使用DOM4J解析XML文档
 *
 * DOM(Document Object Model)文档对象模式下那个
 * DOM解析模式为先将XML文档按照其标签包含的结构转换为一个树状结构数据
 * 使得我们在获取xml文档内容改为遍历树结构完成。
 */
public class ParseXmlDemo {
    public static void main(String[] args) {
        /*
            解析XML的大致步骤
            1:创建SAXReader
            2:使用SAXReader读取XML文档并产生Document对象
                这一步的作用就是DOM解析模式中现将XML文档读取并构建整棵树结构的过程
            3:通过Document对象获取根元素(就是XML中的根标签内容)
            4:从根元素开始逐级获取子元素,以达到解析XML文档内容的目的
         */
        //1
        SAXReader reader = new SAXReader();
        try {
            //2
            Document doc = reader.read("./emplist.xml");
//            reader.read(new File("./emplist.xml"));
//            reader.read(new FileInputStream(new File("emplist.xml")));
            //3
            //document的方法:
            /*
                Document对象提供了获取根元素的方法:
                Element getRootElement()

                Element的每一个实例用于表示XML文档上的一个元素(一对标签)
                其提供了获取XML元素信息的相关方法,常用的有:
                    Attribute attribute("xxx");
                    获取该元素的属性:
                        属性有方法:
                            String getName();String getValue();
                            获取属性名;                获取value

                    String getName()
                    获取元素名(标签名)

                    String getText()
                    获取文本(开始与结束标签之间的文本信息)

                    Element element(String name)
                    获取当前元素中指定名字的子元素

                    List elements()
                    获取当前元素下的所有子元素

                    List elements(String name)
                    获取当前元素下所有的给定的名字的同名子元素

                    String elementText(String name)
                    获取当前元素指定名字的子元素的正文内容
                    String s = e.elementText("xxx");
                    等同于
                    String s = e.element("xxx").getText();
             */
            Element root = doc.getRootElement();
            System.out.println("根目录"+root.getName());
            List<Element> dept = root.elements();
//            List<Element> list = dept.get(0).elements("emp");
//            System.out.println(list.size());
            //遍历每一个《emp》标签来获取员工信息
            for (Element deptEle : dept) {
                List<Element> emp = deptEle.elements("emp");
                for (Element empEle : emp) {
                    //获取工号:
                    //empEle.attributeValue("xxx");=
                    //empEle.attribute("xxx").getValue();
                    int id = Integer.parseInt(empEle.attributeValue("id"));
                    System.out.print("id:"+id+" ");
                    //获取名字
                    Element nameEle = empEle.element("name");
                    String ename = nameEle.getText();
                    System.out.print("name:"+ename+" ");
                    //获取年龄
                    Element ageEle = empEle.element("age");
                    int age = Integer.parseInt(ageEle.getText());
                    System.out.print("age:"+age+" ");
                    //获取性别
                    String sex = empEle.elementText("gender");
                    System.out.print("sex:"+sex+" ");
                    //获取工资
                    String salary = empEle.elementText("salary");
                    System.out.print("salary:"+salary+" ");

                    System.out.println();
                }
            }

            System.out.println("------------------------");
            for (Element e : dept) {
                List<Element> emp = e.elements();
                for (Element em : emp) {
                    List<Element> proper = em.elements();
                    for (Element el : proper) {
                        System.out.print(el.getName()+":"+el.getText()+" ");
                    }
                    System.out.println();
                }
            }
            System.out.println("--------------------");
            System.out.println(root.element("dept").element("emp").elements());

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

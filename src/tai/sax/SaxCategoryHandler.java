package tai.sax;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.ArrayList;

import tai.entity.Category;

public class SaxCategoryHandler extends DefaultHandler {
    public ArrayList<Category> listCategory;
    private Category cat; 

    public SaxCategoryHandler() {
        listCategory = new ArrayList<>();
    }

    @Override
    public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {    
        if (elementName.equalsIgnoreCase("category")) {
            cat = new Category(
                    attributes.getValue("id")
                , attributes.getValue("name")
                , attributes.getValue("image-overview")
            );
        }
    }

    @Override
    public void endElement(String str1, String str2, String element) throws SAXException {    
        if (element.equalsIgnoreCase("category")) {
            listCategory.add(cat);
        }
    }


}
package oop.BookList;
import org.junit.Before;
import org.junit.Test;
import javax.swing.table.DefaultTableModel;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class App_Test {
    private App app;
    private DefaultTableModel model;

    @Before
    public void setUp() {
        app = new App();
        model = new DefaultTableModel(new String[]{"Column1", "Column2"}, 0);
    }

    @Test
    public void testGetTableData() {
        model.addRow(new Object[]{"Data1", "Data2"});
        String[][] data = app.getTableData(model);
        
        assertEquals(1, data.length);
        assertEquals("Data1", data[0][0]);
        assertEquals("Data2", data[0][1]);
    }

    @Test
    public void testLoadTableData() throws Exception {
        // Создание XML-документа для тестирования
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("data");
        doc.appendChild(root);
        
        Element teams = doc.createElement("teams");
        Element team = doc.createElement("row");
        Element column1 = doc.createElement("Column1");
        column1.setTextContent("Team1");
        Element column2 = doc.createElement("Column2");
        column2.setTextContent("Country1");
        
        team.appendChild(column1);
        team.appendChild(column2);
        teams.appendChild(team);
        root.appendChild(teams);
        
        app.loadTableData(doc, "teams", model);
        
        assertEquals(1, model.getRowCount());
        assertEquals("Team1", model.getValueAt(0, 0));
        assertEquals("Country1", model.getValueAt(0, 1));
    }

    @Test
    public void testSaveTableData() throws Exception {
        model.addRow(new Object[]{"Data1", "Data2"});
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("data");
        doc.appendChild(root);
        
        app.saveTableData(doc, root, "testTable", model);
        
        NodeList nodeList = root.getElementsByTagName("testTable");
        assertEquals(1, nodeList.getLength());
        NodeList rows = nodeList.item(0).getChildNodes();
        assertEquals(1, rows.getLength());
        assertEquals("Data1", ((Element) rows.item(0)).getElementsByTagName("Column1").item(0).getTextContent());
        assertEquals("Data2", ((Element) rows.item(0)).getElementsByTagName("Column2").item(0).getTextContent());
    }
}
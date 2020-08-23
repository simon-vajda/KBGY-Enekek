package hu.pimpi.enekek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LyricsActivity extends AppCompatActivity {

    ArrayList<Verse> verses;
    LyricsAdapter adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        verses = new ArrayList<>();

        try {
            String filename = getIntent().getExtras().getString("filename");
            loadData(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.lyrics_recycler_view);
        recyclerView.addItemDecoration(new LyricsMarginDecorator());
        Log.i("dimensions", getResources().getDisplayMetrics().xdpi + "x" + getResources().getDisplayMetrics().ydpi);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        adapter = new LyricsAdapter(this, verses);
        recyclerView.setAdapter(adapter);
    }

    void loadData(String filename) throws IOException, ParserConfigurationException, SAXException {
        InputStream is = getAssets().open("songs/" + filename);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);

        setTitle(((Element) doc.getElementsByTagName("title").item(0)).getTextContent());

        NodeList verseNodes = doc.getElementsByTagName("verse");

        for(int i = 0; i < verseNodes.getLength(); i++) {
            Element verseElement = (Element) verseNodes.item(i);
            String header = verseElement.getAttribute("name");
            Verse.Type type;

            if(header.charAt(0) == 'b') type = Verse.Type.BRIDGE;
            else if(header.charAt(0) == 'c') type = Verse.Type.CHORUS;
            else type = Verse.Type.VERSE;

            NodeList lineNodes = verseElement.getElementsByTagName("lines");
            NodeList lineSections = lineNodes.item(0).getChildNodes();

            String text = "";

            for(int j = 0; j < lineSections.getLength(); j++) {
                String section = lineSections.item(j).getTextContent();
                if(section.length() > 0) {
                    if(text.length() > 0) text += "\n";
                    text += section;
                }
            }

            if(!text.equals("Blank Verse"))
                verses.add(new Verse(type, text));
        }
    }
}
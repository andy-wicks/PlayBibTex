/*
 * This program is made available under the Creative Commons License.
 * No liability to hardware, software or data will be accepted by the author.
 * The program may be changed and distributed freely, as long as the name of
 * the original author, Andy Wicks, remains wherever placed.
 */
package playbibtex;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/**
 *
 * @author Andy Wicks
 * @dateStarted 13-May-2017
 * @lastUpdated 13-May-2017
 * @purpose
 *
 */
public class PlayBibtex extends JFrame implements ActionListener, KeyListener {

    CommonCode cc = new CommonCode();

    Font fnt = new Font("Georgia", Font.PLAIN, 36);
    JPanel pnl = new JPanel(new BorderLayout());
    JEditorPane lft = new JEditorPane();
    JEditorPane cen = new JEditorPane();
    JEditorPane rht = new JEditorPane();
    JScrollPane left = new JScrollPane();
    JScrollPane cent = new JScrollPane();
    JScrollPane rght = new JScrollPane();
    JLabel cenLbl = new JLabel("");

    String title = "PlayBibtex - Andy Wicks - ver. 1.001 - 2017";
    String bibtexName = "";

    // Place the global data structures here, e.g. :-
    ArrayList<String> bibtex = new ArrayList<>();
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> pdfNames = new ArrayList<>();
    //ArrayList<className> cls = new ArrayList<>();

    String bibDir = cc.appDir + "\\..\\Bibtex\\";  // Path to the local BibTeX directory
    String libDir = cc.appDir + "\\..\\Literature Repository\\";  // Path to the local literature repository directory

    public static void main(String[] args) {
        PlayBibtex prg = new PlayBibtex();
    }

    public PlayBibtex() {
        model();
        view();
        controller();
    }

    private void model() {
        bibtex = readBibtex("Bibtex.bib");
        readFileList();
        displayTypes();
        displayBibtex(new ArrayList<String>());
        displayLitRep();
    }

    private void view() {
        try {
            Image image = new ImageIcon("icons/bibtex.png").getImage();
            this.setIconImage(image);
        } catch (Exception e) {
            System.out.println("Appilcation icon not found");
        }

        JMenuBar menuBar;
        JMenu fyle, btex, librep;

        JToolBar toolBar = new JToolBar();

        // Setting up the MenuBar
        menuBar = new JMenuBar();
        fyle = new JMenu("File");
        fyle.setToolTipText("File tasks");
        fyle.setFont(fnt);

        JMenuItem mnuItem = null;

        mnuItem = makeMenuItem("Create", "Create", "Create a new Bibtex.bib", fnt);
        fyle.add(mnuItem);

        fyle.addSeparator();

        mnuItem = makeMenuItem("Search", "Search", "Search Bibtex.bib", fnt);
        fyle.add(mnuItem);

        menuBar.add(fyle);

        btex = new JMenu("BibTeX");
        btex.setToolTipText("BibTeX tasks");
        btex.setFont(fnt);

        mnuItem = makeMenuItem("Search", "Search", "Search for text in BibTeX library", fnt);
        btex.add(mnuItem);

        mnuItem = makeMenuItem("Create", "Create", "Create a new Bibtex.bib", fnt);
        btex.add(mnuItem);

        mnuItem = makeMenuItem("Save this BibTeX", "SaveBibtex", "Save new BibTex from Text area", fnt);
        btex.add(mnuItem);

        btex.addSeparator();

        mnuItem = makeMenuItem("Open Google Scholar", "GScholar", "Open Google Scholar in a browser", fnt);
        btex.add(mnuItem);

        mnuItem = makeMenuItem("Open MS Academic", "MSAcademic", "MS Academic in a browser", fnt);
        btex.add(mnuItem);

        btex.addSeparator();

        mnuItem = makeMenuItem("Sync BibTeX Lib", "Sync", "Sync this library with another", fnt);
        btex.add(mnuItem);

        menuBar.add(btex);

        librep = new JMenu("Literature Repository");
        librep.setToolTipText("Literature repository tasks");
        librep.setFont(fnt);

        mnuItem = makeMenuItem("Search", "LibSearch", "Search file names for text in the literature repository", fnt);
        librep.add(mnuItem);

        mnuItem = makeMenuItem("Sync Repositories", "LibSync", "Sync this repository with another", fnt);
        librep.add(mnuItem);

        menuBar.add(librep);
        //librep.addSeparator();

        mnuItem = makeMenuItem("Exit", "Exit", "Close this program", fnt);
        menuBar.add(mnuItem);

        setJMenuBar(menuBar);

        setLayout(new BorderLayout());

        // Use setSize and setLocationRelative for a specific 
        // size of window or setExtendedState to fill the screen.
        //
        //setSize(500, 500);
        //setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        left = new JScrollPane(lft, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        left.setPreferredSize(new Dimension(260, 0));
        add(left, BorderLayout.WEST);

        cent = new JScrollPane(cen, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(cent, BorderLayout.CENTER);

        rght = new JScrollPane(rht, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        rght.setPreferredSize(new Dimension(560, 0));
        add(rght, BorderLayout.EAST);

        JPanel bot = new JPanel();
        bot.setLayout(new FlowLayout());
        JLabel copy = new JLabel("<html><body>Copyright (c) Andy Wicks 2017</body></html>");
        copy.setFont(fnt);
        bot.add(copy);
        add(bot, BorderLayout.SOUTH);

        setVisible(true);

        // Setting up the ButtonBar
        JButton button = null;
        button = makeNavigationButton("List", "Create",
                "Create a full Bibtex.bib",
                "Make Bibtex.bib");
        toolBar.add(button);
        button = makeNavigationButton("Save", "SaveBibtex",
                "Create a full Bibtex.bib",
                "Make Bibtex.bib");
        toolBar.add(button);
        button = makeNavigationButton("Search", "Search",
                "Find a string in Bibtex.bib",
                "Find text");
        toolBar.add(button);
        toolBar.addSeparator();
        button = makeNavigationButton("exit", "Exit",
                "Exit from this program",
                "Exit");
        toolBar.add(button);

        add(toolBar, BorderLayout.NORTH);
    }

    private void controller() {
        // This is the logic of the program.

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if ("Create".equals(ae.getActionCommand())) {
            // Create a new, blank BibTeX file
            ArrayList<String> newBibtex = new ArrayList<>();
            ArrayList<String> readFile = new ArrayList<>();
            String newBib = "@article{,\n  author = {},\n  note = {},\n  title = {},\n  journal = {},\n  editor = {},\n  month = {},\n  year = {},\n  series = {},\n  volume = {},\n  pages = {},\n  organization = {},\n  publisher = {},\n  booktitle = {},\n  institution = {},\n  chapter = {},\n  edition = {},\n  howpublished = {},\n  key = {},\n  number = {},\n  school = {},\n  type = {},\n  address = {},\n  annote = {},\n  crossref = {},\n}\n";

            newBibtex.clear();

            cen.setText(newBib);
        }
        if ("Search".equals(ae.getActionCommand())) {
            ArrayList<String> fyle = new ArrayList<>();
            ArrayList<String> show = new ArrayList<>();
            boolean fnd = false;

            show.clear();
            String search = JOptionPane.showInputDialog(this, "Wot ya wont?");

            for (String tmp : fileNames) {
                fnd = false;
                fyle = cc.readTextFile(bibDir + tmp);
                for (String lyne : fyle) {
                    if (lyne.contains(search)) {
                        fnd = true;
                    }
                }

                if (fnd) {
                    show.addAll(fyle);
                    show.add("\n\n");
                }
            }
            displayBibtex(show);
        }
        if ("Sync".equals(ae.getActionCommand())) {
            syncBibtex();
        }
        if ("GScholar".equals(ae.getActionCommand())) {
            openSite("https://scholar.google.co.uk/");
        }
        if ("MSAcademic".equals(ae.getActionCommand())) {
            openSite("https://academic.microsoft.com/");
        }
        if ("SaveBibtex".equals(ae.getActionCommand())) {
            String bib[] = cen.getText().split("\n");
            String l0 = "", lm = "", ln = "", br = "}";
            String newBib = "";
            int n = bib.length;

            for (int i = 0; i < n; i++) {
                String tmp = bib[i];
                if (i == 0) {
                    l0 = checkFirstLine(tmp) + "\n";
                } else if (i == n - 2) {
                    ln = checkLastLine(tmp) + "\n";
                } else if (i == n - 1) {
                    br = "}\n";
                } else {
                    lm += checkMidLine(tmp);
                }
            }
            newBib = l0 + lm + ln + br;
            newBib = checkNewLastLine(newBib);
            cen.setText(newBib);

            StringSelection selection = new StringSelection(bibtexName);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            if (!newBib.contains("**PleaseGiveThisAName**")) {
                try {
                    cc.writeTextFile(bibDir + bibtexName + ".bib", newBib, true);
                } catch (IOException ex) {
                    System.out.println("File not saved.");
                }
                JOptionPane.showMessageDialog(null, "This file was saved.");
            } else {
                JOptionPane.showMessageDialog(null, "This file was NOT saved because it did not have a valid name in the first line.");
            }
        }
        if ("LibSearch".equals(ae.getActionCommand())) {
            ArrayList<String> fyle = new ArrayList<>();
            ArrayList<String> show = new ArrayList<>();
            boolean fnd = false;

            show.clear();
            String search = JOptionPane.showInputDialog(this, "Wot ya wont?");

            for (String tmp : pdfNames) {
                if (tmp.contains(search)) {
                    show.add(tmp + "\n");
                }
            }
            displayBibtex(show);
        }
        if ("LibSync".equals(ae.getActionCommand())) {
            syncPDFLib();
        }
        if ("Exit".equals(ae.getActionCommand())) {
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        System.out.println("keyTyped has not been coded yet.");
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        System.out.println("keyPressed has not been coded yet.");
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        System.out.println("keyReleased has not been coded yet.");
    }

    protected JMenuItem makeMenuItem(String txt,
            String actionCommand,
            String toolTipText,
            Font fnt) {

        JMenuItem mnuItem = new JMenuItem();
        mnuItem.setFont(fnt);
        mnuItem.setText(txt);
        mnuItem.setToolTipText(toolTipText);
        mnuItem.setActionCommand(actionCommand);
        mnuItem.addActionListener(this);

        return mnuItem;
    }

    protected JButton makeNavigationButton(String imageName,
            String actionCommand,
            String toolTipText,
            String altText) {

        //Look for the image.
        String imgLocation = cc.appDir + "\\icons\\"
                + imageName
                + ".png";

        //Create and initialize the button.
        JButton button = new JButton();
        button.setToolTipText(toolTipText);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);

        File fyle = new File(imgLocation);
        if (fyle.exists() && !fyle.isDirectory()) {
            // image found
            Icon img;
            img = new ImageIcon(imgLocation);
            button.setIcon(img);
        } else {
            // image NOT found
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }

    private void displayBibtex(ArrayList<String> bt) {
        if (!bt.isEmpty()) {
            String disp = "";
            for (String tmp : bt) {
                disp += tmp + "\n";
            }
            cen.setFont(fnt);
            cen.setAlignmentY(LEFT_ALIGNMENT);
            cen.setText(disp);
        }
    }

    private void saveBibtex(ArrayList<String> bt) {
        try {
            cc.writeTextFile(bibDir + "Bibtex.bib", bt, true);
        } catch (IOException ex) {
            System.out.println(cc.appDir + "\\..\\Bibtex\\Bibtex.bib not found.");;
        }
    }

    private ArrayList<String> readBibtex(String bb) {
        // Read a specific BibTeX file from the local directory.
        ArrayList<String> rb = new ArrayList<>();

        if (cc.isExisting(bibDir + bb) == 1) {
            rb = cc.readTextFile(bibDir + bb);
        } else {
            rb.clear();
        }

        return rb;
    }

    private void readFileList() {
        // This read the list of BibTeX files in the same location as this program.
        fileNames.clear();

        fileNames = cc.getFileList(bibDir, true);
        fileNames.remove("Bibtex.bib");

        fileNames.stream().filter((tmp) -> (!tmp.endsWith(".bib"))).forEach((tmp) -> {
            fileNames.remove(tmp);
        });
    }

    private void displayTypes() {
        //System.out.println(bt.size());
        String disp = "article\nbook\nbooklet\nconference\nelectronic\ninbook\nincollection\ninproceedings\nmanual\nmastersthesis\nmisc\nproceedings\ntechreport\nunpublished\n";
        lft.setFont(fnt);
        lft.setAlignmentY(LEFT_ALIGNMENT);
        lft.setText(disp);
    }

    private void displayLitRep() {
        // This recreates the list of PDF files in the literature repository
        String disp = "";

        pdfNames = cc.getFileList(libDir, "pdf", true);
        for (String tmp : pdfNames) {
            disp += tmp + "\n";
        }
        rht.setFont(fnt);
        rht.setAlignmentY(LEFT_ALIGNMENT);
        rht.setText(disp);
    }

    private void syncBibtex() {
        // Synchronise another BibTeX repository with the local one
        JOptionPane.showMessageDialog(null, "This has not been coded yet");
    }

    private void syncPDFLib() {
        // Synchronise another BibTeX repository with the local one
        JOptionPane.showMessageDialog(null, "This has not been coded yet");
    }

    private void openSite(String site) {
        // Used to open a web site, e.g. Google Scholar, in the default browser.
        URI url = null;
        try {
            url = new URI(site);
        } catch (URISyntaxException ex) {
            JOptionPane.showMessageDialog(null, "Could not find " + site + ".");
        }
        try {
            java.awt.Desktop.getDesktop().browse(url);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not get to " + site + ".");
        }
    }

    private String checkFirstLine(String tmp) {
        String p1, p2;

        int brac = tmp.indexOf("{");
        p1 = tmp.substring(0, brac);
        if (!p1.startsWith("@")) {
            p1 = "@" + p1;
        }

        p2 = tmp.substring(brac + 1);
        bibtexName = p2.replace(",", "").trim();
        if (bibtexName.isEmpty()) {
            bibtexName = "**PleaseGiveThisAName**";
        }

        return p1 + "{" + bibtexName + ",";
    }

    private String checkMidLine(String tmp) {
        if (tmp.contains("{}")) {
            return "";
        }

        String p1, p2;

        int eq = tmp.indexOf("=");
        p1 = tmp.substring(0, eq).trim();

        p2 = tmp.substring(eq + 1).trim();
        if (!p2.startsWith("{")) {
            p2 = "{" + p2;
        }
        if (!p2.endsWith(",")) {
            p2 = p2 + ",";
        }
        if (!p2.endsWith("},")) {
            p2 = p2.replace(",", "},");
        }

        return "  " + p1 + " = " + p2 + "\n";
    }

    private String checkLastLine(String tmp) {
        if (tmp.contains("{}")) {
            return "";
        }

        String p1, p2;

        int eq = tmp.indexOf("=");
        p1 = tmp.substring(0, eq).trim();

        p2 = tmp.substring(eq + 1).trim();
        if (!p2.startsWith("{")) {
            p2 = "{" + p2;
        }
        if (!p2.endsWith(",")) {
            p2 = p2 + ",";
        }
        if (!p2.endsWith("},")) {
            p2 = p2.replace(",", "},");
        }

        return "  " + p1 + " = " + p2 + "\n";
    }

    private String checkNewLastLine(String bib) {
        if (bib.endsWith("\n\n}\n")) {
            bib = bib.replace("\n\n}\n", "\n}\n");
        }
        if (bib.endsWith("},\n}\n")) {
            bib = bib.replace("},\n}\n", "}\n}\n");
        }

        return bib;
    }
}

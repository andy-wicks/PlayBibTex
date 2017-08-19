package playbibtex;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;

/**
 *
 * @author Andy Wicks
 * Started : 2010
 * First deployed : 2010
 * @Version 3.0
 *
 */
public final class CommonCode {

    public static final String ORDERED_DATE_TIME_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String UK_DATE_TIME_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";
    public static final String US_DATE_TIME_FORMAT_NOW = "MM-dd-yyyy HH:mm:ss";
    public static final String ORDERED_DATE_FORMAT_NOW = "yyyy-MM-dd";
    public static final String UK_DATE_FORMAT_NOW = "dd-MM-yyyy";
    public static final String US_DATE_FORMAT_NOW = "MM-dd-yyyy";
    public String orderedDateAndTime;
    public String ukDateAndTime;
    public String usDateAndTime;
    public String orderedDate;
    public String ukDate;
    public String usDate;
    
    public final String userName = System.getProperty("user.name");
    public final String appDir = System.getProperty("user.dir");
    public final String os = System.getProperty("os.name");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String javaVersion = System.getProperty("java.version");
    
    private long startTime;
    private ActionListener calledBy;

    CommonCode(ActionListener caller) {
        calledBy = caller;
	initialiseVariables();
    }

    CommonCode() {
	initialiseVariables();
    }
    
    
    // This is used by CommonCode to set up the public variables.
    private void initialiseVariables() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat o1sdf = new SimpleDateFormat(ORDERED_DATE_TIME_FORMAT_NOW);
        orderedDateAndTime = o1sdf.format(cal.getTime());
        SimpleDateFormat uk1sdf = new SimpleDateFormat(UK_DATE_TIME_FORMAT_NOW);
        ukDateAndTime = uk1sdf.format(cal.getTime());
        SimpleDateFormat us1sdf = new SimpleDateFormat(US_DATE_TIME_FORMAT_NOW);
        usDateAndTime = us1sdf.format(cal.getTime());
        SimpleDateFormat o2sdf = new SimpleDateFormat(ORDERED_DATE_TIME_FORMAT_NOW);
        orderedDate = o2sdf.format(cal.getTime());
        SimpleDateFormat uk2sdf = new SimpleDateFormat(UK_DATE_TIME_FORMAT_NOW);
        ukDate = uk2sdf.format(cal.getTime());
        SimpleDateFormat us2sdf = new SimpleDateFormat(US_DATE_TIME_FORMAT_NOW);
        usDate = us2sdf.format(cal.getTime());
    }

    
    // This reads a text file into an ArrayList of Strings.  The path to the
    // file has to be added.  Use appDir if the files are in the application 
    // directory.  Use fileSeperator if the app may be running under a 
    // different OS.
    public ArrayList<String> readTextFile(String fileName) {
        String line;
        ArrayList file = new ArrayList();

        if ((fileName == null) || (fileName.equals(""))) {
            System.out.println("No file name specified.");
        } else {
            try {
                BufferedReader in = new BufferedReader(new FileReader(fileName));
                if (!in.ready()) {
                    throw new IOException();
                }
                while ((line = in.readLine()) != null) {
                    file.add(line);
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e);
                file.add("File not found");
            }
        }
        return file;
    }
    
    // This writes to a text file using an ArrayList of Strings.  The path to the
    // file has to be added.  Use appDir if the files are in the application 
    // directory.  Use fileSeperator if the app may be running under a 
    // different OS.
    /**
     * Change the contents of text file in its entirety, overwriting any
     * existing text.
     * @param fn
     * @param aContents
     * @param show
     * @throws java.io.FileNotFoundException
     */
    public void writeTextFile(String fn, ArrayList aContents, Boolean show) throws FileNotFoundException, IOException {
        File fileName = new File(fn);
        
        if (show) {
            System.out.println(aContents.size());
        }
        
        Writer output = new BufferedWriter(new FileWriter(fileName));
        try {
            //FileWriter always assumes default encoding is OK!
            for (int i = 0; i < aContents.size(); i++) {
                output.write(aContents.get(i).toString() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            output.close();
        }
    }
    
    // This writes to a text file using an ArrayList of Strings.  The path to the
    // file has to be added.  Use appDir if the files are in the application 
    // directory.  Use fileSeperator if the app may be running under a 
    // different OS.
    /**
     * Change the contents of text file in its entirety, overwriting any
     * existing text.
     * @param fn
     * @param aContents
     * @param show
     * @throws java.io.FileNotFoundException
     */
    public void writeTextFile(String fn, String aContents, Boolean show) throws FileNotFoundException, IOException {
        File fileName = new File(fn);
        
        if (show) {
            System.out.println(aContents + " was saved.");
        }
        
        Writer output = new BufferedWriter(new FileWriter(fileName));
        try {
            //FileWriter always assumes default encoding is OK!
            output.write(aContents);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            output.close();
        }
    }

    
    // Get a list of all the system properties and their values.
    // Not available in unsigned Applets, only applications and signed Applets.
    public void showSystemProperties() {
        Properties sysprops = System.getProperties();
        
        for (Enumeration e = sysprops.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = sysprops.getProperty(key);
            System.out.println(key + "=" + value);
        }
    }

    
    // The next two methods are meant to be used together.  Call startTiming 
    // to initiate a timing session and endTiming will return the elapsed time
    // when called as a String.
    public void startTiming() {
        startTime = System.currentTimeMillis();
    }

    public String endTiming() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return (dateFormat.format(new Date(currentTime - startTime)));
    }


    
    private String lTrim(String ln) {
        while (Character.isWhitespace(ln.charAt(0))) {
            ln = ln.substring(1);
        }

        return (ln);
    }

    private String rTrim(String ln) {
        while (Character.isWhitespace(ln.charAt(ln.length() - 1))) {
            ln = ln.substring(0, ln.length() - 2);
        }

        return (ln);
    }

    
    // This will trim all whitespace characters (spaces, tabs, etc.)
    public String strTrim(String ln) {
        ln = lTrim(ln);
        ln = rTrim(ln);
        return (ln);
    }

    
    // This method returns an array of Strings containing 
    // the tag name (item 0) and data item (item 1).
    public String[] stripXML(String ln) {
        int i;
        int j;
        int nCarat = 0;
        String[] ret = new String[3];

        ln = lTrim(ln);
        i = ln.indexOf(">");
        while (i > 0) {
            nCarat++;
            i = ln.indexOf(">", i + 1);
        }

        if (nCarat == 0) {
            ret[0] = "";
            ret[1] = ln;
        }

        if (nCarat == 1) {
            i = ln.indexOf("<");
            ln = ln.substring(i);
            if (ln.startsWith("<")) {
                ret[2] = "Open";
                ln = ln.substring(1);
                if (ln.startsWith("/")) {
                    ret[2] = "Close";
                    ln = ln.substring(1);
                }
            }
            ret[0] = ln.substring(0, ln.length() - 1);
        }

        if (nCarat == 2) {
            i = ln.indexOf("<") + 1;
            ln = ln.substring(i);
            j = ln.indexOf(">");
            ret[0] = ln.substring(0, j);
            i = ln.indexOf("<") + 1;
            ret[1] = ln.substring(j + 1, i - 1);
            ret[2] = "Both";
        }

        if (nCarat > 2) {
            ret[2] = "Invalid";
        }

        //System.out.println(ln + " ~ " + ret[0] + " - " + ret[1] + " - " + ret[2]);
        return (ret);
    }

    public String properCase(String s) {
        Pattern p = Pattern.compile("(^|\\W)([a-z])");
        Matcher m = p.matcher(s.toLowerCase());
        StringBuffer sb = new StringBuffer(s.length());
        while (m.find()) {
            m.appendReplacement(sb, m.group(1) + m.group(2).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public boolean pingThis(String name) throws UnknownHostException {
        boolean reachable = false;

        System.out.println(name);
        try {
            URL site = new URL("http://" + name + "/");
            URLConnection siteConnection = site.openConnection();
            siteConnection.connect();
            reachable = true;
        } catch (MalformedURLException e) {     // new URL() failed
            System.out.println(e.getMessage());
            reachable = false;
        } catch (IOException e) {               // openConnection() failed
            System.out.println(e.getMessage());
            reachable = false;
        }
        return reachable;
    }

    public Connection connectAndyToOVL() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?" + "user=wicksan_andy&password=hagriD24");
            //conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?", "wicksan_user", "hagRid24");
            return conn;
        } catch (SQLException ex) {
            System.out.println("Did not connect to DB\n");
            System.out.println(ex.getLocalizedMessage().toString() + "\n");
            return null;
        }
    }

    public Connection connectTonyToOVL() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?" + "user=wicksan_andy&password=hagriD24");
            //conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?", "wicksan_andy", "hagriD24");
            return conn;
        } catch (SQLException ex) {
            System.out.println("Did not connect to DB\n");
            System.out.println(ex.getLocalizedMessage().toString() + "\n");
            return null;
        }
    }

    public Connection connectUserToOVL() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?" + "user=wicksan_user&password=hagRid24");
            //conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?", "wicksan_user", "hagRid24");
            return conn;
        } catch (SQLException ex) {
            System.out.println("Did not connect to DB\n");
            System.out.println(ex.getLocalizedMessage().toString() + "\n");
            return null;
        }
    }

    public Connection connectGuestToOVL() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?" + "user=wicksan_guest&password=hagrid24");
            //conn = DriverManager.getConnection("jdbc:mysql://mysql303.ixwebhosting.com/wicksan_heaw2?", "wicksan_guest", "hagrid24");
            return conn;
        } catch (SQLException ex) {
            System.out.println("Did not connect to DB\n");
            System.out.println(ex.getLocalizedMessage().toString() + "\n");
            return null;
        }
    }

    public String add1DayTo(String d) {
        String r = "";
        String[] dmy = new String[3];
        dmy = d.split("/");
        int[] mMax = new int[12];
        String[] mnth = new String[12];
        int day = Integer.parseInt(dmy[0]), mon = Integer.parseInt(dmy[1]), yer = Integer.parseInt(dmy[2]);
        GregorianCalendar cal = new GregorianCalendar();
        boolean leap = cal.isLeapYear(yer);

        mnth[0] = "Jan";
        mMax[0] = 31;
        mnth[1] = "Feb";
        if (leap) {
            mMax[1] = 29;
        } else {
            mMax[1] = 28;
        }
        mnth[2] = "Mar";
        mMax[2] = 31;
        mnth[3] = "Apr";
        mMax[3] = 30;
        mnth[4] = "May";
        mMax[4] = 31;
        mnth[5] = "Jun";
        mMax[5] = 30;
        mnth[6] = "Jul";
        mMax[6] = 31;
        mnth[7] = "Aug";
        mMax[7] = 31;
        mnth[8] = "Sep";
        mMax[8] = 30;
        mnth[9] = "Oct";
        mMax[9] = 31;
        mnth[10] = "Nov";
        mMax[10] = 30;
        mnth[11] = "Dec";
        mMax[11] = 31;

        day++;
        if (day > mMax[(mon - 1)]) {
            day = 1;
            mon++;
        }
        if (mon > 12) {
            mon = 1;
            yer++;
        }
        r = day + "/" + mon + "/" + yer;

        return r;
    }

    public ArrayList<String> getFileList(String dir, Boolean show) {
        System.out.println(dir);
        ArrayList<String> list = new ArrayList<>();
        File[] files = new File(dir).listFiles();  //If this pathname does not denote a directory, then listFiles() returns null.
        if (show) {
            System.out.println("No. of files found: " + files.length);
        }

        if (files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    list.add(file.getName());
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /*
     * Use cc.appDir as the first parameter if working for files in this project.
     */
    public ArrayList<String> getFileList(String dir, String ext, Boolean show) {
        ArrayList<String> list = new ArrayList<>();
        File[] files = new File(dir).listFiles();  //If this pathname does not denote a directory, then listFiles() returns null.
        if (show) {
            System.out.println("No. of files found: " + files.length);
        }

        if (files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.getName().endsWith("." + ext)) {
                        list.add(file.getName());
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * This method counts the number of occurrences of a given <code>char</code>
     * in the Specified <code>String</code> source:
     * http://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string
     */
    public int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    // Check whether the user name matches mine.
    public boolean isAndy() {
        boolean chk = false;

        System.out.println(userName);
        if (userName.equals("lordw")) {
            chk = true;
        }

        return chk;
    }

    // Check whether a file or directory exists.
    public int isExisting(String pth) {
        int itis = 0;  // Assume that it does not exist

        File f = new File(pth);
        if (f.exists())  {
            itis = 1;  // It is a file
            if (f.isDirectory()) {
                itis = 2;  // It is a directory
            }
        }

        return itis;
    }

    protected JMenuItem makeMenuItem(String txt,
            String actionCommand,
            String toolTipText,
            Font fnt) {

        JMenuItem mnuItem = new JMenuItem();
        mnuItem.setText(txt);
        mnuItem.setActionCommand(actionCommand);
        mnuItem.setToolTipText(toolTipText);
        mnuItem.setFont(fnt);
        mnuItem.addActionListener(calledBy);

        return mnuItem;
    }

    protected JButton makeNavigationButton(String imageName,
            String actionCommand,
            String toolTipText,
            String altText) {

        //Look for the image.
        String imgLocation = System.getProperty("user.dir") + "\\icons\\"
                + imageName
                + ".png";

        //Create and initialize the button.
        JButton button = new JButton();
        button.setToolTipText(toolTipText);
        button.setActionCommand(actionCommand);
        button.addActionListener(calledBy);

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
}

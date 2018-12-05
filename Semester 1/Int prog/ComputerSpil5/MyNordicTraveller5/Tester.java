import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Testing class for ComputerGame
 * @author  Nikolaj I. Schwartzbach
 * @version 28-10-2018
 *
 * This class is used to debug your implementation of ComputerGame handins 1-3.
 *
 * It has a static method, debugCG that uploads your implementation to our test server.
 */
public final class Tester {

    /**
     * Main method.
     *
     * If run without arguments, it prompts the user.
     * If run with a single argument, it chooses the option specified.
     * @param args Optional arguments.
     */
    public static void main(String... args) throws IOException {


        int j;
        if(args.length == 0) {
            out("Velkommen til test/debugning af computerspilsopgaven.\n");
            out("Du har nu fem valgmuligheder:");
            out("1. Test af CG1");
            out("2. Test af CG3");
            out("3. Test af CG5");
            out("4. Debugning af CG1");
            out("5. Debugning af CG2");
            out("6. Debugning af CG3");
            j = intIn("Vælg hvad du vil køre ved at indtaste et heltal mellem 1 og 6.\n> ");
        }else
        if(args.length==1){
            j = Integer.parseInt(args[0]);
        } else {
            err("For mange argumenter. Forventer 0 eller 1, du har givet "+args.length+".");
            return;
        }
        switch(j){
            case 1:
                testCG1();
                break;
            case 2:
                testCG3();
                break;
            case 3:
                testCG5();
                break;
            case 4:
                debugCG1();
                break;
            case 5:
                debugCG2();
                break;
            case 6:
                debugCG3();
                break;
            default:
                out("Ugyldig valgmulighed '"+j+"'");
        }
        System.out.println();
    }

    /**
     * Tests whether or not CG1 is (probably) correctly implemented.
     * @return True if yes, false otherwise.
     * @throws IOException If the test file cannot be read.
     */
    public static boolean testCG1() throws IOException {
        return performTest("cg1.dat");
    }

    /**
     * Tests whether or not CG3 is (probably) correctly implemented.
     * @return True if yes, false otherwise.
     * @throws IOException If the test file cannot be read.
     */
    public static boolean testCG3() throws IOException {
        return performTest("cg3.dat");
    }
    
    /**
     * Tests whether or not Log is implemented correctly.
     * It does not interact with the GUI class, so manual testing is also
     * needed.
     * @return True if Log is (probably implemented correctly).
     */
    public static boolean testCG5() {
        // Test af Serializable
        if(!(new Log(0, null) instanceof Serializable)){
            System.err.println("Din implementation af Log implementerer ikke Serializable.");
            return false;
        }
        
        // Test af gammel Settings
        Settings settings = new Settings();
        if(!(settings instanceof Serializable)){
            System.err.println("Du har en gammel udgave af Settings klassen. Download CG5.zip på flg. link:"
                               + "\nhttps://users-cs.au.dk/dintprog/e18/opgaver/CG5.zip");
            return false;
        }
        
        // Test konstruktør
        for(int seed = 0; seed < 100; seed++){
            settings.setRisk(seed);
            Log log = new Log(seed, settings);
            // Test af seed
            if(log.getSeed() != seed){
                System.err.println("Forkert seedværdi. Forventede "+seed+", men fik "+log.getSeed());
                return false;
            }
            // Test af settings
            if(log.getSettings().getRisk() != seed){
                System.err.println("Forkert reference til settings. ");
                return false;
            }
        }
        
        Country country = new Country("Country Z", null);
        Random r = new Random(331);
        // Test af indhold
        for(int i = 0; i < 100; i++){
            Log log = new Log(0,null);
            Map<Integer, String> contents = new HashMap<>();
            
            for(int j = 0; j < 10000; j++){
                int k = r.nextInt(50);
                byte[] bytes = new byte[30];
                r.nextBytes(bytes);
                String s = new String(bytes);
                City c = new City(s, 0, country);
                log.add(k,c);
                contents.put(k,s);
            }
            
            for(int k : contents.keySet())
                if(!contents.get(k).equals(log.getChoice(k))){
                    System.err.println("Forkert valg ved tid t="+k+". Forventede "+contents.get(k)+", men fik "+log.getChoice(k));
                    return false;
                }
            
        }
        return true;
    }

    /**
     * Tests whether or not this computer game (probably) works.
     * Runs a game with a predetermined seed, and checks if all values match with the Forventet values.
     * Will print errors to System.out.
     * @return Returns true if the implementation passes the test, and false otherwise.
     */
    private static boolean performTest(String path) throws IOException {

        if(!Files.exists(Paths.get("network.dat"))) {
            err("Kan ikke udføre lokal uden 'network.dat'. Lukker ned.");
            return false;
        }

        if(!Files.exists(Paths.get(path))){
            err("Kan ikke udføre lokal test uden '"+path+"'. Lukker ned.");
            return false;
        }

        Game g = Generator.generateGame(0, "network.dat");

        boolean cities = false;
        int t = 0;
        int p = 0;
        Class<?> playerClass = null;

        for(String line : new String(Files.readAllBytes(Paths.get(path)), "windows-1252").split("\r\n")) {
            if(line.startsWith("t=") && !line.startsWith("t=0")) {
                g.step();
                t++;
                cities=false;
                p=0;
                continue;
            }

            if(line.startsWith("\tcities")) {
                cities = true;
                continue;
            }
            if(line.startsWith("\t\t") && !cities) {
                Player player = null;
                for(Player pl : g.getPlayers()){
                    if(pl.getClass() == playerClass) {
                        player = pl;
                        break;
                    }
                }
                String l = line.substring("\t\t".length());
                switch(p++) {
                    case 0:
                        if(player.getMoney() != Integer.parseInt(l)){
                            System.out.println("Fejl ved tid "+t+": Spilleren "+player.getName()+" har ikke det forventede antal penge.\n\tForventet: "+l+"€\n\tModtaget: "+player.getMoney()+"€");
                            return false;
                        }
                        break;

                    case 1:
                        if(player.getPosition().getDistance() != Integer.parseInt(l)){
                            System.out.println("Fejl ved tid "+t+": Spilleren "+player.getName()+" har ikke det forventede antal skridt tilbage.\n\tForventet: "+l+"\n\tModtaget: "+player.getPosition().getDistance());
                            return false;
                        }
                        break;

                    case 2:
                        if(!player.getPosition().getFrom().getName().equals(l)){
                            System.out.println("Fejl ved tid "+t+": Spilleren "+player.getName()+" har ikke den forventede 'from' -by.\n\tForventet: "+l+"\n\tModtaget: "+player.getPosition().getFrom().getName());
                            return false;
                        }
                        break;

                    case 3:
                        if(!player.getPosition().getTo().getName().equals(l)){
                            System.out.println("Fejl ved tid "+t+": Spilleren "+player.getName()+" har ikke den forventede 'to' -by.\n\tForventet: "+l+"\n\tModtaget: "+player.getPosition().getTo().getName());
                            return false;
                        }
                        break;
                }
                continue;
            }
            if(line.startsWith("\t") && !cities){
                playerClass = Player.class;
                if(line.startsWith("\tSmart")){
                    playerClass = SmartPlayer.class;
                }
                if(line.startsWith("\tGreedy")){
                    playerClass = GreedyPlayer.class;
                }
                if(line.startsWith("\tRandom")){
                    playerClass = RandomPlayer.class;
                }
                continue;
            }
            if(cities) {
                String[] del = line.substring("\t\t".length()).split("\t");
                int goal = Integer.parseInt(del[1]);
                if(g.getCity(del[0]).getValue() != goal) {
                    System.out.println("Fejl ved tid "+t+": Byen "+del[0]+" har ikke den forventede værdi.\n\tForventet: "+goal+"€\n\tModtaget: "+g.getCity(del[0]).getValue()+"€");
                    return false;
                }
                continue;
            }
        }

        System.out.println("Test successful!");
        return true;
    }

    public static void debugCG1(){ debugCG(1); }
    public static void debugCG2(){ debugCG(2); }
    public static void debugCG3(){ debugCG(3); }

    /**
     * This method uploads your implementation to our test server.
     *
     */
    private static void debugCG(int version){

        out("Online debugning af CG"+version+".");

        Uploader uploader = new Uploader(version);

        if(!uploader.loadExistingSettings()){
            return;
        }

        if(!uploader.findAllFiles()){
            err("Ingen filer fundet. Lukker ned.");
            return;
        }

        if(!uploader.chooseFolderIfMore()){
            err("Ingen folder valgt. Lukker ned.");
            return;
        }

        try {
            uploader.upload();
        } catch (IOException e) {
            err("Kunne ikke uploade til serveren: "+e.getMessage());
        }
    }

    /**
     * This Scanner is used to get input from the user.
     * The variable is justifiably static as it takes non-trivial.
     * time to initialize it.
     */
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Outputs an error message to Systen.err.
     * @param s The string to print
     */
    private static void err(String s){
        System.err.println(s);
    }

    /**
     * Outputs a string to System.out.
     * @param s The string to print.
     */
    private static void out(String s){
        System.out.println(s);
    }

    /**
     * Read an int from System.in
     * @param out
     * @return
     */
    private static int intIn(String out){
        System.out.print("\n"+out);
        try {
            int i = scanner.nextInt();
            System.out.println();
            return i;
        } catch(InputMismatchException e){
            err("Input er ikke et heltal.");
            return -1;
        }
    }

    /**
     * Asks for user input and returns it.
     * @param out The string to print.
     * @param pattern The regex pattern to use for the scanner
     * @return User-input.
     */
    private static String in(String out, String pattern){
        System.out.print("\n"+out);
        try {
            String s = scanner.next(pattern);
            System.out.println();
            return s;
        } catch(InputMismatchException e){
            err("Dit input har et ugyldigt format.");
            return null;
        }
    }

    /**
     * This class carries the current state of the upload:
     *  - identity of student
     *  - version of CG
     *  - contents of files
     */
    private static class Uploader {

        // id and auth token
        private String auID, authToken;

        // OS information
        private String folder, root;

        // CG version
        private int version;

        // lazy reading of files
        private Map<String, Supplier<String>> files;

        private Uploader(int version){
            files = new HashMap<>();
            root = System.getProperty("user.dir");
            this.version = version;
        }

        /**
         * Find all CG files in any subdirectory of user.dir.
         * @return False if encountered a serious error.
         */
        boolean findAllFiles() {
            out("Leder efter ComputerGame projekter i '"+root+"'.\n");
            try {
                Files.walk(Paths.get(root))
                     .filter(p -> Stream.of(getFiles(version))
                                        .anyMatch(s -> p.endsWith(s+".java")))
                     .map(p -> p.toString())
                     .forEach(s ->
                         files.put(s, () -> {
                             try {
                                 return new String(Files.readAllBytes(Paths.get(s)));
                             } catch (IOException e) {
                                 err("Kan ikke læse filen '"+s+"'.");
                                 return "Der var en fejl ved læsning af denne fil.";
                             }
                         }));
                return true;
            } catch (IOException e) {
                err("Kunne ikke læse filer fra operativsystemet: "+e.getMessage());
                return false;
            }
        }

        private String[] getFiles(int version) {
            switch(version) {
                case 1:
                    return new String[]{"City", "Country", "Road", "Position"};
                case 2:
                    return new String[]{"CityTest", "CountryTest", "RoadTest", "PositionTest"};
                case 3:
                    return new String[]{"BorderCity", "CapitalCity", "MafiaCountry",
                                        "BorderCityTest", "CapitalCityTest", "MafiaCountryTest"};
            }
            return null;
        }

        /**
         * If there were multiple folders containing CG files, let the user choose.
         * @return False if a fatal error occurred.
         */
        boolean chooseFolderIfMore(){

            // add to set to prune duplicates
            Set<String> folderSet = new HashSet<>();
            for(String s : files.keySet()) {
            	if(s.contains("/"))
                	folderSet.add(s.substring(0, s.lastIndexOf("/")));
                else
                	folderSet.add(s.substring(0, s.lastIndexOf("\\")));
            }

            // if there are no directories containing CG files
            if(folderSet.size()==0){
                err("Der blev ikke fundet nogen CG"+version+" projekter i en undermappe af '"+root+"'.");
                return false;
            }

            // add to a list
            List<String> folderList = new ArrayList<>(folderSet);

            // if only a single choice, then choose that
            if(folderList.size()==1){
                folder = folderList.get(0);
                return true;
            }

            // otherwise let the user choose
            Collections.sort(folderList);
            out("Der blev fundet flere mapper med ComputerGame projekter.");
            int i = 0;
            for(String s : folderList){
                out(++i+".\t"+s);
            }
            int j = (intIn("Vælg en folder fra ovenstående liste ved at indtaste et heltal mellem 1 og "+folderList.size()+".\n> ")-1);
            if(j<0 ||j>=folderList.size()){
                err("Accepterer kun heltal mellem 1 og "+folderList.size()+". Lukker ned.");
                return false;
            }
            folder = folderList.get(j);
            return true;
        }

        /**
         * Confirms the directory contains all the necessary files.
         * @return
         */
        boolean confirmDirectory(String dir) {
            Stream<String> loadedFilesInDir =
              files.keySet()
                   .stream()
                   .filter(s -> s.startsWith(dir));

            // If there are too few files
            if(loadedFilesInDir.count() < getFiles(version).length)
                return false;

            // Check that each file is contained
            for(String cgFile : getFiles(version))
                if(!loadedFilesInDir.filter(f -> f.endsWith(cgFile+".java")).findAny().isPresent())
                    return false;

            return true;
        }

        /**
         * Reads the given filename from the chosen folder.
         * @param fileName The name of the Java class
         * @return The contents of the file
         * @throws IOException
         */
        private String readFile(String fileName) throws IOException {
            return new String(Files.readAllBytes(Paths.get(folder+"/"+fileName+".java")));
        }

        private void putParams(Map<String,Object> params, String... files) throws IOException {
            for(String f : files)
                params.put(f, readFile(f));
        }
        /**
         * Upload the chosen project to the server using a POST request.
         * @throws IOException
         */
        void upload() throws IOException {
            URL url = new URL("http://dintprog-server.cs.au.dk/upload.php");
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("auID", auID);
            params.put("auth", authToken);
            params.put("version", version);
            putParams(params, getFiles(version));

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0;)
                System.out.print((char)c);
                
            System.out.println();
        }

        /**
         * Location of the saved settings
         */
        private static final String SAVE_LOCATION = "debug-data.dat";

        /**
         * Prompts the student to input auID and authentication token.
         */
        private boolean promptUser(){
            out("For at kunne uploade din implementation, skal vi bruge:");
            out(" - dit auID");
            out(" - din authentication token (kan findes under Grade Center på Blackboard)");
            auID      = in("Indtast auID:\n> ", "au[0-9][0-9][0-9][0-9][0-9][0-9]|au[0-9][0-9][0-9][0-9][0-9]").toLowerCase().replace("au","");
            authToken = in("Indtast authentication token:\n> ", "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]|[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");

            // If the user provided invalid input
            if(authToken == null){
                err("Et authentication token skal have 10 decimaler. Lukker ned.");
                return false;
            }
            
            // Save to file so we don't have to bother the user anymore.
            try {
                PrintWriter pw = new PrintWriter("debug-data.dat");
                pw.print(auID+", "+authToken);
                pw.close();
                return true;
            } catch (FileNotFoundException e) {
                err("Kunne ikke gemme til filen 'debug-data.dat': "+e.getCause().toString());
            }
            return false;
        }

        /**
         * Loads the existing settings if existing, otherwise prompts user.
         */
        boolean loadExistingSettings(){
            if(Files.exists(Paths.get(SAVE_LOCATION))){
                try {
                    String s = new String(Files.readAllBytes(Paths.get(SAVE_LOCATION)));
                    String[] split = s.replace(" ","").split(",");
                    auID = split[0];
                    authToken = split[1];
                    return true;
                } catch (IOException e) {
                    err("Kunne ikke indlæse indstillinger fra filen '"+SAVE_LOCATION+"'.\nStarter forfra...");
                    return promptUser();
                }
            } else return promptUser();
        }
    }
}

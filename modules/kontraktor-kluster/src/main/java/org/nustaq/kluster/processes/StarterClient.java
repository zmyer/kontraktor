package org.nustaq.kluster.processes;

import com.beust.jcommander.JCommander;
import org.nustaq.kontraktor.remoting.tcp.TCPConnectable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by ruedi on 17.04.16.
 */
public class StarterClient {

    public static void main(String[] args) throws Exception {

        if ( args.length >= 1 && args[args.length-1].endsWith(".kson") ) {

            final StarterClientArgs options = new StarterClientArgs();
            final JCommander jCommander = new JCommander(options);
            parseStarterConf(args, options, jCommander);
            options.underride(ProcessStarter.locateProps(0,new File("./"),"troll.properties"));

            // special to process a whole cluster definition form a file (quite a quick hack)
            HashSet<String> groups = new HashSet<>();
            for (int i = 0; i < options.getParameters().size()-1; i++) {
                String arg = options.getParameters().get(i);
                groups.add(arg);
            }
            KlusterConf klusterConf = new KlusterConf( groups, args[args.length-1]);
            if ( klusterConf.getToStart().size() == 0 ) {
                System.out.println("no processes found in "+args[0]);
                System.exit(0);
            }

//            StarterClientArgs options = klusterConf.getToStart().get(0);
            ProcessStarter starter = (ProcessStarter) new TCPConnectable(ProcessStarter.class, options.getHost(), options.getPort())
                .connect(
                    (x, y) -> System.out.println("client disc " + x),
                    act -> {
                        System.out.println("act " + act);
                    }
                ).await();
            klusterConf.getToStart().forEach( opts -> {
                try {
                    runProc(opts, starter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            starter.ping().await();
        } else {
            final StarterClientArgs options = new StarterClientArgs();
            final JCommander jCommander = new JCommander(options);

            parseStarterConf(args, options, jCommander);
            options.underride( ProcessStarter.locateProps() );

            if ( options.isHelp() ) {
                jCommander.usage();
                System.exit(0);
            }

            ProcessStarter starter = (ProcessStarter) new TCPConnectable(ProcessStarter.class, options.getHost(), options.getPort())
                .connect(
                    (x, y) -> System.out.println("client disc " + x),
                    act -> {
                        System.out.println("act " + act);
                    }
                ).await();

            runProc(options, starter);
            starter.ping().await();
        }

        System.exit(0);
    }

    public static void runProc(StarterClientArgs options, ProcessStarter starter) throws IOException {

        if (options.isResync())
            starter.resyncProcesses();

        if (options.getRestartIdOrName()!=null)
            restartIdOrName(starter,options.getRestartIdOrName());

        if ( options.isList()) {
            List<ProcessInfo> pis = starter.getProcesses().await();
            System.out.println("listing "+pis.size()+" processes:");
            pis.stream()
                .sorted( (a,b) -> a.getCmdLine()[0].compareTo(b.getCmdLine()[0]))
                .forEach( pi -> System.out.println(pi));
        }
        if ( options.isListSiblings() ) {
            List<StarterDesc> await = starter.getSiblings().await();
            System.out.println("listing "+await.size()+" siblings");
            await.forEach( sd -> System.out.println(sd) );
        }
        if ( options.getKillPid() != null ) {
            System.out.println("killing "+options.getKillPid());
            Object await = starter.terminateProcess(options.getKillPid(), true, 10).await();
            System.out.println("killed "+await);
        }
        String killMatching = options.getKillMatching();
        if ( killMatching != null ) {
            System.out.println("kill matching "+ killMatching);
            Stream<ProcessInfo> matchinPis = getMatchingProcessInfos(starter, killMatching);
            matchinPis
                .forEach(pi -> {
                        System.out.print("killing " + pi + " .. ");
                        try {
                            Object await = starter.terminateProcess(pi.getId(), true, 10).await();
                            System.out.println(await + " ");
                        } catch (Exception e) {
                            System.out.println("" + e.getMessage());
                        }
                    }
                );
        }
        String[] cmd = new String[options.getParameters().size()];
        options.getParameters().toArray(cmd);
        if ( cmd.length > 0 ) {
            System.out.println("running "+ Arrays.toString(cmd));
            ProcessInfo await = starter.startProcess(options.getRedirect(),options.getId(), options.getName(), options.getWd(), new HashMap<>(), cmd).await();
            System.out.println("started " + await);
        }
        if ( options.getSleep() > 0 ) {
            try {
                Thread.sleep(options.getSleep());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void restartIdOrName(ProcessStarter starter, String restartIdOrName) {
            System.out.println("restart matching "+ restartIdOrName);
            Stream<ProcessInfo> matchinPis = getMatchingProcessInfos(starter, restartIdOrName);
            matchinPis
                .forEach(pi -> {
                        System.out.println("killing " + pi + " .. ");
                        try {
                            Integer await = starter.terminateProcess(pi.getId(), true, 10).await();
                            System.out.println("terminated with code "+ await );
                            ProcStartSpec debug = pi.getSpec();
                            ProcessInfo newPI = starter.startProcessBySpec(debug).await();
                            System.out.println("started "+ newPI );
                        } catch (Exception e) {
                            System.out.println("" + e.getMessage());
                        }
                    }
                );
    }

    public static boolean hasCaps(String killMatching) {
        boolean hasCaps = false;
        for ( int i = 0; i < killMatching.length(); i++ ) {
            if ( Character.isUpperCase(killMatching.charAt(i)) ) {
                hasCaps = true;
                break;
            }
        }
        return hasCaps;
    }

    public static Stream<ProcessInfo> getMatchingProcessInfos(ProcessStarter starter, String killMatching) {
        List<ProcessInfo> pis = starter.getProcesses().await();
        boolean finalHasCaps = hasCaps(killMatching);
        return pis.stream()
            .filter(pi -> {
                String[] cmdLine = pi.getCmdLine();
                if (pi.getId().equals(killMatching))
                    return true;
                boolean match = false;
                for (int i = 0; i < cmdLine.length; i++) {
                    if (finalHasCaps) {
                        if (cmdLine[i].indexOf(killMatching) >= 0) {
                            match = true;
                            break;
                        }
                    } else {
                        if (cmdLine[i].toLowerCase().indexOf(killMatching) >= 0) {
                            match = true;
                            break;
                        }
                    }
                }
                return (match || "all".equals(killMatching));
            });
    }

    public static void parseStarterConf(String[] args, StarterClientArgs options, JCommander jCommander) {
        String lastParms = null;
        // avoid jcommander dequoting + parse failure
        if ( args.length > 0 && args[args.length-1].indexOf(" ") > 0 ) // multiword
        {
            String newargs[] = new String[args.length-1];
            for (int i = 0; i < newargs.length; i++) {
                newargs[i] = args[i];
            }
            lastParms = args[args.length-1];
            args = newargs;
        }

        jCommander.parse(args);
        try {
            options.underride(ProcessStarter.locateProps());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( lastParms != null ) {
            String[] split = lastParms.split(" ");
            Arrays.stream(split).forEach( x -> options.getParameters().add(x));
        }
    }

}
package com;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.OSRSIndices;
import store.cache.index.archive.Archive;
import store.cache.index.archive.file.File;
import store.progress.AbstractProgressListener;
import store.progress.ProgressListener;
import utility.XTEASManager;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * For those curious
 *
 * @version 28/12/21
 * @author Shadowrs r-s@Shadowy tardisfan121@gmail.com
 */
@Slf4j
public class CompareCaches {

    static HashMap<String, Logger> logs = new HashMap<>();


    public static ProgressListener progressListener = new AbstractProgressListener() {

        @Override
        public void finish(String title, String message) {

        }

        @Override
        public void change(double progress, String message) {

        }

    };

    public static void main(String[] args) {
        loadconfig();
        for (OSRSIndices value : OSRSIndices.values()) {
            Logger analytics = LoggerFactory.getLogger(value.name());
            logs.put(value.name(), analytics);
        }

        progressListener.notify(0, "Initializing into cache "+Paths.get(config.getString("into_cache")).toFile().getAbsolutePath());

        CacheLibrary intoCache = CacheLibrary.createUncached(Paths.get(config.getString("into_cache")).toFile().getAbsolutePath());

        progressListener.notify(0, "Initializing from cache "+Paths.get(config.getString("from_cache")).toFile().getAbsolutePath());

        CacheLibrary fromCache = CacheLibrary.createUncached(Paths.get(config.getString("from_cache")).toFile().getAbsolutePath());


        progressListener.notify(0, "Initializing XTEA manager with format "+config.getString("xteas_format")+" and file "+Paths.get(config.getString("xteas_file")).toFile().getAbsolutePath());

        /*
         This is custom and not from the original Valkyr cache editor release. You can delete this section and use the original
         valkyr xtea parsing code, or download this edition from the README
         */
        new XTEASManager.MODERN() {
            {
                parserType = XTEASManager.XTEAParserType.valueOf(config.getString("xteas_format"));
            }
            @Override
            public String filePath() {
                return Paths.get(config.getString("xteas_file")).toFile().getAbsolutePath();
            }
        }.load();

       // System.exit(0); // when testing config file loading paths

        //extracted(intoCache, fromCache, OSRSIndices.SKINS); // test single to logfile

        // do em all
        for (OSRSIndices value : OSRSIndices.values()) {
            extracted(intoCache, fromCache, value);
        }
    }

    public static com.typesafe.config.Config config;

    private static void loadconfig() {
        java.io.File confFile = Paths.get("app.conf").toFile();
        log.info("Config path: {}", confFile.getAbsolutePath());
        Config preconfig = ConfigFactory.systemProperties().withFallback(ConfigFactory.parseFileAnySyntax(confFile));

        java.io.File confFile2 = Paths.get(preconfig.getString("conf_file")).toFile();
        log.info("Config preset path: {}", confFile2.getAbsolutePath());
        config = ConfigFactory.systemProperties().withFallback(ConfigFactory.parseFileAnySyntax(confFile2));
    }

    private static void extracted(CacheLibrary intoCache, CacheLibrary fromCache, OSRSIndices indice) {
        Logger idxLogger = LoggerFactory.getLogger(indice.name());
        Index intoIdx = intoCache.getIndex(indice.ordinal());
        Index fromIdx = fromCache.getIndex(indice.ordinal());

        Set<Integer> archivesIds = new HashSet<>();
        idxLogger.info("archives in {}: {} vs {}", indice, intoIdx.getArchives().length, fromIdx.getArchives().length);
        for (int archiveId : intoIdx.getArchiveIds()) {
            archivesIds.add(archiveId);
        }
        for (int archiveId : fromIdx.getArchiveIds()) {
            archivesIds.add(archiveId);
        }
        idxLogger.info("stored {} archive ids", archivesIds.size());
        Set<Integer> deletedArchivesIds = new HashSet<>();
        Set<Integer> cumulativeDeletedFileIds = new HashSet<>(); // when theres only 1 file per achive, like maps/models

        for (Integer archiveId : archivesIds) {
            Archive intoArch = intoIdx.getArchive(archiveId);
            Archive fromArch = fromIdx.getArchive(archiveId);

            if (intoArch == null) {
                if (fromArch.getFileIds().length == 1 && (fromArch.getFile(fromArch.getFileIds()[0]).getData() == null) || fromArch.getFile(fromArch.getFileIds()[0]).getData() != null && fromArch.getFile(fromArch.getFileIds()[0]).getData().length < 2) {
                    idxLogger.info("idx {} new archive: {} with empty file data {}", indice, idOrName(indice, archiveId), fromArch.getFile(fromArch.getFileIds()[0]).getData());
                } else {
                    idxLogger.info("idx {} new archive: {} with {}", indice, idOrName(indice, archiveId), fromArch.getFileIds().length == 1 ? ("1 file length "+ fromArch.getFile(fromArch.getFileIds()[0]).getData().length) : (fromArch.getFileIds().length+" new files"));
                }
                continue;
            }
            if (fromArch == null) {
               // idxLogger.info("idx {} deleted archive: {}", indice, idOrName(indice, archiveId));
                deletedArchivesIds.add(archiveId);
                continue;
            }
            int intoCount = intoArch.getFiles() == null ? 0 : intoArch.getFiles().length;
            int fromCount = fromArch.getFiles() == null ? 0 : fromArch.getFiles().length;

            if (intoCount != fromCount)
                idxLogger.info("idx {} archive {} has file change count {} vs {}", indice, idOrName(indice, archiveId), fromCount, intoCount);

            //idxLogger.info("idx {} archive {} has {} files", indice, idOrName(indice, archiveId), count);
           // idxLogger.info("idx {} archive {} has {} files", indice, idOrName(indice, archiveId), count);
            Set<Integer> deletedFileIds = new HashSet<>();

            for (int fileId : fromArch.getFileIds()) {
                File fromFile = fromArch.getFile(fileId);
                File intoFile = intoArch.getFile(fileId);
                boolean removed = intoFile != null && fromFile.getData() == null && intoFile.getData() != null;
                boolean bothActive = fromFile != null && intoFile != null;
                boolean bothMissingData = bothActive && intoFile.getData() == null && fromFile.getData() == null;
                boolean missingData = bothActive && !bothMissingData && (intoFile.getData()==null || fromFile.getData()==null);
                boolean changed = intoFile != null && fromFile.getData() != null && intoFile.getData() != null && fromFile.getData().length != intoFile.getData().length;
                if (intoFile == null) {
                    idxLogger.info("idx {} archive {} has new file {}", indice, idOrName(indice, archiveId), fileId);
                }
                else if (removed) {
                    if (indice == OSRSIndices.MODELS || indice == OSRSIndices.MAPS) {
                        cumulativeDeletedFileIds.add(fileId);
                    } else {
                        deletedFileIds.add(fileId);
                        //idxLogger.info("idx {} archive {} file {} was deleted", indice, idOrName(indice, archiveId), fileId);
                    }
                }
                else if (missingData) {
                    if (intoFile.getData() == null && fromFile.getData() != null && fromFile.getData().length < 2) {
                        idxLogger.info("empty map file added archive {}.{}.{}", idOrName(indice, archiveId), indice, fileId);
                    } else {
                        idxLogger.info("idx {} archive {} file {} exists but data was deleted {} vs {}", indice, idOrName(indice, archiveId), fileId, intoFile.getData(), fromFile.getData());
                    }
                }
                else if (changed) {
                    idxLogger.info("idx {} archive {} file {} length changed from old {} to new {} by {} bytes", indice, idOrName(indice, archiveId), fileId, intoFile.getData().length, fromFile.getData().length, fromFile.getData().length-intoFile.getData().length);
                }
            }
            if (deletedFileIds.size() > 0)
                idxLogger.info("Deleted "+deletedFileIds.size()+" files from idx {} archive {} with ids: {}", indice, idOrName(indice, archiveId), Arrays.toString(deletedFileIds.toArray()));
        }
        if (cumulativeDeletedFileIds.size() > 0)
            idxLogger.info("Deleted "+cumulativeDeletedFileIds.size()+" files from each archive in idx {} with ids: {}", indice, Arrays.toString(cumulativeDeletedFileIds.toArray()));
        if (deletedArchivesIds.size() > 0)
            idxLogger.info("Deleted "+deletedArchivesIds.size()+" archives from idx {} with ids: {}", indice, Arrays.toString(deletedArchivesIds.toArray()));
    }

    private static String idOrName(OSRSIndices indice, Integer archiveId) {
        ConfigArchive configArchive = Arrays.stream(ConfigArchive.values()).filter(e -> e.i == archiveId).findFirst().orElse(null);
        if (indice == OSRSIndices.CONFIG && configArchive != null)
            return configArchive.name();
        return ""+archiveId;
    }

    public enum ConfigArchive {

        AREA(35),
        ENUM(8),
        HITBAR(33),
        HITMARK(32),
        IDENTKIT(3),
        ITEM(10),
        INV(5),
        NPC(9),
        OBJECT(6),
        OVERLAY(4),
        PARAMS(11),
        SEQUENCE(12),
        SPOTANIM(13),
        STRUCT(34),
        UNDERLAY(1),
        VARBIT(14),
        VARCLIENT(19),
        VARCLIENTSTRING(15),
        VARPLAYER(16);

        public final int i;

        ConfigArchive(int i) {

            this.i = i;
        }
    }

}

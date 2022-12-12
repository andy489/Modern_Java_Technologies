package bg.sofia.uni.fmi.mjt.io;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;

public class DiskSIzeEstimator {
    public static void main(String... args) throws IOException {
        Iterable<FileStore> partitions = FileSystems.getDefault().getFileStores();

        for (FileStore fs : partitions) {
            System.out.println("Partition: " + fs.name());
            System.out.println("Total space: " + fs.getTotalSpace());
            System.out.println("Unallocated space: " + fs.getUnallocatedSpace());
            System.out.println("Usable space: " + fs.getUsableSpace());
            System.out.println();
        }
    }
}

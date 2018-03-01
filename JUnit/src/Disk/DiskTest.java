package Disk;

import org.junit.Test;

public class DiskTest {
    @Test
    // infinite loop
    public void testDisk1() {
        Disk disk = new Disk(1002, -12);
        disk.manipulate();
    }
}
